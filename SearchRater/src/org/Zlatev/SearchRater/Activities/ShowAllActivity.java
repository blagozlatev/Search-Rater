package org.Zlatev.SearchRater.Activities;

import java.util.ArrayList;

import org.Zlatev.SearchRater.R;
import org.Zlatev.SearchRater.Classes.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowAllActivity extends Activity {

	static ArrayList<Bundle> bundledNamesAndLinks = new ArrayList<Bundle>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_all);
		final DatabaseHandler db = new DatabaseHandler(this);
		final ListView blockedList = (ListView) findViewById(R.id.blockedList);

		if (db.isEmpty()) {
			blockedList.setAdapter(null);
			dialogCreator(getString(R.string.error),
					getString(R.string.no_blocked), true);
		} else {
			bundledNamesAndLinks = db.getAllLinks();
			listSetter(blockedList, true);
		}

		blockedList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!blockedList.isItemChecked(position)) {
					blockedList.setItemChecked(position, true);
				} else {
					blockedList.setItemChecked(position, false);
				}
			}
		});

		blockedList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String linkSplit[] = getLinkFromBundle(position).split("/", 4);
				String linkToUse = linkSplit[0] + "//" + linkSplit[2];
				if (blockedList.isItemChecked(position)) {
					setListForSingleLink(blockedList, linkToUse, false);
					db.deleteLink(linkToUse);
				} else {
					setListForSingleLink(blockedList, linkToUse, true);
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

	private void dialogCreator(final String title, final String message,
			final boolean finalize) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (finalize) {
							finish();
						}
						return;
					}
				});
		builder.create().show();
	}

	protected String getTitleFromBundle(final int position) {
		return bundledNamesAndLinks.get(position).getString("title");
	}

	protected String getLinkFromBundle(final int position) {
		return bundledNamesAndLinks.get(position).getString("link");
	}
}
