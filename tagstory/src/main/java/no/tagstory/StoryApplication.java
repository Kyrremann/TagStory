package no.tagstory;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import no.tagstory.statistics.DistanceLogger;
import no.tagstory.statistics.StoryHistory;
import no.tagstory.statistics.StoryStatistic;
import no.tagstory.story.Story;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

public class StoryApplication extends Application {

	private Location startLocation;
	private ArrayList<Location> currentLocationHistoryList;
	private long startTime;

	private StoryHistory storyHistory;
	private JSONArray markedstories;
	private StoryStatistic storyStatistic;
	private Intent distanceLoggerIntent;

	@Override
	public void onCreate() {
		super.onCreate();
		currentLocationHistoryList = new ArrayList<>();
		storyHistory = new StoryHistory();
	}

	public StoryHistory getStoryHistory() {
		return storyHistory;
	}

	@Deprecated
	public long getStartTime() {
		return startTime;
	}

	@Deprecated
	public void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	@Deprecated
	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	@Deprecated
	public float distanceFromStartTo(Location dest) {
		return startLocation.distanceTo(dest);
	}

	@Deprecated
	public boolean addLocation(Location location) {
		return currentLocationHistoryList.add(location);
	}

	@Deprecated
	public float distanceWalked() {
		float distance = 0f;
		Location last = null;
		for (Location l : currentLocationHistoryList) {
			if (last != null) {
				distance += last.distanceTo(l);
			}
			last = l;
		}
		return distance;
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

	public StoryStatistic stopStory() {
		storyStatistic.stop();
		stopService(distanceLoggerIntent);
		return storyStatistic;
	}

	public StoryStatistic getStoryStatistic() {
		return storyStatistic;
	}
}
