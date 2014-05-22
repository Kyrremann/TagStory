package no.tagstory.story.activity;

import no.tagstory.kines_bursdag.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import no.tagstory.story.StoryPartOption;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StoryTravelActivity extends FragmentActivity {

	protected static final String OPTION = "OPTION";
	protected String previousTag;
	protected Story story;
	protected StoryPartOption option;
	protected String partTag;
	protected TextView hintText;
	protected ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_option);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle.getSerializable(OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);

		setTitle(option.getUUID());
		if (option.getOptHintText().length() > 0) {
			hintText = (TextView) findViewById(R.id.story_option_hint);
			hintText.setText(option.getOptHintText());
			hintText.setVisibility(View.VISIBLE);
		}

		if (option.getOptSelectMethod().equals(StoryPartOption.HINT_IMAGE)) {
			((LinearLayout) findViewById(R.id.story_option_layout))
					.addView(createImageHint(option.getOptImageSrc()));
		}
		// if (story.getStoryPart(partTag).getOptionsTitle() != null) {
		// ((TextView) findViewById(R.id.story_activity_travel)).setText(story
		// .getStoryPart(partTag).getOptionsTitle());
		// }
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
			String tagMode = story.getStoryPart(partTag).getTagMode();
			if (tagMode.equals(StoryPart.TAG_QR)) {
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// The Intents Fairy has delivered us some data!
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				Log.d("QR", "Contents: " + contents);
				Log.d("QR", "Format: " + format);
				// TODO: What to do when user scanning the wrong QR code?
				checkTagData(contents, false);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	protected void checkTagData(String tag, boolean debug) {
		// TODO: Implement randomized tags
		if (tag.equals(story.getStoryPart(option.getOptNext())
				.getBelongsToTag()) || debug) {
			// progressDialog.dismiss();
			if (!debug) {
				Log.d("TAG", "Correct tag: " + tag);
			} else {
				Log.d("TAG", "Someone is cheating");
			}
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, option.getOptNext());
			intent.putExtra(StoryActivity.PREVIOUSTAG, partTag);
			startActivity(intent);
		} else {
			// TODO: What do to when user scan wrong tag?
			Toast.makeText(this, "Sorry, you're scanning the wrong tag",
					Toast.LENGTH_SHORT).show();
		}
	}
}
