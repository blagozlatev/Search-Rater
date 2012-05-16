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
import android.widget.Toast;

public class MainActivity extends Activity {

	static ArrayList<Bundle> bundledNamesAndLinks = new ArrayList<Bundle>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		instructionDialogCreator();

		final DatabaseHandler db = new DatabaseHandler(this);
		final EditText editText = (EditText) findViewById(R.id.editText);
		final ListView list = (ListView) findViewById(R.id.resultList);
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		Button btnShowAllBlocked = (Button) findViewById(R.id.btnShowAllBlocked);

		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!editText.getText().equals("")) {
					bundledNamesAndLinks = JSONHandler
							.getResultsFromJSON(editText.getText().toString());
					if (bundledNamesAndLinks.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								R.string.retrieve_error, Toast.LENGTH_LONG)
								.show();
					} else {
						listSetter(list);
						ArrayList<String> blockedLinks = db
								.getAllLinksForQuery(editText.getText()
										.toString());
						if (!blockedLinks.isEmpty()) {
							findMatchesAndSetListView(list, blockedLinks);
						}
					}
				}
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
				if (list.isItemChecked(position)) {
					list.setItemChecked(position, false);
					db.deleteLink(getLinkFromBundle(position));
				} else {
					list.setItemChecked(position, true);
					db.addLink(getSearchQueryForBundle(),
							getLinkFromBundle(position));
				}
				return true;
			}
		});

	}

	private void instructionDialogCreator() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title);
		builder.setMessage(R.string.dialog_message);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		builder.create().show();
	}

	private void findMatchesAndSetListView(ListView list,
			ArrayList<String> blockedLinks) {
		for (int i = 0; i < blockedLinks.size(); i++) {
			for (int k = 0; k < bundledNamesAndLinks.size(); k++) {
				if (blockedLinks.get(i).equals(getLinkFromBundle(k))) {
					list.setItemChecked(k, true);
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

	protected String getSearchQueryForBundle() {
		return bundledNamesAndLinks.get(0).getString("search_query");
	}

	private void listSetter(ListView list) {
		ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
			titles.add(getTitleForLink(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, titles);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setAdapter(adapter);
	}
}