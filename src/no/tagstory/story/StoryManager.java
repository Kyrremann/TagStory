package no.tagstory.story;

import android.content.Context;
import android.database.Cursor;
import no.tagstory.utils.Database;
import no.tagstory.utils.JsonParser;
import org.json.JSONException;

import java.io.IOException;

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

	/**
	 * @return
	 */
	public Cursor getCursorOverStories() {
		// TODO Convert cursor to a StoryList-object and return a list
		return database.getStoryList();
	}

	public Story getStory(String id) {
		try {
			return JsonParser.parseJsonToStory(context, id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getStoriesFromServer() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				CommunicationManager communicationManager = new CommunicationManager();
//				try {
//					// TODO: Save JSON file to phone
//					// Internal storage
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
}
