package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.content.Context;

public interface FrontendControllerInterface {
	void MailReturned();
	void level1Finished();
	void initDone();
	void initProgress(int percent);
	Context getContext();
}
