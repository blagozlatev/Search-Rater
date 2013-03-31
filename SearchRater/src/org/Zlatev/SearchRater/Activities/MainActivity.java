package org.Zlatev.SearchRater.Activities;

import org.Zlatev.SearchRater.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button btnSearchActivity = (Button) findViewById(R.id.btnSearchActivity);
		Button btnShowAllActivity = (Button) findViewById(R.id.btnShowAllActivity);

		btnSearchActivity.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent SearchActivity = new Intent(MainActivity.this,
						SearchActivity.class);
				startActivity(SearchActivity);
			}
		});

		btnShowAllActivity.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent ShowAllActivity = new Intent(MainActivity.this,
						ShowAllActivity.class);
				startActivity(ShowAllActivity);
			}
		});
	}
}