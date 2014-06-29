package no.tagstory.story.activity;

import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.TagStoryActivity;
import no.tagstory.story.StoryTag;
import no.tagstory.utils.ServerCommunication;
import no.tagstory.story.Story;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoryFinishedActivity extends Activity {

	private Story story;
	private StoryTag part;
	private String partTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.story_finished);
		setContentView(R.layout.activity_story_finished);

		story = (Story) getIntent().getSerializableExtra(StoryActivity.EXTRA_STORY);
		setTitle(story.getTitle());
		partTag = getIntent().getStringExtra(StoryActivity.EXTRA_TAG);
		part = story.getStoryPart(partTag);

		((TextView) findViewById(R.id.story_finished_text)).setText(part
				.getDescription());
		printStatistics();
	}

	private void printStatistics() {
		// String statistics =
		// getResources().getString(R.string.dummy_statistics);
		StoryApplication application = (StoryApplication) getApplication();
		String distance = "" + (int) application.distanceWalked();
		String time = ""
				+ (int) (((System.currentTimeMillis() - application.getStartTime()) / 1000) / 60);
		String statistics = "Statistics:\n";
		statistics += "Distance: " + distance + "meter\n";
		statistics += "Time used: " + time + "minutter";
		// statistics += "Now: " + System.currentTimeMillis();
		// statistics += "Then: " + application.getStartTime();
		((TextView) findViewById(R.id.story_statistic)).setText(statistics);
		ServerCommunication.sendTempStatistic(time, distance);
	}

	public void endOfStory(View view) {
		// NavUtils.navigateUpTo(this, new Intent(this, TagStoryActivity.class));
		// finish();
		Intent intent = new Intent(this, TagStoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
