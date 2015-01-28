package no.tagstory.utils;

import android.content.Context;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class StoryParser {

	// Main content
	public static final String STORY = "story";
	public static final String UUID = "UUID";
	public static final String AUTHOR = "author";
	public static final String TITLE = "title";
	public static final String RELEASE_DATE = "date";
	public static final String AGEGROUP = "agegroup";
	public static final String GENRE = "genre";
	public static final String DESCRIPTION = "description";
	public static final String START_TAG = "start_tag";
	public static final String KEYWORDS = "keywords";
	public static final String COUNTRY = "country";
	public static final String IMAGE = "image"; // This image needs to be public
	public static final String TAG_TYPES = "tag_types"; // List seperated with ;
	public static final String GAME_MODES = "game_modes"; // List seperated with ;
	public static final String AREA = "area"; // Should be a Google Maps location
	public static final String LANGUAGE = "language"; // Use ISO 639-1
	public static final String TAGS = "tags";
	// Optinal
	public static final String URL = "url";

	// Tags
	public static final String TAG_TITLE = "title";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_MODE = "tag_mode"; // List seperated with ;
	public static final String TAG_OPTIONS = "options";
	public static final String GAME_MODE = "game_mode";
	public static final String GAME_BUTTON = "game_button";
	// Optional
	public static final String CHOICE_DESCRIPTION = "choice_description";
	public static final String CHOICE_IMAGE = "choice_image";
	public static final String ENDPOINT = "isEndPoint"; // Last tag has to have this sat to true

	// Options
	public static final String HINT_METHOD = "method";
	public static final String NEXT = "next";
	// Optional
	public static final String LONG = "long";
	public static final String LAT = "lat";
	public static final String ZOOM_LEVEL = "zoom_level";
	public static final String HINT_IMAGE_SOURCE = "image_source";
	public static final String HINT_SOUND_SOURCE = "sound_source";
	public static final String ARROW_LENGTH = "arrow_length";
	public static final String PROPAGATING_TEXT = "propagatingText";
	// Used to check for hint method
	public static final String HINT_TEXT = "text";
	public static final String HINT_IMAGE = "image";
	public static final String HINT_MAP = "map";
	public static final String HINT_ARROW = "arrow";
	public static final String HINT_SOUD = "sound";

	// Quiz
	public static final String QUIZ = "quiz";
	public static final String QUIZ_Q = "quizQ";
	public static final String QUIZ_A = "quizA";
	public static final String QUIZ_C = "quizC";

	// Camera
	public static final String CAMERA = "camera";

	private static JSONObject parseJson(Context context, String filename) throws IOException, JSONException {
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
		JSONObject storyObject = parseJson(context, UUID).getJSONObject(STORY);

		// Required
		Story story = new Story();
		story.setUUID(storyObject.getString(StoryParser.UUID));
		story.setAuthor(storyObject.getString(AUTHOR));
		story.setStoryTitle(storyObject.getString(TITLE));
		story.setDescription(storyObject.getString(DESCRIPTION));
		story.setAgeGroup(storyObject.getString(AGEGROUP));
		story.setReleaseDate(storyObject.getString(RELEASE_DATE));
		story.setImage(storyObject.getString(IMAGE));
		story.setStartTag(storyObject.getString(START_TAG));
		story.setLanguage(storyObject.getString(LANGUAGE));
		story.setTagTypes(Arrays.asList(storyObject.getString(TAG_TYPES).toLowerCase().split(";")));
		story.setGameModes(Arrays.asList(storyObject.getString(GAME_MODES).toLowerCase().split(";")));
		story.setGenre(storyObject.getString(GENRE));
		story.setArea(storyObject.getString(AREA));
		story.setCountry(storyObject.getString(COUNTRY));
		story.setKeywords(storyObject.getString(KEYWORDS).split(";"));

		// Optional
		story.setUrl(storyObject.optString(URL));
		// story.setEstimatedTime(storyObject.optString(ESTIMATED_TIME)); TODO Estimated time

		story.setTags(parseTags(storyObject.getJSONObject(TAGS)));

		return story;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, StoryTag> parseTags(JSONObject jsonObject) throws JSONException {
		HashMap<String, StoryTag> map = new HashMap<String, StoryTag>();
		Iterator<String> tagKeys = jsonObject.keys();

		while (tagKeys.hasNext()) {
			String tagKey = tagKeys.next();
			JSONObject jsonTag = jsonObject.getJSONObject(tagKey);

			// Required
			StoryTag storyTag = new StoryTag();
			storyTag.setUUID(tagKey);
			storyTag.setDescription(jsonTag.getString(TAG_DESCRIPTION));

			storyTag.setTagMode(jsonTag.optString(TAG_MODE));

			// OptionalstoryTag.setIsEndpoint(jsonTag.optBoolean(ENDPOINT, false));
			storyTag.setGameMode(jsonTag.optString(GAME_MODE));

			if (!storyTag.isEndpoint()) { // Not the last tag
				if (storyTag.hasGameMode()) {
					parseGameMode(storyTag, jsonTag);
				}

				if (jsonTag.has(CHOICE_DESCRIPTION)) { // TODO rename to single question
					storyTag.setChoiceDescription(jsonTag.getString(CHOICE_DESCRIPTION));
					storyTag.setGameButton(jsonTag.getString(GAME_BUTTON));
					// TODO rename to single question button name
					// TODO implement usage of that
				}

				if (jsonTag.has(CHOICE_IMAGE)) {
					storyTag.setChoiceImage(jsonTag.optString(CHOICE_IMAGE));
				}

				if (jsonTag.has(TAG_TITLE)) {
					storyTag.setOptionsTitle(jsonTag.getString(TAG_TITLE));
				}

				storyTag.setOptions(parseOptions(jsonTag.getJSONObject(TAG_OPTIONS)));
			}
			map.put(tagKey, storyTag);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	private static void parseGameMode(StoryTag storyTag, JSONObject jsonTag) throws JSONException {
		if (storyTag.isQuizGameMode()) {
			storyTag.setGameButton(jsonTag.getString(GAME_BUTTON));

			JSONObject quiz = jsonTag.getJSONObject(QUIZ);
			Iterator<String> quizKeys = quiz.keys();
			JSONObject question;
			int location;
			String quizKey;

			while (quizKeys.hasNext()) {
				quizKey = quizKeys.next();
				question = quiz.getJSONObject(quizKey);
				location = Integer.parseInt(quizKey);
				storyTag.addToQuiz(location,
						question.getString(QUIZ_Q),
						question.getBoolean(QUIZ_A));
				if (!question.isNull(QUIZ_C))
					storyTag.addCorrectionToQuiz(location,
							question.getString(QUIZ_C));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, StoryTagOption> parseOptions(JSONObject jsonOptions) throws JSONException {
		HashMap<String, StoryTagOption> map = new HashMap<String, StoryTagOption>(jsonOptions.length());
		Iterator<String> optionKeys = jsonOptions.keys();

		while (optionKeys.hasNext()) {
			String optionKey = optionKeys.next();
			JSONObject jsonOption = jsonOptions.getJSONObject(optionKey);

			String hintMethod = jsonOption.getString(HINT_METHOD);
			StoryTagOption option = new StoryTagOption(optionKey, hintMethod, jsonOption.getString(NEXT));

			option.setHintText(jsonOption.optString(HINT_TEXT));
			option.setZoomLevel(jsonOption.optInt(ZOOM_LEVEL, 15));

			if (hintMethod.equals(HINT_IMAGE)) {
				option.setImageSrc(jsonOption.getString(HINT_IMAGE_SOURCE));
			} else if (hintMethod.equals(HINT_MAP)) {
				option.setLatitude(jsonOption.getDouble(LAT));
				option.setLongitude(jsonOption.getDouble(LONG));
			} else if (hintMethod.equals(HINT_ARROW)) {
				option.setLatitude(jsonOption.getDouble(LAT));
				option.setLongitude(jsonOption.getDouble(LONG));
				option.setArrowLength(jsonOption.getBoolean(ARROW_LENGTH));
			} else if (hintMethod.equals(HINT_IMAGE)) {
				option.setImageSrc(jsonOption.getString(HINT_IMAGE_SOURCE));
			} else if (jsonOption.has(PROPAGATING_TEXT)) {
				option.setPropagatingText(jsonOption.getString(PROPAGATING_TEXT));
			} else if (hintMethod.equals(HINT_SOUD)) {
				option.setSoundSrc(jsonOption.getString(HINT_SOUND_SOURCE));
			}

			map.put(optionKey, option);
		}

		return map;
	}
}
