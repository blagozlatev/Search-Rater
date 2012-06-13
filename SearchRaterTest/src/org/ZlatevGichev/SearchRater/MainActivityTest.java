

package org.ZlatevGichev.SearchRater;


import junit.framework.Assert;

import org.ZlatevGichev.SearchRater.MainActivity;

import com.jayway.android.robotium.solo.Solo;


import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;


public class MainActivityTest extends
				ActivityInstrumentationTestCase2<MainActivity> {

			private MainActivity mainActivity;
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

			public void testBlockedAndUnblockedResults() throws Exception {
				setUp();
				
				solo.clickOnText("OK");
				solo.clickOnText("Show all blocked results");
				
				Assert.assertTrue(solo.searchText("There are no blocked links"));
				
				solo.clickOnText("OK");
				solo.enterText(0, "Alfa Romeo");
				solo.clickOnButton("Search");
				solo.waitForText("Getting results");

				solo.clickLongInList(1); //first result is blocked
				solo.clickLongInList(2); //second result is blocked
				
				Assert.assertTrue(solo.searchText("Alfa Romeo Australia")); 
				
				solo.clickOnButton("Show all blocked results");
				
				Assert.assertTrue(solo.searchText("en.wikipedia.org"));
				
				solo.clickOnButton("Search");
				solo.clickInList(2);
				solo.clickLongInList(1); // first result is unblocked
				solo.clickLongInList(2); // second result is unblocked
				solo.clickInList(2);
				
				Assert.assertFalse(solo.searchText("Alfa Romeo Australia"));
				
				solo.goBack();
				tearDown();
			}

			@Override
			public void tearDown() throws Exception {
				solo.finishOpenedActivities();
			}

			public void testGetTitleForLink() throws Exception {
				setUp();
				testBundle.putString("title", "Alfa Romeo");
				MainActivity.bundledNamesAndLinks.add(0, testBundle);
				String testGetTitle = mainActivity.getTitleFromBundle(0);
				assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("title"), testGetTitle);
			}


			public void testGetLinkFromBundle() throws Exception {
				setUp();
				testBundle.putString("link", "www.google.com");
				MainActivity.bundledNamesAndLinks.add(0, testBundle);
				String testGetLink = mainActivity.getLinkFromBundle(0);
				assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("link"), testGetLink);
			} 

			/*public void testSetListForSingleLink () throws Exception {
				setUp();
				testBundle.putString("link", "www.abv.bg");
				MainActivity.bundledNamesAndLinks.add(0, testBundle);
				
				mainActivity.setListForSingleLink(list, "www.abv.bg", true);
			}
			/*public void testGetSearchQueryForBundle() throws Exception {
				setUp();
				testBundle.putString("search_query", "Alfa");
				MainActivity.bundledNamesAndLinks.add(0, testBundle);
				String testGetSearchQuery = mainActivity.getSearchQueryForBundle();
				assertEquals(MainActivity.bundledNamesAndLinks.get(0).getString("search_query"), testGetSearchQuery);
			}*/
}