package de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;

import android.os.AsyncTask;

public class SendMailTask extends AsyncTask<String, Void, Boolean>{
		
		private String from;
		private String to;
		private String usermessage;

		public SendMailTask(String from, String to, String usermessage){
			this.from=from;
			this.to=to;
			this.usermessage=usermessage;
		}

		protected Boolean doInBackground(String... params) {
			JSONRPCClient client = JSONRPCClientFactory.getClient();
			try {
				return client.callBoolean("sendMail", from, to, usermessage);
			} catch (JSONRPCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}