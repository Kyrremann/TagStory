package no.tagstory.honeycomb;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import no.tagstory.TagStoryActivity;
import no.tagstory.utils.Database;

public class TagStoryActivityHoneyComb extends TagStoryActivity implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		storyCursor.moveToPosition(position);
		Intent detailIntent = new Intent(
				TagStoryActivityHoneyComb.this,
				StoryDetailActivityHoneycomb.class);
		detailIntent.putExtra(Database.STORY_ID,
				storyCursor.getString(0));
		startActivity(detailIntent);
	}
}
