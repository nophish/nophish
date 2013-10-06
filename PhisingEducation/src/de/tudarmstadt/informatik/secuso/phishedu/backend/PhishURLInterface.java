package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * This is the Interface for all Phishing URLs.
 * This is needed because I want to implement the Attacks as decorators.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface PhishURLInterface{
	/**
	 * Return the parts of the URL
	 * @return String parts of a url. When concardinatend they build up the whole url.
	 */
	public String[] getParts();
	/**
	 * Return what type of Site the url is supposed to link to.
	 * @return The linked Site type
	 */
	public PhishSiteType getSiteType();
	/**
	 * What type of attack was applyed to the URL
	 * @return The type of attack was applyed
	 */
	public PhishAttackType getAttackType();
	/**
	 * What should the user click to get the points.
	 * @return indexes of the array returned by {@link #getParts()} that are correct.
	 */
	public int[] getCorrectParts();
	
	/**
	 * Get the points resulting in his selection
	 * @param result the result returned from {@link #getResult(boolean)} 
	 * @return the points the user gets for his selection
	 */
	public int getPoints(PhishResult result);
	
	/**
	 * Get the result if the user selected or did not select the URL
	 * @param acceptance true of the user clicked the checkmark. Otherwise false
	 * @return Result depending on the acceptance
	 */
	public PhishResult getResult(boolean acceptance);
}