package org.ZlatevGichev.SearchRater;


import org.junit.Test;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;


public class DatabaseHandlerTest extends AndroidTestCase {

	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_SEARCH_QUERY = "search_query";
	private static final String KEY_LINK = "link";

	private DatabaseHandler databaseHandler1;
	private Context context_1;
	SQLiteDatabase db;

	@Override
	 protected void setUp() throws Exception {
	  super.setUp();
	  context_1 = getContext();
	  databaseHandler1 = new DatabaseHandler(context_1);
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
