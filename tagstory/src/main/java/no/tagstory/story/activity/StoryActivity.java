package no.tagstory.story.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;

import java.io.FileNotFoundException;

import static no.tagstory.story.activity.utils.TravelIntentUtil.*;

public class StoryActivity extends Activity {

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

	@Override
	protected void onResume() {
		super.onResume();

		setTitle(tag.getTitle());
		setTagDescription();

		if (tag.isEndpoint()) {
			switchToEndpointActivity();
		} else {
			if (tag.hasSingleQuestion()) {
				initializeSingleQuestion();
			}
			setTravelButton(tag);
		}
	}

	private void setTagDescription() {
		((TextView) findViewById(R.id.story_part_desc)).setText(tag
				.getDescription());
	}

	// TODO: Should be handled by the travel activity
	private void switchToEndpointActivity() {
		Intent intent = new Intent(this, StoryFinishedActivity.class);
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(EXTRA_TAG, tagId);
		startActivity(intent);
		finish();
	}

	private void initializeSingleQuestion() {
		((TextView) findViewById(R.id.story_part_choice)).setText(tag.getQuestion());
		if (tag.hasSingleQuestionImage()) {
			ImageView imageView = (ImageView) findViewById(R.id.story_tag_image);
			imageView.setVisibility(View.VISIBLE);
			String imagefile = tag.getImage();
			if (imagefile != null
					&& imagefile.length() != 0) {
				try {
					Bitmap myBitmap = BitmapFactory.decodeStream(openFileInput(imagefile));
					((ImageView) findViewById(R.id.story_detail_image))
							.setImageBitmap(myBitmap);
				} catch (FileNotFoundException e) {
				}
			}
		}
	}

	private void setTravelButton(StoryTag tag) {
		Button travelButton = (Button) findViewById(R.id.story_activity_travel);

		if (hasUserAlreadyVisitedTag()) {
			goDirectlyToNextTag(travelButton);
		} else if (tag.hasOnlyOneOption()) {
			onlyOneOption(tag, travelButton);
		} else {
			severalOptions(tag, travelButton);
		}
	}

	private boolean hasUserAlreadyVisitedTag() {
		return storyHistory.hasNext();
	}

	private void goDirectlyToNextTag(Button travelButton) {
		travelButton.setText(R.string.next_tag);
		travelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
				intent.putExtra(StoryActivity.EXTRA_STORY, story);
				intent.putExtra(StoryActivity.EXTRA_TAG, storyHistory.getNextStory().getUUID());
				storyHistory.next();
				startActivity(intent);
			}
		});
	}

	private void severalOptions(final StoryTag tag, Button button) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] keys = tag.getOptionsTitles();

		builder.setSingleChoiceItems(keys, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String selectedValue = (String) ((AlertDialog) dialog).getListView().getItemAtPosition(which);
				StoryTagOption option = tag.getOption(selectedValue);
				startActivity(createTravelIntent(
						getApplicationContext(), story, tag,
						option));
				dialog.cancel();
			}
		});

		button.setText(this.tag.getTravelButton());

		final Dialog dialog = builder.create();
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.show();
			}
		});
	}

	private void onlyOneOption(final StoryTag tag, Button button) {
		final StoryTagOption option = tag.getFirstOption();

		button.setText(tag.getTravelButton());

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent;
				if (tag.isQuiz()) {
					intent = createQuizActivity(getApplicationContext(), story, tagId);
				} else if (tag.isCamera()) {
					intent = createCameraActivity(getApplicationContext(), story, tagId);
				} else {
					intent = createTravelIntent(getApplicationContext(),
							story, tag, option);
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (storyHistory.hasPrevious()) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, storyHistory.getPreviousStory().getUUID());
			storyHistory.previous();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
			// super.onBackPressed();
			// This is the root node, go back to StoryDetails
			Intent intent = ClassVersionFactory.createIntent(this,
					StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(Database.STORY_ID, story.getUUID());
			startActivity(intent);
			finish();
		}
	}
}
