package no.uio.ifi.inf5261.tagstory.Database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryPart;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;

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
			GAME_MODES = "gameModes", AREA = "area", LANGUAGE = "language";
	public static final String BELONSG_TO_TAG = "belongsToTag",
			TAG_MODE = "tagMode", PART_DESCRIPTION = "desc",
			CHOICE_DESCRIPTION = "choiceDesc", IS_ENDPOINT = "isEndPoint",
			PART_OPTIONS = "options";
	public static final String OPT_SELECT_METHOD = "selectMethod",
			OPT_LONG = "long", OPT_LAT = "lat", OPT_HINT_TEXT = "hintText",
			OPT_NEXT = "next", OPT_IMAGE_SRC = "imageSrc",
			OPT_SOUND_SRC = "soundSrc", OPT_ARROW_LENGTH = "arrowLength";
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
		if (storyObject.has(URL) && !storyObject.isNull(GENRE)) {
			story.setGenre(storyObject.getString(GENRE));
		}
		if (storyObject.has(URL) && !storyObject.isNull(AREA)) {
			story.setArea(storyObject.getString(AREA));
		}
		if (storyObject.has(URL) && !storyObject.isNull(COUNTRY)) {
			story.setCountry(storyObject.getString(COUNTRY));
		}
		if (storyObject.has(URL) && !storyObject.isNull(KEYWORDS)) {
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
					object.getString(GAME_MODE), object.getString(IS_ENDPOINT));

			if (!storyPart.getIsEndpoint()) {
				if (storyPart.getGameMode() != null
						&& storyPart.getGameMode().equals(QUIZ)) {
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
				}
				storyPart.setChoiceDescription(object
						.getString(CHOICE_DESCRIPTION));
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
			StoryPartOption option = new StoryPartOption(key,
					object.getString(OPT_SELECT_METHOD),
					object.getString(OPT_HINT_TEXT), object.getString(OPT_NEXT));

			if (object.getString(OPT_SELECT_METHOD).equals(HINT_IMAGE))
				option.setOptImageSrc(object.getString(OPT_IMAGE_SRC));
			else if (object.getString(OPT_SELECT_METHOD).equals(HINT_MAP)) {
				option.setOptLat(object.getDouble(OPT_LAT));
				option.setOptLong(object.getDouble(OPT_LONG));
			} else if (object.getString(OPT_SELECT_METHOD).equals(HINT_ARROW)) {
				option.setOptLat(object.getDouble(OPT_LAT));
				option.setOptLong(object.getDouble(OPT_LONG));
				option.setOptArrowLength(object.getBoolean(OPT_ARROW_LENGTH));
			} else if (object.getString(OPT_SELECT_METHOD).equals(HINT_IMAGE))
				option.setOptImageSrc(object.getString(OPT_IMAGE_SRC));

			map.put(key, option);
		}

		return map;
	}
}
