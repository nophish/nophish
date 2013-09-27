package de.tudarmstadt.informatik.secuso.phishedu.backend;

public enum PhishResult {
	//The user correctly detected and identified a phish
	Phish_Detected(0),
	//The user Detected that it was no phish
	NoPhish_Detected(1),
	//The user could not identify the phish
	Phish_NotDetected(2),
	//The user identified a no-phish URL as phishy
	NoPhish_NotDetected(3);

	private int value;

	private PhishResult(int value) {
		this.value = value;
	}
	
	public int getValue() {
        return value;
    }
}
