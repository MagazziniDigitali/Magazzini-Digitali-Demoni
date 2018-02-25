/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleValidate;
import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniValidate {

	private Logger log = Logger.getLogger(MDDemoniValidate.class);

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniValidate() {
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * @throws MDDemoniException 
	 * 
	 */
	public void execute(Vector<String> params, String application) 
			throws MDDemoniException {
		OggettoDigitaleValidate odBusiness = null;

		log.debug("\n"+"Eseguo la Validazione ");

		try {
			if (params != null && params.size()==1){
				odBusiness = new OggettoDigitaleValidate(null, log,"Validate");
				odBusiness.esegui(params.get(0), application);
			} else {
				throw new MDDemoniException("[Validate] Numero parametri non corretti");
			}
		} catch (SQLException e) {
			throw  new MDDemoniException("[Validate] ["+params.get(0)+"]"+e.getMessage(),e);
		} catch (MDDemoniException e) {
			throw e;
		}
	}
}
