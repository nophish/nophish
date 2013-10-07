package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
public class AwarenessActivity extends FragmentActivity {

	static final int ITEMS = 3;
	MyAdapter mAdapter;
	ViewPager mPager;
	EditText mEditSender;
	EditText mEditReceiver;
	EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_layout);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show image
				return AwarenessEmailSpoofigImageOneFragment.init(position);
			case 1: // Fragment # 1 - This will show image
				return AwarenessEmailSpoofigImageTwoFragment.init(position);
			default:// Fragment # 3 - Will show image with button
				return AwarenessEmailSpoofingImageEndFragment.init(position);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void goToYouDontBelieveUs(View view) {
		setContentView(R.layout.awareness_you_dont_believe_us);
	}

	public void goToEmailForm(View view) {
		setContentView(R.layout.awareness_send_email_form);
		ScrollView sv = (ScrollView) findViewById(R.id.awareness_scrollview_send_email_form);
		sv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				hideKeyboard(view);
				return false;
			}
		});
	}

	/**
	 * 
	 * @param view
	 */
	public void sendEmail(View view) {

		//if keyboard is not hidden, it should now be hidden.
		hideKeyboard(view);
		
		// get User Input
		mEditSender = (EditText) findViewById(R.id.awareness_edit_sender_email);
		mEditReceiver = (EditText) findViewById(R.id.awareness_edit_receiver_email);
		mEditText = (EditText) findViewById(R.id.awareness_edit_text);

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
				// BackendController.getInstance().sendMail(from, to,
				// userMessage);

				// Pop up with go to E-Mail on your Smartphone

				showAlertDialog();

			}
		}

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

		// Setting erhalten Button
		alertDialog.setPositiveButton(
				getString(R.string.alert_btn_proceed_no_awareness),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						setContentView(R.layout.awareness_no_awareness);

						// sollte auch hier awareness als erledigt markiert
						// werden?

					}

				});

		// Setting nicht erhalten Button
		alertDialog.setNegativeButton(
				getString(R.string.awareness_resend_email),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						setContentView(R.layout.awareness_send_email_form);

					}

				}

		);

		// Showing Alert Message
		alertDialog.show();
	}

	
	
	public void goToGameIntro(View view){
		Intent gameIntroIntent = new Intent(this, IntroGameActivity.class);
		gameIntroIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(gameIntroIntent);
	}
	
	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
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

	protected void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
