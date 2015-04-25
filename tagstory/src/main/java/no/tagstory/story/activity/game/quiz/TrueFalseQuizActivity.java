package no.tagstory.story.activity.game.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.story.activity.game.AbstractGameModeActivity;
import no.tagstory.story.game.quiz.QuizNodeInterface;
import no.tagstory.story.game.quiz.TrueFalseQuizNode;

import java.util.List;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public class TrueFalseQuizActivity extends AbstractGameModeActivity {

	private int questionIndex;
	private int quizScore;
	private TrueFalseQuizNode currentQuestion;
	private TextView questionView;
	private List<QuizNodeInterface> questions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz_true_false);
		questionView = (TextView) findViewById(R.id.question);
		questionIndex = 0;
		questions = tag.getQuizQuestions();
	}

	@Override
	protected void onResume() {
		super.onResume();
		nextQuestion();
	}

	private void nextQuestion() {
		if (questionIndex >= questions.size()) {
			showQuizScoreAndEnd();
		} else {
			currentQuestion = (TrueFalseQuizNode) questions.get(questionIndex);
			questionView.setText(currentQuestion.getQuestion());
		}
	}

	public void answer(View view) {
		switch (view.getId()) {
			case R.id.quiz_true:
				checkCorrectAnswer(true);
				break;
			case R.id.quiz_false:
				checkCorrectAnswer(false);
				break;
		}
	}

	private void checkCorrectAnswer(boolean answer) {
		if (currentQuestion.isCorrect(answer)) {
			quizScore++;
			Toast.makeText(this, R.string.quiz_correct_answer, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.quiz_wrong_answer, Toast.LENGTH_SHORT).show();
		}
		questionIndex++;

		if (currentQuestion.hasCorrection()) {
			// TODO show a dialog for correction
			nextQuestion();
		} else {
			nextQuestion();
		}
	}

	private void showQuizScoreAndEnd() {
		// TODO Save quiz score to database
		finishedGame();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_quiz_continue_title);
		builder.setMessage(getString(R.string.dialog_quiz_continue_message, quizScore, questions.size()));
		builder.setNeutralButton(R.string.dialog_quiz_continue, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				endGame();
				dialog.cancel();
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}
}
