package no.tagstory;

import java.io.IOException;

import no.tagstory.communication.Database;
import no.tagstory.story.Story;
import no.tagstory.story.StoryManager;
import no.tagstory.story.activity.StoryActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryDetailActivity extends Activity {

	private final static int ENABLE_GPS = 1001;

	protected String story_id;
	protected StoryManager storyManager;
	protected Story story;
	protected AlertDialog enableGPSDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_detail);

		storyManager = new StoryManager(this);

		story_id = getIntent().getStringExtra(Database.STORY_ID);
		if (story_id != null)
			story = storyManager.getStory(story_id);
		else
			story = (Story) getIntent().getSerializableExtra(
					StoryActivity.STORY);

		if (story != null) {
			// ((TextView) findViewById(R.id.story_detail_title))
			// .setText(story.getTitle());
			// ((TextView) findViewById(R.id.story_detail_author))
			// .setText(story.getAuthor());
			setTitle(story.getTitle());
			// ((TextView) findViewById(R.id.story_detail_age))
			// .setText("Agegroup: " + story.getAgeGroup());
			((TextView) findViewById(R.id.story_detail_desc)).setText(story
					.getDesc());
			try {
				((ImageView) findViewById(R.id.story_detail_image))
						.setImageDrawable(Drawable.createFromStream(getAssets()
								.open(story.getImage()), story.getImage()));
				// .setImBackgroundDrawable(Drawable.createFromStream(getAssets().open(story.getImage()),
				// story.getImage()));
			} catch (IOException e) {
				// TODO: Add standard image
				e.printStackTrace();
			}
			// ((TextView) findViewById(R.id.story_detail_area))
			// .setText("Area: " + story.getArea());
			// ((TextView) findViewById(R.id.story_detail_date))
			// .setText(story.getDate());
			// ((TextView) findViewById(R.id.story_detail_keywords))
			// .setText(story.getKeywords().toString());
			// ((TextView) findViewById(R.id.story_detail_tagcount))
			// .setText(story.getTagCount() + " tags");
			// ((TextView) findViewById(R.id.story_detail_genre))
			// .setText("Genre: " + story.getGenre());
		}
	}

	public void startStory(View v) {
		if (v.getId() == R.id.start_story_button) {
			startStory();
		}
	}

	protected void startStory() {
		if (isGPSEnabled()) {
			((StoryApplication) getApplication()).setStartTime(System
					.currentTimeMillis());
			((StoryApplication) getApplication()).emptyHistory();
			startStoryActivity();
		}
	}

	protected void startStoryActivity() {
		Intent intent = new Intent(this, StoryActivity.class);
		intent.putExtra(StoryActivity.STORY, story);
		intent.putExtra(StoryActivity.PARTTAG, story.getStartTag());
		startActivity(intent);
		finish();
	}

	protected void buildAlertMessageNoGps() {
		if (enableGPSDialog == null) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(
					R.string.dialog_enable_gps));
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.dialog_yes,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int id) {

							startActivityForResult(new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS),
									ENABLE_GPS);
						}
					});
			builder.setNegativeButton(R.string.dialog_no,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int id) {
							// TODO User won't turn on GPS
							dialog.cancel();
						}
					});
			enableGPSDialog = builder.create();
		}
		enableGPSDialog.show();
	}

	protected boolean isGPSEnabled() {
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider.contains("gps")) {
			return true;
		} else {
			buildAlertMessageNoGps();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out
				.println("returning" + "-" + resultCode + " - " + requestCode);
		if (resultCode == ENABLE_GPS) {
			startStory();
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuItem item = menu.add(Menu.NONE, 0, Menu.NONE,
	// R.string.detail_menu_start_story);
	// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// return true;
	// }

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					StoryListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/
}
