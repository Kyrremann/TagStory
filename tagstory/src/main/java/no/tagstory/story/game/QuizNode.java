package no.tagstory.story.game;

import java.io.Serializable;

public class QuizNode implements Serializable {

	private static final long serialVersionUID = 782243272221115589L;
	private String question;
	private String correction;
	private boolean answer;

	public QuizNode(String question, boolean answer) {
		this.question = question;
		this.answer = answer;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @return the correction
	 */
	public String getCorrection() {
		return correction;
	}

	/**
	 * @param correction the correction to set
	 */
	public void setCorrection(String correction) {
		this.correction = correction;
	}

	/**
	 * @return the answer
	 */
	public boolean isAnswer() {
		return answer;
	}
}
