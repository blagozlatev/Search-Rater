package org.ZlatevGichev.SearchRater;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		final EditText editText = (EditText) findViewById(R.id.editText);

		ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearchImg);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (!editText.getText().equals("")) {
					Intent i = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse("http://www.google.com/#&q="
									+ editText.getText()));
					startActivity(i);
				}
			}
		});
	}
}