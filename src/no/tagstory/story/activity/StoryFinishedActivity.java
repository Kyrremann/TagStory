package no.tagstory.story.activity;

import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.TagStoryActivity;
import no.tagstory.communication.ServerCommunication;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.TextView;

public class StoryFinishedActivity extends Activity {

	private Story story;
	private StoryPart part;
	private String partTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.story_finished);
		setContentView(R.layout.activity_story_finished);

		story = (Story) getIntent().getSerializableExtra(StoryActivity.STORY);
		setTitle(story.getTitle());
		partTag = getIntent().getStringExtra(StoryActivity.PARTTAG);
		part = story.getStoryPart(partTag);

		((TextView) findViewById(R.id.story_finished_text)).setText(part
				.getDescription());
		printStatistics();
	}

	private void printStatistics() {
		// String statistics =
		// getResources().getString(R.string.dummy_statistics);
		StoryApplication application = (StoryApplication) getApplication();
		String distance = "" + application.distanceWalked();
		String time = ""
				+ ((System.currentTimeMillis() - application.getStartTime()) / 1000);
		String statistics = "Statistics:\n";
		statistics += "Distance: " + distance + "\n";
		statistics += "Time used: " + time;
		// statistics += "Now: " + System.currentTimeMillis();
		// statistics += "Then: " + application.getStartTime();
		((TextView) findViewById(R.id.story_statistic)).setText(statistics);
		ServerCommunication.sendTempStatistic(time, distance);
	}

	public void endOfStory(View view) {
		NavUtils.navigateUpTo(this, new Intent(this, TagStoryActivity.class));
		finish();
	}

}
