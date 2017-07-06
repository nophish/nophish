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

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BasePhishURL;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Task gets URLs from the backend server.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class GetUrlsTask extends AsyncTask<Integer, Integer, PhishURL[]>{
	private UrlsLoadedListener controller;
	private PhishAttackType type = PhishAttackType.NoPhish;

	/**
	 * This is the main constructor
	 * @param controller The Controller that will be notified when download is finished.
	 */
	public GetUrlsTask(UrlsLoadedListener controller){
		this.controller=controller;
	}

	protected PhishURL[] doInBackground(Integer... params) {

        if(false){
            int count = params[0];
            this.type = PhishAttackType.NoPhish;
            for(PhishAttackType type : PhishAttackType.values()){
                if(type.getValue() == params[1]){
                    this.type=type;
                    break;
                }
            }
            try {
                String url = "https://api.no-phish.de/urls/"+this.type.toString()+".json";
                HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    BasePhishURL[] result = BackendControllerImpl.deserializeURLs(out.toString());
                    if(result.length > count){
                        result = Arrays.copyOf(result, count);
                    }
                    return result;
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
		return new PhishURL[0];
	}

	protected void onProgressUpdate(Integer... progress) {
		this.controller.urlDownloadProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(PhishURL[] result) {
		this.controller.urlsReturned(result, this.type);
	}
}
