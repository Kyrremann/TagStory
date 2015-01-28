package no.tagstory.story;

import android.annotation.SuppressLint;
import no.tagstory.story.game.QuizNode;
import no.tagstory.utils.StoryParser;

import java.io.Serializable;
import java.util.HashMap;

public class StoryTag implements Serializable {

	public static final String TAG_GPS = "gps";
	public static final String TAG_QR = "qr";
	public static final String TAG_NFC = "nfc";
	public static final String TAG_QR_NFC = "qr_nfc";

	private static final long serialVersionUID = -8334768426662100348L;
	private String UUID;
	private String belongsToTag;
	private String description;
	private String choiceDescription;
	private String choiceImage;
	private String gameMode;
	private String gameButton;
	private String tagMode;
	private boolean endpoint;
	private String optionsTitle;
	private HashMap<String, StoryTagOption> options;
	private HashMap<Integer, QuizNode> quiz;

	public StoryTag(String UUID, String belongsToTag, String description,
	                String isEndpoint) {
		this(UUID, belongsToTag, description, null, null, null, isEndpoint);
	}

	public StoryTag(String UUID, String belongsToTag, String description,
	                String gameMode, String isEndpoint) {
		this(UUID, belongsToTag, description, null, gameMode, null, isEndpoint);
	}

	@SuppressLint("UseSparseArrays")
	public StoryTag(String UUID, String belongsToTag, String description,
	                String choiceDescription, String gameMode,
	                HashMap<String, StoryTagOption> options, String isEndpoint) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
		this.setChoiceDescription(choiceDescription);
		this.setGameMode(gameMode);
		this.setOptions(options);
		this.setIsEndpoint(isEndpoint);

		if (getGameMode().equals(StoryParser.QUIZ))
			this.quiz = new HashMap<Integer, QuizNode>();
	}

	public StoryTag() {

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
	public HashMap<String, StoryTagOption> getOptions() {
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
	 * @param tagMode the tagMode to set
	 */
	public void setTagMode(String tagMode) {
		this.tagMode = tagMode;
	}

	/**
	 * @param gameButton the gameButton to set
	 */
	public void setGameButton(String gameButton) {
		this.gameButton = gameButton;
	}

	/**
	 * @param gameMode the gameMode to set
	 */
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	/**
	 * @param isEndpoint the endpoint to set
	 */
	public void setIsEndpoint(String isEndpoint) {
		if (isEndpoint.equalsIgnoreCase("true"))
			this.endpoint = true;
		else
			this.endpoint = false;
	}

	/**
	 * @param isEndpoint the endpoint to set
	 */
	public void setIsEndpoint(boolean isEndpoint) {
		this.endpoint = isEndpoint;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(HashMap<String, StoryTagOption> options) {
		this.options = options;
	}

	/**
	 * @param choiceDescription the choiceDescription to set
	 */
	public void setChoiceDescription(String choiceDescription) {
		this.choiceDescription = choiceDescription;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param belongsToTag the belongsToTag to set
	 */
	public void setBelongsToTag(String belongsToTag) {
		this.belongsToTag = belongsToTag;
	}

	/**
	 * @param uUID the uUID to set
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

	public String getChoiceImage() {
		return choiceImage;
	}

	public void setChoiceImage(String choiceImage) {
		this.choiceImage = choiceImage;
	}

	public boolean hasSingleQuestion() {
		return getChoiceDescription() != null
				&& getChoiceDescription().length() != 0;
	}

	public boolean hasSingleQuestionImage() {
		return getChoiceImage() != null && !getChoiceImage().equals("");
	}

	public boolean hasGameMode() {
		return getGameMode() != null
				&& (getGameMode().equals(StoryParser.QUIZ)
				|| getGameMode().equals(StoryParser.CAMERA));
	}

	public boolean isQuizGameMode() {
		return getGameMode().equals(StoryParser.QUIZ);
	}

	public boolean isCameraGameMode() {
		return getGameMode().equals(StoryParser.CAMERA);
	}

	public boolean hasOnlyOneOption() {
		return getOptions().size() == 1;
	}

	public String[] getOptionsTitles() {
		return getOptions().keySet().toArray(
				new String[getOptions().size()]);
	}

	public StoryTagOption getOption(String key) {
		return getOptions().get(key);
	}

	public StoryTagOption getFirstOption() {
		return getOptions().values().iterator().next();
	}

	public boolean isQrMode() {
		return tagMode.equals(StoryTag.TAG_QR);
	}
}
