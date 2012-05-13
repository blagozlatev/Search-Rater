package org.ZlatevGichev.SearchRater;


import java.util.ArrayList;

import org.junit.Test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;


public class DatabaseHandlerTest extends AndroidTestCase {

	@Override
	 protected void setUp() throws Exception {
	  super.setUp();

	  context_1 = getContext();
	  databaseHandler1 = new DatabaseHandler(context_1);

	 }

	private DatabaseHandler databaseHandler1;
	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_SEARCH_QUERY = "search_query";
	private static final String KEY_LINK = "link";
	private Context context_1;
	SQLiteDatabase db;

	@Test
	public void testGetAllContactsForQuery() throws Throwable {
		ArrayList<String> Test = new ArrayList<String>();
		String query = "blagovest zlatev";
		String url1 = "www.abv.bg";
		String url2 = "www.google.bg";

		setUp();
		DatabaseHandler dbHandlerForContacts = databaseHandler1;

		Test.add(url1);
		Test.add(url2);
		dbHandlerForContacts.addLink(query, url1);
		dbHandlerForContacts.addLink(query, url2);

		ArrayList<String> urlList = dbHandlerForContacts.getAllContactsForQuery(query);
	    assertEquals(Test, urlList);
		if ( urlList.isEmpty() == false) {
			dbHandlerForContacts.deleteLink(url1);
			dbHandlerForContacts.deleteLink(url2);	
		}
	}

	@Test
	public void testAddLink() throws Throwable {
		String searchQuery = "blago";
		String URL = "www.abv.bg";
		databaseHandler1.addLink(searchQuery , URL);
		setUp();
		db = databaseHandler1.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				+ " WHERE " + KEY_SEARCH_QUERY + "=" + "'" + searchQuery + "'", 
				null);
		assertTrue(cursor.moveToFirst());
		db.close();
	}

	@Test
	 public void testDeleteLink() throws Throwable {
		 String searchQuery = "krasi"; 
		 String URL = "www.google.com";

		databaseHandler1.addLink(searchQuery, URL);
		setUp();
		db = databaseHandler1.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				 + " WHERE " + KEY_LINK + "=" + "'" + URL + "'", 
				 null);
		if(cursor.moveToFirst()) {
			databaseHandler1.deleteLink(URL);
			assertFalse(cursor.moveToNext());
		}
	} 
}
