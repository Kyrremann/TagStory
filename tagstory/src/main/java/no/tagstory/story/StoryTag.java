package no.tagstory.story;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.tagstory.story.game.quiz.QuizTypeEnum;
import no.tagstory.story.game.quiz.QuizNodeInterface;
import no.tagstory.story.game.quiz.TrueFalseQuizNode;

public class StoryTag implements Serializable {

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
	private List<StoryTagOption> options;
	private QuizTypeEnum quiztype;
	private List<QuizNodeInterface> quiz;
	private boolean skipable;

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

	public List<StoryTagOption> getOptions() {
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

	public void setOptions(List<StoryTagOption> options) {
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

	public String[] getOptionsAnswers() {
		String[] answers = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			answers[i] = options.get(i).getAnswer();
		}
		return answers;
	}

	public StoryTagOption getOption(int index) {
		return getOptions().get(index);
	}

	public StoryTagOption getFirstOption() {
		return getOptions().get(0);
	}

	public QuizTypeEnum getQuiztype() {
		return quiztype;
	}

	public void setQuiztype(QuizTypeEnum quiztype) {
		this.quiztype = quiztype;
	}

	public boolean isQuiztypeTrueFalse() {
		return QuizTypeEnum.TRUEFALSEQUIZ == this.quiztype;
	}

	public void setSkipable(boolean skipable) {
		this.skipable = skipable;
	}

	public boolean isSkipable() {
		return skipable;
	}

	public void addQuizNode(QuizNodeInterface node) {
		if (quiz == null) {
			quiz = new ArrayList<>();
		}
		quiz.add(node);
	}

	public List<QuizNodeInterface> getQuizQuestions() {
		return quiz;
	}
}
