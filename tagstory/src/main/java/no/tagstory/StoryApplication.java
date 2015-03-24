package no.tagstory;

import android.app.Application;
import android.content.Intent;

import android.database.Cursor;
import android.location.Location;
import no.tagstory.statistics.DistanceLogger;
import no.tagstory.statistics.HistoryNode;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.story.Story;

import no.tagstory.utils.Database;
import no.tagstory.utils.DateUtils;
import org.json.JSONArray;

import java.text.ParseException;
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
		distanceLoggerIntent = new Intent(this, DistanceLogger.class);
		startService(distanceLoggerIntent);
		this.storyStatistic = new StoryStatistic(story.getUUID(), new Date());
	}

	public void resumeStory(Database database, Cursor saveTravels) {
		int id = saveTravels.getInt(0);
		int statisticsId = saveTravels.getInt(1);
		String storyId = saveTravels.getString(2);
		Date saved;
		try {
			saved = DateUtils.parseSqliteDate(saveTravels.getString(3));
		} catch (ParseException e) {
			saved = null;
		}

		StoryStatistic statistic = database.getStatistic(statisticsId);
		Cursor locations = database.getLocations(statisticsId);
		// TODO: Sort by timestamp
		if (locations.getCount() > 0) {
			locations.moveToFirst();
			Location location = new Location(locations.getString(3));
			location.setLatitude(locations.getDouble(1));
			location.setLongitude(2);
			statistic.addLocation(location);
		}
		locations.close();
		Cursor histories = database.getHistories(statisticsId);
		HistoryNode root = null;
		if (histories.getCount() > 0) {
			histories.moveToFirst();
			root = findHistoryRoot(histories);
			HistoryNode current = root;
			while (current != null) {
				current = findNextHistoryNode(histories, current);
			}
		}
		getStoryHistory().resumeStory(storyId, root, histories.getCount());
		histories.close();
	}

	private HistoryNode findNextHistoryNode(Cursor histories, HistoryNode current) {
		histories.moveToFirst();
		String currentId = current.getTagUUID();
		String nextId = current.next.getTagUUID();
		while (!histories.isAfterLast()) {
			if (histories.getString(3).equals(currentId)
				&& histories.getString(2).equals(nextId)) {
				HistoryNode next = new HistoryNode(histories.getString(2));
				next.previous = current;
				next.next = null;
				current.next = next;
				return next;
			}
		}
		return null;
	}

	private HistoryNode findHistoryRoot(Cursor histories) {
		histories.moveToFirst();
		while (!histories.isAfterLast()) {
			if (histories.getInt(5) == 1) {
				HistoryNode root = new HistoryNode(histories.getString(2));
				HistoryNode next = new HistoryNode(histories.getString(4));
				root.previous = null;
				next.previous = root;
				root.next = next;
				return root;
			}
		}

		return null;
	}

	public StoryStatistic stopStory() {
		storyStatistic.stop();
		stopService(distanceLoggerIntent);
		return storyStatistic;
	}

	public StoryStatistic getStoryStatistic() {
		return storyStatistic;
	}
}
