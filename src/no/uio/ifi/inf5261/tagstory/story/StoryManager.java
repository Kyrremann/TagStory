package no.uio.ifi.inf5261.tagstory.story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.uio.ifi.inf5261.tagstory.Database.CommunicationManager;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import android.content.Context;
import android.database.Cursor;

public class StoryManager {

	private Database database;
	private Context context;

	public StoryManager(Context context) {
		this.context = context;
		database = new Database(this.context);
		database.open();
	}

	public void CloseDatabase() {
		database.close();
	}

	/**
	 * 
	 * @return
	 */
	public Cursor getStoryList() {
		return database.getStoryList();
	}

	public Story getStory(String author, String storyName) {
		return parseCursorToStory(database.getStory(author, storyName));
	}

	private Story parseCursorToStory(Cursor cursor) {
		cursor.moveToFirst();
		Story story = new Story(cursor.getString(0), cursor.getString(1),
				cursor.getString(2));

		return story;
	}

	public Story getStory(String id) {
		return parseCursorToStory(database.getStory(id));
	}

	public void getStoriesFromServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				CommunicationManager communicationManager = new CommunicationManager();
				try {
					insertJSONStoriesToDatabase(communicationManager
							.getAllStories());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void insertJSONStoriesToDatabase(JSONArray allStories) throws JSONException {
		for (int i = 0; i < allStories.length(); i++) {
			JSONObject jsonObject = (JSONObject) allStories.get(i);
			System.out.println(jsonObject);
		}
	}
}
