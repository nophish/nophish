package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.AbstractUrlDecorator;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractAttack extends AbstractUrlDecorator {
	protected PhishURLInterface object;
	
	/**
	 * This constructor takes a random Valid URL and decorates it.
	 */
	public AbstractAttack(){
		this.object=BackendController.getInstance().getPhishURL(PhishAttackType.NoPhish);
	}
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractAttack(PhishURLInterface object){
		this.object=object;
	}

}
