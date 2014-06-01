/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniValidate extends Thread {

	private Logger log = Logger.getLogger(getClass());

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniValidate(Runnable target, String name) {
		super(target, name);
	}

	@Override
	public void run() {
		try {
			while (true) {
				execute();
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	private void execute() {
		OggettoDigitaleBusiness odBusiness = null;

		odBusiness = new OggettoDigitaleBusiness();
		odBusiness.validate("Validate");
	}
}
