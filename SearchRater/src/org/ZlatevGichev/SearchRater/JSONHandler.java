package org.ZlatevGichev.SearchRater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class JSONHandler {

	protected static JSONObject connectAndGetJSON(String searchQuery) {
		String url = convertSearchQueryToURL(searchQuery);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		JSONObject json = new JSONObject();
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				json = new JSONObject(result);
				instream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	protected static String convertSearchQueryToURL(String searchQuery) {
		String baseUrl = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCZI07EqEqZJUOTX_yDGRFsuBzHseo3WRo&cx=013036536707430787589:_pqjad5hr1a&q=";
		String endURL = "&alt=json";
		String urlToReturn = baseUrl + searchQuery + endURL;
		urlToReturn = urlToReturn.replaceAll("\\s+", "%20");
		return urlToReturn;
	}

	protected static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static ArrayList<Bundle> getResultsFromJSON(String searchQuery) {
		ArrayList<Bundle> NamesAndLinksToReturn = new ArrayList<Bundle>();
		try {
			JSONObject json = connectAndGetJSON(searchQuery);
			JSONArray jsonArray = json.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				Bundle bundle = new Bundle();
				if (i == 0) {
					bundle.putString("search_query", searchQuery);
				}
				bundle.putString("title",
						jsonArray.getJSONObject(i).getString("title")
								.toString());
				bundle.putString("link",
						jsonArray.getJSONObject(i).getString("link").toString());
				NamesAndLinksToReturn.add(bundle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		};
		return NamesAndLinksToReturn;
	}
	
}
