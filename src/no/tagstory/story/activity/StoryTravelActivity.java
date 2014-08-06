package no.tagstory.story.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.StoryApplication;
import no.tagstory.R;
import no.tagstory.honeycomb.StoryActivityHoneycomb;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPartOption;
import no.tagstory.utils.ClassVersionFactory;

public class StoryTravelActivity extends FragmentActivity {

	public static final String OPTION = "OPTION";
	private static final int QR_REQUEST_CODE = 0;
	protected Story story;
	protected StoryPartOption option;
	protected String tagId;
	protected TextView hintText;
	private Dialog helpDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_option);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle.getSerializable(OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);

		setTitle(option.getTitle());

		if (option.hasHintText()) {
			setHintText();
		}

		if (option.hasHintImage()) {
			setHintImage();
		}
	}

	private void setHintImage() {
		((LinearLayout) findViewById(R.id.story_option_layout))
				.addView(createImageHint(option.getOptImageSrc()));
	}

	private void setHintText() {
		hintText = (TextView) findViewById(R.id.story_option_hint);
		hintText.setText(option.getOptHintText());
		hintText.setVisibility(View.VISIBLE);
	}

	private View createImageHint(String optImageSrc) {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(getResources().getIdentifier(optImageSrc,
				"drawable", getPackageName()));
		return imageView;
	}

	public void scanTag(View v) {
		if (v.getId() == R.id.scan_tag) {
			System.out.println("pressing scan");
			if (story.getTag(tagId).isQrMode()) {
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
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

	private AlertDialog createHelpDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.qr_about_title);
		builder.setMessage(R.string.qr_about_content);
		builder.setCancelable(true);
		builder.setNeutralButton(R.string.dialog_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
					                    int which) {
						dialog.cancel();
					}
				});
		builder.setNegativeButton(R.string.skip,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
					                    int which) {
						skipTag();
					}
				});
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
				// TODO: What to do when user scanning the wrong QR code?
				checkTagData(contents);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	protected void checkTagData(String tagId) {
		// TODO: Implement randomized tags
		if (tagId.equals(story.getTag(option.getOptNext())
				.getBelongsToTag())) {
			goToNextActivity();
		} else {
			// TODO: What do to when user scan wrong tag?
			Toast.makeText(this, "Sorry, you're scanning the wrong tag",
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void skipTag() {
		Log.d("SKIP", "User skipped tag");
		goToNextActivity();
	}

	private void goToNextActivity() {
		Intent intent = ClassVersionFactory.createIntent(getApplicationContext(),
				StoryActivityHoneycomb.class, StoryActivity.class);
		intent.putExtra(StoryActivity.EXTRA_STORY, story);
		intent.putExtra(StoryActivity.EXTRA_TAG, option.getOptNext());
		((StoryApplication) getApplication()).addTagTohistory(tagId, option.getOptNext());
		startActivity(intent);
		finish();
	}
}
