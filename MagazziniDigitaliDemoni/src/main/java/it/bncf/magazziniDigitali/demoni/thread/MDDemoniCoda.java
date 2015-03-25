/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleCodaBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public class MDDemoniCoda extends MDDemoniThred {

	private Logger log = Logger.getLogger(MDDemoniCoda.class);

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniCoda(Runnable target, String name) {
		super(target, name,false);
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	protected void execute() {
		OggettoDigitaleCodaBusiness odBusiness = null;

		odBusiness = new OggettoDigitaleCodaBusiness(null);
		odBusiness.coda(getName(), testMode, log);
	}

	@Override
	protected void finalize() {
	}

}
