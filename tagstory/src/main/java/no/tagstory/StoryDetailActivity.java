package no.tagstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.adapters.SavedTravelsListAdapter;
import no.tagstory.honeycomb.TagStoryActivityHoneycomb;
import no.tagstory.story.Story;
import no.tagstory.story.StoryManager;
import no.tagstory.story.TagTypeEnum;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.activity.utils.PhoneRequirementsUtils;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;
import no.tagstory.utils.Tuple;
import no.tagstory.utils.http.SimpleStoryHandler;
import no.tagstory.utils.http.StoryProtocol;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StoryDetailActivity extends Activity implements SimpleStoryHandler.SimpleCallback {

	private final static String LOG = "STORYDETAIL";
	private final static int ENABLE_GPS = 1001;

	protected String storyId;
	protected Story story;
	protected AlertDialog enableGPSDialog, enableNFCDialog, enableQRDialog;
	protected StoryApplication storyApplication;

	private boolean isOutdated;
	private Menu menu;
	private ProgressDialog progressDialog;
	private List<Tuple> savedTravels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_detail);

		storyApplication = (StoryApplication) getApplication();

		storyId = getIntent().getStringExtra(StoryParser.UUID);
		if (storyId != null) {
			StoryManager storyManager = new StoryManager(this);
			story = storyManager.getStory(storyId);
			storyManager.closeDatabase();
		} else {
			story = (Story) getIntent().getSerializableExtra(
					StoryActivity.EXTRA_STORY);
		}

		if (story != null) {
			checkIfStoryIsOutdated();
			boolean showDefault = false;
			setTitle(story.getTitle());
			((TextView) findViewById(R.id.story_detail_desc)).setText(story
					.getDescription());

			String imagefile = story.getImage();
			if (imagefile != null
					&& imagefile.length() != 0) {
				try {
					Bitmap myBitmap = BitmapFactory.decodeStream(openFileInput(imagefile));
					((ImageView) findViewById(R.id.story_detail_image))
							.setImageBitmap(myBitmap);
				} catch (FileNotFoundException e) {
					showDefault = true;
				}
			} else {
				showDefault = true;
			}

			if (showDefault) {
				((ImageView) findViewById(R.id.story_detail_image))
						.setImageResource(R.drawable.placeimg_960_720_nature_1);
			}

			checkIfThereAreSavedTravels();
		}
	}

	private void checkIfThereAreSavedTravels() {
		Database database = new Database(getApplicationContext());
		database.open();
		Button resume = (Button) findViewById(R.id.resume_story);
		if (database.hasSaveTravels(storyId)) {
			resume.setVisibility(View.VISIBLE);
		} else {
			resume.setVisibility(View.GONE);
		}
		database.close();
	}

	private void checkIfStoryIsOutdated() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int version = StoryProtocol.getStoryVersion(getApplicationContext(), storyId);
				StoryManager storyManager = new StoryManager(getApplicationContext());
				if (storyManager.isStoryOutdated(storyId, version)) {
					Log.d(LOG, "Story is outdated");
					isOutdated = true;
				} else {
					Log.d(LOG, "Story is up to date");
					isOutdated = false;
				}
				storyManager.closeDatabase();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (isOutdated) {
							Toast.makeText(getApplicationContext(), R.string.market_outdated, Toast.LENGTH_SHORT).show();
						}
						menu.findItem(R.id.menu_update).setVisible(isOutdated);
					}
				});
			}
		}).start();
	}

	public void startStory(View v) {
		switch (v.getId()) {
			case R.id.start_story:
				if (hasPhoneRequirements()) {
					return;
				}
				startStory();
				break;
			case R.id.resume_story:
				if (hasPhoneRequirements()) {
					return;
				}
				resumeStory();
				break;
		}
	}

	private boolean hasPhoneRequirements() {
		String mode = PhoneRequirementsUtils.checkGamemodes(this, story.getGameModes());
		if (mode != null) {
			// No game mode requirements, yet
		}

		TagTypeEnum type = PhoneRequirementsUtils.checkTagtypes(this, story.getTagTypes());
		if (type != null) {
			if (type.isQR()) {
				showNoQrDialog();
			} else if (type.isGPS()) {
				showNoGpsDialog();
			} else if (type.isNFC()) {
				showNoNfcDialog();
			}

			return true;
		}

		return false;
	}

	protected void resumeStory() {
		final Database database = new Database(this);
		database.open();
		final Cursor saveTravelCursor = database.getSaveTravels(storyId);
		if (saveTravelCursor.getCount() > 1) {
			savedTravels = generateListBasedOnCursor(saveTravelCursor);
			saveTravelCursor.close();
			database.close();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setAdapter(new SavedTravelsListAdapter(this, savedTravels), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (savedTravels.size() > which
							&& which >= 0) {
						Tuple tuple = savedTravels.get(which);
						Database database = new Database(getApplicationContext());
						database.open();
						try {
							storyApplication.resumeStory(database, (Integer) tuple.o1, story.getUUID());
							startStoryActivity();
						} catch (RuntimeException e) {
						} finally {
							database.close();
						}
					}
					dialog.cancel();
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					checkIfThereAreSavedTravels();
				}
			});
			builder.create().show();
		} else {
			saveTravelCursor.moveToFirst();
			storyApplication.resumeStory(database, saveTravelCursor.getInt(1), story.getUUID());
			saveTravelCursor.close();
			database.close();
			startStoryActivity();
		}
	}

	private List<Tuple> generateListBasedOnCursor(Cursor cursor) {
		List<Tuple> tuples = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			tuples.add(new Tuple(cursor.getInt(1), cursor.getString(3)));
			cursor.moveToNext();
		}

		return tuples;
	}

	protected void startStory() {
		storyApplication.startStory(story);
		startStoryActivity();
	}

	protected void startStoryActivity() {
		Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
		intent.putExtra(StoryActivity.EXTRA_STORY, story);
		intent.putExtra(StoryActivity.EXTRA_TAG, story.getStartTagId());
		startActivity(intent);
	}

	private void showNoQrDialog() {
		if (enableQRDialog == null) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(
					R.string.dialog_download_barcodescanner));
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.dialog_go_to_play_store, new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					String appPackageName = getString(R.string.barcode_scanner_play_store);
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
					}
				}
			});
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
						                    final int id) {
							dialog.cancel();
						}
					});
			enableQRDialog = builder.create();
		}
		enableQRDialog.show();
	}

	private void showNoNfcDialog() {
		if (enableNFCDialog == null) {
			// TODO
		}
	}

	protected void showNoGpsDialog() {
		if (enableGPSDialog == null) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(
					R.string.dialog_enable_gps));
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
						                    final int id) {

							startActivityForResult(new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS),
									ENABLE_GPS);
						}
					});
			builder.setNegativeButton(R.string.no,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
						                    final int id) {
							dialog.cancel();
						}
					});
			enableGPSDialog = builder.create();
		}
		enableGPSDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO this is not really working
		if (resultCode == ENABLE_GPS) {
			startStory();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.story_detail, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isOutdated) {
			menu.findItem(R.id.menu_update).setVisible(true);
		} else {
			menu.findItem(R.id.menu_update).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete_story:
				StoryManager storyManager = new StoryManager(this);
				if (storyManager.deleteStory(storyId)) {
					Intent intent = ClassVersionFactory.createIntent(getApplicationContext(),
							TagStoryActivityHoneycomb.class, TagStoryActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					storyManager.closeDatabase();

					finish();
				} else {
					Toast.makeText(this, "Story was not deleted, please try again.", Toast.LENGTH_SHORT).show();
				}
				storyManager.closeDatabase();
				return true;

			case R.id.menu_update:
				deleteStory();
				downloadStory();
				return true;
		}

		return false;
	}

	private void deleteStory() {
		StoryManager storyManager = new StoryManager(this);
		storyManager.deleteStory(storyId);
		storyManager.closeDatabase();
	}

	private void downloadStory() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.market_info_downloading_story));
		progressDialog.show();
		final Handler handler = new SimpleStoryHandler(this);

		new Thread(new Runnable() {
			@Override
			public void run() {
				StoryProtocol.downloadStory(getApplicationContext(), handler, storyId);
			}
		}).start();
	}

	@Override
	public void onMessageDone() {
		progressDialog.cancel();
		isOutdated = false;
	}

	@Override
	public void onMessageInfo(int arg1, int arg2) {
		progressDialog.setMessage(getString(R.string.market_info_downloading_assets) + arg1 + "%");
	}

	@Override
	public void onMessageFail(int error) {
		switch (error) {
			case SimpleStoryHandler.MESSAGE_FAIL_HTTP:
				Toast.makeText(getApplicationContext(), getString(R.string.market_error_http), Toast.LENGTH_SHORT).show();
				break;
			case SimpleStoryHandler.MESSAGE_FAIL_JSON:
				Toast.makeText(getApplicationContext(), getString(R.string.market_error_data), Toast.LENGTH_SHORT).show();
				break;
		}
	}
}
