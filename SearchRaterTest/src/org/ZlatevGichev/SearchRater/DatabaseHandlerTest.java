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
	private static final String KEY_SEARCH_QUERY = "search_query";
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

		ArrayList<Bundle> linkLists = dbHandlerForLinks.getAllLinks();
		assertEquals(testBundle.toString(), linkLists.toString());
		assertTrue(!linkLists.isEmpty());

	}


	public void testAddLink() throws Throwable {
		String searchQuery = "blago";
		String URL = "http://www.abv.bg";
		
		databaseHandler1.addLink(searchQuery , URL);
		setUp();
		db = databaseHandler1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				+ " WHERE " + KEY_SEARCH_QUERY + "=" + "'" + searchQuery + "'", 
				null);
		if(cursor.moveToFirst()) {
			assertEquals("http://www.abv.bg",  String.valueOf(cursor.getString(2)));
			databaseHandler1.deleteLink(URL);
		}
		db.close();
	}


	 public void testDeleteLink() throws Throwable {
		String searchQuery = "krasi"; 
		String URL = "http://www.google.com/";

		databaseHandler1.addLink(searchQuery, URL);
		setUp();
		db = databaseHandler1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				 + " WHERE " + KEY_LINK + "=" + "'" + URL + "'", 
				 null);
		databaseHandler1.deleteLink(URL);
		if(cursor.moveToFirst()) {
			assertFalse(cursor.moveToNext());
		}
	}
}
