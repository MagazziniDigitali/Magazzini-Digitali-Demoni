/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import java.util.Vector;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleSolrIndex;
import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public class MDDemoniSolrIndex {

	private Logger log = Logger.getLogger(MDDemoniSolrIndex.class);

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniSolrIndex() {
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * @throws MDDemoniException 
	 * 
	 */
	public void execute(Vector<String> params, String application) 
			throws MDDemoniException {
		OggettoDigitaleSolrIndex odSolrIndex = null;
		
		if (params != null && params.size()==1){
			odSolrIndex = new OggettoDigitaleSolrIndex(null, log, "SolrIndex");
			odSolrIndex.esegui(params.get(0), application);
		} else {
			throw new MDDemoniException("[SolrIndex] ["+params.get(0)+"] Numero parametri non corretti");
		}
	}

}
