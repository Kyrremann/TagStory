package no.tagstory.story.activity.game.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.activity.game.AbstractGameModeActivity;
import no.tagstory.story.game.quiz.QuizNodeInterface;
import no.tagstory.story.game.quiz.TrueFalseQuizNode;
import org.w3c.dom.Text;

import java.io.StringBufferInputStream;
import java.util.List;

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
		} else {
			quizScore--;
		}
		questionIndex++;

		if (currentQuestion.hasCorrection()) {
			// TODO show a dialog for correction
		} else {
			nextQuestion();
		}
	}

	private void showQuizScoreAndEnd() {

	}
}
