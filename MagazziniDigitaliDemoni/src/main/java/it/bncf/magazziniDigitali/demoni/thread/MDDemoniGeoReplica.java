/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleGeoReplica;
import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniGeoReplica {

	private Logger log = Logger.getLogger(MDDemoniGeoReplica.class);
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniGeoReplica() {
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * @throws MDDemoniException 
	 * 
	 */
	public void execute(Vector<String> params, String application) 
			throws MDDemoniException {
		OggettoDigitaleGeoReplica odgr = null;

		log.debug("\n"+"Eseguo la Geo replica ");

		if (params != null && params.size()==1){
			odgr = new OggettoDigitaleGeoReplica(null, log,"GeoReplica");
			odgr.esegui(params.get(0), application);
		} else {
			throw new MDDemoniException("[GeoReplica] ["+params.get(0)+"]Numero parametri non corretti");
		}
	}
}
