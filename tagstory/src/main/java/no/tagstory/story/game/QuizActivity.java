package no.tagstory.story.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import no.tagstory.story.activity.StoryActivity;

import java.util.HashMap;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class QuizActivity extends Activity {

	private Story story;
	private StoryTag part;
	private String partTag; //, previousTag;
	private int quizNumber, quizPoint;
	private LinearLayout layout;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		partTag = bundle.getString(StoryActivity.EXTRA_TAG);
		part = story.getTag(partTag);

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
		textView.setBackgroundResource(R.drawable.background_border_light_green);
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
				else {
					AlertDialog.Builder builder = new Builder(this);
					builder.setTitle(R.string.story_quiz_correction);
					if (node.getCorrection() != null)
						builder.setMessage(node.getCorrection());
					else
						builder.setMessage("Du svarte feil");
					builder.setNeutralButton(R.string.story_quiz_next,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
								                    int which) {
									dialog.dismiss();
									// addQuestion(++quizNumber);
								}
							});
					builder.create().show();
				}
				break;
			case R.id.story_part_quiz_no:
				if (!node.isAnswer())
					quizPoint++;
				else {
					AlertDialog.Builder builder = new Builder(this);
					builder.setTitle(R.string.story_quiz_correction);
					if (node.getCorrection() != null)
						builder.setMessage(node.getCorrection());
					else
						builder.setMessage("Du svarte feil");
					builder.setNeutralButton(R.string.story_quiz_next,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
								                    int which) {
									dialog.dismiss();
									// addQuestion(++quizNumber);
								}
							});
					builder.create().show();
				}
				break;
		}

		// TODO textView.setAlpha(.5f);

		addQuestion(++quizNumber);
	}

	private void goToTagDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.story_next_part);
		builder.setCancelable(false);
		if (part.getOptions().size() == 1) {
			final StoryTagOption option = part.getOptions().values()
					.iterator().next();
			builder.setMessage("You scored " + quizPoint
					+ " point(s) of possible " + part.getQuizSize() + ".");
			builder.setNeutralButton(R.string.story_ready,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = createTravelIntent(
									getApplicationContext(), story, story.getTag(partTag), option
							);
							startActivity(intent);
						}
					});
		} else {
			final HashMap<String, StoryTagOption> options = part.getOptions();
			final String[] keys = options.keySet().toArray(
					new String[options.size()]);

			builder.setMessage("You scored " + quizPoint
					+ " point(s) of possible " + part.getQuizSize() + ".");
			builder.setSingleChoiceItems(keys, -1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							StoryTagOption option = options
									.get(((AlertDialog) dialog).getListView()
											.getItemAtPosition(which));
							startActivity(createTravelIntent(
									getApplicationContext(), story, story.getTag(partTag), option
							));
						}
					});

		}

		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, tagId);
			intent.putExtra(StoryActivity.PREVIOUSTAG, previousTag);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/
}
