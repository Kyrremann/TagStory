package no.uio.ifi.inf5261.tagstory.story;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoryPartOption implements Serializable {

	public static String HINT_IMAGE = "hint_image";
	public static String HINT_SOUND = "hint_sound";
	public static String HINT_TEXT = "hint_text";
	public static String HINT_MAP = "hint_map";
	public static String HINT_ARROW = "hint_arrow";
	public static String HINT_QUIZ = "hint_quiz";
	
	private static final long serialVersionUID = -7137600478159292595L;
	private String UUID;
	private String optSelectMethod;
	private String optHintText;
	private String optNext;
	private String optImageSrc;
	private String optSoundSrc;
	private double optLong;
	private double optLat;
	private boolean optArrowLength;
	private List<QuizNode> quiz;
	
	public StoryPartOption(String UUID, String optSelectMethod, String optHintText, String optNext) {
		this.setUUID(UUID);
		this.optSelectMethod = optSelectMethod;
		this.setOptHintText(optHintText);
		this.optNext = optNext;
		this.quiz = new ArrayList<QuizNode>();
	}

	/**
	 * @return the optSelectMethod
	 */
	public String getOptSelectMethod() {
		return optSelectMethod;
	}

	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * @return the optHintText
	 */
	public String getOptHintText() {
		return optHintText;
	}

	/**
	 * @return the optNext
	 */
	public String getOptNext() {
		return optNext;
	}

	/**
	 * @return the optImageSrc
	 */
	public String getOptImageSrc() {
		return optImageSrc;
	}

	/**
	 * @return the optSoundSrc
	 */
	public String getOptSoundSrc() {
		return optSoundSrc;
	}

	/**
	 * @return the optLong
	 */
	public double getOptLong() {
		return optLong;
	}
	
	/**
	 * @return the optLat
	 */
	public double getOptLat() {
		return optLat;
	}
	
	/**
	 * 
	 * @return the optQuiz
	 */
	public List<QuizNode> getQuiz() {
		return quiz;
	}
	
	/**
	 * 
	 * @param location of the question
	 * @retur the question at the specified location
	 * @see List
	 */
	public QuizNode getFromQuiz(int location) {
		return quiz.get(location);
	}

	/**
	 * @return the optArrowLength
	 */
	public boolean isOptArrowLength() {
		return optArrowLength;
	}
	
	public void addToQuiz(int location, String question, boolean answere) {
		quiz.add(location, new QuizNode(question, answere));
	}

	/**
	 * @param optArrowLength the optArrowLength to set
	 */
	public void setOptArrowLength(boolean optArrowLength) {
		this.optArrowLength = optArrowLength;
	}

	/**
	 * @param optSoundSrc the optSoundSrc to set
	 */
	public void setOptSoundSrc(String optSoundSrc) {
		this.optSoundSrc = optSoundSrc;
	}

	/**
	 * @param optImageSrc the optImageSrc to set
	 */
	public void setOptImageSrc(String optImageSrc) {
		this.optImageSrc = optImageSrc;
	}

	/**
	 * @param optNext the optNext to set
	 */
	public void setOptNext(String optNext) {
		this.optNext = optNext;
	}

	/**
	 * @param optHintText the optHintText to set
	 */
	public void setOptHintText(String optHintText) {
		this.optHintText = optHintText;
	}

	/**
	 * @param uUID the uUID to set
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	/**
	 * @param optSelectMethod the optSelectMethod to set
	 */
	public void setOptSelectMethod(String optSelectMethod) {
		this.optSelectMethod = optSelectMethod;
	}

	public void setOptLong(double d) {
		this.optLong = d;
	}

	public void setOptLat(double d) {
		this.optLat = d;
	}
	
	public class QuizNode {
		
		private String question;
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
		 * @return the answer
		 */
		public boolean isAnswer() {
			return answer;
		}
	}

}
