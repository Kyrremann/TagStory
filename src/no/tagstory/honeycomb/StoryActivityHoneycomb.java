package no.tagstory.honeycomb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import no.tagstory.story.activity.StoryActivity;

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
//			Intent intent;
//			if (storyApplication.hasPreviousTag(tagId)) {
//				intent = new Intent(this, StoryDetailActivityHoneycomb.class);
//				intent.putExtra(EXTRA_STORY, story);
//				intent.putExtra(EXTRA_TAG, storyApplication.getPreviousTag(tagId));
//			} else {
//				intent = new Intent(this, StoryActivityHoneycomb.class);
//				intent.putExtra(EXTRA_STORY, story);
//				intent.putExtra(StoryActivity.EXTRA_TAG, tagId);
//			}
//			NavUtils.navigateUpTo(this, intent);
			// TODO Test if this is enough
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
