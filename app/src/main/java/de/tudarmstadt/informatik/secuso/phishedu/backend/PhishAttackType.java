package de.tudarmstadt.informatik.secuso.phishedu.backend;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.AbstractAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.FindDomainAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HTTPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HomoglyphicAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HostInPathAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttackNoBrand;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.KeepAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.MisleadingAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.NoPhishAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.PhishTankURLAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.SubdomainAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.TotallyUnrelatedAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.TypoAttack;

/**
 * This enum shows what type of attack {@link BackendController} call returned.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishAttackType {
	/**
	 *  This Attack forces the URL to be a noPhish URL.
	 *  This is something different then the KeepAttack which just passes to the decorated object.
	 */
	NoPhish(0,NoPhishAttack.class),
	/** It is a phish but not specified */
	AnyPhish(1, PhishTankURLAttack.class),
	/** The Attack for level 1 */
	FindDomain(2, FindDomainAttack.class),
	/** IP */
	IPNoBrand(3, IPAttackNoBrand.class),
	/** This Attack replaces the urls with something totally different */
	TotallyUnrelated(4, TotallyUnrelatedAttack.class),
	/** Subdomains */
	Subdomain(5, SubdomainAttack.class),
	/** Hostname in Path */
	HostInPath(6, HostInPathAttack.class),
	/** misleading */
	Misleading(7, MisleadingAttack.class),
	/** typo */
	Typo(8, TypoAttack.class),
	/** homographic */
	Homoglyphic(9, HomoglyphicAttack.class),
	/** https -> http */
	HTTP(10, HTTPAttack.class),
	/** This Attack keeps the supplied PhishURL */
	Keep(11, KeepAttack.class)
	;
	
	
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
	
	/**
	 * Gets a short name for this attack.
	 * @return A Attack name String
	 */
	public String getName(){
		return super.toString()+"Attack";
	}

}
