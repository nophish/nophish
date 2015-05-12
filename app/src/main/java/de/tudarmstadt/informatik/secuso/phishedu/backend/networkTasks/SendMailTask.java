package de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;

import android.os.AsyncTask;

/**
 * This Task sends out a mail via the backend server.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class SendMailTask extends AsyncTask<String, Void, String>{
		
		private String from;
		private String to;
		private String usermessage;
        private SendMailListener controller;

		/**
		 * This is the main constructor
		 * @param from The sender of the mail.
		 * @param to The receiver of the mail.
		 * @param usermessage This custom message will be inserted in the mail.
		 */
		public SendMailTask(String from, String to, String usermessage, SendMailListener controller){
			this.from=from;
			this.to=to;
			this.usermessage=usermessage;
            this.controller=controller;
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

        @Override
        protected void onPostExecute(String result) {
            this.controller.returnedResult(result);
        }

        public interface SendMailListener{
            public void returnedResult(String result);
        }
}