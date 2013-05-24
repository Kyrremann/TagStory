package no.uio.ifi.inf5261.tagstory.story;

import java.io.IOException;

import org.json.JSONException;

import no.uio.ifi.inf5261.tagstory.Database.Database;
import no.uio.ifi.inf5261.tagstory.Database.JsonParser;
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
