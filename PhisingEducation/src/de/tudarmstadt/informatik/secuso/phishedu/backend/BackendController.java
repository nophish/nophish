package de.tudarmstadt.informatik.secuso.phishedu.backend;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.SparseArray;

public class BackendController implements BackendControllerInterface{
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
	private FrontendControllerInterface frontend;
	private SparseArray<BackendReturn> returns=new SparseArray<BackendController.BackendReturn>();
	private int current_call_id = 0;
	private Context context;
	
	public BackendController(Context context, FrontendControllerInterface frontend) {
		this.frontend=frontend;
		this.context=context;
	}
	
	
	private void taskcallback(String result, int call_id){
		BackendReturn callback = returns.get(call_id);
		if(callback != null){
			callback.result(result);
		}
		returns.remove(call_id);
	}
	
	private synchronized void dispatch(String method, String... params){
		dispatch(null, method, params);
	}
	
	private synchronized void dispatch(BackendReturn callback, String method, String... params){
		BackendTask task = new BackendTask(current_call_id,method);
		returns.put(current_call_id, callback);
		current_call_id++;
		task.execute(params);
	}
	
	public void sendtoBrowser(Class resultActivity, String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}
	
	public void sendMail(String from, String to, String usermessage){
		dispatch("sendMail", from, to, usermessage);
	}


	@Override
	public void setFrontend(FrontendControllerInterface frontend) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void StartLevel1() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String[] getNextUrl() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getPoints() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public PhishResult userClicked(boolean accptance) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PhishType getType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean partClicked(int part) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isLevelUp() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
