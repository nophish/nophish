package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * This enum shows what type of attack {@link BackendControllerInterface#getNextUrl()} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishAttackType {
	/**
	 * IP
	 */
	IP(3),
	/**
	 * Subdomains
	 */
	Sudomains(4),
	/**
	 * The Attack for level 2 
	 */
	Level2Attack(5),
	/**
	 * It has no Attack. It is no phishing website
	 */
	NoPhish(0),
	/**
	 * It is a phish but not specified
	 */
	AnyPhish(1),
	/**
	 * Something completly different
	 */
	Generic(2);
	
	private int value;

	private PhishAttackType(int value) {
		this.value = value;
	}
	
	/**
	 * Get the integer value of the current result.
	 * This is imported when using this enum for indexing arrays.
	 * @return the integer Value of the current result
	 */
	public int getValue() {
        return value;
    }
}
