package no.tagstory.story.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.StoryApplication;
import no.tagstory.TagStoryActivity;
import no.tagstory.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.utils.ServerCommunication;

public class StoryFinishedActivity extends AbstractStoryActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_finished);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTagDescription();
//		printStatistics();
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
		Intent intent = new Intent(this, TagStoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

}
