package org.Zlatev.SearchRater.Classes;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "blockedLinks";
	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_LINK = "link";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BLOCKED_RESULTS
				+ "(" + KEY_LINK + " TEXT PRIMARY KEY)";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKED_RESULTS);
		onCreate(db);
	}

	public ArrayList<Bundle> getAllLinks() {
		ArrayList<Bundle> linkList = new ArrayList<Bundle>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + KEY_LINK + " FROM "
				+ TABLE_BLOCKED_RESULTS, null);
		if (cursor.moveToFirst()) {
			do {
				String link = cursor.getString(0);
				Bundle bundle = new Bundle();
				bundle.putString("link", link);
				bundle.putString("title", link.substring(7));
				linkList.add(bundle);
			} while (cursor.moveToNext());
		}
		db.close();
		return linkList;
	}

	public void addLink(String link) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LINK, link);
		db.insert(TABLE_BLOCKED_RESULTS, null, values);
		db.close();
	}

	public void deleteLink(String link) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BLOCKED_RESULTS, KEY_LINK + " = ?",
				new String[] { link });
		db.close();
	}

	public boolean isEmpty() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BLOCKED_RESULTS, null);
		if (cur != null) {
			cur.moveToFirst();
			if (cur.getInt(0) == 0) {
				db.close();
				return true;
			}
		}
		db.close();
		return false;
	}
}