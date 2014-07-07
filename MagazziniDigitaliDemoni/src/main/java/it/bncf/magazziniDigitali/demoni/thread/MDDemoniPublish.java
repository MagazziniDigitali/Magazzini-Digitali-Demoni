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
public class MDDemoniPublish extends Thread {

	private Logger log = Logger.getLogger(getClass());

	private boolean testMode = false;
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniPublish(Runnable target, String name) {
		super(target, name);
	}

	@Override
	public void run() {
		try {
			log.info("Start Demone per la Pubblicazione");
			while (true) {
				execute();
				if (testMode){
					break;
				}
				Thread.sleep(10000);
			}
			log.info("Stop Demone per la Validazione");
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
		odBusiness.publish(getName(), testMode, log);
	}

	/**
	 * @param testMode the testMode to set
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}
}
