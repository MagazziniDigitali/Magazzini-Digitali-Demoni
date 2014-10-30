/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleGeoReplicaBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniGeoReplica extends MDDemoniThred {

	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniGeoReplica(Runnable target, String name) {
		super(target, name);
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	protected void execute() {
		OggettoDigitaleGeoReplicaBusiness odBusiness = null;

		odBusiness = new OggettoDigitaleGeoReplicaBusiness(null);
		odBusiness.esegui(getName(), testMode, log);
	}
}
