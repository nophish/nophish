package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhisheduUrlReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Uri data = intent.getData();
		if(data.getHost() == "maillink"){
			
		}else{
			
		}
	}

}
