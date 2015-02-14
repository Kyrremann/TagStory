package no.tagstory.story.game;

import android.app.Activity;
import android.os.Bundle;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.activity.StoryActivity;

public abstract class AbstractGameModeActivity extends Activity {

	protected Story story;
	protected StoryTag tag;
	protected String tagId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);
		tag = story.getTag(tagId);
	}
}
