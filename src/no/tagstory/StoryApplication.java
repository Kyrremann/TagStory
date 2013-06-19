package no.tagstory;

import java.util.ArrayList;

import android.app.Application;
import android.location.Location;

public class StoryApplication extends Application {

	private ArrayList<String> currentTagHistoryList;
	private Location startLocation;
	private ArrayList<Location> currentLocationHistoryList;
	private long startTime;

	public Location getStartLocation() {
		return startLocation;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getLastTag() {
		return currentTagHistoryList.get(currentTagHistoryList.size() - 1);
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public float distanceFromStartTo(Location dest) {
		return startLocation.distanceTo(dest);
	}

	public float distanceWalked() {
		float distance = 0f;
		for (Location l : currentLocationHistoryList) {
			distance += l.distanceTo(l);
		}
		return distance;
	}
}
