package no.uio.ifi.inf5261.tagstory.story;

import java.io.Serializable;

public class StoryPartOption implements Serializable {

	public static String HINT_IMAGE = "hint_image";
	public static String HINT_SOUND = "hint_sound";
	public static String HINT_TEXT = "hint_text";
	public static String HINT_MAP = "hint_map";
	public static String HINT_ARROW = "hint_arrow";
	
	private static final long serialVersionUID = -7137600478159292595L;
	private String UUID;
	private String optSelectMethod;
	private String optHintText;
	private String optNext;
	private String optImageSrc;
	private String optSoundSrc;
	private String optCord;
	private boolean optArrowLength;
	
	public StoryPartOption(String UUID, String optSelectMethod, String optHintText, String optNext) {
		this.setUUID(UUID);
		this.optSelectMethod = optSelectMethod;
		this.setOptHintText(optHintText);
		this.optNext = optNext;
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
	 * @return the optCord
	 */
	public String getOptCord() {
		return optCord;
	}

	/**
	 * @return the optArrowLength
	 */
	public boolean isOptArrowLength() {
		return optArrowLength;
	}

	/**
	 * @param optArrowLength the optArrowLength to set
	 */
	public void setOptArrowLength(boolean optArrowLength) {
		this.optArrowLength = optArrowLength;
	}

	/**
	 * @param optCord the optCord to set
	 */
	public void setOptCord(String optCord) {
		this.optCord = optCord;
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

}
