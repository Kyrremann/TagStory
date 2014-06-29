package no.tagstory.honeycomb;

import no.tagstory.StoryDetailActivity;
import no.tagstory.story.activity.StoryActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class StoryDetailActivityHoneycomb extends StoryDetailActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					TagStoryActivityHoneyComb.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
