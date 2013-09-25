package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.content.Context;

public interface FrontendControllerInterface {
	void MailReturned();
	void level1Finished();
	void initDone();
	Context getContext();
}
