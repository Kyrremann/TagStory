package no.tagstory.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

public class Database {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "TagStory";

	private static final String STORY_TABLE_NAME = "STORIES";
	public static final String STORY_ID = "_id";
	public static final String STORY_AUTHOR = "author";
	public static final String STORY_TITLE = "title";
	public static final String STORY_LOCATION = "location";
	public static final String STORY_IMAGE = "image";
	public static final String STORY_VERSION = "version";
	private static final String STORY_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s INTEGER NOT NULL);",
			STORY_TABLE_NAME, STORY_ID, STORY_AUTHOR, STORY_TITLE, STORY_LOCATION, STORY_IMAGE, STORY_VERSION);

	public static final String STATISTICS_TABLE_NAME = "STATISTICS";
	public static final String STATISTICS_ID = "_id";
	public static final String STATISTICS_STORY_ID = "story_id";
	public static final String STATISTICS_DATE = "date";
	public static final String STATISTICS_DURATION = "duration";
	public static final String STATISTICS_DISTANCE = "distance";
	public static final String STATISTICS_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s INTEGER NOT NULL," +
					"%s REAL);",
			STATISTICS_TABLE_NAME, STATISTICS_ID, STATISTICS_STORY_ID, STATISTICS_DATE, STATISTICS_DURATION, STATISTICS_DISTANCE);

	//TODO change name
	private static final String SAVE_QUIT_TABLE_NAME = "SAVEQUIT";
	public static final String SAVE_QUIT_ID = "_id";
	public static final String SAVE_QUIT_STATISTIC_ID = "statistic_id";
	public static final String SAVE_QUIT_HISTORY_ROOT_ID = "history_root_id";
	public static final String SAVE_QUIT_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s TEXT NOT NULL, " +
					"%s TEXT NOT NULL);",
			SAVE_QUIT_TABLE_NAME, SAVE_QUIT_ID, SAVE_QUIT_STATISTIC_ID,
			SAVE_QUIT_HISTORY_ROOT_ID);


	private static final String LOCATIONS_TABLE_NAME = "LOCATION";
	public static final String LOCATIONS_ID = "_id";
	public static final String LOCATIONS_SAVE_QUIT_ID = "sq_id";
	public static final String LOCATIONS_LATITUDE = "latitude";
	public static final String LOCATIONS_LONGITUDE = "longitude";
	public static final String LOCATIONS_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s INTEGER NOT NULL," +
					"%s DOUBLE," +
					"%s DOUBLE);",
			LOCATIONS_TABLE_NAME, LOCATIONS_ID, LOCATIONS_SAVE_QUIT_ID, LOCATIONS_LATITUDE,
			LOCATIONS_LONGITUDE);

	private static final String HISTORY_TABLE_NAME = "HISTORY";
	public static final String HISTORY_ID = "_id";
	public static final String HISTORY_STORY_ID = "story_id";
	public static final String HISTORY_PREVIOUS_TAG_ID = "history_previous";
	public static final String HISTORY_NEXT_TAG_ID = "history_next";
	public static final String HISTORY_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s TEXT NOT NULL," +
					"%s TEXT," +
					"%s TEXT);",
			HISTORY_TABLE_NAME, HISTORY_ID, HISTORY_STORY_ID, HISTORY_PREVIOUS_TAG_ID,
			HISTORY_NEXT_TAG_ID);


	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
			db.execSQL(STATISTICS_CREATE);
			db.execSQL(SAVE_QUIT_CREATE);
			db.execSQL(LOCATIONS_CREATE);
			db.execSQL(HISTORY_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("UPGRADE", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// Kills the table and existing data
			db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE_NAME);

			// Recreates the database with a new version
			onCreate(db);
		}

	}

	public Database(Context context) {
		this.context = context;
	}

	public Database open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		if (db.getVersion() != DATABASE_VERSION)
			dbHelper.onUpgrade(db, db.getVersion(), DATABASE_VERSION);
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insertStory(String uuid, String author, String title, String location, String image, int version) {
		ContentValues values = new ContentValues(5);
		values.put(STORY_ID, uuid);
		values.put(STORY_AUTHOR, author);
		values.put(STORY_TITLE, title);
		values.put(STORY_LOCATION, location);
		values.put(STORY_IMAGE, image);
		values.put(STORY_VERSION, version);
		return db.insert(STORY_TABLE_NAME, null, values) != -1;
	}

	/**
	 * @return A Cursor containing a list of stories titles and the author.<br />
	 * Sorted descending.
	 */
	public Cursor getStoryList() {
		return db.query(STORY_TABLE_NAME, null, null, null, null, null,
				STORY_TITLE + " DESC");
	}

	public boolean deleteStory(String id) {
		int result = db.delete(STORY_TABLE_NAME, STORY_ID + "=?", new String[]{id});
		return result > 0;
	}


	public boolean hasStory(String id) {
		int result = db.query(STORY_TABLE_NAME, new String[]{STORY_ID}, STORY_ID + "=?", new String[]{id}, null, null, null).getCount();
		return result == 1;
	}

	public boolean isStoryOutdated(String id, int latestVersion) {
		Cursor result = db.query(STORY_TABLE_NAME, new String[]{STORY_VERSION}, STORY_ID + "=?", new String[]{id}, null, null, null);
		if (result.getCount() == 1) {
			result.moveToFirst();
			return result.getInt(result.getColumnIndex(STORY_VERSION)) < latestVersion;
		}

		return false;
	}

	public Cursor getStatistics() {
		return db.query(STATISTICS_TABLE_NAME, null, null, null, null, null,
				STATISTICS_DATE + " DESC");
	}

	public boolean insertStatistic(String storyId, Date startTime, long duration, int distance) {
		ContentValues values = new ContentValues(4);
		values.put(STATISTICS_STORY_ID, storyId);
		values.put(STATISTICS_DATE, DateUtils.formatSqliteDate(startTime));
		values.put(STATISTICS_DURATION, duration);
		values.put(STATISTICS_DISTANCE, distance);
		return db.insert(STATISTICS_TABLE_NAME, null, values) != -1;
	}

	public boolean insertSaveAndQuit(String statisticId, String historyRootId) {
		ContentValues mValues = new ContentValues(2);
		mValues.put(SAVE_QUIT_STATISTIC_ID, statisticId);
		mValues.put(SAVE_QUIT_HISTORY_ROOT_ID, historyRootId);
		return db.insert(SAVE_QUIT_TABLE_NAME, null, mValues) != -1;
	}

	public Cursor getSaveAndQuit(String historyRootId) {
		return db.query(
				SAVE_QUIT_TABLE_NAME, new String[] {SAVE_QUIT_ID},
				SAVE_QUIT_HISTORY_ROOT_ID + "=?", new String[]{historyRootId},
				null, null, null);
	}

	public boolean deleteSaveAndQuit(String sqId) {
		int result = db.delete(SAVE_QUIT_TABLE_NAME, SAVE_QUIT_HISTORY_ROOT_ID
				+ "=?", new String[]{sqId});
		return result > 0;
	}

	public boolean insertLocation(String sqId, double latitude, double longitude) {
		ContentValues mValues = new ContentValues(3);
		mValues.put(LOCATIONS_SAVE_QUIT_ID, sqId);
		mValues.put(LOCATIONS_LATITUDE, latitude);
		mValues.put(LOCATIONS_LONGITUDE, longitude);
		return db.insert(LOCATIONS_TABLE_NAME, null, mValues) != -1;
	}

	public Cursor getLocation() {
		return db.query(LOCATIONS_TABLE_NAME, null, null, null, null, null,
				null);
	}

	public boolean deleteLocation(String locationId) {
		return db.delete(LOCATIONS_TABLE_NAME, LOCATIONS_ID + "=?",
				new String[]{locationId}) > 0;
	}

	public boolean insertHistory(String storyId, String historyPrevious,
	                             String historyNext) {
		ContentValues mValues = new ContentValues(3);
		mValues.put(HISTORY_STORY_ID, storyId);
		mValues.put(HISTORY_PREVIOUS_TAG_ID, historyPrevious);
		mValues.put(HISTORY_NEXT_TAG_ID, historyNext);
		return db.insert(HISTORY_TABLE_NAME, null, mValues) != -1;
	}

	public Cursor getHistory() {
		return db.query(HISTORY_TABLE_NAME, null, null, null, null, null,
				null);
	}

	public boolean deleteHistory(String historyId) {
		return db.delete(HISTORY_TABLE_NAME, HISTORY_ID + "=?",
				new String[]{historyId}) > 0;
	}


}
