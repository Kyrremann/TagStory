package no.tagstory.story.activity.game.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.activity.game.AbstractGameModeActivity;

public class TrueFalseQuizActivity extends AbstractGameModeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz_true_false);
		questionView = (TextView) findViewById(R.id.question);
	}

	public void answer(View view) {
		switch (view.getId()) {
			case R.id.quiz_true:
				checkCorrectAnswer("true");
				break;
			case R.id.quiz_false:
				checkCorrectAnswer("false");
				break;
		}
	}
}
