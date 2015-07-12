package no.tagstory.story.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;

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

	protected void checkAndSetImages() {
		if (tag.hasImageTop()) {
			setImage(tag.getImageTop(), R.id.story_tag_image_top);
		}
		if (tag.hasImageMiddle()) {
			setImage(tag.getImageMiddle(), R.id.story_tag_image_middle);
		}
		if (tag.hasImageBottom()) {
			setImage(tag.getImageBottom(), R.id.story_tag_image_bottom);
		}
	}

	private void setImage(String imagefile, int viewId) {
		ImageView imageView = (ImageView) findViewById(viewId);
		imageView.setVisibility(View.VISIBLE);
		if (imagefile != null && imagefile.length() != 0) {
			try {
				Bitmap myBitmap = BitmapFactory.decodeStream(openFileInput(imagefile));
				imageView.setImageBitmap(myBitmap);
			} catch (FileNotFoundException e) {
				Log.d("TAG_IMAGE", "Can't find the image, hiding view");
				imageView.setVisibility(View.GONE);
			}
		}
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

		storyApplication.saveStory(story.getUUID());
	}

	private boolean hasNotFinishedTheFirstTag() {
		return storyHistory.getSize() == 1;
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
