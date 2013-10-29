package de.tudarmstadt.informatik.secuso.phishedu.backend.generator;

import de.tudarmstadt.informatik.secuso.phishedu.backend.AbstractUrlDecorator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This is the Base Class of all Generators.
 * Currently in mainly extends AbstractAttack because A generator is nothing more than an Attack.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BaseGenerator extends AbstractUrlDecorator {

	/**
	 * To build an Generator we need a url to decorate
	 * @param object the decorated URL
	 */
	public BaseGenerator(PhishURLInterface object) {
		super(object);
	}
	
	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.NoPhish;
	}
}
