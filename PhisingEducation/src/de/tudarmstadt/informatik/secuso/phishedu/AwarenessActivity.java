package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class AwarenessActivity extends SwipeActivity {

	protected static final int[] levelLayoutIds = {
		R.layout.level_00_intro_02,
	};
	
	@Override
	protected int getPageCount() {
		return levelLayoutIds.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(levelLayoutIds[page],	container, false);
	}

	public void skipSendEmail(View view){
		Intent levelIntent = new Intent(this, LevelFinishedActivity.class);
		levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, 0);
		startActivity(levelIntent);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void sendEmail(View view) {

		// if keyboard is not hidden, it should now be hidden.
		hideKeyboard(view);

		// get User Input
		EditText mEditSender = (EditText) findViewById(R.id.awareness_edit_sender_email);
		EditText mEditReceiver = (EditText) findViewById(R.id.awareness_edit_receiver_email);
		EditText mEditText = (EditText) findViewById(R.id.awareness_edit_text);

		String from = mEditSender.getText().toString();
		String to = mEditReceiver.getText().toString();
		String userMessage = mEditText.getText().toString();

		String message;

		// check if all is there (at least sender and receiver)
		if (from.trim().equals("") || to.trim().equals("")) {
			message = getString(R.string.awareness_missing_email_sender_or_receiver);
			displayToast(message);
		} else {
			// check whether email format is valid
			if (!isValidEmailAddress(from)) {
				message = getString(R.string.awareness_invalid_email);
				displayToast(message);
			} else {
				// Input is OK send email
				// invoke Backendcontroller
				/*
				 * TODO:
				 */
				BackendController.getInstance().sendMail(from, to, userMessage);

				// Pop up with go to E-Mail on your Smartphone

				showAlertDialog();

			}
		}

	}


	public static boolean isValidEmailAddress(String email) {
		boolean stricterFilter = true;
		String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
		String emailRegex = stricterFilter ? stricterFilterString : laxString;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	private void showAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AwarenessActivity.this);
	
		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.awareness_email_sent));
	
		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.awareness_alert_message));
	
		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.e_mail);
	
		// button for resend
		alertDialog.setNeutralButton(R.string.awareness_resend_email,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
	
						setContentView(R.layout.level_00_intro_02);
					}
	
				});
		// Showing Alert Message
		alertDialog.show();
	
	}

	protected void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
}
