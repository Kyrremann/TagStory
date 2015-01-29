package no.tagstory.story;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Story implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String UUID;
	private String description;
	private String author;
	private String date;
	private List<String> keywords;
	private String ageGroup;
	private String genre;
	private String area;
	private String startTag;
	private String image;
	private String language;
	private String country;
	private String url;
	private HashMap<String, StoryTag> storyParts;
	private List<TagTypeEnum> tagTypes;
	private List<GameModeEnum> gameModes;
	private int estimatedTime;
	private StoryStatusEnum status;

	public String getTitle() {
		return title;
	}

	public String getUUID() {
		return UUID;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public String getGenre() {
		return genre;
	}

	public String getArea() {
		return area;
	}

	public String getStartTagId() {
		return startTag;
	}

	public StoryTag getStartTag() {
		return getTag(startTag);
	}

	public HashMap<String, StoryTag> getStoryParts() {
		return storyParts;
	}

	public StoryTag getTag(String key) {
		return storyParts.get(key);
	}

	public String getImage() {
		return image;
	}

	public String getLanguage() {
		return language;
	}

	public String getCountry() {
		return country;
	}

	public String getUrl() {
		return url;
	}

	public List<TagTypeEnum> getTagTypes() {
		return tagTypes;
	}

	public List<GameModeEnum> getGameModes() {
		return gameModes;
	}

	public void setGameModes(List<GameModeEnum> gameModes) {
		this.gameModes = gameModes;
	}

	public void setTagTypes(List<TagTypeEnum> tagTypes) {
		this.tagTypes = tagTypes;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void addStoryPart(String key, StoryTag object) {
		storyParts.put(key, object);
	}

	public void setTags(HashMap<String, StoryTag> storyParts) {
		this.storyParts = storyParts;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = Arrays.asList(keywords);
	}

	public void setReleaseDate(String date) {
		// TODO: Convert to date format
		this.date = date;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setStatus(StoryStatusEnum status) {
		this.status = status;
	}

	public StoryStatusEnum getStatus() {
		return status;
	}
}