package org.ZlatevGichev.SearchRater;

import static org.junit.Assert.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.OpenableColumns;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;

public class DBHandlerTest extends AndroidTestCase {
	private static final String DATABASE_NAME = "blockedLinksByWords";
	
	private SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
       // getContext().deleteDatabase(DATABASE_NAME);
        
        //db = getContext().openDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        assertNotNull(db);
    }
	/*@Before
	public void setUp() throws Exception {
	}


*/
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
