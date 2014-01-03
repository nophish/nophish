package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class UnrelatedAttack extends SubdomainAttack {
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public UnrelatedAttack(PhishURL object) {
		super(object);
	}

	protected static final String[] PHISHER_DOMAINS ={
		"login.com",
		"anmeldung.de",
		"security.com",
		"security-update.com",
		"sicherheit.de",
		"accounts.com",
		"konto.de",
		"verify.com",
		"verification.com",
		"verifizierung.de",
		"signin.com",
		"home.com",
		"update.de",
		"registration.com",
		"registrierung.com",
		"settings.com",
		"einstellungen.de",
		"service.de",
		"support.com",
		"hilfe.de",
		"abo.net",
		"abonnement.biz"
	};
	
	protected String[] getPhisherDomains(){
		return PHISHER_DOMAINS;
	}
	
	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Unrelated;
	}
}
