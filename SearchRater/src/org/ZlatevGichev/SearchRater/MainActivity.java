package org.ZlatevGichev.SearchRater;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	static ArrayList<Bundle> bundledNamesAndLinks = new ArrayList<Bundle>();
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dialogCreator(getString(R.string.intsructions_title),
				getString(R.string.instructions_message));
		final DatabaseHandler db = new DatabaseHandler(this);
		final EditText editText = (EditText) findViewById(R.id.editText);
		final ListView list = (ListView) findViewById(R.id.resultList);
		final Button btnSearch = (Button) findViewById(R.id.btnSearch);
		final Button btnShowAllBlocked = (Button) findViewById(R.id.btnShowAllBlocked);

		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isConnectionAvailable()) {
					btnSearch.setClickable(false);
					progressDialogCreator(4000,
							getString(R.string.get_results), btnSearch);
					if (!editText.getText().equals("")) {
						bundledNamesAndLinks = JSONHandler
								.getResultsFromJSON(editText.getText()
										.toString());
						if (bundledNamesAndLinks.isEmpty()) {
							dialogCreator(getString(R.string.error),
									getString(R.string.retrieve_error));
						} else {
							listSetter(list, false);
							getAllBlockedLinksAndSetList(db, list, true);
						}
					}
				} else {
					dialogCreator(getString(R.string.error),
							getString(R.string.connection_error));
				}
			}
		});

		btnShowAllBlocked.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				if (db.isEmpty()) {
					list.setAdapter(null);
					dialogCreator(getString(R.string.error),
							getString(R.string.no_blocked));					
				} else {
					bundledNamesAndLinks = db.getAllLinks();
					progressDialogCreator(500, getString(R.string.get_blocked),
							btnShowAllBlocked);
					listSetter(list, true);
				}
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!list.isItemChecked(position)) {
					list.setItemChecked(position, true);
				} else {
					if (isConnectionAvailable()) {
						list.setItemChecked(position, false);
						Intent i = new Intent(
								android.content.Intent.ACTION_VIEW, Uri
										.parse(getLinkFromBundle(position)));
						startActivity(i);
					} else {
						dialogCreator(getString(R.string.error),
								getString(R.string.connection_error));
					}
				}
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String linkSplit[] = getLinkFromBundle(position).split("/", 4);
				String linkToUse = linkSplit[0] + "//" + linkSplit[2];
				if (list.isItemChecked(position)) {
					setListForSingleLink(list, linkToUse, false);
					db.deleteLink(linkToUse);
				} else {
					setListForSingleLink(list, linkToUse, true);
					db.addLink(linkToUse);
				}
				return true;
			}
		});

	}

	private void setListForSingleLink(final ListView list,
			final String linkToUse, boolean block) {
		for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
			if (getLinkFromBundle(i).startsWith(linkToUse)) {
				list.setItemChecked(i, block);
			}
		}
	}

	private void dialogCreator(final String title, final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		builder.create().show();
	}

	private void setListForAllLinks(ListView list,
			final ArrayList<String> blockedLinks, final boolean block) {
		for (int i = 0; i < blockedLinks.size(); i++) {
			for (int k = 0; k < bundledNamesAndLinks.size(); k++) {
				if (getLinkFromBundle(k).startsWith(blockedLinks.get(i))) {
					list.setItemChecked(k, block);
				}
			}
		}
	}

	protected String getTitleFromBundle(final int position) {
		return bundledNamesAndLinks.get(position).getString("title");
	}

	protected String getLinkFromBundle(final int position) {
		return bundledNamesAndLinks.get(position).getString("link");
	}

	private void listSetter(ListView list, final boolean all) {
		ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
			titles.add(getTitleFromBundle(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, titles);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setAdapter(adapter);
		if (all) {
			for (int i = 0; i < list.getCount(); i++) {
				list.setItemChecked(i, true);
			}
		}
	}

	private void getAllBlockedLinksAndSetList(final DatabaseHandler db,
			final ListView list, final boolean block) {
		ArrayList<Bundle> allBlocked = db.getAllLinks();
		ArrayList<String> blockedLinks = new ArrayList<String>();
		for (int i = 0; i < allBlocked.size(); i++) {
			blockedLinks.add(allBlocked.get(i).getString("link"));
		}

		if (!blockedLinks.isEmpty()) {
			setListForAllLinks(list, blockedLinks, block);
		}
	}

	private void progressDialogCreator(final int milliseconds,
			final String message, final Button button) {
		progressDialog = ProgressDialog.show(MainActivity.this, "", message);
		new Thread() {
			public void run() {
				try {
					sleep(milliseconds);
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();
				button.setClickable(true);
			}
		}.start();
	}

	private boolean isConnectionAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInformation = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInformation != null;
	}
}