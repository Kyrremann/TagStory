package no.uio.ifi.inf5261.tagstory.story.game;

import java.util.HashMap;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryPart;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;
import no.uio.ifi.inf5261.tagstory.story.StoryTravelActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuizActivity extends Activity {

	private Story story;
	private StoryPart part;
	private String partTag, previousTag;
	private int quizNumber, quizPoint;
	private LinearLayout layout;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);
		part = story.getStoryPart(partTag);

		layout = (LinearLayout) findViewById(R.id.activity_layout_quiz);

		addQuestion(++quizNumber);
	}

	private void addQuestion(int location) {

		if (part.getQuizSize() < location) {
			goToTagDialog();
			return;
		}

		textView = new TextView(this);
		textView.setText(part.getQuizNode(location).getQuestion());
		textView.setBackgroundResource(R.drawable.description_textview_green);
		// textView.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		textView.setTextSize(21);
		textView.setPadding(16, 16, 16, 16);
		// textView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// v.setAlpha(1f);
		// }
		// });
		layout.addView(textView, 0);
	}

	public void quizAnswer(View view) {

		// TODO: Add points to the story/user if the user answer correct
		QuizNode node = part.getQuizNode(quizNumber);
		switch (view.getId()) {
		case R.id.story_part_quiz_yes:
			if (node.isAnswer())
				quizPoint++;
			break;
		case R.id.story_part_quiz_no:
			if (!node.isAnswer())
				quizPoint++;
			break;
		}

		textView.setAlpha(.5f);

		if (node.getCorrection() != null) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(R.string.story_quiz_correction);
			builder.setMessage(node.getCorrection());
			builder.setNeutralButton(R.string.story_quiz_next,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							addQuestion(++quizNumber);
						}
					});
			builder.create().show();
		} else
			addQuestion(++quizNumber);
	}

	private void goToTagDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.story_next_part);
		builder.setCancelable(false);
		if (part.getOptions().size() == 1) {
			final StoryPartOption option = part.getOptions().values()
					.iterator().next();
			builder.setMessage("You scored " + quizPoint
					+ " point(s) of possible " + part.getQuizSize() + ".");
			builder.setNeutralButton(R.string.story_ready,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = StoryActivity.createTravelIntent(
									getApplicationContext(), story, option,
									option.getOptNext(), partTag);
							startActivity(intent);
						}
					});
		} else {
			final HashMap<String, StoryPartOption> options = part.getOptions();
			final String[] keys = options.keySet().toArray(
					new String[options.size()]);

			builder.setMessage("You scored " + quizPoint
					+ " point(s) of possible " + part.getQuizSize() + ".");
			builder.setSingleChoiceItems(keys, -1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							StoryPartOption option = options
									.get(((AlertDialog) dialog).getListView()
											.getItemAtPosition(which));
							startActivity(StoryActivity.createTravelIntent(
									getApplicationContext(), story, option,
									option.getOptNext(), partTag));
						}
					});

		}

		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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
