package no.tagstory.utils;

import java.util.Locale;

public class StringUtils {

	public static String formatDistance(int distance) {
		if (distance < 1000) {
			return distance + " meters";
		} else {
			return String.format(Locale.ENGLISH,
					"%.2f km",
					distance / 1000.0);
		}
	}

	public static String formatDuration(long duration) {
		long seconds = duration / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
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
