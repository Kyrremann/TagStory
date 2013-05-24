package no.uio.ifi.inf5261.tagstory;

import java.io.IOException;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryDetailActivity extends FragmentActivity {

	private String ID;
	private StoryManager storyManager;
	private Story story;

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// MenuItem item = menu.add(Menu.NONE, 0, Menu.NONE,
	// R.string.detail_menu_start_story);
	// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	//
	// return true;
	// }

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
