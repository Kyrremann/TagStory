package no.tagstory.story;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.HashMap;

import no.tagstory.communication.JsonParser;
import no.tagstory.story.StoryPartOption;
import no.tagstory.story.game.QuizNode;

public class StoryPart implements Serializable {

	public static final String TAG_GPS = "gps";
	public static final String TAG_QR = "qr";
	public static final String TAG_NFC = "nfc";
	public static final String TAG_QR_NFC = "qr_nfc";
	
	private static final long serialVersionUID = -8334768426662100348L;
	private String UUID;
	private String belongsToTag;
	private String description;
	private String choiceDescription;
	private String gameMode;
	private String gameButton;
	private String tagMode;
	private boolean endpoint;
	private String optionsTitle;
	private HashMap<String, StoryPartOption> options;
	private HashMap<Integer, QuizNode> quiz;

	public StoryPart(String UUID, String belongsToTag, String description,
			String isEndpoint) {
		this(UUID, belongsToTag, description, null, null, null, isEndpoint);
	}

	public StoryPart(String UUID, String belongsToTag, String description,
			String gameMode, String isEndpoint) {
		this(UUID, belongsToTag, description, null, gameMode, null, isEndpoint);
	}

	@SuppressLint("UseSparseArrays")
	public StoryPart(String UUID, String belongsToTag, String description,
			String choiceDescription, String gameMode,
			HashMap<String, StoryPartOption> options, String isEndpoint) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
		this.setChoiceDescription(choiceDescription);
		this.setGameMode(gameMode);
		this.setOptions(options);
		this.setIsEndpoint(isEndpoint);

		if (getGameMode().equals(JsonParser.QUIZ))
			this.quiz = new HashMap<Integer, QuizNode>();
	}

	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * @return the belongsToTag
	 */
	public String getBelongsToTag() {
		return belongsToTag;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the choiceDescription
	 */
	public String getChoiceDescription() {
		return choiceDescription;
	}

	/**
	 * @return the options
	 */
	public HashMap<String, StoryPartOption> getOptions() {
		return options;
	}
	
	public String getOptionsTitle() {
		return optionsTitle;
	}

	public void setOptionsTitle(String optionsTitle) {
		this.optionsTitle = optionsTitle;
	}

	/**
	 * @return the endpoint
	 */
	public boolean isEndpoint() {
		return endpoint;
	}

	/**
	 * @return the gameMode
	 */
	public String getGameMode() {
		if (gameMode == null) {
			return "";
		}
		return gameMode;
	}

	/**
	 * @return the quiz
	 */
	public HashMap<Integer, QuizNode> getQuiz() {
		return quiz;
	}

	public int getQuizSize() {
		return quiz.size();
	}

	public QuizNode getQuizNode(int location) {
		return quiz.get(location);
	}

	/**
	 * @return the gameButton
	 */
	public String getGameButton() {
		return gameButton;
	}

	/**
	 * @return the tagMode
	 */
	public String getTagMode() {
		return tagMode;
	}

	/**
	 * @param tagMode
	 *            the tagMode to set
	 */
	public void setTagMode(String tagMode) {
		this.tagMode = tagMode;
	}

	/**
	 * @param gameButton
	 *            the gameButton to set
	 */
	public void setGameButton(String gameButton) {
		this.gameButton = gameButton;
	}

	/**
	 * @param gameMode
	 *            the gameMode to set
	 */
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	/**
	 * @param endpoint
	 *            the endpoint to set
	 */
	public void setIsEndpoint(String isEndpoint) {
		if (isEndpoint.equalsIgnoreCase("true"))
			this.endpoint = true;
		else
			this.endpoint = false;
	}

	/**
	 * @param endpoint
	 *            the endpoint to set
	 */
	public void setIsEndpoint(boolean isEndpoint) {
		this.endpoint = isEndpoint;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(HashMap<String, StoryPartOption> options) {
		this.options = options;
	}

	/**
	 * @param choiceDescription
	 *            the choiceDescription to set
	 */
	public void setChoiceDescription(String choiceDescription) {
		this.choiceDescription = choiceDescription;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param belongsToTag
	 *            the belongsToTag to set
	 */
	public void setBelongsToTag(String belongsToTag) {
		this.belongsToTag = belongsToTag;
	}

	/**
	 * @param uUID
	 *            the uUID to set
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public void addToQuiz(int location, String question, boolean answer) {
		quiz.put(location, new QuizNode(question, answer));
	}

	public void addCorrectionToQuiz(int location, String correction) {
		quiz.get(location).setCorrection(correction);
	}

}
