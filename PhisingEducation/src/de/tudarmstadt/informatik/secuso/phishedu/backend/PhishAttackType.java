package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * This enum shows what type of attack {@link BackendControllerInterface#getNextUrl()} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishAttackType {
	/** It has no Attack. It is no phishing website */
	NoPhish(0),
	/** It is a phish but not specified */
	AnyPhish(1),
	/** The Attack for level 2 */
	Level2Attack(2),
	/** Subdomains */
	Sudomains(3),
	/** IP */
	IP(4),
	/** Nonsense Attack */
	Nonsense(5),
	/** unrelated Attack */
	Unrelated(6),
	/** misleading */
	Misleading(7),
	/** homographic */
	Homographic(8),
	/** Hostname in Path */
	HostInPath(9),
	/** https -> http */
	HTTP(10),
	/** Something completly different */
	Generic(Integer.MAX_VALUE);
	
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
