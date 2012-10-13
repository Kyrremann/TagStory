package no.uio.ifi.inf5261.tagstory.story;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.StoryListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;

public class StoryFinished extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.story_finished);
		setContentView(R.layout.activity_story_finished);
	}

	public void doneWithSotry(View view) {
		NavUtils.navigateUpTo(this, new Intent(this, StoryListActivity.class));
	}

}
