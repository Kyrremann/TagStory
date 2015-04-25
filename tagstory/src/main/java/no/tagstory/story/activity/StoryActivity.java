package no.tagstory.story.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import no.tagstory.R;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createCameraActivity;
import static no.tagstory.story.activity.utils.TravelIntentUtil.createQuizActivity;
import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class StoryActivity extends AbstractStoryActivity {

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
			setTravelButton();
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

	private void setTravelButton() {
		Button travelButton = (Button) findViewById(R.id.story_activity_travel);

		if (hasUserAlreadyVisitedTag()) {
			travelButton.setText(R.string.story_button_next_tag);
			goDirectlyToNextTagClickListener(travelButton);
		} else if (hasUserPlayedTheGame()) {
			travelButton.setText(R.string.story_button_next_tag);
			goDirectlyToTravelActivityClickListener();
		} else if (tag.hasOnlyOneOption()) {
			travelButton.setText(tag.getTravelButton());
			onlyOneOptionClickListener(travelButton);
		} else {
			travelButton.setText(tag.getTravelButton());
			severalOptionsClickListener(travelButton);
		}
	}

	private void goDirectlyToTravelActivityClickListener() {

	}

	private boolean hasUserPlayedTheGame() {
		return storyHistory.hasFinishedGame();
	}

	private boolean hasUserAlreadyVisitedTag() {
		return storyHistory.hasNext();
	}

	private void goDirectlyToNextTagClickListener(Button travelButton) {
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

	private void severalOptionsClickListener(Button travelButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setSingleChoiceItems(tag.getOptionsAnswers(), -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				StoryTagOption option = tag.getOption(which);
				startActivity(createTravelIntent(getApplicationContext(), story, tag, option));
				dialog.cancel();
			}
		});

		final Dialog dialog = builder.create();
		travelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.show();
			}
		});
	}

	private void onlyOneOptionClickListener(Button travelButton) {
		final StoryTagOption option = tag.getFirstOption();
		travelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent;
				if (tag.isQuiz()) {
					intent = createQuizActivity(getApplicationContext(), story, tagId, tag.isQuiztypeTrueFalse());
				} else if (tag.isCamera()) {
					intent = createCameraActivity(getApplicationContext(), story, tagId);
				} else {
					intent = createTravelIntent(getApplicationContext(), story, tag, option);
				}
				startActivity(intent);
			}
		});
	}
}
