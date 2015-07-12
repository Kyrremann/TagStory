package no.tagstory.story.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.TagStoryActivity;
import no.tagstory.R;
import no.tagstory.statistics.StoryStatistic;

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

		checkAndSetImages();
	}

	private void showStatistics() {
		storyApplication.stopStory();

		StoryStatistic storyStatistic = storyApplication.getStoryStatistic();
		if (!storyStatistic.isSaved()) {
			int id = storyStatistic.saveToDatebase(this);
			storyApplication.getStoryHistory().saveToDatabase(this, id);
		}

		((TextView) findViewById(R.id.story_statistic)).setText(storyStatistic.formatStatistic());
	}

	public void endOfStory(View view) {
		if (storyApplication.getStoryStatistic().hasId()) {
			storyApplication.deleteSavedStory();
		}
		Intent intent = new Intent(this, TagStoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

}
