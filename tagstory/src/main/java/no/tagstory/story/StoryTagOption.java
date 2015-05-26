package no.tagstory.story;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;

import java.io.Serializable;

public class StoryTagOption implements Serializable {

	private static final long serialVersionUID = -7137600478159292595L;

	private String answer;
	private HintMethodEnum method;
	private String hintText;
	private String next;
	private String title;
	private String soundSrc;
	private String propagatingText;
	private double longitude;
	private double latitude;
	private String arrowLength;
	private int zoomLevel;
	private String imageTop;
	private String imageBottom;

	public StoryTagOption(String answer, HintMethodEnum method, String next) {
		this.answer = answer;
		this.method = method;
		this.next = next;
	}

	public HintMethodEnum getMethod() {
		return method;
	}

	public String getAnswer() {
		return answer;
	}

	public String getTitle() {
		return StringUtil.isBlank(title) ? getAnswer() : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHintText() {
		return hintText;
	}

	public String getNext() {
		return next;
	}

	public String getSoundSrc() {
		return soundSrc;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getPropagatingText() {
		return propagatingText;
	}

	public void setPropagatingText(String propagatingText) {
		this.propagatingText = propagatingText;
	}

	public void setArrowLength(String arrowLength) {
		this.arrowLength = arrowLength;
	}

	public void setSoundSrc(String soundSrc) {
		this.soundSrc = soundSrc;
	}

	public String getArrowLength() {
		return arrowLength;
	}

	
	public void setNext(String next) {
		this.next = next;
	}

	
	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	
	public void setAnswer(String uUID) {
		answer = uUID;
	}

	public void setMethod(HintMethodEnum method) {
		this.method = method;
	}

	public void setLongitude(double d) {
		this.longitude = d;
	}

	public void setLatitude(double d) {
		this.latitude = d;
	}

	public boolean hasHintText() {
		return !StringUtil.isBlank(hintText);
	}

	public boolean hasHintImage() {
		return getMethod().isImage();
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public boolean isSound() {
		return method.isSound();
	}

	public boolean isArrow() {
		return method.isArrow();
	}

	public boolean isMap() {
		return method.isMap();
	}

	public boolean isImage() {
		return method.isImage();
	}

	public void setImageTop(String imageTop) {
		this.imageTop = imageTop;
	}

	public String getImageTop() {
		return imageTop;
	}

	public void setImageBottom(String imageBottom) {
		this.imageBottom = imageBottom;
	}

	public String getImageBottom() {
		return imageBottom;
	}

	public boolean hasImageTop() {
		return !StringUtil.isBlank(imageTop);
	}

	public boolean hasImageBottom() {
		return !StringUtil.isBlank(imageBottom);
	}
}
