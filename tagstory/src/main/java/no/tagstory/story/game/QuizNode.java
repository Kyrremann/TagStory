package no.tagstory.story.game;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizNode implements Serializable {

	private static final long serialVersionUID = 782243272221115589L;
	private String question;
	private String correction;
	private boolean singleAnswer;
	private ArrayList<String> multipleAnswer;

	public QuizNode(String question, boolean singleAnswer) {
		this.question = question;
		this.singleAnswer = singleAnswer;
	}

	public QuizNode(String question, ArrayList<String> multipleAnswer) {
		this.question = question;
		this.multipleAnswer = new ArrayList<>();
		this.multipleAnswer.addAll(multipleAnswer);
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
		return singleAnswer;
	}

	public ArrayList<String> getMultipleAnswer() {
		return multipleAnswer;
	}
}
