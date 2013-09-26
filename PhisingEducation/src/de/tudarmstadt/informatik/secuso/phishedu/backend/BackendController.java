package de.tudarmstadt.informatik.secuso.phishedu.backend;

import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class BackendController extends BroadcastReceiver implements BackendControllerInterface {
	// Create client specifying JSON-RPC version 2.0
	private FrontendControllerInterface frontend;
	private static BackendController instance=new BackendController();
	private boolean inited = false;
	private String[] urlCache;
	
	private BackendController() {
	}
	
	public static BackendControllerInterface getInstance(){
		return instance;
	}
	
	private void checkinited(){
		if(!this.inited){
			throw new IllegalStateException("initialize me first! Call backendcontroller.init()");
		}
	}
	
	public void init(FrontendControllerInterface frontend){
		this.frontend=frontend;
		new GetUrlsTask(this).execute(100);		
	}
	
	public void urlsReturned(String[] urls){
		this.urlCache=urls;
		this.inited=true;
		this.frontend.initDone();
	}
	
	public void urlDownloadProgress(int percent){
		this.frontend.initProgress(percent);
	}
	
	public void sendMail(String from, String to, String usermessage){
		checkinited();
		new SendMailTask(from, to, usermessage).execute();
	}

	@Override
	public void StartLevel1() {
		checkinited();
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://clemens.schuhklassert.de"));
		this.frontend.getContext().startActivity(browserIntent);
	}


	@Override
	public String[] getNextUrl() {
		checkinited();
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getLevel() {
		checkinited();
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getPoints() {
		checkinited();
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public PhishResult userClicked(boolean accptance) {
		checkinited();
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PhishType getType() {
		checkinited();
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean partClicked(int part) {
		checkinited();
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isLevelUp() {
		checkinited();
		// TODO Auto-generated method stub
		return false;
	}
	
	public void receivedURL(Uri uri){
		
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		Uri data = intent.getData();	
		if(data.getHost()=="maillink"){
			this.frontend.MailReturned();
		}else if(data.getHost()=="level1phinished"){
			this.frontend.level1Finished();
		}
	}
	
}
