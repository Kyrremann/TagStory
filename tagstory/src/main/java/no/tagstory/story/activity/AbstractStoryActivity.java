package no.tagstory.story.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;

public abstract class AbstractStoryActivity extends Activity implements AlertDialog.OnClickListener {

	public static final String EXTRA_STORY = "EXTRA_STORY";
	public static final String EXTRA_TAG = "TAG";

	protected Story story;
	protected StoryTag tag;
	protected String tagId;
	protected StoryApplication storyApplication;
	protected StoryHistory storyHistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		storyApplication = (StoryApplication) getApplication();
		storyHistory = storyApplication.getStoryHistory();
		story = (Story) getIntent().getSerializableExtra(EXTRA_STORY);
		tagId = getIntent().getStringExtra(EXTRA_TAG);
		tag = story.getTag(tagId);
	}

	protected void setTagDescription() {
		((TextView) findViewById(R.id.description)).setText(tag.getDescription());
	}

	@Override
	public void onBackPressed() {
		if (storyHistory.hasPrevious()) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, storyHistory.getPreviousStoryId());
			storyHistory.previous();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
			// super.onBackPressed();
			// This is the root node, go back to StoryDetails
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_leave_story_title);
			builder.setMessage(R.string.dialog_leave_story_message);
			builder.setPositiveButton(R.string.yes, this);
			builder.setNeutralButton(R.string.cancel, this);
			builder.setNegativeButton(R.string.no, this);
			builder.create().show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tag_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save_and_quit:
				saveStory();
				quitStory();
				break;
			case R.id.menu_quit:
				quitStory();
				break;
		}
		return true;
	}

	public void saveStory() {
		if (isStartTag() && hasNotFinishedTheFirstTag()) {
			Toast.makeText(this, R.string.toast_cant_save_start_tag, Toast.LENGTH_SHORT).show();
			return;
		}

		StoryStatistic mStoryStatistic = storyApplication.stopStory();
		StoryHistory mStoryHistory = storyApplication.getStoryHistory();
		int statisticsId = mStoryStatistic.saveToDatebase(this);
		mStoryHistory.saveToDatabase(this, statisticsId);
		Database database = new Database(this);
		database.open();
		database.insertSaveTravel(statisticsId, story.getUUID());
		database.close();
	}

	private boolean hasNotFinishedTheFirstTag() {
		return storyHistory.getSize() == 0;
	}

	private boolean isStartTag() {
		return story.getStartTagId().equals(tagId);
	}

	private void quitStory() {
		storyApplication.stopStory();
		Intent intent = ClassVersionFactory.createIntent(this,
				StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(StoryParser.UUID, story.getUUID());
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				saveStory();
				quitStory();
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				quitStory();
				break;
		}
		dialog.dismiss();
	}
}
