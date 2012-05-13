package org.ZlatevGichev.SearchRater;


import org.junit.Test;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;



public class DatabaseHandlerTest extends AndroidTestCase {

	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_SEARCH_QUERY = "search_query";

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
	 public void testDeleteLink() throws Throwable {
		 String searchQuery = "blago"; 
		 databaseHandler1.addLink(searchQuery, "www.google.com");
		 setUp();
		 db = databaseHandler1.getWritableDatabase();
		 Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
		 		 + " WHERE " + KEY_SEARCH_QUERY + "=" + "'" + searchQuery + "'", 
		 		  null);
		 if(cursor.moveToFirst()) {
			 databaseHandler1.deleteLink("www.google.com");
			 assertFalse(cursor.moveToNext());
		 }
		 db.close();
	 }
}
