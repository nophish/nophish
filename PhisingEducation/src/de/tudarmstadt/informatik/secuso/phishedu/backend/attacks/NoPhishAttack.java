package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;


import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This Attack is a Attack that does no attack.
 * This is for simplyfing the URL generation.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class NoPhishAttack extends AbstractAttack {

	/**
	 * See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 * @param object See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 */
	public NoPhishAttack(PhishURL object) {super(object);}
	
	public PhishAttackType getAttackType() {
		return object.getAttackType();
	}
	
	@Override
	public PhishResult getResult(boolean acceptance) {
		if(acceptance){
			return PhishResult.NoPhish_Detected;
		}else{
			return PhishResult.NoPhish_NotDetected;
		}
	}
	
}
