package no.tagstory.story.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class StoryPropagatingActivity extends Activity {

	private Story story;
	private StoryTag part;
	private StoryTagOption option;
	private String partTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_propagating);

		story = (Story) getIntent().getSerializableExtra(StoryActivity.EXTRA_STORY);
		setTitle(story.getTitle()); // TODO: Should be tag title
		partTag = getIntent().getStringExtra(StoryActivity.EXTRA_TAG);
		part = story.getTag(partTag);
		option = (StoryTagOption) getIntent().getSerializableExtra(
				StoryTravelActivity.OPTION);

		((TextView) findViewById(R.id.story_propagating)).setText(option.getPropagatingText());
	}

	public void whereToGo(View v) {
		if (v.getId() == R.id.where_to_go) {
			startActivity(createTravelIntent(this, story, part,
					option, true));
		}
	}
}
