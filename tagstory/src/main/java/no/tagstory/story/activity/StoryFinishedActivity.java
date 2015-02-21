package no.tagstory.story.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.StoryApplication;
import no.tagstory.TagStoryActivity;
import no.tagstory.R;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.utils.http.ServerCommunication;

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
		showStatistics();
	}

	private void showStatistics() {
		StoryApplication application = (StoryApplication) getApplication();
		StoryStatistic storyStatistic = application.stopStory();
		((TextView) findViewById(R.id.story_statistic)).setText(storyStatistic.formatStatistic());

		if (!storyStatistic.isSaved()) {
			storyStatistic.saveToDatebase(this);
		}
	}

	public void endOfStory(View view) {
		Intent intent = new Intent(this, TagStoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

}
