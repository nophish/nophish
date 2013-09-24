package de.tudarmstadt.informatik.secuso.phishedu.backend;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;

import android.os.AsyncTask;
import android.util.SparseArray;

public class BackendController{
	public BackendController(BackendCallback frontend) {
		this.frontend=frontend;
	}
	
	private interface BackendReturn{
		public void result(String result);
	}
	private class BackendTask extends AsyncTask<String, Integer, String>{
		private JSONRPCClient client;
		private String method;
		private int call_id;
		
		public BackendTask(int call_id, String method){
			this.method=method;
			this.call_id=call_id;
		}
		
		@Override
		protected void onPreExecute() {
			this.client = JSONRPCClient.create("http://api.no-phish.de/");
			client.setConnectionTimeout(2000);
			client.setSoTimeout(2000);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				return this.client.callString(this.method);
			} catch (JSONRPCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "ERROR!!";
		}
		
		@Override
		protected void onPostExecute(String result) {
			taskcallback(result, this.call_id);
		}
	}
	// Create client specifying JSON-RPC version 2.0
	private BackendCallback frontend;
	private SparseArray<BackendReturn> returns=new SparseArray<BackendController.BackendReturn>();
	private int current_call_id = 0;
	
	private void taskcallback(String result, int call_id){
		returns.get(call_id).result(result);
		returns.remove(call_id);
	}
	
	private synchronized void dispatch(BackendReturn callback, String method, String... params){
		BackendTask task = new BackendTask(current_call_id,method);
		returns.put(current_call_id, callback);
		current_call_id++;
		task.execute(params);
	}
	
	public void test(String testparam){
		BackendReturn callback = new BackendReturn() {public void result(String result) {
			frontend.test_returned(result);
		}}; 
		dispatch(callback,"testmethod", "testparam");
	}
	
}
