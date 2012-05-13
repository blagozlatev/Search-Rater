package org.ZlatevGichev.SearchRater;

import java.io.ByteArrayInputStream;

import java.io.InputStream;



import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;



public class TestJSONHandler extends AndroidTestCase{

	String urlToReturn = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCZI07EqEqZJUOTX_yDGRFsuBzHseo3WRo&cx=013036536707430787589:_pqjad5hr1a&q=Blagovest%20Zlatev&alt=json";
	String searchQuery = "Blagovest Zlatev";
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test 
	public void testConnectAndGetJSON() {
		try {
			JSONObject test = JSONHandler.connectAndGetJSON("www.dir.bg"); 
			JSONObject test1 = JSONHandler.connectAndGetJSON("www.google.com"); 
			JSONObject test2 = JSONHandler.connectAndGetJSON("www.google.com"); 
			assertNotNull(test);
			assertNotNull(test1);
			assertNotNull(test2);
			assertNotNull(test1.getString("queries"));
			assertNotNull(test2.getString("queries"));
			assertNotSame(test.getString("queries"), test2.getString("queries"));
			assertEquals(test1.getString("queries"), test2.getString("queries"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConvertSearchQueryToURL() {

		String convert = JSONHandler.convertSearchQueryToURL(searchQuery);
		assertEquals(convert, urlToReturn.replaceAll("\\s+", "%20"));
	}
	
	@Test
	public void testConvertStreamToString() {
		
	    InputStream input = new ByteArrayInputStream(searchQuery.getBytes());
	    String convert = JSONHandler.convertStreamToString(input).trim();
		    assertTrue(searchQuery.equals(convert));

	}

}
