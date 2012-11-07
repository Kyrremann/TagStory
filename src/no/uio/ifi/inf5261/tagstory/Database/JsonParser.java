package no.uio.ifi.inf5261.tagstory.Database;

import java.io.IOException;
import java.io.InputStream;
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
			TAG_COUNT = "tagCount", PARTS = "parts", GAME_MODE = "gameMode",
			GAME_BUTTON = "gameButton";
	public static final String BELONSG_TO_TAG = "belongsToTag",
			PART_DESCRIPTION = "partDesc", CHOICE_DESCRIPTION = "choiceDesc",
			IS_ENDPOINT = "isEndPoint", PART_OPTIONS = "partOptions";
	public static final String OPT_SELECT_METHOD = "optSelectMethod",
			OPT_LONG = "optLong", OPT_LAT = "optLat",
			OPT_HINT_TEXT = "optHintText", OPT_NEXT = "optNext",
			OPT_IMAGE_SRC = "optImageSrc", OPT_SOUND_SRC = "optSoundSrc",
			OPT_ARROW_LENGTH = "optArrowLength";
	public static final String QUIZ = "quiz", QUIZ_Q = "quizQ",
			QUIZ_A = "quizA", QUIZ_C = "quizC";
	public static final String HINT_TEXT = "hint_text",
			HINT_IMAGE = "hint_image", HINT_MAP = "hint_map",
			HINT_ARROW = "hint_arrow", HINT_SOUD = "hint_sound",
			HINT_QUIZ = "hint_quiz";

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
		// Log.d(UUID, storyObject.toString(4));

		Story story = new Story(storyObject.getString(JsonParser.UUID),
				storyObject.getString(AUTHOR), storyObject.getString(TITLE));
		story.setAgeGroup(storyObject.getString(AGEGROUP));
		story.setDate(storyObject.getString(DATE));
		story.setGenre(storyObject.getString(GENRE));
		story.setDesc(storyObject.getString(DESCRIPTION));
		story.setStartTag(storyObject.getString(START_TAG));
		story.setKeywords(storyObject.getString(KEYWORDS).split(";"));
		story.setTagCount(storyObject.getInt(TAG_COUNT));
		story.setStoryParts(parseJsonParts(storyObject.getJSONObject(PARTS),
				story.getTagCount()));

		return story;
	}

	private static HashMap<String, StoryPart> parseJsonParts(
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
				if (storyPart.getGameMode() != null && storyPart.getGameMode().equals(QUIZ)) {
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
						if (question.has(QUIZ_C))
							storyPart.addCorrectionToQuiz(location,
									question.getString(QUIZ_C));
					}
				}
				storyPart.setChoiceDescription(object
						.getString(CHOICE_DESCRIPTION));
				storyPart.setOptions(parseJsonOptions(object
						.getJSONObject(PART_OPTIONS)));
			}
			map.put(key, storyPart);
		}

		return map;
	}

	private static HashMap<String, StoryPartOption> parseJsonOptions(
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
