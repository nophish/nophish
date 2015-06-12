/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2.backend.networkTasks;

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