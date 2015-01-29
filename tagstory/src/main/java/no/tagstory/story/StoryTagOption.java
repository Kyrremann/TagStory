package no.tagstory.story;

import java.io.Serializable;

public class StoryTagOption implements Serializable {

	private static final long serialVersionUID = -7137600478159292595L;

	private String UUID;
	private HintMethodEnum method;
	private String hintText;
	private String next;
	private String imageSrc;
	private String soundSrc;
	private String propagatingText;
	private double longitude;
	private double latitude;
	private String arrowLength;
	private int zoomLevel;

	public StoryTagOption(String UUID, HintMethodEnum method, String next) {
		this.UUID = UUID;
		this.method = method;
		this.next = next;
	}

	
	public HintMethodEnum getMethod() {
		return method;
	}

	
	public String getUUID() {
		return UUID;
	}

	
	public String getTitle() {
		return getUUID();
	}

	
	public String getHintText() {
		return hintText;
	}

	
	public String getNext() {
		return next;
	}

	
	public String getImageSrc() {
		return imageSrc;
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

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	
	public void setNext(String next) {
		this.next = next;
	}

	
	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	
	public void setUUID(String uUID) {
		UUID = uUID;
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
		return getHintText().length() > 0;
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
}
