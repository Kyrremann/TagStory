package no.tagstory.story.game.quiz;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;

public class TrueFalseQuizNode implements QuizNodeInterface {

	public static final long serialVerisonID = 5751722078L;
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

	public boolean isCorrect(String answer) {
		return this.answer == Boolean.getBoolean(answer);
	}

	public boolean hasCorrection() {
		return StringUtil.isBlank(correction);
	}
}
