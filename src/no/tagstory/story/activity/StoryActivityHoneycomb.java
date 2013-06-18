package no.tagstory.story.activity;

import no.tagstory.StoryDetailActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

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
			if (previousTag == null) {
				intent = new Intent(this, StoryDetailActivity.class);
				intent.putExtra(STORY, story);
			} else {
				intent = new Intent(this, StoryActivity.class);
				intent.putExtra(STORY, story);
				intent.putExtra(StoryActivity.PARTTAG, partTag);
			}
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
