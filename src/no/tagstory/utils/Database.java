package no.tagstory.utils;

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
	public static final String STORY_LOCATION = "LOCATION";
	public static final String STORY_IMAGE = "IMAGE";
	
	public static final String POINTS_TABLE_NAME = "POINTS";
	public static final String POINTS_USERNAME = "_id";
	public static final String POINTS_STORY = "PASSWORD";
	public static final String POINTS_DATE = "DATE";
	public static final String POINTS_SCORE = "SCORE";
	
	private static final String STORY_CREATE = "CREATE TABLE "
			+ STORY_TABLE_NAME + " (" + STORY_ID + " TEXT, " + STORY_AUTHOR
			+ " TEXT, " + STORY_TITLE + " TEXT, " + STORY_LOCATION + " TEXT, " + STORY_IMAGE + " TEXT);";
	
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
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('0dd941f0-c943-11e2-8b8b-0800200c9a66', 'Klima nettverket', 'Oslo klimatur', 'Oslo', 'slottet.jpg')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('5fee5ee0-e11f-11e3-8b68-0800200c9a66', 'Kine Gjerstad Eide', 'Kiness bursdagsløp', 'Oslo', 'kine_gevaer.jpg')");

			db.execSQL("INSERT INTO " +
					STORY_TABLE_NAME +
					" VALUES ('58876600-0df3-11e4-9191-0800200c9a66', 'Kine Gjerstad Eide', 'Jentedagen 2014', 'Oslo', '')");
			*/
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
	 * @return A Cursor containing a list of stories titles and the author.
	 */
	public Cursor getStoryList() {
		return db.query(STORY_TABLE_NAME, null, null, null, null, null,
				STORY_TITLE + " DESC");
	}
}
