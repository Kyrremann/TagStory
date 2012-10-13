package no.uio.ifi.inf5261.tagstory.story;

import java.io.Serializable;
import java.util.HashMap;

public class StoryPart implements Serializable {

	private static final long serialVersionUID = -8334768426662100348L;
	private String UUID;
	private String belongsToTag;
	private String description;
	private String choiceDescription;
	private boolean isEndpoint;
	private HashMap<String, StoryPartOption> options;

	public StoryPart(String UUID, String belongsToTag, String description,
			String isEndpoint) {
		this.setUUID(UUID);
		this.setBelongsToTag(belongsToTag);
		this.setDescription(description);
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
}
