package no.tagstory.story.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTagOption;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;

import java.io.FileNotFoundException;

public class StoryTravelActivity extends FragmentActivity {

	public static final String OPTION = "OPTION";
	private static final int QR_REQUEST_CODE = 0;
	protected Story story;
	protected StoryTagOption option;
	protected String tagId;
	protected TextView hintText;
	private Dialog helpDialog;
	private StoryApplication storyApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_option);

		Bundle bundle = getIntent().getExtras();
		option = (StoryTagOption) bundle.getSerializable(OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);
		storyApplication = (StoryApplication) getApplication();
		setTitle(option.getTitle());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (option.hasHintText()) {
			setHintText();
		}

		if (option.hasHintImage()) {
			setHintImage();
		}
	}

	private void setHintImage() {
		try {
			ImageView imageView = new ImageView(this);
			Bitmap myBitmap = BitmapFactory.decodeStream(openFileInput(option.getImageSrc()));
			imageView.setImageBitmap(myBitmap);
			((LinearLayout) findViewById(R.id.story_option_layout))
					.addView(imageView);
		} catch (FileNotFoundException e) {
		}
	}

	private void setHintText() {
		hintText = (TextView) findViewById(R.id.story_option_hint);
		hintText.setText(option.getHintText());
		hintText.setVisibility(View.VISIBLE);
	}

	public void scanTag(View v) {
		if (v.getId() == R.id.scan_tag) {
			if (story.getTag(tagId).getTagType().isQR()) {
				// TODO Should be switched out with an internal implementation
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, QR_REQUEST_CODE);
			}
		} else if (v.getId() == R.id.help_button) {
			showHelpDialog();
		}
	}

	private void showHelpDialog() {
		if (helpDialog == null) {
			helpDialog = createHelpDialog();
		}
		helpDialog.show();
	}

	protected AlertDialog createHelpDialog() {
		return createHelpDialog(R.string.dialog_qr_about_title, R.string.dialog_qr_about_content);
	}

	protected AlertDialog createHelpDialog(int title, int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNeutralButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		if (story.getTag(tagId).isSkipable()) {
			builder.setNegativeButton(R.string.skip,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							skipTag();
						}
					});
		}
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == QR_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.d("QR", "Contents: " + contents);
				Log.d("QR", "Format: " + format);
				checkTagData(contents);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	protected void checkTagData(String tagId) {
		// TODO: Implement randomized tags
		if (tagId.equals(option.getNext())) {
			goToNextActivity();
		} else {
			// TODO: What do to when user scan wrong tag?
			Toast.makeText(this, "Sorry, you're scanning the wrong tag", Toast.LENGTH_SHORT).show();
		}
	}

	protected void skipTag() {
		Log.d("SKIP", "User skipped tag");
		goToNextActivity();
	}

	private void goToNextActivity() {
		Intent intent = new Intent(this, StoryActivity.class);
		intent.putExtra(StoryActivity.EXTRA_STORY, story);
		intent.putExtra(StoryActivity.EXTRA_TAG, option.getNext());
		StoryHistory storyHistory = ((StoryApplication) getApplication()).getStoryHistory();
		storyHistory.push(story.getTag(option.getNext()));
		startActivity(intent);
		finish();
	}

	// TODO The following code is the same as the one in AbstractStoryActivity.java
	// It should be possible to share it...
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
		return storyApplication.getStoryHistory().getSize() == 0;
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
}
