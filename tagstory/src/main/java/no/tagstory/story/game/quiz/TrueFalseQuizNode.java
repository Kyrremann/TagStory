package no.tagstory.story.game.quiz;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;
import no.tagstory.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class TrueFalseQuizNode implements QuizNodeInterface {

	private String question;
	private String correction;
	private boolean answer;

	public TrueFalseQuizNode(String question, boolean answer, String correction) {
		this.question = question;
		this.answer = answer;
		this.correction = correction;
	}

	public String getQuestion() {
		return question;
	}

	public String getCorrection() {
		return correction;
	}

	public boolean isCorrect(boolean answer) {
		return this.answer == answer;
	}

	public boolean hasCorrection() {
		return StringUtil.isBlank(correction);
	}
}
