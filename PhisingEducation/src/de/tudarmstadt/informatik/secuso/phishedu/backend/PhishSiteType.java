package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * This enum shows what type of phish the last {@link BackendControllerInterface#getNextUrl()} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishSiteType {
	/**
	 * Online social networks
	 */
	OSN(3),
	/**
	 * Finanace
	 */
	Bank(4),
	//...
	/**
	 * It is a phish but not specified
	 */
	AnyPhish(1),
	/**
	 * Something completly different
	 */
	Generic(2);
	
	private int value;

	private PhishSiteType(int value) {
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
