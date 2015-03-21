package no.tagstory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat sqliteDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	private static SimpleDateFormat statisticDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static String formatSqliteDate(Date date) {
		if (date == null) {
			return "";
		}

		return sqliteDate.format(date);
	}

	public static Date parseSqliteDate(String date) throws ParseException {
		return sqliteDate.parse(date);
	}

	public static String formatStatisticDate(Date date) {
		if (date == null) {
			return "";
		}

		return statisticDate.format(date);
	}

	public static String parseAndFormatCursorDate(String stringDate) throws ParseException {
		Date date = parseSqliteDate(stringDate);
		return formatStatisticDate(date);
	}
}
