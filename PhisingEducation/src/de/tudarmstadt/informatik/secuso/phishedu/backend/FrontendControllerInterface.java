package de.tudarmstadt.informatik.secuso.phishedu.backend;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;

import android.content.Context;
import android.net.Uri;

public interface FrontendControllerInterface {
	//Context functions
	GamesClient getGamesClient();
	AppStateClient getAppStateClient();
	Context getContext();

	//Callback functions
	void initProgress(int percent);
	void initDone();
	void MailReturned();
	void level1Finished();
	void onLevelChange(int level);
	void startBrowser(Uri url);
}
