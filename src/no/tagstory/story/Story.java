package no.tagstory.story;

import java.io.Serializable;
// import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Story implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String title;
	private String UUID;
	private String desc;
	private String author;
	// private Date date;
	private String date;
	private List<String> keywords;
	private int tagCount;
	private String ageGroup;
	private String genre;
	private String area;
	private String startTag;
	private String image;
	private String language;
	private String country;
	private String url;
	private HashMap<String, StoryTag> storyParts;
	private List<String> tagTypes;
	private List<String> gameModes;
	
	public Story(String UUID, String author, String title) {
		this.UUID = UUID;
		this.author = author;
		this.title = title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the hash
	 */
	public String getUUID() {
		return UUID;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}
	/**
	 * @return the tagCount
	 */
	public int getTagCount() {
		return tagCount;
	}
	/**
	 * @return the ageGroup
	 */
	public String getAgeGroup() {
		return ageGroup;
	}
	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @return the startTag
	 */
	public String getStartTag() {
		return startTag;
	}

	/**
	 * @return the parts
	 */
	public HashMap<String,StoryTag> getStoryParts() {
		return storyParts;
	}
	
	public StoryTag getTag(String key) {
		return storyParts.get(key);
	}
	
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the tagTypes
	 */
	public List<String> getTagTypes() {
		return tagTypes;
	}

	/**
	 * @return the gameModes
	 */
	public List<String> getGameModes() {
		return gameModes;
	}

	/**
	 * @param gameModes the gameModes to set
	 */
	public void setGameModes(List<String> gameModes) {
		this.gameModes = gameModes;
	}

	/**
	 * @param tagTypes the tagTypes to set
	 */
	public void setTagTypes(List<String> tagTypes) {
		this.tagTypes = tagTypes;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	public void addStoryPart(String key, StoryTag object) {
		storyParts.put(key, object);
	}

	/**
	 * @param storyParts the parts to set
	 */
	public void setStoryParts(HashMap<String, StoryTag> storyParts) {
		this.storyParts = storyParts;
	}

	/**
	 * @param startTag the startTag to set
	 */
	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}
	/**
	 * @param ageGroup the ageGroup to set
	 */
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}
	/**
	 * @param tagCount the tagCount to set
	 */
	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = Arrays.asList(keywords);
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		// TODO: Convert to date format
		 this.date = date;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @param UUID the hash to set
	 */
	public void setUUID(String UUID) {
		this.UUID = UUID;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public boolean requireGPS() {
		return getTagTypes().contains("GPS");
	}
}