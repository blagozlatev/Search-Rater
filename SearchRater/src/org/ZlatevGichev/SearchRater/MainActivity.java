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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static ArrayList<Bundle> bundledNamesAndLinks = new ArrayList<Bundle>();
	int count = 0;
	static String querySearch;
	static JSONObject googleJSONQueryResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		final EditText editText = (EditText) findViewById(R.id.editText);
		final TextView textv = (TextView) findViewById(R.id.textView);
		Button nextButton = (Button) findViewById(R.id.btnNext);

		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!editText.getText().equals("")) {
					String baseLinkForSearch = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBJWWAjCjFy-JnrYYkwDJJwrUwJYtzEDTk&cx=013036536707430787589:_pqjad5hr1a&q=";
					String endLinkForSearch = "&alt=json";
					
					querySearch = baseLinkForSearch + editText.getText()
							+ endLinkForSearch;
					querySearch = querySearch.replaceAll("\\s+", "%20");
					googleJSONQueryResult = connect(querySearch);
					parseJSONForNameAndLink(googleJSONQueryResult);
				}
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (!bundledNamesAndLinks.isEmpty()
						&& count < bundledNamesAndLinks.size()) {
					textv.setText("Title: "
							+ bundledNamesAndLinks.get(count).getString("title")
							+ "\n" + "Link: "
							+ bundledNamesAndLinks.get(count).getString("link"));
					count++;
				} else if (bundledNamesAndLinks.isEmpty()) {
					Toast.makeText(getBaseContext(),
							"The search did not return any results",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getBaseContext(),
							"There are no more results", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	public static JSONObject connect(String url) {
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

	public static String convertStreamToString(InputStream is) {
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

	public static void parseJSONForNameAndLink(JSONObject json) {
		try {
			JSONArray jsonArray = json.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				Bundle bundle = new Bundle();
				bundle.putString("title",
						jsonArray.getJSONObject(i).getString("title")
								.toString());
				bundle.putString("link",
						jsonArray.getJSONObject(i).getString("link").toString());
				bundledNamesAndLinks.add(bundle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		;
	}
}