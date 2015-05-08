package no.tagstory.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.IInterface;
import android.util.Log;
import no.tagstory.statistics.HistoryNode;
import no.tagstory.statistics.StoryStatistic;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Database {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "TagStory";

	private static final String STORY_TABLE_NAME = "STORIES";
	private static final String STORY_ID = "_id";
	private static final String STORY_AUTHOR = "author";
	private static final String STORY_TITLE = "title";
	private static final String STORY_LOCATION = "location";
	private static final String STORY_IMAGE = "image";
	private static final String STORY_VERSION = "version";
	private static final String STORY_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s INTEGER NOT NULL);",
			STORY_TABLE_NAME, STORY_ID, STORY_AUTHOR, STORY_TITLE, STORY_LOCATION, STORY_IMAGE, STORY_VERSION);

	private static final String STATISTICS_TABLE_NAME = "STATISTICS";
	private static final String STATISTICS_ID = "_id";
	private static final String STATISTICS_STORY_ID = "story_id";
	public static final String STATISTICS_START_DATE = "start_date";
	private static final String STATISTICS_END_DATE = "end_date";
	private static final String STATISTICS_DURATION = "duration";
	public static final String STATISTICS_DISTANCE = "distance";
	private static final String STATISTICS_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT," +
					"%s INTEGER NOT NULL," +
					"%s REAL);",
			STATISTICS_TABLE_NAME, STATISTICS_ID, STATISTICS_STORY_ID, STATISTICS_START_DATE,
			STATISTICS_END_DATE, STATISTICS_DURATION, STATISTICS_DISTANCE);

	private static final String SAVE_TRAVEL_TABLE_NAME = "SAVE_TRAVEL";
	private static final String SAVE_TRAVEL_ID = "_id";
	private static final String SAVE_TRAVEL_STATISTIC_ID = "statistic_id";
	private static final String SAVE_TRAVEL_STORY_ID = "story_id";
	public static final String SAVE_TRAVEL_TIME_SAVED = "time_saved";
	private static final String SAVE_TRAVEL_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s INTEGER NOT NULL, " +
					"%s INTEGER NOT NULL," +
					"%s TEXT NOT NULL);",
			SAVE_TRAVEL_TABLE_NAME, SAVE_TRAVEL_ID, SAVE_TRAVEL_STATISTIC_ID, SAVE_TRAVEL_STORY_ID, SAVE_TRAVEL_TIME_SAVED);


	private static final String LOCATIONS_TABLE_NAME = "LOCATIONS";
	private static final String LOCATIONS_ID = "_id";
	private static final String LOCATIONS_STATISTIC_ID = "statistic_id";
	private static final String LOCATIONS_LATITUDE = "latitude";
	private static final String LOCATIONS_LONGITUDE = "longitude";
	private static final String LOCATIONS_PROVIDER = "provider";
	private static final String LOCATIONS_TIMESTAMP = "timestamp";
	private static final String LOCATIONS_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s INTEGER NOT NULL," +
					"%s DOUBLE NOT NULL," +
					"%s DOUBLE NOT NULL," +
					"%s STRING NOT NULL," +
					"%s INTEGER NOT NULL);",
			LOCATIONS_TABLE_NAME, LOCATIONS_ID, LOCATIONS_STATISTIC_ID,
			LOCATIONS_LATITUDE, LOCATIONS_LONGITUDE, LOCATIONS_PROVIDER, LOCATIONS_TIMESTAMP);

	private static final String HISTORY_TABLE_NAME = "HISTORY";
	private static final String HISTORY_ID = "_id";
	private static final String HISTORY_STATISTICS_ID = "statistics_id";
	private static final String HISTORY_TAG_ID = "tag_id";
	private static final String HISTORY_PREVIOUS_TAG = "previous_tag";
	private static final String HISTORY_NEXT_TAG = "next_tag";
	private static final String HISTORY_FINISHED_GAME = "finished_game";
	private static final String HISTORY_ROOT = "root";
	private static final String HISTORY_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s INTEGER NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT," +
					"%s TEXT," +
					"%s INTEGER DEFAULT 0," +
					"%s INTEGER DEFAULT 0);",
			HISTORY_TABLE_NAME, HISTORY_ID, HISTORY_STATISTICS_ID, HISTORY_TAG_ID,
			HISTORY_PREVIOUS_TAG, HISTORY_NEXT_TAG, HISTORY_FINISHED_GAME, HISTORY_ROOT);

	private static final String QUIZ_SCORE_TABLE_NAME = "QUIZ_SCORE";
	private static final String QUIZ_SCORE_ID = "_id";
	private static final String QUIZ_SCORE_SCORE = "score";
	private static final String QUIZ_SCORE_TAG_ID = "tag";
	private static final String QUIZ_SCORE_STORY_ID = "story";
	private static final String QUIZ_SCORE_CREATE = String.format(Locale.ENGLISH,
			"CREATE TABLE %s (" +
					"%s INTEGER PRIMARY KEY AUTOINCREMENT," +
					"%s INTEGER NOT NULL," +
					"%s TEXT NOT NULL," +
					"%s TEXT NOT NULL);",
			QUIZ_SCORE_TABLE_NAME, QUIZ_SCORE_ID, QUIZ_SCORE_SCORE,
			QUIZ_SCORE_TAG_ID, QUIZ_SCORE_STORY_ID);

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
			db.execSQL(STATISTICS_CREATE);
			db.execSQL(SAVE_TRAVEL_CREATE);
			db.execSQL(LOCATIONS_CREATE);
			db.execSQL(HISTORY_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("UPGRADE", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// Kills the table and existing data
			// db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE_NAME);

			// Recreates the database with a new version
			// onCreate(db);
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
		Cursor cursor = db.query(STORY_TABLE_NAME, new String[]{STORY_ID}, STORY_ID + "=?", new String[]{id}, null, null, null);
		int result = cursor.getCount();
		cursor.close();
		return result == 1;
	}

	public boolean isStoryOutdated(String id, int latestVersion) {
		boolean isOutdated = false;
		Cursor result = db.query(STORY_TABLE_NAME, new String[]{STORY_VERSION}, STORY_ID + "=?", new String[]{id}, null, null, null);
		if (result.getCount() == 1) {
			result.moveToFirst();
			isOutdated = result.getInt(result.getColumnIndex(STORY_VERSION)) < latestVersion;
		}
		result.close();

		return isOutdated;
	}

	public int getStatisticId(String storyId, Date startTime) {
		int id = -1;
		Cursor cursor = db.query(STATISTICS_TABLE_NAME, new String[]{ STATISTICS_ID },
				String.format(Locale.ENGLISH, "%s = ? AND %s = ?", STATISTICS_STORY_ID, STATISTICS_START_DATE), new String[]{ storyId, DateUtils.formatSqliteDate(startTime) },
				null, null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			id = cursor.getInt(0);
		}
		cursor.close();

		return id;
	}

	public StoryStatistic getStatistic(int statisticsId) throws RuntimeException {
		Cursor cursor = db.query(STATISTICS_TABLE_NAME, null, STATISTICS_ID + "=?", new String[]{ Integer.toString(statisticsId) },
				null, null, null);
		if (cursor.getCount() == 0) {
			throw new RuntimeException("No statistics for id " + statisticsId);
		}
		cursor.moveToFirst();

		Date startDate;
		try {
			startDate = DateUtils.parseSqliteDate(cursor.getString(2));
		} catch (ParseException e) {
			startDate = null;
		}
		StoryStatistic statistic = new StoryStatistic(cursor.getString(1), startDate);
		statistic.setDuration(cursor.getLong(4));
		statistic.setDistance(cursor.getInt(5));
		statistic.setSaved(false);
		cursor.close();
		return statistic;
	}

	public Cursor getStatistics() {
		return db.query(STATISTICS_TABLE_NAME, null, STATISTICS_END_DATE + "IS NOT NULL",
				null, null, null, STATISTICS_START_DATE + " DESC");
	}

	public boolean insertStatistic(String storyId, Date startTime, Date endTime, long duration, int distance) {
		return db.insert(STATISTICS_TABLE_NAME, null,
				generateStatisticValues(storyId, startTime, endTime, duration, distance)) != -1;
	}

	public boolean updateStatistic(String storyId, Date startTime, Date endTime, long duration, int distance) {
		return db.update(STATISTICS_TABLE_NAME,
				generateStatisticValues(storyId, startTime, endTime, duration, distance),
				null, null) != 0;
	}

	private ContentValues generateStatisticValues(String storyId, Date startTime, Date endTime, long duration, int distance) {
		ContentValues values = new ContentValues(5);
		values.put(STATISTICS_STORY_ID, storyId);
		values.put(STATISTICS_START_DATE, DateUtils.formatSqliteDate(startTime));
		values.put(STATISTICS_END_DATE, DateUtils.formatSqliteDate(endTime));
		values.put(STATISTICS_DURATION, duration);
		values.put(STATISTICS_DISTANCE, distance);
		return values;
	}

	public Cursor getLocations(int statisticsId) {
		return db.query(LOCATIONS_TABLE_NAME, new String[]{ LOCATIONS_LATITUDE, LOCATIONS_LONGITUDE },
				LOCATIONS_STATISTIC_ID + "=?", new String[]{Integer.toString(statisticsId)},
				null, null, null);
	}

	public boolean insertLocation(int statisticId, Location location) {
		ContentValues mValues = new ContentValues(3);
		mValues.put(LOCATIONS_STATISTIC_ID, statisticId);
		mValues.put(LOCATIONS_LATITUDE, location.getLatitude());
		mValues.put(LOCATIONS_LONGITUDE, location.getLongitude());
		mValues.put(LOCATIONS_PROVIDER, location.getProvider());
		mValues.put(LOCATIONS_TIMESTAMP, location.getTime());
		return db.insert(LOCATIONS_TABLE_NAME, null, mValues) != -1;
	}

	public Cursor getHistories(int statisticsId) {
		return db.query(HISTORY_TABLE_NAME, null, HISTORY_STATISTICS_ID + "=?", new String[]{ Integer.toString(statisticsId) },
				null, null, null);
	}

	public boolean insertHistory(int statisticsId, HistoryNode node) {
		ContentValues mValues = new ContentValues(4);
		mValues.put(HISTORY_STATISTICS_ID, statisticsId);
		mValues.put(HISTORY_TAG_ID, node.getTagUUID());
		mValues.put(HISTORY_FINISHED_GAME, node.finishedGame);
		if (node.hasPrevious()) {
			mValues.put(HISTORY_PREVIOUS_TAG, node.previous.getTagUUID());
		}
		if (node.hasNext()) {
			mValues.put(HISTORY_NEXT_TAG, node.next.getTagUUID());
		}
		mValues.put(HISTORY_ROOT, node.root);
		return db.insert(HISTORY_TABLE_NAME, null, mValues) != -1;
	}

	public boolean insertSaveTravel(int statisticsId, String storyId) {
		ContentValues values = new ContentValues();
		values.put(SAVE_TRAVEL_STATISTIC_ID, statisticsId);
		values.put(SAVE_TRAVEL_STORY_ID, storyId);
		values.put(SAVE_TRAVEL_TIME_SAVED, DateUtils.formatSqliteDate(new Date()));
		return db.insert(SAVE_TRAVEL_TABLE_NAME, null, values) != -1;
	}

	public boolean hasSaveTravels(String storyId) {
		boolean hasStory = false;
		Cursor cursor = db.query(SAVE_TRAVEL_TABLE_NAME, new String[]{SAVE_TRAVEL_STORY_ID},
				SAVE_TRAVEL_STORY_ID + "=?", new String[]{ storyId },
				null, null, null);
		if (cursor.getCount() > 0) {
			hasStory = true;
		}
		cursor.close();

		return hasStory;
	}

	public Cursor getSaveTravels(String storyId) {
		return db.query(SAVE_TRAVEL_TABLE_NAME, null,
				SAVE_TRAVEL_STORY_ID + "=?", new String[]{storyId},
				null, null, null);
	}

	public void deleteSavedTravel(String id) {
		db.delete(SAVE_TRAVEL_TABLE_NAME, SAVE_TRAVEL_STATISTIC_ID + "=?", new String[]{ id });
	}

	public void saveQuizScore(int quizScore, String tagId, String storyId) {
		ContentValues values = new ContentValues(3);
		values.put(QUIZ_SCORE_SCORE, quizScore);
		values.put(QUIZ_SCORE_TAG_ID, tagId);
		values.put(QUIZ_SCORE_STORY_ID, storyId);
	}
}