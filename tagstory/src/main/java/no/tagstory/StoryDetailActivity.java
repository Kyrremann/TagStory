package no.tagstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.story.Story;
import no.tagstory.story.StoryManager;
import no.tagstory.story.TagTypeEnum;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.activity.utils.PhoneRequirementsUtils;
import no.tagstory.utils.Database;

import java.io.FileNotFoundException;

public class StoryDetailActivity extends Activity {

	private final static int ENABLE_GPS = 1001;

	protected String story_id;
	protected StoryManager storyManager;
	protected Story story;
	protected AlertDialog enableGPSDialog, enableNFCDialog, enableQRDialog;
	protected StoryApplication storyApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_detail);

		storyManager = new StoryManager(this);
		storyApplication = (StoryApplication) getApplication();

		story_id = getIntent().getStringExtra(Database.STORY_ID);
		if (story_id != null) {
			story = storyManager.getStory(story_id);
		} else {
			story = (Story) getIntent().getSerializableExtra(
					StoryActivity.EXTRA_STORY);
		}

		if (story != null) {
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
		}
	}

	public void startStory(View v) {
		if (v.getId() == R.id.start_story_button) {
			if (hasPhoneRequirements()) {
				return;
			}
			startStory();
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

	protected void startStory() {
		storyApplication.getStoryHistory().startStory(story);
		startStoryActivity();
	}

	protected void startStoryActivity() {
		Intent intent = new Intent(this, StoryActivity.class);
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
		if (resultCode == ENABLE_GPS) {
			startStory();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.story_detail, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete_story:
				// TODO: Add transactions
				if (storyManager.deleteStory(story_id) && deleteFile(story_id + ".json")) {
					finish();
				} else {
					Toast.makeText(this, "Story was not deleted, please try again.", Toast.LENGTH_SHORT).show();
				}
				break;
		}

		return true;
	}
}
