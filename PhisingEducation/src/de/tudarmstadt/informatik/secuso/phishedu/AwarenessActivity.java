package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnShowListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class AwarenessActivity extends PhishBaseActivity {

	private static String from;
	private static String to;
	private static String userMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getActivity().getApplicationContext()).getAccounts();
		ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line);
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				toAdapter.add(account.name);
			}
		}
		final String[] fromMails = getResources().getStringArray(R.array.fromMails);
		ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,fromMails);
		AutoCompleteTextView fromView = (AutoCompleteTextView) v.findViewById(R.id.awareness_edit_sender_email);
		fromView.setAdapter(fromAdapter);
		fromView.setThreshold(1);
		PopupListener listener = new PopupListener();
		fromView.setOnFocusChangeListener(listener);
		fromView.setOnClickListener(listener);
		AutoCompleteTextView toView = (AutoCompleteTextView) v.findViewById(R.id.awareness_edit_receiver_email);
		toView.setAdapter(toAdapter);
		toView.setThreshold(1);
		toView.setOnFocusChangeListener(listener);
		toView.setOnClickListener(listener);
		return v;
	}
	
	private class PopupListener implements View.OnFocusChangeListener, View.OnClickListener{
		@Override
		public void onClick(View v) {
			((AutoCompleteTextView) v).showDropDown();
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				((AutoCompleteTextView) v).showDropDown();
			}
		}
		
	}
	
	
	public void skipSendEmail(View view){
		BackendControllerImpl.getInstance().skipLevel0();
	}

	/**
	 * 
	 * @param view
	 */
	public void sendEmail(View view) {

		// if keyboard is not hidden, it should now be hidden.
		hideKeyboard(view);

		// get User Input
		EditText mEditSender = (EditText) getView().findViewById(R.id.awareness_edit_sender_email);
		EditText mEditReceiver = (EditText) getView().findViewById(R.id.awareness_edit_receiver_email);
		EditText mEditText = (EditText) getView().findViewById(R.id.awareness_edit_text);

		from = mEditSender.getText().toString();
		to = mEditReceiver.getText().toString();
		userMessage = mEditText.getText().toString();

		String toastMsg;

		// check if all is there (at least sender and receiver)
		if (from.trim().equals("") || to.trim().equals("")) {
			toastMsg = getString(R.string.awareness_missing_email_sender_or_receiver);
			displayToast(toastMsg);
		} else {
			// check whether email format is valid
			if (!(isValidEmailAddress(from))||!(isValidEmailAddress(to)) ) {
				toastMsg = getString(R.string.awareness_invalid_email);
				displayToast(toastMsg);
			} else {
				// Input is OK send email
				// invoke Backendcontroller
				/*
				 * TODO:
				 */
				BackendControllerImpl.getInstance().sendMail(from, to, userMessage);

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
		Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG)
		.show();
	}

	private void showAlertDialog() {
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		dialogbuilder.setTitle(getString(R.string.awareness_email_sent));

		// Setting Dialog Message
		dialogbuilder.setMessage(getString(R.string.awareness_alert_message));

		// Setting Icon to Dialog
		dialogbuilder.setIcon(R.drawable.e_mail);

		// button for resend
		dialogbuilder.setNeutralButton(R.string.awareness_resend_email,

				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showSecondAlertDialog();
			}

		});
		
		AlertDialog dialog = dialogbuilder.create();
		
		Alertenabler enabler = new Alertenabler();
		dialog.setOnShowListener(enabler);
		// Showing Alert Message
		dialog.show();
		enabler.execute();
	}

	private void showSecondAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.awareness_email_sent));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.awareness_second_alert_message));

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.e_mail);

		// button for resend
		alertDialog.setNeutralButton(R.string.awareness_resend_email,

				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText mEditSender = (EditText) getView().findViewById(R.id.awareness_edit_sender_email);
				EditText mEditReceiver = (EditText) getView().findViewById(R.id.awareness_edit_receiver_email);
				EditText mEditText = (EditText) getView().findViewById(R.id.awareness_edit_text);

				mEditSender.setText(AwarenessActivity.from);
				mEditReceiver.setText(AwarenessActivity.to);
				mEditText.setText(AwarenessActivity.userMessage);

				dialog.dismiss();
			}

		});
		// Showing Alert Message
		alertDialog.show();

	}

	protected void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	class Alertenabler extends AsyncTask<Integer, Integer, Integer> implements OnShowListener{
		private Button button=null;
		private static final int TIMEOUT=5;
		private String basetext;
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			button.setText(basetext+" ("+values[0].toString()+")");
		}
		
		protected void onPostExecute(Integer result) {
			button.setText(basetext);
			button.setEnabled(true);
		};
		
		@Override
		protected Integer doInBackground(Integer... params) {
			button.setEnabled(false);
			//wait a little until the dialog is shown.
			for (int i = 5; i > 0 && this.button == null ; i--) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i = TIMEOUT; i > 0 ; i--) {
				publishProgress(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return 0;
		}

		@Override
		public void onShow(DialogInterface dialog) {
			AlertDialog adialog=(AlertDialog) dialog;
			this.button=adialog.getButton(AlertDialog.BUTTON_NEUTRAL);
			this.basetext=this.button.getText().toString();
			this.button.setEnabled(false);
		}
	}


	@Override
	public int getLayout() {
		return R.layout.awareness;
	}
	
	@Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.button_abschicken:
        	sendEmail(view);
        }
    }

	@Override
	public int[] getClickables() {
		return new int[] {
				R.id.button_abschicken
		};
	}

}
