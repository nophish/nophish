package de.tudarmstadt.informatik.secuso.phishedu.backend;

public interface BackendControllerInterface {
	void setFrontend(FrontendControllerInterface frontend);
	void sendMail(String Subject, String from, String to);
	void StartLevel1();
	String[] getNextUrl();
	void init();
	int getLevel();
	int getPoints();
	PhishResult userClicked(boolean accptance);
	PhishType getType();
	boolean partClicked(int part);
	boolean isLevelUp();
	
}
