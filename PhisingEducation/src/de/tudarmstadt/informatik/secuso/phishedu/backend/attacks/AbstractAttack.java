package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.AbstractUrlDecorator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractAttack extends AbstractUrlDecorator {
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractAttack(PhishURL object){
		this.object=object;
	}
	
	@Override
	public PhishResult getResult(boolean acceptance) {
		if(acceptance){
			return PhishResult.Phish_NotDetected;
		}else{
			return PhishResult.Phish_Detected;
		}
	}
	
}
