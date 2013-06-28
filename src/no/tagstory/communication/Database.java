package no.tagstory.communication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
	public static final String STORY_PLACE = "PLACE";
	public static final String STORY_IMAGE = "IMAGE";
	
	public static final String USER_TABLE_NAME = "USER";
	public static final String USER_ID = "_id";
	public static final String USER_PASSWORD = "PASSWORD";
	
	public static final String POINTS_TABLE_NAME = "POINTS";
	public static final String POINTS_USERNAME = "_id";
	public static final String POINTS_STORY = "PASSWORD";
	public static final String POINTS_DATE = "DATE";
	public static final String POINTS_SCORE = "SCORE";
	
	private static final String STORY_CREATE = "CREATE TABLE "
			+ STORY_TABLE_NAME + " (" + STORY_ID + " TEXT, " + STORY_AUTHOR
			+ " TEXT, " + STORY_TITLE + " TEXT, " + STORY_PLACE + " TEXT, " + STORY_IMAGE + " TEXT);";
	
	private static final String USER_CREATE = "CREATE TABLE "
			+ USER_TABLE_NAME + " (" + USER_ID + " TEXT, " + USER_PASSWORD
			+ " TEXT);";
	
	private static final String POINTS_CREATE = "CREATE TABLE "
			+ POINTS_TABLE_NAME + " (" + POINTS_USERNAME + " TEXT, " + POINTS_STORY
			+ " TEXT, " + POINTS_DATE + " TEXT, " + POINTS_SCORE + " TEXT);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
			db.execSQL(USER_CREATE);
			db.execSQL(POINTS_CREATE);
			populate(db);
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

		public void populate(SQLiteDatabase db) {
			// STORIES
			/*
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('4ff0b8f0-18a6-11e2-892e-0800200c9a66', 'Kyrre Havik Eriksen', 'Skattejakt på Blindern', 'blindern.jpg')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('ae7fed40-19c4-11e2-892e-0800200c9a66', 'Randall Munroe', 'Pwned (Xkcd.com)', 'sognsvann.jpg')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('5640553e-8fe9-46d3-b647-6aebb70882a5', 'Thomas Portilla', 'Naturquiz for Sognsvann', 'blindern.jpg')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('4ef0b8f0-18a6-11e2-892e-0800200c9a66', 'Bao Marianna Nguyen', 'Treasure hunt at Blindern', 'sognsvann.jpg')");
			 */
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('0dd941f0-c943-11e2-8b8b-0800200c9a66', 'Klima nettverket', 'Oslo klimatur', 'Oslo', 'slottet.jpg')");
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

	/**
	 * 
	 * @return A Cursor containing a list of stories titles and the author.
	 */
	public Cursor getStoryList() {
		return db.query(STORY_TABLE_NAME, null, null, null, null, null,
				STORY_TITLE + " DESC");
	}
	
	public void setTable(String rowID, String update) {
		db.execSQL("UPDATE " + STORY_TABLE_NAME + " SET value ='" + update
				+ "' WHERE _id = '" + rowID + "'");
	}
	

	public long setUsernameAndPassword(String username, String password) {
		ContentValues values = new ContentValues(2);
		values.put(USER_ID, username);
		values.put(USER_PASSWORD, password);
		int rows = db.update(USER_TABLE_NAME, values, USER_ID + " =?", new String[] { username });
		if (rows == 0)
			return db.insert(USER_TABLE_NAME, null, values);
		
		return rows;
	}
	
	public boolean isCorrectPassword(String username, String password) {
		Cursor cursor = db.query(USER_TABLE_NAME, new String[] { USER_PASSWORD }, USER_ID + " =?", new String[] { USER_ID }, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() == 0)
			return false;
		return cursor.getString(0).equals(password);
	}
	
	public boolean isExistsUser(String username) {
		Cursor cursor = db.query(USER_TABLE_NAME, new String[] { USER_PASSWORD }, USER_ID + " =?", new String[] { USER_ID }, null, null, null);
		return cursor.getCount() != 0;
	}
}
