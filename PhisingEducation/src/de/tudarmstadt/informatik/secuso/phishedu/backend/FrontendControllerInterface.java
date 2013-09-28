package de.tudarmstadt.informatik.secuso.phishedu.backend;

import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.content.Context;

public interface FrontendControllerInterface {
	void MailReturned();
	void level1Finished();
	void initDone();
	void initProgress(int percent);
	BaseGameActivity getMasterActivity();
	GamesClient getGamesClient();
	Context getContext();
	void onLevelChange(int level);
}
