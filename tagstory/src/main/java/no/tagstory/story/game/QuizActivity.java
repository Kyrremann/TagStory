package no.tagstory.story.game;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.StoryTagOption;

import java.util.List;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class QuizActivity extends AbstractGameModeActivity {

	private int quizIndex;
	private int quizPoint;
	private LinearLayout layout;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz);

		layout = (LinearLayout) findViewById(R.id.activity_layout_quiz);
		quizIndex = 0;

		addQuestion();
	}

	private void addQuestion() {
		if (quizIndex >= tag.getQuizSize()) {
			goToTagDialog();
			return;
		}

		textView = new TextView(this);
		textView.setText(tag.getQuizNode(quizIndex).getQuestion());
		textView.setBackgroundResource(R.drawable.background_border_light_green);
		textView.setTextSize(21);
		textView.setPadding(16, 16, 16, 16);
		layout.addView(textView, 0);
	}

	public void quizAnswer(View view) {
		// TODO: Add points to the story/user if the user answer correct
		QuizNode node = tag.getQuizNode(quizIndex);
		switch (view.getId()) {
			case R.id.quiz_true:
				if (node.isAnswer()) {
					quizPoint++;
					nextQuestion();
				} else {
					AlertDialog.Builder builder = new Builder(this);
					builder.setCancelable(false);
					builder.setTitle(R.string.story_quiz_correction);
					if (node.getCorrection() != null) {
						builder.setMessage(node.getCorrection());
					} else {
						builder.setMessage("Du svarte feil");
					}
					builder.setNeutralButton(R.string.story_quiz_next, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							nextQuestion();
						}
					});
					builder.create().show();
				}
				break;
			case R.id.quiz_false:
				if (!node.isAnswer()) {
					quizPoint++;
					nextQuestion();
				} else {
					AlertDialog.Builder builder = new Builder(this);
					builder.setTitle(R.string.story_quiz_correction);
					builder.setCancelable(false);
					if (node.getCorrection() != null) {
						builder.setMessage(node.getCorrection());
					} else {
						builder.setMessage("Du svarte feil");
					}
					builder.setNeutralButton(R.string.story_quiz_next, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							nextQuestion();
						}
					});
					builder.create().show();
				}
				break;
		}
	}

	private void nextQuestion() {
		quizIndex++;
		addQuestion();
	}

	private void goToTagDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.story_quiz_next_part);
		builder.setCancelable(false);
		if (tag.getOptions().size() == 1) {
			final StoryTagOption option = tag.getFirstOption();
			builder.setMessage("You scored " + quizPoint
					+ " point(s) of possible " + tag.getQuizSize() + ".");
			builder.setNeutralButton(R.string.story_quiz_ready, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(createTravelIntent(getApplicationContext(), story, story.getTag(tagId), option));
					finish();
				}
			});
		} else {
			final List<StoryTagOption> options = tag.getOptions();
			builder.setMessage("You scored " + quizPoint + " point(s) of possible " + tag.getQuizSize() + ".");
			builder.setSingleChoiceItems(tag.getOptionsAnswers(), -1, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					StoryTagOption option = options.get(which);
					startActivity(createTravelIntent(getApplicationContext(), story, story.getTag(tagId), option));
					finish();
				}
			});

		}

		builder.create().show();
	}
}
