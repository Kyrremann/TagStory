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
	public static final String STORY_AUTHOR = "AUTHOR";
	public static final String STORY_TITLE = "TITLE";
	public static final String STORY_LOCATION = "LOCATION";
	public static final String STORY_IMAGE = "IMAGE";
	private static final String STORY_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL);",
			STORY_TABLE_NAME, STORY_ID, STORY_AUTHOR, STORY_TITLE, STORY_LOCATION, STORY_IMAGE);

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

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
			db.execSQL(STATISTICS_CREATE);
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

	public boolean insertStory(String uuid, String author, String title, String location, String image) {
		ContentValues values = new ContentValues(5);
		values.put(STORY_ID, uuid);
		values.put(STORY_AUTHOR, author);
		values.put(STORY_TITLE, title);
		values.put(STORY_LOCATION, location);
		values.put(STORY_IMAGE, image);
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
}
