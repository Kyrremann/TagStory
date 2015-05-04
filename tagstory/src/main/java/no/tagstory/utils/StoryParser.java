package no.tagstory.utils;

import android.content.Context;

import no.tagstory.story.game.quiz.QuizFactory;
import no.tagstory.story.game.quiz.QuizNodeInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.tagstory.story.GameModeEnum;
import no.tagstory.story.HintMethodEnum;
import no.tagstory.story.Story;
import no.tagstory.story.StoryStatusEnum;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import no.tagstory.story.TagTypeEnum;
import no.tagstory.story.game.quiz.QuizTypeEnum;

public class StoryParser {

	// Main content
	public static final String STORY = "story";
	public static final String UUID = "UUID";
	public static final String AUTHOR = "author";
	public static final String TITLE = "title";
	public static final String RELEASE_DATE = "release_date";
	public static final String AGEGROUP = "age_group";
	public static final String GENRE = "genre";
	public static final String DESCRIPTION = "description";
	public static final String START_TAG = "start_tag";
	public static final String KEYWORDS = "keywords";
	public static final String COUNTRY = "country"; // Use ISO 3166-1 alpha-2
	public static final String IMAGE = "image"; // This image needs to be public
	public static final String TAG_TYPES = "tag_types"; // List seperated with ;
	public static final String GAME_MODES = "game_modes"; // List seperated with ;
	public static final String AREA = "area"; // Should be a Google Maps location
	public static final String LANGUAGE = "language"; // Use ISO 639-1
	public static final String STATUS = "status";
	public static final String TAGS = "tags";
	public static final String VERSION = "version";
	// Optinal
	public static final String URL = "url";

	public static final String ESTIMATED_TIME = "estimated_time";
	// Tags
	public static final String TAG_TITLE = "title";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_TYPE = "tag_type";
	public static final String TRAVEL_BUTTON = "travel_button";
	public static final String GAME_MODE = "game_mode";
	public static final String TAG_OPTIONS = "options";
	// Optional
	public static final String QUESTION = "question";
	public static final String TAG_IMAGE = "image";
	public static final String SKIPABLE = "skippable";

	public static final String ENDPOINT = "isEndPoint"; // Last tag has to have this sat to true
	// Options
	public static final String ANSWER = "answer";
	public static final String HINT_METHOD = "method";
	public static final String HINT_TITLE = "title";
	public static final String NEXT = "next";
	// Optional
	public static final String LONG = "long";
	public static final String LAT = "lat";
	public static final String ZOOM_LEVEL = "zoom_level";
	public static final String HINT_IMAGE_SOURCE = "image_source";
	public static final String HINT_SOUND_SOURCE = "sound_source";
	public static final String ARROW_LENGTH = "arrow_length";
	public static final String PROPAGATING_TEXT = "propagatingText";

	public static final String HINT_TEXT = "text";
	// Quiz
	public static final String QUIZ_TYPE = "quiz_type";
	public static final String QUIZ = "quiz";
	public static final String QUIZ_QUESTION = "question";
	public static final String QUIZ_ANSWER = "answer";
	public static final String QUIZ_ANSWERS = "answers";
	// Optional
	public static final String QUIZ_CORRECTION = "correction";
	// Camera
	public static final String CAMERA = "camera";

	public static JSONObject parseJson(Context context, String filename) throws IOException, JSONException {
		if (!filename.endsWith(".json")) {
			filename += ".json";
		}

		FileInputStream fileInputStream = context.openFileInput(filename);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		return new JSONObject(sb.toString());
	}

