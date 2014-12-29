/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitalePublishBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniPublish extends MDDemoniThred {

	private Logger log = Logger.getLogger(MDDemoniPublish.class);
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniPublish(Runnable target, String name) {
		super(target, name,true);
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	protected void execute() {
		OggettoDigitalePublishBusiness odBusiness = null;

		odBusiness = new OggettoDigitalePublishBusiness(null);
		odBusiness.publish(getName(), testMode, log);
	}
}
