package no.tagstory.story.activity.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.game.quiz.QuizNodeInterface;
import no.tagstory.story.game.quiz.TrueFalseQuizNode;
import no.tagstory.utils.Database;

import java.util.List;

import static no.tagstory.story.activity.utils.TravelIntentUtil.createTravelIntent;

public abstract class AbstractGameModeActivity extends Activity {

	protected Story story;
	protected StoryTag tag;
	protected String tagId;
	protected StoryApplication application;

	protected int questionIndex;
	protected int quizScore;
	protected List<QuizNodeInterface> questions;
	protected QuizNodeInterface currentQuestion;
	protected TextView questionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);
		tag = story.getTag(tagId);
		application = (StoryApplication) getApplication();
		if (application.getStoryHistory().hasFinishedGame()) {
			endGame();
		}

		quizScore = 0;
		questionIndex = 0;
		questions = tag.getQuizQuestions();
	}

	@Override
	protected void onResume() {
		super.onResume();
		nextQuestion();
	}

	public abstract void answer(View view);

	protected void nextQuestion() {
		if (questionIndex >= questions.size()) {
			showQuizScoreAndEnd();
		} else {
			currentQuestion = questions.get(questionIndex);
			setQuestion();
			onNextQuestion();
		}
	}

	protected void onNextQuestion() {
	}

	protected void checkCorrectAnswer(String answer) {
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

	protected void setQuestion() {
		questionView.setText(currentQuestion.getQuestion());
	}

	protected void finishedGame() {
		application.getStoryHistory().setFinishedGame(true);
	}

	protected void endGame() {
		startActivity(createTravelIntent(getApplicationContext(), story, tag, tag.getFirstOption()));
		finish();
	}

	protected void saveQuizScore() {
		Database database = new Database(this);
		database.open();
		database.saveQuizScore(quizScore, tagId, story.getUUID());
		database.close();
	}

	protected void showQuizScoreAndEnd() {
		saveQuizScore();
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
