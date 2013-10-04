package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;

/**
 * This is the common interface for all Attacks. Each Attack will implement this interface.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface AttackInterface {
	public PhishAttackType getType();
}
