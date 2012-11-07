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
	public static final String STORY_TITLE = "TITLE";

	private static final String STORY_CREATE = "CREATE TABLE "
			+ STORY_TABLE_NAME + " (" + STORY_ID + " TEXT, " + STORY_AUTHOR
			+ " TEXT, " + STORY_TITLE + " TEXT);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORY_CREATE);
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
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('4ff0b8f0-18a6-11e2-892e-0800200c9a66', 'Kyrre Havik Eriksen', 'Skattejakt på blindern')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('ae7fed40-19c4-11e2-892e-0800200c9a66', 'Randall Munroe', 'Pwned (Xkcd.com)')");
			db.execSQL("INSERT INTO "
					+ STORY_TABLE_NAME
					+ " VALUES ('5640553e-8fe9-46d3-b647-6aebb70882a5', 'Thomas Portilla', 'Naturquiz for Sognsvann')");
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
}
