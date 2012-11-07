package no.uio.ifi.inf5261.tagstory.story;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import no.uio.ifi.inf5261.tagstory.story.game.QuizNode;

public class StoryPart implements Serializable {

	private static final long serialVersionUID = -8334768426662100348L;
	private String UUID;
	private String belongsToTag;
	private String description;
	private String choiceDescription;
	private String gameMode;
	private String gameButton;
	private boolean isEndpoint;
	private HashMap<String, StoryPartOption> options;
	private ArrayList<QuizNode> quiz;

	public StoryPart(String UUID, String belongsToTag, String description,
			String isEndpoint) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
		this.setIsEndpoint(isEndpoint);
	}
	
	public StoryPart(String UUID, String belongsToTag, String description, String gameMode,
			String isEndpoint) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
		this.setGameMode(gameMode);
		this.setIsEndpoint(isEndpoint);
	}

	public StoryPart(String UUID, String belongsToTag, String description,
			String choiceDescription, String isEndpoint,
			HashMap<String, StoryPartOption> options) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
		this.setChoiceDescription(choiceDescription);
		this.setIsEndpoint(isEndpoint);
		this.setOptions(options);
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

	/**
	 * @return the isEndpoint
	 */
	public boolean getIsEndpoint() {
		return isEndpoint;
	}

	/**
	 * @return the gameMode
	 */
	public String getGameMode() {
		return gameMode;
	}

	/**
	 * @return the quiz
	 */
	public ArrayList<QuizNode> getQuiz() {
		return quiz;
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
	 * @param isEndpoint
	 *            the isEndpoint to set
	 */
	public void setIsEndpoint(String isEndpoint) {
		if (isEndpoint.equalsIgnoreCase("true"))
			this.isEndpoint = true;
		else
			this.isEndpoint = false;
	}

	/**
	 * @param isEndpoint
	 *            the isEndpoint to set
	 */
	public void setIsEndpoint(boolean isEndpoint) {
		this.isEndpoint = isEndpoint;
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
		quiz.add(location, new QuizNode(question, answer));
	}

	public void addCorrectionToQuiz(int location, String correction) {
		quiz.get(location).setCorrection(correction);
	}

}
