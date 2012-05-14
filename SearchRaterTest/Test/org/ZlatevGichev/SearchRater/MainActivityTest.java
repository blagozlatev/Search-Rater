package org.ZlatevGichev.SearchRater;


import java.util.ArrayList;

import org.junit.Test;

import android.content.Context;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
		private String testString;
		private  MainActivity mainActivity;
		private TextView editText;
		private ListView resultList;
		private	Button btnSearch;
		private DatabaseHandler databaseHandler;
		private Context context;
		ArrayList<Bundle> bundledNamesAndLinks = new ArrayList<Bundle>();
		Bundle testBundle;
		
	public MainActivityTest() {
		super("org.ZlatevGichev.SearchRater", MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		databaseHandler = new DatabaseHandler(context);
		testBundle = new Bundle();
		mainActivity = getActivity();  
		editText = (TextView) mainActivity.findViewById(R.id.editText);
		resultList = (ListView) mainActivity.findViewById(R.id.resultList);
		btnSearch = (Button) mainActivity.findViewById(R.id.btnSearch);
		testString = mainActivity.getString(R.id.editText);
	}

	@Test
	public void testGetTitleForLink() throws Exception {
		
		setUp();
		testBundle.putString("title", "Alfa Romeo");
		MainActivity.bundledNamesAndLinks.add(0, testBundle);
		String testGetTitle = mainActivity.getTitleForLink(0);
		assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("title"), testGetTitle);
	}
	@Test
	public void testGetLinkFromBundle() throws Exception {
		
		setUp();
		testBundle.putString("link", "www.google.com");
		MainActivity.bundledNamesAndLinks.add(0, testBundle);
		String testGetLink = mainActivity.getLinkFromBundle(0);
		assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("link"), testGetLink);
		
	}
	@Test
	public void testGetSearchQueryForBundle() throws Exception {
		
		setUp();
		testBundle.putString("search_query", "Alfa");
		MainActivity.bundledNamesAndLinks.add(0, testBundle);
		String testGetSearchQuery = mainActivity.getSearchQueryForBundle();
		assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("search_query"), testGetSearchQuery);
		
	}
	
}
