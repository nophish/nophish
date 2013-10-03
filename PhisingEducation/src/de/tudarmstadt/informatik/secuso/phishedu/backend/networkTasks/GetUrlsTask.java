package de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishType;

import android.os.AsyncTask;

/**
 * This Task gets URLs from the backend server.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class GetUrlsTask extends AsyncTask<Integer, Integer, String[]>{
	private UrlsLoadedListener controller;
	/** Static for getting Phish urls */
	public static final int PHISH_URLS = 0;
	/** Static for getting no-phish urls */
	public static final int VALID_URLS = 1;
	
	private PhishType type = PhishType.NoPhish;
	
	/**
	 * This is the main constructor
	 * @param controller The Controller that will be notified when download is finished.
	 */
	public GetUrlsTask(UrlsLoadedListener controller){
		this.controller=controller;
	}

	protected String[] doInBackground(Integer... params) {
		JSONRPCClient client = JSONRPCClientFactory.getClient();
		int count = params[0];
		this.type = PhishType.NoPhish;
		for(PhishType type : PhishType.values()){
			if(type.getValue() == params[1]){
				this.type=type;
				break;
			}
		}
		try {
			JSONArray result = client.callJSONArray("getURLs", count, type);
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
		return new String[0];
	}

	protected void onProgressUpdate(Integer... progress) {
	     this.controller.urlDownloadProgress(progress[0]);
	 }
	
	@Override
	protected void onPostExecute(String[] result) {
		this.controller.urlsReturned(result, this.type);
	}
}
