package no.tagstory;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Application;
import android.location.Location;

public class StoryApplication extends Application {

	private LinkedList<TagHistory> tagHistory;
	private Location startLocation;
	private ArrayList<Location> currentLocationHistoryList;
	private long startTime;
	
	@Override
	public void onCreate() {
		super.onCreate();
		currentLocationHistoryList = new ArrayList<Location>();
		tagHistory = new LinkedList<TagHistory>();
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getLastTag() {
		return tagHistory.getLast().getTagId();
	}

	public void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public float distanceFromStartTo(Location dest) {
		return startLocation.distanceTo(dest);
	}
	
	public boolean addLocation(Location location) {
		return currentLocationHistoryList.add(location);
	}

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
	
	public void emptyHistory() {
		currentLocationHistoryList.clear();
		tagHistory.clear();
	}

	public void addTagTohistory(String tag, String nextTag) {
		tagHistory.add(new TagHistory(tag, nextTag));
	}

	public boolean hasUserVisited(String tagId) {
		return tagHistory.contains(tagId);
	}

	public String getNextTagFor(String tagId) {
		return tagHistory.get(tagHistory.lastIndexOf(tagId)).getNextTag();
	}

	public boolean hasPreviousTag(String tagId) {
		return tagHistory.lastIndexOf(tagId) > 0;
	}

	public String getPreviousTag(String tagId) {
		return tagHistory.get(tagHistory.lastIndexOf(tagId) - 1).getTagId();
	}

	private class TagHistory {

		private final String tag;
		private final String nextTag;

		public TagHistory(String tag, String nextTag) {
			this.tag = tag;
			this.nextTag = nextTag;
		}

		public String getTagId() {
			return tag;
		}

		public String getNextTag() {
			return nextTag;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof String) {
				return tag.equals(o);
			}

			return super.equals(o);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
}
