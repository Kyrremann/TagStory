package no.tagstory.statistics;

import android.content.Context;
import android.location.Location;

import no.tagstory.utils.Database;
import no.tagstory.utils.DateUtils;
import no.tagstory.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StoryStatistic {

	private String storyId;
	private Date startTime;
	private long duration;
	private int distance;
	private boolean isSaved;
	private List<Location> locations;

	public StoryStatistic(String storyId, Date startTime) {
		this.storyId = storyId;
		this.startTime = startTime;
		locations = new ArrayList<>();
	}

	public void stop() {
		Date stopTime = new Date();
		this.duration = stopTime.getTime() - startTime.getTime();
		if (locations.size() > 0) {
			this.distance = calculateDistance();
		} else {
			this.distance = 0;
		}
	}

	private int calculateDistance() {
		Location lastLocation = locations.get(0);
		int distance = 0;
		for (int i = 0; i < locations.size(); i++) {
			Location location = locations.get(i);
			distance += lastLocation.distanceTo(location);
			lastLocation = location;
		}

		return distance;
	}

	public void saveToDatebase(Context context) {
		Database database = new Database(context);
		database.open();
		isSaved = database.insertStatistic(storyId, startTime, duration, distance);
		database.close();
	}

	public String formatStatistic() {
		return String.format(Locale.ENGLISH,
				"Started: %s%n" +
						"Distance: %s%n" +
						"Time used: %s",
				DateUtils.formatStatisticDate(startTime), StringUtils.formatDistance(distance), StringUtils.formatDuration(duration));
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void addLocation(Location location) {
		locations.add(location);
	}

	public String getStoryId() {
		return storyId;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public double getLocationLatitude(int index) {
		return locations.get(index).getLatitude();
	}

	public double getLocationLongitude(int index) {
		return locations.get(index).getLongitude();
	}
}
