package no.tagstory.story.activity.game.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.story.activity.game.AbstractGameModeActivity;
import no.tagstory.story.game.quiz.MultipleAnswersQuizNode;

public class MultiLongQuizActivity extends AbstractGameModeActivity implements Dialog.OnClickListener {

	private Dialog answersDialog;
	private String[] answers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_quiz_multi_long);
		questionView = (TextView) findViewById(R.id.question);
	}

	@Override
	protected void onNextQuestion() {
		answers = ((MultipleAnswersQuizNode) currentQuestion).getAnswers();
	}

	@Override
	public void answer(View view) {
		if (answersDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_quiz_answers_title);
			builder.setCancelable(true);
			builder.setSingleChoiceItems(answers, -1, this);
			answersDialog = builder.create();
		}
		answersDialog.show();
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int which) {
		if (which > 0 && which < answers.length) {
			checkCorrectAnswer(answers[which]);
		}
		dialogInterface.dismiss();
	}
}
