package no.tagstory.story.activity;

import no.tagstory.kines_bursdag.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import no.tagstory.story.StoryPartOption;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoryPropagatingActivity extends Activity {

	private Story story;
	private StoryPart part;
	private StoryPartOption option;
	private String partTag;
	private String previousTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_propagating);

		story = (Story) getIntent().getSerializableExtra(StoryActivity.STORY);
		setTitle(story.getTitle()); // TODO: Should be part title
		partTag = getIntent().getStringExtra(StoryActivity.PARTTAG);
		part = story.getStoryPart(partTag);
		option = (StoryPartOption) getIntent().getSerializableExtra(
				StoryTravelActivity.OPTION);
		// TODO: previous tag not working properly, should be a list
		previousTag = getIntent().getStringExtra(StoryActivity.PREVIOUSTAG);

		((TextView) findViewById(R.id.story_propagating)).setText(option.getOptPropagatingText());
	}

	public void whereToGo(View v) {
		if (v.getId() == R.id.where_to_go) {
			startActivity(StoryActivity.createTravelIntent(this, story, part,
					option, partTag, previousTag, true));
		}
	}
}