	public static Story parseJsonToStory(Context context, String UUID)
			throws JSONException, IOException {
		JSONObject storyObject = parseJson(context, UUID);

		// Required
		Story story = new Story();
		story.setUUID(storyObject.getString(StoryParser.UUID));
		story.setAuthor(storyObject.getString(AUTHOR));
		story.setTitle(storyObject.getString(TITLE));
		story.setDescription(storyObject.getString(DESCRIPTION));
		story.setAgeGroup(storyObject.getString(AGEGROUP));
		story.setReleaseDate(storyObject.getString(RELEASE_DATE)); // TODO convert to date
		story.setImage(storyObject.getString(IMAGE));
		story.setStartTag(storyObject.getString(START_TAG));
		story.setLanguage(storyObject.getString(LANGUAGE));
		story.setTagTypes(TagTypeEnum.convert(Arrays.asList((storyObject.getString(TAG_TYPES).toLowerCase().split(";")))));
		story.setGameModes(GameModeEnum.convert(Arrays.asList(storyObject.getString(GAME_MODES).toLowerCase().split(";"))));
		story.setGenre(storyObject.getString(GENRE));
		story.setArea(storyObject.getString(AREA));
		story.setCountry(storyObject.getString(COUNTRY));
		story.setKeywords(storyObject.getString(KEYWORDS).split(";"));
		story.setStatus(StoryStatusEnum.fromString(storyObject.getString(STATUS).toLowerCase()));
		story.setVersion(storyObject.getInt(StoryParser.VERSION));

		// Optional
		story.setUrl(storyObject.optString(URL));
		story.setEstimatedTime(storyObject.optInt(ESTIMATED_TIME));

		story.setTags(parseTags(storyObject.getJSONObject(TAGS)));

		return story;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, StoryTag> parseTags(JSONObject jsonObject) throws JSONException {
		HashMap<String, StoryTag> map = new HashMap<>();
		Iterator<String> tagKeys = jsonObject.keys();

		while (tagKeys.hasNext()) {
			String tagKey = tagKeys.next();
			JSONObject jsonTag = jsonObject.getJSONObject(tagKey);

			// Required
			StoryTag storyTag = new StoryTag(tagKey, jsonTag.getString(TAG_DESCRIPTION), jsonTag.optBoolean(ENDPOINT, false));

			if (!storyTag.isEndpoint()) { // Not the last tag
				storyTag.setTagType(TagTypeEnum.fromString(jsonTag.getString(TAG_TYPE)));
				storyTag.setTitle(jsonTag.getString(TAG_TITLE));
				storyTag.setTravelButton(jsonTag.getString(TRAVEL_BUTTON));

				storyTag.setSkipable(jsonTag.optBoolean(SKIPABLE, false));

				if (jsonTag.has(GAME_MODE)) {
					storyTag.setGameMode(GameModeEnum.fromString(jsonTag.getString(GAME_MODE)));
					parseGameMode(storyTag, jsonTag);
				} else {
					storyTag.setGameMode(GameModeEnum.NONE);
				}

				if (jsonTag.has(QUESTION)) {
					storyTag.setQuestion(jsonTag.getString(QUESTION));
				}

				if (jsonTag.has(TAG_IMAGE)) {
					storyTag.setImage(jsonTag.optString(TAG_IMAGE));
				}

				storyTag.setOptions(parseOptions(jsonTag.getJSONArray(TAG_OPTIONS)));

			}
			map.put(tagKey, storyTag);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	private static void parseGameMode(StoryTag storyTag, JSONObject jsonTag) throws JSONException {
		if (storyTag.isQuiz()) {
			storyTag.setQuiztype(QuizTypeEnum.fromString(jsonTag.getString(QUIZ_TYPE)));

			JSONArray quiz = jsonTag.getJSONArray(QUIZ);

			for (int index = 0; index < quiz.length(); index++) {
				QuizNodeInterface node = creatQuizObject(storyTag.getQuiztype(), quiz.getJSONObject(index));
				storyTag.addQuizNode(node);
			}
		}
	}

	private static QuizNodeInterface creatQuizObject(QuizTypeEnum quizType, JSONObject jsonQuestion) throws JSONException {
		switch(quizType) {
			case TRUEFALSEQUIZ:
				return QuizFactory.createTrueFalseQuiz(jsonQuestion);
			case MULTIQUIZLONG:
				return QuizFactory.createMultiquizLong(jsonQuestion);
			case MULTIQUIZSHORT:
				return QuizFactory.createMultiquizShort(jsonQuestion);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<StoryTagOption> parseOptions(JSONArray jsonOptions) throws JSONException {
		List<StoryTagOption> storyTagOptions = new ArrayList<>(jsonOptions.length());

		for (int index = 0; index < jsonOptions.length(); index++) {
			JSONObject jsonOption = jsonOptions.getJSONObject(index);

			StoryTagOption option = new StoryTagOption(jsonOption.getString(ANSWER), HintMethodEnum.fromString(jsonOption.getString(HINT_METHOD)), jsonOption.getString(NEXT));

			option.setTitle(jsonOption.optString(HINT_TITLE));
			option.setHintText(jsonOption.optString(HINT_TEXT));
			option.setZoomLevel(jsonOption.optInt(ZOOM_LEVEL, 15));

			if (option.isImage()) {
				option.setImageSrc(jsonOption.getString(HINT_IMAGE_SOURCE));
			} else if (option.isMap()) {
				option.setLatitude(jsonOption.getDouble(LAT));
				option.setLongitude(jsonOption.getDouble(LONG));
			} else if (option.isArrow()) {
				option.setLatitude(jsonOption.getDouble(LAT));
				option.setLongitude(jsonOption.getDouble(LONG));
				option.setArrowLength(jsonOption.getString(ARROW_LENGTH));
			} else if (option.isSound()) {
				option.setSoundSrc(jsonOption.getString(HINT_SOUND_SOURCE));
			}

			if (jsonOption.has(PROPAGATING_TEXT)) {
				option.setPropagatingText(jsonOption.getString(PROPAGATING_TEXT));
			}

			storyTagOptions.add(option);
		}

		return storyTagOptions;
	}
}
