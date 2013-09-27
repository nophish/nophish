package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.app.Activity;
import android.content.Context;

public interface FrontendControllerInterface {
	void MailReturned();
	void level1Finished();
	void initDone();
	void initProgress(int percent);
	Activity getMasterActivity();
	Context getContext();
}
