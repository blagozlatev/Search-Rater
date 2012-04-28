package org.ZlatevGichev.SearchRater;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "blockedLinksByWords";
	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_ID = "id";
	private static final String KEY_SEARCH_QUERY = "search_query";
	private static final String KEY_LINK = "link";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BLOCKED_RESULTS
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SEARCH_QUERY
				+ " TEXT," + KEY_LINK + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKED_RESULTS);
		onCreate(db);
	}

	public ArrayList<String> getAllContactsForQuery(String searchQuery) {
		ArrayList<String> linkList = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				+ " WHERE " + KEY_SEARCH_QUERY + "=" + "'" + searchQuery + "'",
				null);
		if (cursor.moveToFirst()) {
			do {
				String link = cursor.getString(2);
				linkList.add(link);
			} while (cursor.moveToNext());
		}
		db.close();
		return linkList;
	}

	public void addLink(String searchQuery, String link) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SEARCH_QUERY, searchQuery);
		values.put(KEY_LINK, link);
		db.insert(TABLE_BLOCKED_RESULTS, null, values);
		db.close();
	}

	public void deleteLink(String url) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BLOCKED_RESULTS, KEY_LINK + " = ?",
				new String[] { url });
		db.close();
	}
}