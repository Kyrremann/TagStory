package no.tagstory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat sqliteDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	public static String formatSqliteDate(Date date) {
		return sqliteDate.format(date);
	}

	public static Date parseSqliteDate(String date) throws ParseException {
		return sqliteDate.parse(date);
	}
}
