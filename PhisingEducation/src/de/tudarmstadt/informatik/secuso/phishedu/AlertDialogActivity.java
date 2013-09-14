package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class AlertDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_email);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AlertDialogActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("E-Mail versandt");

		// Setting Dialog Message
		alertDialog
				.setMessage("Jetzt rufe bitte deine E-Mails ab und kehre anschließend zurück in die App.");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.ic_launcher);

		// Setting erhalten Button
		alertDialog.setPositiveButton("E-Mail erhalten",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

//						// Write your code here to invoke YES event
//						Toast.makeText(getApplicationContext(),
//								"You clicked on E-Mail erhalten", Toast.LENGTH_SHORT)
//								.show();
						
						Intent intent = new Intent(AlertDialogActivity.this, FinalAwarenessActivity.class);
						startActivity(intent);
					}
					
					
				});

		// Setting nicht erhalten Button
		alertDialog.setNegativeButton("E-Mail nicht erhalten",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						// TODO: Write your code here to execute after dialog
						// closed
//						Toast.makeText(getApplicationContext(),
//								"You clicked on nicht erhalten",
//								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(AlertDialogActivity.this, ResendEmailDialogOptionActivity.class);
						startActivity(intent);
					}
					
					
				}

		);

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert_dialog, menu);
		return true;
	}

}
