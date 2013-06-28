package no.tagstory.communication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import no.tagstory.story.StoryPartOption;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JsonParser {

	public static final String STORY = "story", UUID = "UUID",
			AUTHOR = "author", TITLE = "title", AGEGROUP = "agegroup",
			DATE = "date", GENRE = "genre", DESCRIPTION = "description",
			START_TAG = "startTag", KEYWORDS = "keywords",
			TAG_COUNT = "tagCount", PARTS = "parts", COUNTRY = "country",
			IMAGE = "image", URL = "url", TAG_TYPES = "tagTypes",
			GAME_MODES = "gameModes", AREA = "area", LANGUAGE = "language",
			OPTIONS_TITLE = "optionsTitle";
	public static final String BELONSG_TO_TAG = "belongsToTag",
			TAG_MODE = "tagMode", PART_DESCRIPTION = "desc",
			CHOICE_DESCRIPTION = "choiceDesc", IS_ENDPOINT = "isEndPoint",
			PART_OPTIONS = "options";
	public static final String OPT_SELECT_METHOD = "selectMethod",
			OPT_LONG = "long", OPT_LAT = "lat", OPT_HINT_TEXT = "hintText",
			OPT_NEXT = "next", OPT_IMAGE_SRC = "imageSrc",
			OPT_SOUND_SRC = "soundSrc", OPT_ARROW_LENGTH = "arrowLength",
			PROPAGATING_TEXT = "propagatingText";
	public static final String GAME_MODE = "gameMode",
			GAME_BUTTON = "gameButton", QUIZ = "quiz", QUIZ_Q = "quizQ",
			QUIZ_A = "quizA", QUIZ_C = "quizC";
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
		story.setTagCount(storyObject.getInt(TAG_COUNT));
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

	private static HashMap<String, StoryPart> parseJsonStoryParts(
			JSONObject jsonObject, int tagCount) throws JSONException {
		HashMap<String, StoryPart> map = new HashMap<String, StoryPart>(
				tagCount);

		@SuppressWarnings("unchecked")
		Iterator<String> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject object = jsonObject.getJSONObject(key);
			StoryPart storyPart = new StoryPart(key,
					object.getString(BELONSG_TO_TAG),
					object.getString(PART_DESCRIPTION),
					object.getString(IS_ENDPOINT));

			if (!storyPart.isEndpoint()) { // Not the last tag
				if (storyPart.getGameMode() != null
						&& storyPart.getGameMode().equals(QUIZ)) { // Tag is a
																	// quiz
					storyPart.setGameButton(object.getString(GAME_BUTTON));
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
						storyPart.addToQuiz(location,
								question.getString(QUIZ_Q),
								question.getBoolean(QUIZ_A));
						if (!question.isNull(QUIZ_C))
							storyPart.addCorrectionToQuiz(location,
									question.getString(QUIZ_C));
					}
				} // End of tag quiz
				if (object.has(CHOICE_DESCRIPTION)) {
					storyPart.setChoiceDescription(object
							.getString(CHOICE_DESCRIPTION));
				}
				if (object.has(OPTIONS_TITLE)) {
					storyPart.setOptionsTitle(object.getString(OPTIONS_TITLE));
				}
				storyPart.setOptions(parseJsonStoryPartOptions(object
						.getJSONObject(PART_OPTIONS)));
				storyPart.setTagMode(object.getString(TAG_MODE));
			}
			map.put(key, storyPart);
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
