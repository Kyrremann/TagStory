package no.tagstory;

import android.app.Application;
import android.content.Intent;

import android.database.Cursor;
import no.tagstory.statistics.*;
import no.tagstory.story.Story;

import no.tagstory.utils.Database;
import org.json.JSONArray;

import java.util.Date;

public class StoryApplication extends Application {

	private StoryHistory storyHistory;
	private JSONArray markedstories;
	private StoryStatistic storyStatistic;
	private Intent distanceLoggerIntent;

	@Override
	public void onCreate() {
		super.onCreate();
		storyHistory = new StoryHistory();
	}

	public StoryHistory getStoryHistory() {
		return storyHistory;
	}

	public void setMarkedstories(JSONArray markedstories) {
		this.markedstories = markedstories;
	}

	public JSONArray getMarketStories() {
		if (markedstories != null) {
			return markedstories;
		}
		return new JSONArray();
	}

	public boolean isMarketStoriesEmptyOrOutdated() {
		return true;
//		markedstories == null
//				|| markedstories.length() == 0;
		// TODO add datetracking
	}

	public void startStory(Story story) {
		getStoryHistory().startStory(story);
		startDistanceLogger();
		this.storyStatistic = new StoryStatistic(story.getUUID(), new Date());
	}

	private void startDistanceLogger() {
		//distanceLoggerIntent = new Intent(this, DistanceLogger.class);
		//startService(distanceLoggerIntent);
	}

	public void resumeStory(Database database, int statisticsId, String storyId) throws RuntimeException {
		StoryStatistic statistic = database.getStatistic(statisticsId);
		statistic.setId(statisticsId);
		Cursor locationCursor = database.getLocations(statisticsId);
		locationCursor.moveToFirst();
		if (locationCursor.getCount() > 0) {
			while (locationCursor.isAfterLast()) {
				Location location = new Location(locationCursor.getString(4));
				location.setId(locationCursor.getInt(0));
				location.setLatitude(locationCursor.getDouble(2));
				location.setLongitude(locationCursor.getDouble(3));
				location.setTime(locationCursor.getInt(5));
				statistic.addLocation(location);
				locationCursor.moveToNext();
			}
		}
		statistic.sortLocations();
		locationCursor.close();
		Cursor histories = database.getHistories(statisticsId);
		HistoryNode root = null;
		if (histories.getCount() > 0) {
			histories.moveToFirst();
			root = findHistoryRootNode(histories);
			HistoryNode current = root;
			while (current.next != null) {
				current = findNextHistoryNode(histories, current);
			}
		}
		getStoryHistory().resumeStory(storyId, root, histories.getCount());
		histories.close();

		startDistanceLogger();
		this.storyStatistic = statistic;
	}

	private HistoryNode findNextHistoryNode(Cursor histories, HistoryNode current) {
		histories.moveToFirst();
		String currentId = current.getTagUUID();
		String nextId = current.next.getTagUUID();
		while (!histories.isAfterLast()) {
			if (hasThisHistoryNodeCurrentAsPrevious(histories, currentId)
				&& isThisHistoryNodeTheNextOne(histories, nextId)) {
				HistoryNode newCurrent = new HistoryNode(histories.getString(2));
				newCurrent.id = histories.getInt(0);
				newCurrent.finishedGame = histories.getInt(5) == 1 ? true : false;
				newCurrent.root = histories.getInt(6) == 1 ? true : false;
				if (!histories.isNull(4)) {
					HistoryNode next = new HistoryNode(histories.getString(4));
					newCurrent.next = next;
					next.previous = newCurrent;
				} else {
					newCurrent.next = null;
				}
				newCurrent.previous = current;
				current.next = newCurrent;
				return newCurrent;
			}
			histories.moveToNext();
		}
		throw new RuntimeException("Can't find the next history node. Looking for " + currentId + " and " + nextId);
	}

	private boolean hasThisHistoryNodeCurrentAsPrevious(Cursor cursor, String currentId) {
		if (cursor.isNull(3)) {
			return false;
		}
		return cursor.getString(3).equals(currentId);
	}

	private boolean isThisHistoryNodeTheNextOne(Cursor cursor, String nextId) {
		if (cursor.isNull(2)) {
			return false;
		}
		return cursor.getString(2).equals(nextId);
	}

	private HistoryNode findHistoryRootNode(Cursor histories) {
		histories.moveToFirst();
		while (!histories.isAfterLast()) {
			if (histories.getInt(6) == 1) {
				HistoryNode root = new HistoryNode(histories.getString(2));
				HistoryNode next = new HistoryNode(histories.getString(4));
				root.previous = null;
				next.previous = root;
				root.next = next;
				root.id = histories.getInt(0);
				root.finishedGame = histories.getInt(5) == 1 ? true : false;
				root.root = histories.getInt(6) == 1 ? true : false;
				return root;
			}
			histories.moveToNext();
		}

		throw new RuntimeException("Can't find root HistoryNode");
	}

	public void stopStory() {
		if (distanceLoggerIntent != null) {
			stopService(distanceLoggerIntent);
		}
	}

	public StoryStatistic getStoryStatistic() {
		return storyStatistic;
	}

	public void saveStory(String storyId) {
		StoryStatistic storyStatistic = getStoryStatistic();
		StoryHistory mStoryHistory = getStoryHistory();
		int statisticsId = storyStatistic.saveToDatebase(this);
		mStoryHistory.saveToDatabase(this, statisticsId);
		Database database = new Database(this);
		database.open();
		if (database.hasSaveTravel(statisticsId, storyId)) {
			database.updateSaveTravel(statisticsId);
		} else {
			database.insertSaveTravel(statisticsId, storyId);
		}
		database.close();
	}

	public void deleteSavedStory() {
		Database database = new Database(this);
		database.open();
		database.deleteSaveTravel(storyStatistic.getId(), storyStatistic.getStoryId());
		database.close();
	}
}
