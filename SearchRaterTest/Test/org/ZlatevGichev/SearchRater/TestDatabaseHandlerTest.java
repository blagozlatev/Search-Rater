package org.ZlatevGichev.SearchRater;



import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;



public class TestDatabaseHandlerTest extends AndroidTestCase {

	private DatabaseHandler databaseHandler1;
	private static final String TABLE_BLOCKED_RESULTS = "blocked_results";
	private static final String KEY_SEARCH_QUERY = "search_query";
	private static final String KEY_LINK = "link";
	private Context context_1;
	SQLiteDatabase db;

	@Before
	public void createDatabaseHandler() throws Throwable {
		//databaseHandler = new DatabaseHandler(context);
		context_1 = getContext();
		databaseHandler1 = new DatabaseHandler(context_1);
		databaseHandler1.addLink("blago", "www.google.com");
		
	}
	
	@Test
	public void testDatabaseHandler() {
		
		//assertEquals(null, databaseHandler1);
		//assertNull(databaseHandler1);
	}

	@Test
	public void testOnCreateSQLiteDatabase() {
		
	}

	@Test
	public void testOnUpgradeSQLiteDatabaseIntInt() {
	
	}

	@Test
	public void testGetAllContactsForQuery() {
		/*final DatabaseHandler databaseHandlerTvaEZaKOnete = new DatabaseHandler(context_1);

	    ArrayList<String> SerqshtiKone = databaseHandlerTvaEZaKOnete.getAllContactsForQuery("Blagovest Zlatev");

		assertTrue(!SerqshtiKone.isEmpty()); */
	}

	@Test
	public void testAddLink() {
		assertNotNull(TABLE_BLOCKED_RESULTS);
		db = databaseHandler1.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLOCKED_RESULTS
				    + " WHERE " + KEY_SEARCH_QUERY + "=" + "'" + "blago" + "'", 
				    null);
		if (cursor.moveToFirst()) {
			Log.d("myapp","imame rezultat");
		}
		else {
			Log.d("myapp","nqmame");
		}
		db.close();
	/*	new DatabaseHandler(context_1).addLink(new MainActivity().getSearchQueryForBundle(),
				new MainActivity().getLinkFromBundle(-1)); */
	}

	@Test
	public void testDeleteLink() {
		databaseHandler1.addLink("email", "www.abv.bg");
		databaseHandler1.deleteLink("www.abv.bg");
		db = databaseHandler1.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM"+ TABLE_BLOCKED_RESULTS
			    + " WHERE " + KEY_LINK + "=" + "'" + "www.abv.bg" + "'", 
			    null);
		if (cursor.moveToFirst()) {
			Log.d("myapp"," not deleted");
		}
		db.close();
	}

}
