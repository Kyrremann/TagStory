package no.tagstory.statistics;

import android.content.Context;
import android.location.Location;
import no.tagstory.utils.Database;
import no.tagstory.utils.DateUtils;
import no.tagstory.utils.StringUtils;

import java.util.*;

public class StoryStatistic {

	private int id;
	private String storyId;
	private Date startTime;
	private Date endTime;
	private long duration;
	private int distance;
	private boolean isSaved;
	private List<Location> locations;

	public StoryStatistic(String storyId, Date startTime) {
		this.storyId = storyId;
		this.startTime = startTime;
		locations = new ArrayList<>();
		duration = 0;
		distance = 0;
		id = -1;
	}

	public int saveToDatebase(Context context) {
		Database database = new Database(context);
		database.open();
		Date stopTime = new Date();
		this.duration = stopTime.getTime() - startTime.getTime();
		if (id == -1) {
			isSaved = database.insertStatistic(storyId, startTime, endTime, duration, distance);
			id = database.getStatisticId(storyId, startTime);
		} else {
			isSaved = database.updateStatistic(id, storyId, startTime, endTime, duration, distance);
		}
		saveLocations(database, id);
		database.close();
		return id;
	}

	private void saveLocations(Database database, int statisticsId) {
		database.clearOldLocations(statisticsId);
		for (Location location : locations) {
			database.insertLocation(statisticsId, location);
		}
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
		if (!locations.isEmpty()) {
			distance += locations.get(locations.size() - 1).distanceTo(location);
		}
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

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setSaved(boolean saved) {
		this.isSaved = saved;
	}

	public void sortLocations() {
		Collections.sort(locations, new Comparator<Location>() {
			@Override
			public int compare(Location l1, Location t2) {
				return new Long(l1.getTime()).compareTo(new Long(t2.getTime()));
			}
		});
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean hasId() {
		return id != -1;
	}
}
