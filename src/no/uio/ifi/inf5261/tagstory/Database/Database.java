package no.uio.ifi.inf5261.tagstory.Database;

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
	public static final String STORY_TITLE = "STORY_NAME";
	public static final String STORY_START = "START";
	public static final String STORY_AGE = "AGE";
	public static final String STORY_GENRE = "GENRE";
	public static final String STORY_AREA = "AREA";
	public static final String STORY_DESC = "DESC";
	public static final String STORY_TAG_COUNT = "TAG_COUNT";
	public static final String STORY_DATE = "DATE";

	private static final String TAG_TABLE_NAME = "TAGS";
	public static final String TAG_ID = "_id";
	public static final String TAG_GPS = "GPS";
	public static final String TAG_STORIES = "TAG_STORIES";

	private static final String PARTS_TABLE_NAME = "PARTS";
	public static final String PARTS_ID = "_id";
	public static final String PARTS_STORY = "STORY";
	public static final String PARTS_TAG = "TAG";
	public static final String PARTS_TEXT = "TEXT";
	public static final String PARTS_WAYPOINT = "WAYPOINT";

	// private static final String = "";

	private static final String STORY_CREATE = "CREATE TABLE "
			+ STORY_TABLE_NAME + " (" + STORY_ID + " TEXT, " + STORY_AUTHOR
			+ " TEXT, " + STORY_TITLE + " TEXT, " + STORY_START + " TEXT, "
			+ STORY_DESC + " TEXT, " + STORY_AGE + " TEXT, " + STORY_GENRE
			+ " TEXT, " + STORY_AREA + " TEXT, " + STORY_TAG_COUNT + " TEXT, "
			+ STORY_DATE + " TEXT);";
	private static final String TAG_CREATE = "CREATE TABLE " + TAG_TABLE_NAME
			+ " (" + TAG_ID + " TEXT, " + TAG_GPS + " TEXT, " + TAG_STORIES
			+ " TEXT);";
	private static final String PARTS_CREATE = "CREATE TABLE "
			+ PARTS_TABLE_NAME + " (" + PARTS_ID + " int AUTO_INCREMENT, "
			+ PARTS_STORY + " TEXT, " + PARTS_TAG + " TEXT, " + PARTS_TEXT
			+ " TEXT, " + PARTS_WAYPOINT + " TEXT);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
			db.execSQL(TAG_CREATE);
			db.execSQL(PARTS_CREATE);
			populate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("UPGRADE", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// Kills the table and existing data
			db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PARTS_TABLE_NAME);

			// Recreates the database with a new version
			onCreate(db);
		}

		public void populate(SQLiteDatabase db) {
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('0', 'Haruki Murakami', 'Kafka on the Shore', 'tag1', "
					+ "'Comprising two distinct but interrelated plots, the narrative runs back and forth between the two, taking up each plotline in alternating chapters.', "
					+ "'All ages', 'Novel', 'Japan',  '3', '2002')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('1', 'Henrik Ibsen', 'Peer Gynt', 'tag', 'desc', 'age', 'genre', 'area', 'count', 'date')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('2', 'Jane Austen', 'Pride and Prejudice', 'tag', 'desc', 'age', 'genre', 'area', 'count', 'date')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('3', 'J.R.R. Tolkien', 'Lord of the Rings', 'tag', 'desc', 'age', 'genre', 'area', 'count', 'date')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('4', 'Randall Munroe', 'Pwned (Xkcd.com)', 'tag', 'desc', 'age', 'genre', 'area', 'count', 'date')");
			db.execSQL("INSERT INTO "
					+ PARTS_TABLE_NAME
					+ " VALUES ('0', '3', 'test5', 'Welcome to text-only Counter Strike. You are in a dark, outdoor map.', 'north; south; east; west;')");
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
		return db.query(STORY_TABLE_NAME, new String[] { STORY_ID, STORY_TITLE,
				STORY_AUTHOR }, null, null, null, null, STORY_TITLE + " DESC");
	}

	public void setTable(String rowID, String update) {
		db.execSQL("UPDATE " + STORY_TABLE_NAME + " SET value ='" + update
				+ "' WHERE _id = '" + rowID + "'");
	}

	/**
	 * Return a specific story based on Author and title of the Story.
	 * 
	 * @param author
	 * @param storyName
	 * @return
	 */
	public Cursor getStory(String author, String storyName) {
		return db.query(STORY_TABLE_NAME, null, STORY_AUTHOR + "=? AND "
				+ STORY_TITLE + "=?", new String[] { author, storyName }, null,
				null, null);
	}

	/**
	 * Return a specific story based on the ID.
	 * 
	 * @param id
	 * @return
	 */
	public Cursor getStory(String id) {
		return db.query(STORY_TABLE_NAME, null, STORY_ID + "=?",
				new String[] { id }, null, null, null);
	}
}
