package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class NonsenseAttack extends SubdomainAttack {
	public NonsenseAttack(PhishURL object) {
		super(object);
	}

	protected static final String[] PHISHER_DOMAINS ={
		"yxtar.com",
		"badcat.de",
		"myponyfarm.com",
		"kjshkd.de",
		"ddffgh.de",
		"qieoqq.com",
		"events-ma.de",
		"hitrakstaffing.com",
		"friendsoflight.com",
		"comil11cenepa.de",
		"birdsnbeesbaby.com",
		"mabaatiroofings.com",
		"jshk.de"
	};
	
	protected String[] getPhisherDomains(){
		return PHISHER_DOMAINS;
	}
	
}
