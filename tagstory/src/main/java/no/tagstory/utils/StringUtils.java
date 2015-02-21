package no.tagstory.utils;

import java.util.Locale;

public class StringUtils {

	public static String formatDistance(int distance) {
		if (distance < 1000) {
			return distance + " meters";
		} else {
			return distance / 1000 + " km";
		}
	}

	public static String formatDuration(long duration) {
		long seconds = duration / 1000;
		long minutes = seconds / 60;
		long hours = seconds / 60;
		if (seconds < 60) {
			return seconds + " seconds";
		} else if (minutes < 60) {
			return minutes + " minutes";
		} else {
			return String.format(Locale.ENGLISH,
					"%sh and %sm",
					hours, (minutes - hours * 60));
		}
	}
}
