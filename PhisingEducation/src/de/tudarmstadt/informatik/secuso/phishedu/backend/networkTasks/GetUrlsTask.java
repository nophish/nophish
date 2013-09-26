package de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

import android.os.AsyncTask;

public class GetUrlsTask extends AsyncTask<Integer, Integer, String[]>{
	private BackendController controller;
	
	public GetUrlsTask(BackendController controller){
		this.controller=controller;
	}

	protected String[] doInBackground(Integer... params) {
		JSONRPCClient client = JSONRPCClientFactory.getClient();
		int count = params[0];
		try {
			JSONArray result = client.callJSONArray("getURLs", count);
			String[] urls = new String[result.length()];
			for(int i=0; i<result.length(); i++){
				urls[i]=result.getString(i);
				publishProgress((int) ((i / (float) count) * 100));
			}
			return urls;
		} catch (JSONRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
	     this.controller.urlDownloadProgress(progress[0]);
	 }
	
	@Override
	protected void onPostExecute(String[] result) {
		this.controller.urlsReturned(result);
	}
}
