package no.tagstory.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryPartOption;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JsonParser {

	public static final String STORY = "story", UUID = "UUID",
			AUTHOR = "author", TITLE = "title", AGEGROUP = "agegroup",
			DATE = "date", GENRE = "genre", DESCRIPTION = "description",
			START_TAG = "start_tag", KEYWORDS = "keywords",
			TAG_COUNT = "tag_count", PARTS = "tags", COUNTRY = "country",
			IMAGE = "image", URL = "url", TAG_TYPES = "tag_types",
			GAME_MODES = "game_modes", AREA = "area", LANGUAGE = "language",
			OPTIONS_TITLE = "options_title";
	public static final String BELONSG_TO_TAG = "belongs_to_tag",
			TAG_MODE = "tag_mode", PART_DESCRIPTION = "description",
			CHOICE_DESCRIPTION = "choice_description",CHOICE_IMAGE = "choice_image", IS_ENDPOINT = "isEndPoint",
			PART_OPTIONS = "options";
	public static final String OPT_SELECT_METHOD = "hint_method",
			OPT_LONG = "long", OPT_LAT = "lat", OPT_HINT_TEXT = "hint_text",
			OPT_NEXT = "next", OPT_IMAGE_SRC = "image_source",
			OPT_SOUND_SRC = "sound_source", OPT_ARROW_LENGTH = "arrow_length",
			PROPAGATING_TEXT = "propagatingText";
	public static final String GAME_MODE = "game_mode",
			GAME_BUTTON = "game_button", QUIZ = "quiz", QUIZ_Q = "quizQ",
			QUIZ_A = "quizA", QUIZ_C = "quizC", CAMERA = "camera";
	public static final String HINT_TEXT = "hint_text",
			HINT_IMAGE = "hint_image", HINT_MAP = "hint_map",
			HINT_ARROW = "hint_arrow", HINT_SOUD = "hint_sound";

	public static JSONObject parseJson(Context context, String UUID)
			throws IOException, JSONException {
		InputStream inputStream = context.getAssets().open(UUID);
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		inputStream.close();
		return new JSONObject(new String(buffer));
	}

	public static Story parseJsonToStory(Context context, String UUID)
			throws JSONException, IOException {
		JSONObject storyObject = parseJson(context, UUID).getJSONObject(STORY);

		// Required
		Story story = new Story(storyObject.getString(JsonParser.UUID),
				storyObject.getString(AUTHOR), storyObject.getString(TITLE));
		story.setAgeGroup(storyObject.getString(AGEGROUP));
		story.setDate(storyObject.getString(DATE));
		story.setDesc(storyObject.getString(DESCRIPTION));
		story.setImage(storyObject.getString(IMAGE));
		story.setStartTag(storyObject.getString(START_TAG));
		story.setTagCount(storyObject.getInt(TAG_COUNT)); // TODO Do we need this?
		story.setStoryParts(parseJsonStoryParts(
				storyObject.getJSONObject(PARTS), story.getTagCount()));
		story.setLanguage(storyObject.getString(LANGUAGE));
		story.setTagTypes(Arrays.asList(storyObject.getString(TAG_TYPES).split(
				";")));
		story.setGameModes(Arrays.asList(storyObject.getString(GAME_MODES)
				.split(";")));

		// Optional
		if (storyObject.has(GENRE) && !storyObject.isNull(GENRE)) {
			story.setGenre(storyObject.getString(GENRE));
		}
		if (storyObject.has(AREA) && !storyObject.isNull(AREA)) {
			story.setArea(storyObject.getString(AREA));
		}
		if (storyObject.has(COUNTRY) && !storyObject.isNull(COUNTRY)) {
			story.setCountry(storyObject.getString(COUNTRY));
		}
		if (storyObject.has(KEYWORDS) && !storyObject.isNull(KEYWORDS)) {
			story.setKeywords(storyObject.getString(KEYWORDS).split(";"));
		}
		if (storyObject.has(URL) && !storyObject.isNull(URL)) {
			story.setUrl(storyObject.getString(URL));
		}

		return story;
	}

	private static HashMap<String, StoryTag> parseJsonStoryParts(
			JSONObject jsonObject, int tagCount) throws JSONException {
		HashMap<String, StoryTag> map = new HashMap<String, StoryTag>(
				tagCount);

		@SuppressWarnings("unchecked")
		Iterator<String> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject object = jsonObject.getJSONObject(key);
			StoryTag storyTag = new StoryTag(key,
					object.optString(BELONSG_TO_TAG, ""), // TODO is this really necesasry?
					object.getString(PART_DESCRIPTION),
					object.optString(IS_ENDPOINT, "false")); // Should be boolean, not string
			if (object.has(GAME_MODE)) {
				storyTag.setGameMode(object.getString(GAME_MODE));
			}

			if (!storyTag.isEndpoint()) { // Not the last tag
				if (storyTag.getGameMode() != null) {
					if (storyTag.getGameMode().equals(QUIZ)) { // Tag is a
																	// quiz
					storyTag.setGameButton(object.getString(GAME_BUTTON));
					JSONObject quiz = object.getJSONObject(QUIZ);

					@SuppressWarnings("unchecked")
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
				} // End of tag quiz
				} // End of Game Mode
				if (object.has(CHOICE_DESCRIPTION)) {
					storyTag.setChoiceDescription(object
							.getString(CHOICE_DESCRIPTION));
					storyTag.setGameButton(object.getString(GAME_BUTTON));
				}
				if (object.has(CHOICE_IMAGE)) {
					storyTag.setChoiceImage(object.getString(CHOICE_IMAGE));
				}
				if (object.has(OPTIONS_TITLE)) {
					storyTag.setOptionsTitle(object.getString(OPTIONS_TITLE));
				}
				storyTag.setOptions(parseJsonStoryPartOptions(object
						.getJSONObject(PART_OPTIONS)));
				storyTag.setTagMode(object.getString(TAG_MODE));
			}
			map.put(key, storyTag);
		}

		return map;
	}

	private static HashMap<String, StoryPartOption> parseJsonStoryPartOptions(
			JSONObject jsonObject) throws JSONException {
		HashMap<String, StoryPartOption> map = new HashMap<String, StoryPartOption>(
				jsonObject.length());

		@SuppressWarnings("unchecked")
		Iterator<String> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject object = jsonObject.getJSONObject(key);
			String selectMethod = object.getString(OPT_SELECT_METHOD);
			StoryPartOption option = new StoryPartOption(key, selectMethod,
					object.getString(OPT_HINT_TEXT), object.getString(OPT_NEXT));

			if (selectMethod.equals(HINT_IMAGE)) {
				option.setOptImageSrc(object.getString(OPT_IMAGE_SRC));
			}
			if (selectMethod.equals(HINT_MAP)) {
				option.setLatitude(object.getDouble(OPT_LAT));
				option.setLongitude(object.getDouble(OPT_LONG));
			}
			if (selectMethod.equals(HINT_ARROW)) {
				option.setLatitude(object.getDouble(OPT_LAT));
				option.setLongitude(object.getDouble(OPT_LONG));
				option.setOptArrowLength(object.getBoolean(OPT_ARROW_LENGTH));
			}
			if (selectMethod.equals(HINT_IMAGE)) {
				option.setOptImageSrc(object.getString(OPT_IMAGE_SRC));
			}
			if (object.has(PROPAGATING_TEXT)) {
				option.setOptPropagatingText(object.getString(PROPAGATING_TEXT));
			}

			map.put(key, option);
		}

		return map;
	}
}
