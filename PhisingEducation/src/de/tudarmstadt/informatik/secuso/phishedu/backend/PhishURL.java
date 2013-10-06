package de.tudarmstadt.informatik.secuso.phishedu.backend;

/**
 * this is for internally holding the phishing urls
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class PhishURL implements PhishURLInterface{
	private String[] parts = new String[0];
	private PhishSiteType siteType = PhishSiteType.AnyPhish;
	private PhishAttackType attackType = PhishAttackType.NoPhish;
	private int[] correctparts = new int[0];
	/**
	 * This stores the points the user gets for his detection.
	 * 0: he got it right
	 * 1: he did not get it right
	 */
	private int[] points = {15,-10};
	
	public String[] getParts(){return parts;}
	public PhishSiteType getSiteType(){return this.siteType;}
	public PhishAttackType getAttackType(){return this.attackType;}
	public int[] getCorrectParts(){return this.correctparts;}
	
	public int getPoints(PhishResult result){
		boolean correct = (result == PhishResult.NoPhish_Detected || result==PhishResult.Phish_Detected);
		return points[correct ? 0 : 1];
	}
	
	public PhishResult getResult(boolean acceptance){
		PhishResult result;
		if(this.attackType == PhishAttackType.NoPhish && acceptance){
			result =  PhishResult.NoPhish_Detected;
		}else if(this.attackType == PhishAttackType.NoPhish && !acceptance){
			result =  PhishResult.NoPhish_NotDetected;
		}else if(this.attackType != PhishAttackType.NoPhish && acceptance){
			result =  PhishResult.Phish_NotDetected;
		}else if(this.attackType != PhishAttackType.NoPhish && !acceptance){
			result =  PhishResult.Phish_Detected;
		}else {
			throw new IllegalStateException("Something went horrably wrong! We should not be here.");
		}
		return result;
	}
}