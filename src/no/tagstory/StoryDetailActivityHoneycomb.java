package no.tagstory;

import java.io.IOException;

import no.tagstory.Database.Database;
import no.tagstory.story.Story;
import no.tagstory.story.StoryManager;
import no.tagstory.story.activity.StoryActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryDetailActivityHoneycomb extends Activity {

	private String ID;
	private StoryManager storyManager;
	private Story story;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		storyManager = new StoryManager(this);

		ID = getIntent().getStringExtra(Database.STORY_ID);
		if (ID != null)
			story = storyManager.getStory(ID);
		else
			story = (Story) getIntent().getSerializableExtra(
					StoryActivity.STORY);

		if (story != null) {
			// ((TextView) findViewById(R.id.story_detail_title))
			// .setText(story.getTitle());
			// ((TextView) findViewById(R.id.story_detail_author))
			// .setText(story.getAuthor());
			setTitle(story.getTitle());
			// ((TextView) findViewById(R.id.story_detail_age))
			// .setText("Agegroup: " + story.getAgeGroup());
			((TextView) findViewById(R.id.story_detail_desc)).setText(story
					.getDesc());
			try {
				((ImageView) findViewById(R.id.story_detail_image))
						.setImageDrawable(Drawable.createFromStream(getAssets()
								.open(story.getImage()), story.getImage()));
				// .setImBackgroundDrawable(Drawable.createFromStream(getAssets().open(story.getImage()),
				// story.getImage()));
			} catch (IOException e) {
				// TODO: Add standard image
				e.printStackTrace();
			}
			// ((TextView) findViewById(R.id.story_detail_area))
			// .setText("Area: " + story.getArea());
			// ((TextView) findViewById(R.id.story_detail_date))
			// .setText(story.getDate());
			// ((TextView) findViewById(R.id.story_detail_keywords))
			// .setText(story.getKeywords().toString());
			// ((TextView) findViewById(R.id.story_detail_tagcount))
			// .setText(story.getTagCount() + " tags");
			// ((TextView) findViewById(R.id.story_detail_genre))
			// .setText("Genre: " + story.getGenre());
		}
	}

	public void startStory(View v) {
		if (v.getId() == R.id.start_story_button) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, story.getStartTag());
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					StoryListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
