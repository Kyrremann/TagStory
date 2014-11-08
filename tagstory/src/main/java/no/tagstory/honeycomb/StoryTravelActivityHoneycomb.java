package no.tagstory.honeycomb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.activity.StoryTravelActivity;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StoryTravelActivityHoneycomb extends StoryTravelActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, tagId);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
