package no.tagstory.story;

import java.io.Serializable;

public class StoryTagOption implements Serializable {

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
	private String optPropagatingText;
	private double longitude;
	private double latitude;
	private boolean optArrowLength;
	private int zoomLevel;

	public StoryTagOption(String UUID, String optSelectMethod, String optNext) {
		this.UUID = UUID;
		this.optSelectMethod = optSelectMethod;
		this.optNext = optNext;
	}

	/**
	 * @return the optSelectMethod
	 */
	public String getOptSelectMethod() {
		if (optSelectMethod == null) {
			return "";
		}
		return optSelectMethod;
	}

	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * Placeholder method to the title of the tag.
	 * The title is the same as the UUID, but title sounds more sane.
	 * @return The value of the UUID
	 */
	public String getTitle() {
		return getUUID();
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
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the optLat
	 */
	public double getLatitude() {
		return latitude;
	}

	public String getOptPropagatingText() {
		return optPropagatingText;
	}

	public void setPropagatingText(String optPropagatingText) {
		this.optPropagatingText = optPropagatingText;
	}

	/**
	 * @param optArrowLength
	 *            the optArrowLength to set
	 */
	public void setArrowLength(boolean optArrowLength) {
		this.optArrowLength = optArrowLength;
	}

	/**
	 * @param optSoundSrc
	 *            the optSoundSrc to set
	 */
	public void setOptSoundSrc(String optSoundSrc) {
		this.optSoundSrc = optSoundSrc;
	}

	/**
	 * @param optImageSrc
	 *            the optImageSrc to set
	 */
	public void setImageSrc(String optImageSrc) {
		this.optImageSrc = optImageSrc;
	}

	/**
	 * @param optNext
	 *            the optNext to set
	 */
	public void setOptNext(String optNext) {
		this.optNext = optNext;
	}

	/**
	 * @param optHintText
	 *            the optHintText to set
	 */
	public void setHintText(String optHintText) {
		this.optHintText = optHintText;
	}

	/**
	 * @param uUID
	 *            the uUID to set
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	/**
	 * @param optSelectMethod
	 *            the optSelectMethod to set
	 */
	public void setOptSelectMethod(String optSelectMethod) {
		this.optSelectMethod = optSelectMethod;
	}

	public void setLongitude(double d) {
		this.longitude = d;
	}

	public void setLatitude(double d) {
		this.latitude = d;
	}

	/**
	 * @return the optArrowLength
	 */
	public boolean isOptArrowLength() {
		return optArrowLength;
	}

	public boolean hasHintText() {
		return getOptHintText().length() > 0;
	}

	public boolean hasHintImage() {
		return getOptSelectMethod().equals(StoryTagOption.HINT_IMAGE);
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}
}
