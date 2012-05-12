package org.ZlatevGichev.SearchRater;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;


public class TestJSONHandler extends AndroidTestCase{

	String urlToReturn = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCZI07EqEqZJUOTX_yDGRFsuBzHseo3WRo&cx=013036536707430787589:_pqjad5hr1a&q=blago&alt=json";
	String data = "blago";
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testGetResultsFromJSON() {

	}

	@Test
	public void testConvertSearchQueryToURL() {

		String convert = JSONHandler.convertSearchQueryToURL(data);
		assertEquals(convert, urlToReturn.replaceAll("\\s+", "%20"));
	}
	
	@Test
	public void testConvertStreamToString() {
		
	    InputStream input = new ByteArrayInputStream(data.getBytes());
	    String convert = JSONHandler.convertStreamToString(input).trim();
		    assertTrue(data.equals(convert));

	}

}
