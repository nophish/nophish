package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * This enum shows what type of phish the last {@link BackendControllerInterface#getNextUrl()} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishType {
	/**
	 * Online social networks
	 */
	OSN,
	/**
	 * Finanace
	 */
	Bank,
	//...
	/**
	 * It was no phish
	 */
	NoPhish,
	/**
	 * Something completly different
	 */
	Generic
}
