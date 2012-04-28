package org.ZlatevGichev.SearchRater;

import java.util.ArrayList;

import android.app.Activity;
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
	private static final String TITLE = "title";
	private static final String LINK = "link";
	private static final String SEARCH_QUERY = "search_query";
	DatabaseHandler db = new DatabaseHandler(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText editText = (EditText) findViewById(R.id.editText);
		final ListView list = (ListView) findViewById(R.id.resultList);

		Button btnSearch = (Button) findViewById(R.id.btnSearch);
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
						ArrayList<String> titles = new ArrayList<String>();
						for (int i = 0; i < bundledNamesAndLinks.size(); i++) {
							titles.add(bundledNamesAndLinks.get(i).getString(
									TITLE));
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								getBaseContext(),
								android.R.layout.simple_list_item_single_choice,
								titles);
						list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						list.setAdapter(adapter);
						ArrayList<String> blockedLinks = db.getAllContactsForQuery(editText.getText().toString());
						if (!blockedLinks.isEmpty()) {
							for (int i=0; i<blockedLinks.size();i++){
								for (int k=0; k<bundledNamesAndLinks.size();k++){
									if (blockedLinks.get(i).equals(bundledNamesAndLinks.get(k).getString(LINK))){
										list.setItemChecked(k, true);
									}
								}
							}
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
							Uri.parse(bundledNamesAndLinks.get(position)
									.getString(LINK)));
					startActivity(i);
				}
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.isItemChecked(position)) {
					list.setItemChecked(position, false);
					db.deleteLink(bundledNamesAndLinks.get(position).getString(
							LINK));
				} else {
					list.setItemChecked(position, true);
					db.addLink(
							bundledNamesAndLinks.get(position).getString(
									SEARCH_QUERY),
							bundledNamesAndLinks.get(position).getString(LINK));
				}
				return true;
			}
		});

	}
}