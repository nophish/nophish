package de.tudarmstadt.informatik.secuso.phishedu.backend;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.AbstractAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HTTPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HomoglyphicAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HostInPathAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.KeepAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.Level2Attack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.MisleadingAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.NonsenseAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.PhishTankURLAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.SubdomainAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.TypoAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.UnrelatedAttack;

/**
 * This enum shows what type of attack {@link BackendController#getNextUrl()} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishAttackType {
	/** It has no Attack. It is no phishing website */
	NoPhish(0,KeepAttack.class),
	/** It is a phish but not specified */
	AnyPhish(1, PhishTankURLAttack.class),
	/** The Attack for level 2 */
	Level2Attack(2, Level2Attack.class),
	/** Subdomains */
	Sudomains(3, SubdomainAttack.class),
	/** IP */
	IP(4, IPAttack.class),
	/** Nonsense Attack */
	Nonsense(5, NonsenseAttack.class),
	/** unrelated Attack */
	Unrelated(6, UnrelatedAttack.class),
	/** misleading */
	Misleading(7, MisleadingAttack.class),
	/** homographic */
	Homoglyphic(8, HomoglyphicAttack.class),
	/** Hostname in Path */
	HostInPath(9, HostInPathAttack.class),
	/** https -> http */
	HTTP(10, HTTPAttack.class),
	/** typo */
	Typo(11, TypoAttack.class);
	
	private int value;
	private Class<? extends AbstractAttack> attack_class;

	private PhishAttackType(int value, Class<? extends AbstractAttack>  attack_class) {
		this.value = value;
		this.attack_class = attack_class;
	}
	
	/**
	 * Get the integer value of the current result.
	 * This is imported when using this enum for indexing arrays.
	 * @return the integer Value of the current result
	 */
	public int getValue() {
        return value;
    }
	
	/**
	 * Get the Attack Class for this Attack
	 * @return The Attacktype Class for this Attack
	 */
	public Class<? extends AbstractAttack> getAttackClass() {
        return attack_class;
    }
}
