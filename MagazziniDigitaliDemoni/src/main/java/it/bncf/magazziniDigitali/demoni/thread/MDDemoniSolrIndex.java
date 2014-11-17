/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleSolrIndexBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public class MDDemoniSolrIndex extends MDDemoniThred {

	private Logger log = Logger.getLogger(MDDemoniSolrIndex.class);

	private boolean testMode = false;

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniSolrIndex(Runnable target, String name) {
		super(target, name);
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	protected void execute() {
		OggettoDigitaleSolrIndexBusiness odBusiness = null;
		
		odBusiness = new OggettoDigitaleSolrIndexBusiness(null);
		odBusiness.solrIndex(getName(), testMode, log);
	}

}
