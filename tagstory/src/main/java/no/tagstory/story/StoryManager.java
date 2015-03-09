package no.tagstory.story;

import android.content.Context;
import android.database.Cursor;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class StoryManager {

	private Database database;
	private Context context;

	public StoryManager(Context context) {
		this.context = context;
		database = new Database(this.context);
		database.open();
	}

	public void closeDatabase() {
		database.close();
	}

	public Cursor getCursorOverStories() {
		// TODO Convert cursor to a StoryList-object and return a list
		return database.getStoryList();
	}

	public Story getStory(String id) {
		try {
			return StoryParser.parseJsonToStory(context, id);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean deleteStory(String id) {
		return database.deleteStory(id) && deleteAllAssetsForStory(id) &&
				context.deleteFile(id + ".json");
	}

	public boolean hasStory(String id) {
		return database.hasStory(id);
	}

	public boolean isStoryOutdated(String id, int version) {
		return database.isStoryOutdated(id, version);
	}

	private boolean deleteAllAssetsForStory(String id) {
		try {
			JSONObject mStoryObject = StoryParser.parseJson(context, id);

			if (mStoryObject.has(StoryParser.IMAGE)) {
				context.deleteFile(mStoryObject.getString(StoryParser.IMAGE));
			}

			JSONObject mTagObjects = mStoryObject.getJSONObject(StoryParser.TAGS);
			Iterator<String> mKeys = mTagObjects.keys();
			while (mKeys.hasNext()) {
				String mKey = mKeys.next();
				JSONObject mTag = mTagObjects.getJSONObject(mKey);

				if (mTag.has(StoryParser.TAG_OPTIONS)) {
					JSONArray mOptions = mTag.getJSONArray(StoryParser.TAG_OPTIONS);
					for (int index = 0; index < mOptions.length(); index++) {
						JSONObject mOption = mOptions.getJSONObject(index);
						if (mOption.has(StoryParser.HINT_IMAGE_SOURCE)) {
							context.deleteFile((String) mOption.get(StoryParser.HINT_IMAGE_SOURCE));
						}
						if (mOption.has(StoryParser.HINT_SOUND_SOURCE)) {
							context.deleteFile((String) mOption.get(StoryParser.HINT_SOUND_SOURCE));
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
