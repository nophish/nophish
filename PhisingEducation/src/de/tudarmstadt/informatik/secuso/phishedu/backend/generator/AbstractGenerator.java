package de.tudarmstadt.informatik.secuso.phishedu.backend.generator;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.AbstractAttack;

/**
 * This is the Base Class of all Generators.
 * Currently in mainly extends AbstractAttack because A generator is nothing more than an Attack.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractGenerator extends AbstractAttack {

	/**
	 * To build an Generator we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractGenerator(PhishURLInterface object) {
		super(object);
	}

}
