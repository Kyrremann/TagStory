package no.tagstory.honeycomb;

import no.tagstory.R;
import no.tagstory.StoryAdapter;
import no.tagstory.TagStoryActivity;
import no.tagstory.communication.Database;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TagStoryActivityHoneyComb extends TagStoryActivity {

	@Override
	protected void initializeListView() {
		listView = (ListView) findViewById(R.id.story_list);
		listView.setAdapter(new StoryAdapter(this, R.layout.story_list_item,
				storyCursor));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View view, int position,
					long id) {
				storyCursor.moveToPosition(position);
				Intent detailIntent = new Intent(
						TagStoryActivityHoneyComb.this,
						StoryDetailActivityHoneycomb.class);
				detailIntent.putExtra(Database.STORY_ID,
						storyCursor.getString(0) + ".json");
				startActivity(detailIntent);

			}
		});
	}
}
