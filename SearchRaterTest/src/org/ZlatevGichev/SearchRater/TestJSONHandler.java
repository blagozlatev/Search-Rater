package org.ZlatevGichev.SearchRater;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;

import org.ZlatevGichev.SearchRater.JSONHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;


public class TestJSONHandler extends AndroidTestCase{

	String urlToReturn = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCYLfuVS63QDAJCcXDh_gvRbObMEPRgqAI&cx=013036536707430787589:_pqjad5hr1a&q=Blagovest%20Zlatev&alt=json";
	String searchQuery = "Blagovest Zlatev";
	
	public void testConnectAndGetJSON() {
		JSONObject test = JSONHandler.connectAndGetJSON("Blagovest Zlatev");
		HttpParams httpParameters = new BasicHttpParams();
		
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpGet httpget = new HttpGet(urlToReturn);
		HttpResponse response;
		JSONObject json = new JSONObject();
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = JSONHandler.convertStreamToString(instream);
				json = new JSONObject(result);
				instream.close();
				assertEquals(test.getString("queries"), json.getString("queries"));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("MMYApp", String.valueOf(json));
}
		
	public void testGetResultsFromJSON () {
		ArrayList<Bundle> testNamesAndLinks = new ArrayList<Bundle>();
		ArrayList<Bundle> testGetRes =  JSONHandler.getResultsFromJSON("Alfa Romeo");
		assertNotNull(testGetRes.get(2).getString("title"));
		try {
			JSONObject json = JSONHandler.connectAndGetJSON("Alfa Romeo");
			JSONArray jsonArray = json.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				Bundle bundle = new Bundle();
				bundle.putString("title",
						jsonArray.getJSONObject(i).getString("title")
								.toString());
				bundle.putString("link",
						jsonArray.getJSONObject(i).getString("link").toString());
				testNamesAndLinks.add(bundle);
			}
		Log.d("test", String.valueOf(testGetRes));
		Log.d("get(2)", testGetRes.get(2).getString("title"));
		assertEquals(testGetRes.get(2).getString("title"), testNamesAndLinks.get(2).getString("title"));
		} catch (JSONException e) {
			e.printStackTrace();
		};
		
	}

	public void testConvertSearchQueryToURL() {

		String convert = JSONHandler.convertSearchQueryToURL(searchQuery);
		assertEquals(convert, urlToReturn.replaceAll("\\s+", "%20"));
	}
	

	public void testConvertStreamToString() {
		
	    InputStream input = new ByteArrayInputStream(searchQuery.getBytes());
	    String convert = JSONHandler.convertStreamToString(input).trim();
		    assertTrue(searchQuery.equals(convert));

	}

}
