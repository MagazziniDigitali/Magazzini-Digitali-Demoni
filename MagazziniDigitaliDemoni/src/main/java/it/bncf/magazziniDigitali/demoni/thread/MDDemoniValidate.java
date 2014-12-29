/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleValidateBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniValidate extends MDDemoniThred {

	private Logger log = Logger.getLogger(MDDemoniValidate.class);

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniValidate(Runnable target, String name) {
		super(target, name,true);
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	protected void execute() {
		OggettoDigitaleValidateBusiness odBusiness = null;

		odBusiness = new OggettoDigitaleValidateBusiness(null);
		odBusiness.validate(getName(), testMode, log);
	}
}
