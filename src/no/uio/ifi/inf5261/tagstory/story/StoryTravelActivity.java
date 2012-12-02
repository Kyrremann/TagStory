package no.uio.ifi.inf5261.tagstory.story;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.story.option.NFCActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoryTravelActivity extends NFCActivity {

	public static final String OPTION = "OPTION";
	private String previousTag;
	private View layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_option);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle.getSerializable(OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);

		setTitle(option.getUUID());

		((TextView) findViewById(R.id.story_option_hint)).setText(option
				.getOptHintText());
		layout = findViewById(R.id.story_option_layout);

		if (option.getOptSelectMethod().equals(StoryPartOption.HINT_IMAGE)) {
			((LinearLayout) layout).addView(createImageHint(option
					.getOptImageSrc()));
		}
	}

	private View createImageHint(String optImageSrc) {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(getResources().getIdentifier(optImageSrc,
				"drawable", getPackageName()));
		return imageView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, partTag);
			intent.putExtra(StoryActivity.PREVIOUSTAG, previousTag);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
