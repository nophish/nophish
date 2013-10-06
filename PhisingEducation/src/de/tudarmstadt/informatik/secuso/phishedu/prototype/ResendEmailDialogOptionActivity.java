package de.tudarmstadt.informatik.secuso.phishedu.prototype;

import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.R.drawable;
import de.tudarmstadt.informatik.secuso.phishedu.R.layout;
import de.tudarmstadt.informatik.secuso.phishedu.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class ResendEmailDialogOptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ResendEmailDialogOptionActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("E-Mail nicht erhalten?");

		// Setting Dialog Message
		alertDialog
				.setMessage("Wenn du möchtest, kannst du die E-Mail erneut versenden lassen. Falls du das nicht möchtest, kannst du mit \"Weiter\" fortfahren.");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.ic_launcher);

		// Setting erhalten Button
		alertDialog.setPositiveButton("E-Mail erneut senden",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

//						// Write your code here to invoke YES event
//						Toast.makeText(getApplicationContext(),
//								"You clicked on E-Mail erhalten", Toast.LENGTH_SHORT)
//								.show();
						
					
					}
					
					
				});

		// Setting nicht erhalten Button
		alertDialog.setNegativeButton("Weiter",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						// TODO: Write your code here to execute after dialog
						// closed
//						Toast.makeText(getApplicationContext(),
//								"You clicked on nicht erhalten",
//								Toast.LENGTH_SHORT).show();
						
					}
					
					
				}

		);

		// Showing Alert Message
		alertDialog.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resend_email_dialog_option, menu);
		return true;
	}

	
}
