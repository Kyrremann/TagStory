package no.uio.ifi.inf5261.tagstory;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

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
        
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            ID = getIntent().getStringExtra(Database.STORY_ID);
            if (ID != null)
            story = storyManager.getStory(ID);
            else
            	story = (Story) getIntent().getSerializableExtra(StoryActivity.STORY);
            arguments.putSerializable(StoryActivity.STORY, story);
            StoryDetailFragment fragment = new StoryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.story_detail_container, fragment)
                    .commit();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	MenuItem item = menu.add(Menu.NONE, 0, Menu.NONE, R.string.detail_menu_start_story);
    	item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, StoryListActivity.class));
            return true;
        } else if (item.getItemId() == 0) {
        	Intent intent = new Intent(this, StoryActivity.class);
        	intent.putExtra(StoryActivity.STORY, story);
        	intent.putExtra(StoryActivity.PARTTAG, story.getStartTag());
        	startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
