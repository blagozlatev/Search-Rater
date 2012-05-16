package org.ZlatevGichev.SearchRater;


import junit.framework.Assert;

import org.ZlatevGichev.SearchRater.MainActivity;
import org.junit.Test;

import com.jayway.android.robotium.solo.Solo;


import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;


public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
		
		private  MainActivity mainActivity;
		private Solo solo;
		Bundle testBundle;

	public MainActivityTest() {		
		super("org.ZlatevGichev.SearchRater", MainActivity.class);	
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		solo = new Solo(getInstrumentation(), getActivity());
		testBundle = new Bundle();
		mainActivity = getActivity();
	}
	@Test
	public void testBlockedAndUnblockedResults() throws Exception {
		  	setUp();
		  	solo.clickOnText("OK");
		  	solo.enterText(0, "Alfa Romeo");
		  	solo.clickOnButton("Search");
		  	solo.clickInList(1);
		  	Assert.assertTrue(solo.searchText("Alfa Romeo Australia")); //first result is blocked
		  	solo.clickLongInList(2); // second result is blocked
		  	solo.clickInList(2);
		  	Assert.assertTrue(solo.searchText("Alfa Romeo Australia"));
		  	solo.clickLongInList(2); // second result is unblocked
		  	Assert.assertTrue(solo.searchText("Alfa Romeo Australia")); 
		  	solo.goBack();
		  	tearDown();
	}	

	@Override
	public void tearDown() throws Exception {
			solo.finishOpenedActivities();
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
