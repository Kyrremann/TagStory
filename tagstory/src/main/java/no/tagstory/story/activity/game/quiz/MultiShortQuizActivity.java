package no.tagstory.story.activity.game.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.activity.game.AbstractGameModeActivity;
import no.tagstory.story.game.quiz.MultipleAnswersQuizNode;

public class MultiShortQuizActivity extends AbstractGameModeActivity {

	TextView answer1, answer2, answer3, answer4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz_multi_short);
		questionView = (TextView) findViewById(R.id.question);

		answer1 = (TextView) findViewById(R.id.answer_1);
		answer2 = (TextView) findViewById(R.id.answer_2);
		answer3 = (TextView) findViewById(R.id.answer_3);
		answer4 = (TextView) findViewById(R.id.answer_4);
	}

	@Override
	protected void onNextQuestion() {
		MultipleAnswersQuizNode question = (MultipleAnswersQuizNode) currentQuestion;
		answer1.setText(question.getAnswers()[0]);
		answer2.setText(question.getAnswers()[1]);
		answer3.setText(question.getAnswers()[2]);
		answer4.setText(question.getAnswers()[3]);
	}

	@Override
	public void answer(View view) {
		String answer;
		switch (view.getId()) {
			case R.id.answer_1:
				answer = (String) ((TextView) view).getText();
				break;
			case R.id.answer_2:
				answer = (String) ((TextView) view).getText();
				break;
			case R.id.answer_3:
				answer = (String) ((TextView) view).getText();
				break;
			case R.id.answer_4:
				answer = (String) ((TextView) view).getText();
				break;
			default:
				answer = "";
		}
		checkCorrectAnswer(answer);
	}
}
