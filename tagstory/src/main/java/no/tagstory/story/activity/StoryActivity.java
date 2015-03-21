package no.tagstory.story.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.TagStoryActivity;
import no.tagstory.honeycomb.TagStoryActivityHoneycomb;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;

import java.io.FileNotFoundException;

import static no.tagstory.story.activity.utils.TravelIntentUtil.*;

public class StoryActivity extends AbstractStoryActivity {
	Database mDatabase;

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

	// TODO: Should be handled by the travel activity
	private void switchToEndpointActivity() {
		Intent intent = new Intent(this, StoryFinishedActivity.class);
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(EXTRA_TAG, tagId);
		startActivity(intent);
		finish();
	}

	private void initializeSingleQuestion() {
		TextView questionView = (TextView) findViewById(R.id.story_part_choice);
		questionView.setText(tag.getQuestion());
		questionView.setVisibility(View.VISIBLE);

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
		travelButton.setText(R.string.story_button_next_tag);
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
		builder.setSingleChoiceItems(tag.getOptionsAnswers(), -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				StoryTagOption option = tag.getOption(which);
				startActivity(createTravelIntent(getApplicationContext(), story, tag, option));
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
					intent = createTravelIntent(getApplicationContext(), story, tag, option);
				}
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tag_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save_and_quit_story:
				saveAndQuitStory();
				break;
		}
		return true;
	}

	public void saveAndQuitStory() {
		//TODO
		//pause statistikken, lagre statistikken i databasen og continue i storydetail
		mDatabase = new Database(this);
		mDatabase.open();

		StoryHistory mStoryNode = storyApplication.getStoryHistory();
		StoryStatistic mStoryStatistic = storyApplication.getStoryStatistic();
		mDatabase.insertSQ(mStoryStatistic.getStoryId(), mStoryNode.getRootNode().getTagUUID());
		Cursor cursor = mDatabase.getSQ(mStoryNode.getRootNode().getTagUUID());

		mDatabase.insertLocation(cursor.getString(1),
				mStoryStatistic.getLastLocationLatitude(),mStoryStatistic.getLastLocationLongitude());
		mDatabase.insertHistory(mStoryNode.getStory().getUUID(), mStoryNode.getPreviousStory().getUUID()
				, mStoryNode.getNextStory().getUUID());
		this.storyApplication.stopStory();

		finish();

	}

}
