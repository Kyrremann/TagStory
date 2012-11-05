package no.uio.ifi.inf5261.tagstory.Database;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryPart;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JsonParser {

	public static final String STORY = "story", UUID = "UUID",
			AUTHOR = "author", TITLE = "title", AGEGROUP = "agegroup",
			DATE = "date", GENRE = "genre", DESCRIPTION = "description",
			START_TAG = "startTag", KEYWORDS = "keywords",
			TAG_COUNT = "tagCount", PARTS = "parts";
	public static final String BELONSG_TO_TAG = "belongsToTag",
			PART_DESCRIPTION = "partDesc", CHOICE_DESCRIPTION = "choiceDesc",
			IS_ENDPOINT = "isEndPoint", PART_OPTIONS = "partOptions";
	public static final String OPT_SELECT_METHOD = "optSelectMethod",
			OPT_LONG = "optLong", OPT_LAT = "optLat",
			OPT_HINT_TEXT = "optHintText", OPT_NEXT = "optNext",
			OPT_IMAGE_SRC = "optImageSrc", OPT_SOUND_SRC = "optSoundSrc",
			OPT_ARROW_LENGTH = "optArrowLength";

	public static JSONObject parseJson(Context context, String UUID)
			throws IOException, JSONException {
		InputStream inputStream = context.getAssets().open(UUID);
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		// Log.d("UUID", new String(buffer));
		inputStream.close();
		// System.out.println(new JSONObject(new String(buffer)));

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
					object.getString(IS_ENDPOINT));
			if (!storyPart.getIsEndpoint()) {
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

			@SuppressWarnings("unchecked")
			Iterator<String> optKeys = object.keys();
			while (optKeys.hasNext()) {
				String optKey = optKeys.next();
				if (optKey.equals(OPT_ARROW_LENGTH))
					option.setOptArrowLength(object
							.getBoolean(OPT_ARROW_LENGTH));
				else if (optKey.equals(OPT_LONG))
					option.setOptLong(object.getDouble(OPT_LONG));
				else if (optKey.equals(OPT_LAT))
					option.setOptLat(object.getDouble(OPT_LAT));
				else if (optKey.equals(OPT_IMAGE_SRC))
					option.setOptImageSrc(object.getString(OPT_IMAGE_SRC));
				else if (optKey.equals(OPT_SOUND_SRC))
					option.setOptSoundSrc(object.getString(OPT_SOUND_SRC));
			}
			map.put(key, option);
		}

		return map;
	}
}
