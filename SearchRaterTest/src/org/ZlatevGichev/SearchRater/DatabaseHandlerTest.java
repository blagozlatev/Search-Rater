package org.ZlatevGichev.SearchRater;


import java.util.ArrayList;

import org.ZlatevGichev.SearchRater.DatabaseHandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.test.AndroidTestCase;


public class DatabaseHandlerTest extends AndroidTestCase {

	@Override
	 protected void setUp() throws Exception {
	  super.setUp();

	  context_1 = getContext();
	  databaseHandler1 = new DatabaseHandler(context_1);
	  databaseHandler = new DatabaseHandler(context_1);
	 }

	private DatabaseHandler databaseHandler1;
	private DatabaseHandler databaseHandler;
	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_LINK = "link";
	private Context context_1;
	SQLiteDatabase db;
	
	public void testGetAllLinks() throws Throwable {
		ArrayList<Bundle> testBundle = new ArrayList<Bundle>();
		Bundle bundle = new Bundle();
		String title = "www.alfaromeo.com";
		String url = "http://www.alfaromeo.com";
		
		bundle.putString("title", title);
		bundle.putString("link", url);
		testBundle.add(bundle);

		setUp();
		DatabaseHandler dbHandlerForLinks = databaseHandler;

		dbHandlerForLinks.addLink(url);
		
		ArrayList<Bundle> linkLists = dbHandlerForLinks.getAllLinks();
		
		assertEquals(testBundle.toString(), linkLists.toString());
		assertTrue(!linkLists.isEmpty());
		
		dbHandlerForLinks.deleteLink(url);
	}


	public void testAddLink() throws Throwable {
		String URL = "http://www.abv.bg";
		
		setUp();
		databaseHandler1.addLink(URL);
		
		db = databaseHandler1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT " + KEY_LINK + " FROM "
				+ TABLE_BLOCKED_RESULTS, null);
		if (cursor.moveToFirst()) {
			do {
				String link = cursor.getString(0);
				assertEquals("http://www.abv.bg",  link);
				databaseHandler1.deleteLink(URL);
			} while (cursor.moveToNext());
		}
		db.close();
	}


	 public void testDeleteLink() throws Throwable {
		String link, URL = "http://www.google.com/";
		ArrayList<Bundle> emptyList = new ArrayList<Bundle>();
		
		setUp();
		databaseHandler1.addLink(URL);
		
		db = databaseHandler1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT " + KEY_LINK + " FROM "
				+ TABLE_BLOCKED_RESULTS, null);
		
		if (cursor.moveToFirst()) {
			do {
				link = cursor.getString(0);
				assertEquals("http://www.google.com/",  link);
				databaseHandler1.deleteLink(URL);
			} while (cursor.moveToNext());
			
		assertEquals(emptyList, databaseHandler1.getAllLinks());
		}
	}
	public void testIsEmpty() throws Throwable {
		boolean bool;
			setUp();
			databaseHandler1.addLink("www.abv.bg");
			
			db = databaseHandler1.getWritableDatabase();
			
			Cursor cur = db.rawQuery("SELECT " + KEY_LINK + " FROM "
					+ TABLE_BLOCKED_RESULTS, null);
			
			if(cur != null) {
				cur.moveToFirst();
				if(cur.getInt(0) == 0) {
					db.close();
					bool = true;
					assertTrue(bool);
				}
			}
			db.close();
			bool = false;
			
			assertFalse(bool);
			assertFalse(databaseHandler1.isEmpty());
			
			databaseHandler1.deleteLink("www.abv.bg");
			
			assertTrue(databaseHandler1.isEmpty());

	}
}
