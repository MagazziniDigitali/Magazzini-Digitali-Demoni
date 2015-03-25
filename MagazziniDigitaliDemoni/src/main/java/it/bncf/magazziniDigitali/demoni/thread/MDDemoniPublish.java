/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitalePublish;
import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;

import java.sql.SQLException;
import java.util.Vector;

import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniPublish {

	private Logger log = Logger.getLogger(MDDemoniPublish.class);
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniPublish() {
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * @throws MDDemoniException 
	 * 
	 */
	public void execute(Vector<String> params, String application) 
			throws MDDemoniException {
		OggettoDigitalePublish odBusiness = null;

		log.debug("Eseguo la Geo replica ");

		try {
			if (params != null && params.size()==1){
				odBusiness = new OggettoDigitalePublish(null, log,"Publish");
				odBusiness.esegui(params.get(0), application);
			} else {
				throw new MDDemoniException("[Publish] ["+params.get(0)+"] Numero parametri non corretti");
			}
		} catch (ConfigurationException e) {
			throw  new MDDemoniException("[Publish] ["+params.get(0)+"] "+e.getMessage(),e);
		} catch (SQLException e) {
			throw  new MDDemoniException("[Publish] ["+params.get(0)+"] "+e.getMessage(),e);
		} catch (MDDemoniException e) {
			throw e;
		}
	}
}
