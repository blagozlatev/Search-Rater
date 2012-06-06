package org.ZlatevGichev.SearchRater;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dialogCreator(R.string.intsructions_title,
				R.string.instructions_message);

		final DatabaseHandler db = new DatabaseHandler(this);
		final EditText editText = (EditText) findViewById(R.id.editText);
		final ListView list = (ListView) findViewById(R.id.resultList);
		final Button btnSearch = (Button) findViewById(R.id.btnSearch);
		final Button btnShowAllBlocked = (Button) findViewById(R.id.btnShowAllBlocked);

		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				btnSearch.setCursorVisible(false);
				btnSearch.setClickable(false);
				bundledNamesAndLinks.clear();
				if (!editText.getText().equals("")) {
					bundledNamesAndLinks = JSONHandler
							.getResultsFromJSON(editText.getText().toString());
					if (bundledNamesAndLinks.isEmpty()) {
						dialogCreator(R.string.error, R.string.retrieve_error);
					} else {
						listSetter(list, false);
						getAllBlockedLinksAndSetList(db, list, true);
					}
				}
				btnSearch.setClickable(true);
				btnSearch.setCursorVisible(true);
			}
		});

		btnShowAllBlocked.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				btnShowAllBlocked.setCursorVisible(false);
				btnShowAllBlocked.setClickable(false);
				bundledNamesAndLinks.clear();
				bundledNamesAndLinks = db.getAllLinks();
				if (bundledNamesAndLinks.isEmpty()) {
					dialogCreator(R.string.error, R.string.no_blocked);
				} else {
					listSetter(list, true);
				}
				btnShowAllBlocked.setCursorVisible(true);
				btnShowAllBlocked.setClickable(true);
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!list.isItemChecked(position)) {
					list.setItemChecked(position, true);
				} else {
					list.setItemChecked(position, false);
					Intent i = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse(getLinkFromBundle(position)));
					startActivity(i);
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

			private void setListForSingleLink(final ListView list,
					String linkToUse, boolean block) {
				for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
					if (getLinkFromBundle(i).startsWith(linkToUse)) {
						list.setItemChecked(i, block);
					}
				}
			}
		});

	}

	private void dialogCreator(int title, int message) {
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

	private void findMatchesAndSetListView(ListView list,
			ArrayList<String> blockedLinks, boolean block) {
		for (int i = 0; i < blockedLinks.size(); i++) {
			for (int k = 0; k < bundledNamesAndLinks.size(); k++) {
				if (getLinkFromBundle(k).startsWith(blockedLinks.get(i))) {
					list.setItemChecked(k, block);
				}
			}
		}
	}

	protected String getTitleForLink(int position) {
		return bundledNamesAndLinks.get(position).getString("title");
	}

	protected String getLinkFromBundle(int position) {
		return bundledNamesAndLinks.get(position).getString("link");
	}

	private void listSetter(ListView list, boolean all) {
		ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
			titles.add(getTitleForLink(i));
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
			final ListView list, boolean block) {
		ArrayList<Bundle> allBlocked = db.getAllLinks();
		ArrayList<String> blockedLinks = new ArrayList<String>();
		for (int i = 0; i < allBlocked.size(); i++) {
			blockedLinks.add(allBlocked.get(i).getString("link"));
		}

		if (!blockedLinks.isEmpty()) {
			findMatchesAndSetListView(list, blockedLinks, block);
		}
	}
}