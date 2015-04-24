package no.tagstory.story.game.quiz;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.tagstory.R;
import no.tagstory.story.StoryTagOption;
import no.tagstory.story.game.AbstractGameModeActivity;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class QuizActivity extends AbstractGameModeActivity {

	private int quizIndex;
	private int quizPoint;
	private LinearLayout layout;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (tag.isQuiztypeTrueFalse()) {
			setContentView(R.layout.activity_story_quiz_true_false);
			textView = (TextView) findViewById(R.id.question);
		} else {
			setContentView(R.layout.activity_quiz_multiple);
			textView = (TextView) findViewById(R.id.question_multiple);
		}
		quizIndex = 0;
		addQuestion();
	}

	private void addQuestion() {
		if (quizIndex >= tag.getQuizSize()) {
			goToTagDialog();
			return;
		}

		textView.setText(tag.getQuizNode(quizIndex).getQuestion());
		textView.setBackgroundResource(R.drawable.background_border_light_green);
		textView.setTextSize(21);
		textView.setPadding(16, 16, 16, 16);
	}

	public void quizAnswerMMultiple(View view) {
		QuizNodeInterface node = tag.getQuizNode(quizIndex);

		//TODO loop list of answers and build an alertdialog

		AlertDialog.Builder mBuildAnswers = new Builder(this);
		mBuildAnswers.setCancelable(false);
		mBuildAnswers.setAdapter(createListOfAnswers(node), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), "hello man, IMPLEMENT CORRECT ANSWER?", Toast.LENGTH_LONG).show();
				nextQuestion();
			}
		});
		mBuildAnswers.show();


	}

	private ArrayAdapter<String> createListOfAnswers(QuizNodeInterface node) {
		ArrayAdapter<String> mAnswers = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice,
				node.getMultipleAnswer());
		return mAnswers;
	}


	public void quizAnswer(View view) {
		// TODO: Add points to the story/user if the user answer correct
		QuizNodeInterface node = tag.getQuizNode(quizIndex);
		switch (view.getId()) {
			case R.id.quiz_true:
				if (node.isAnswer()) {
					quizPoint++;
					nextQuestion();
				} else {
					AlertDialog.Builder builder = createResultDialog(R.string.story_quiz_correction, node);
					builder.create().show();
				}
				break;
			case R.id.quiz_false:
				if (!node.isAnswer()) {
					quizPoint++;
					nextQuestion();
				} else {
					AlertDialog.Builder builder = createResultDialog(R.string.story_quiz_correction, node);
					builder.create().show();
				}
				break;
		}
	}

	private AlertDialog.Builder createResultDialog(int title, QuizNodeInterface node) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
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
		return builder;
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
