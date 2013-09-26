package de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;

import android.os.AsyncTask;

public class SendMailTask extends AsyncTask<String, Void, String>{
		
		private String from;
		private String to;
		private String usermessage;

		public SendMailTask(String from, String to, String usermessage){
			this.from=from;
			this.to=to;
			this.usermessage=usermessage;
		}

		protected String doInBackground(String... params) {
			JSONRPCClient client = JSONRPCClientFactory.getClient();
			try {
				return client.callString("sendMail", from, to, usermessage);
			} catch (JSONRPCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}