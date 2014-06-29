package no.tagstory.honeycomb;

import no.tagstory.story.activity.StoryActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import static no.tagstory.story.activity.StoryActivity.EXTRA_TAG;

@SuppressLint("NewApi")
public class StoryActivityHoneycomb extends StoryActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent;
			if (storyApplication.hasPreviousTag(tagId)) {
				intent = new Intent(this, StoryDetailActivityHoneycomb.class);
				intent.putExtra(EXTRA_STORY, story);
				intent.putExtra(EXTRA_TAG, storyApplication.getPreviousTag(tagId));
			} else {
				intent = new Intent(this, StoryActivityHoneycomb.class);
				intent.putExtra(EXTRA_STORY, story);
				intent.putExtra(StoryActivity.EXTRA_TAG, tagId);
			}
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
