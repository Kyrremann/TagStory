package no.tagstory.story.activity.game;

import android.app.Activity;
import android.os.Bundle;

import no.tagstory.StoryApplication;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.activity.StoryActivity;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public abstract class AbstractGameModeActivity extends Activity {

	protected Story story;
	protected StoryTag tag;
	protected String tagId;
	protected StoryApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);
		tag = story.getTag(tagId);
		application = (StoryApplication) getApplication();
		if (application.getStoryHistory().hasFinishedGame()) {
			endGame();
		}
	}

	public void finishedGame() {
		application.getStoryHistory().setFinishedGame(true);
	}

	public void endGame() {
		startActivity(createTravelIntent(getApplicationContext(), story, tag, tag.getFirstOption()));
		finish();
	}
}
