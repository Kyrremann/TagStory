package no.tagstory.story;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;
import com.amazonaws.util.StringUtils;
import no.tagstory.story.game.QuizNode;

import java.io.Serializable;
import java.util.HashMap;

public class StoryTag implements Serializable {

	public static final String TAG_GPS = "gps";
	public static final String TAG_QR = "qr";
	public static final String TAG_NFC = "nfc";
	public static final String TAG_QR_NFC = "qr_nfc";

	private static final long serialVersionUID = -8334768426662100348L;
	private String UUID;
	private String description;
	private String question;
	private String image;
	private GameModeEnum gameMode;
	private String travelButton;
	private TagTypeEnum tagType;
	private boolean endpoint;
	private String title;
	private HashMap<String, StoryTagOption> options;
	private HashMap<Integer, QuizNode> quiz;

	public StoryTag(String UUID, String description, boolean endpoint) {
		this.UUID = UUID;
		this.description = description;
		this.endpoint = endpoint;
	}

	public String getUUID() {
		return UUID;
	}

	public String getDescription() {
		return description;
	}


	public String getQuestion() {
		return question;
	}

	public HashMap<String, StoryTagOption> getOptions() {
		return options;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEndpoint() {
		return endpoint;
	}

	public GameModeEnum getGameMode() {
		return gameMode;
	}

	public HashMap<Integer, QuizNode> getQuiz() {
		return quiz;
	}

	public int getQuizSize() {
		return quiz.size();
	}

	public QuizNode getQuizNode(int location) {
		return quiz.get(location);
	}

	public String getTravelButton() {
		return travelButton;
	}

	public TagTypeEnum getTagType() {
		return tagType;
	}

	public void setTagType(TagTypeEnum tagType) {
		this.tagType = tagType;
	}

	public void setTravelButton(String travelButton) {
		this.travelButton = travelButton;
	}

	public void setGameMode(GameModeEnum gameMode) {
		this.gameMode = gameMode;
	}

	public void setOptions(HashMap<String, StoryTagOption> options) {
		this.options = options;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public void addToQuiz(int location, String question, boolean answer) {
		quiz.put(location, new QuizNode(question, answer));
	}

	public void addCorrectionToQuiz(int location, String correction) {
		quiz.get(location).setCorrection(correction);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean hasSingleQuestion() {
		return !StringUtil.isBlank(question);
	}

	public boolean hasSingleQuestionImage() {
		return !StringUtil.isBlank(image);
	}

	public boolean isQuiz() {
		return gameMode.isQuiz();
	}

	public boolean isCamera() {
		return gameMode.isCamera();
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
}
