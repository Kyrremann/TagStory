package no.uio.ifi.inf5261.tagstory.story.option;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;
import no.uio.ifi.inf5261.tagstory.story.StoryTravelActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuizActivity extends Activity {

	private Story story;
	private StoryPartOption option;
	private String partTag, previousTag;
	private int quizNumber;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle
				.getSerializable(StoryTravelActivity.OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);

		layout = (LinearLayout) findViewById(R.id.activity_quiz_layout);

		if (option.getOptHintText().length() > 0) {
			TextView textView = new TextView(this);
			textView.setText(option.getOptHintText());
			textView.setBackgroundResource(R.drawable.description_textview_green);
			layout.addView(textView);
		}

		addQuestion(quizNumber++);

	}

	private void addQuestion(int location) {
		TextView textView = new TextView(this);
		textView.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		textView.setBackgroundColor(R.drawable.description_textview_green);
		textView.setText(option.getFromQuiz(location).getQuestion());
		layout.addView(textView);
	}

	public void quizAnswere(View view) {

		// TODO: Add points to the story/user if the user answer correct
		switch (view.getId()) {
		case R.id.story_part_quiz_yes:
			break;
		case R.id.story_part_quiz_no:
			break;
		}
		addQuestion(quizNumber++);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem item = menu.add(Menu.NONE, 0, Menu.NONE,
				R.string.story_scan_tag);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
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
		} else if (item.getItemId() == 0) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, option.getOptNext());
			intent.putExtra(StoryActivity.PREVIOUSTAG, partTag);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
