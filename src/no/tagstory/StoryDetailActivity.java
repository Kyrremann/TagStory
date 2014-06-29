package no.tagstory;

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
import no.tagstory.R;
import no.tagstory.honeycomb.StoryActivityHoneycomb;
import no.tagstory.story.Story;
import no.tagstory.story.StoryManager;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;

import java.io.IOException;

public class StoryDetailActivity extends Activity {

	private final static int ENABLE_GPS = 1001;

	protected String story_id;
	protected StoryManager storyManager;
	protected Story story;
	protected AlertDialog enableGPSDialog;
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
			setTitle(story.getTitle());
			((TextView) findViewById(R.id.story_detail_desc)).setText(story
					.getDesc());
			// TODO Check if user has image, should be mandatory
			try {
				((ImageView) findViewById(R.id.story_detail_image))
						.setImageDrawable(Drawable.createFromStream(getAssets()
								.open(story.getImage()), story.getImage()));
			} catch (IOException e) {
				((ImageView) findViewById(R.id.story_detail_image))
						.setImageResource(R.drawable.dummy320240);
			}
		}
	}

	public void startStory(View v) {
		if (v.getId() == R.id.start_story_button) {
			if (story.requireGPS()
					&& isGPSDisabled()) {
				showNoGpsDialog();
				return;
			}
			startStory();
		}
	}

	protected void startStory() {
		storyApplication.emptyHistory();
		storyApplication.startTimer();
		startStoryActivity();
	}

	protected void startStoryActivity() {
		Intent intent = ClassVersionFactory.createIntent(this,
				StoryActivityHoneycomb.class, StoryActivity.class);
		intent.putExtra(StoryActivity.EXTRA_STORY, story);
		intent.putExtra(StoryActivity.EXTRA_TAG, story.getStartTag());
		startActivity(intent);
		finish();
	}

	protected void showNoGpsDialog() {
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
							dialog.cancel();
						}
					});
			enableGPSDialog = builder.create();
		}
		enableGPSDialog.show();
	}

	protected boolean isGPSDisabled() {
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return provider != null
				&& provider.contains("gps");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ENABLE_GPS) {
			startStory();
		}
	}
}
