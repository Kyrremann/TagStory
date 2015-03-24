package no.tagstory.story.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;

public abstract class AbstractStoryActivity extends Activity {

	public static final String EXTRA_STORY = "EXTRA_STORY";
	public static final String EXTRA_TAG = "TAG";

	protected Story story;
	protected StoryTag tag;
	protected String tagId;
	protected StoryApplication storyApplication;
	protected StoryHistory storyHistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		storyApplication = (StoryApplication) getApplication();
		storyHistory = storyApplication.getStoryHistory();
		story = (Story) getIntent().getSerializableExtra(EXTRA_STORY);
		tagId = getIntent().getStringExtra(EXTRA_TAG);
		tag = story.getTag(tagId);
	}

	protected void setTagDescription() {
		((TextView) findViewById(R.id.description)).setText(tag.getDescription());
	}

	@Override
	public void onBackPressed() {
		if (storyHistory.hasPrevious()) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, storyHistory.getPreviousStory());
			storyHistory.previous();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
			// super.onBackPressed();
			// This is the root node, go back to StoryDetails
			Intent intent = ClassVersionFactory.createIntent(this,
					StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(Database.STORY_ID, story.getUUID());
			startActivity(intent);
			finish();
		}
	}
}
