/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;

import org.apache.log4j.Logger;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * @author massi
 *
 */
public class MDDemoniCoda extends Thread {

	private Logger log = Logger.getLogger(getClass());

	private boolean testMode = false;

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniCoda(Runnable target, String name) {
		super(target, name);
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			log.info("Start Demone per la Generazione del file Coda");
			while (true) {
				execute();
				if (testMode){
					break;
				}
				Thread.sleep(Long.parseLong(Configuration.getValue("demoni.Coda.timeOut")));
			}
			log.info("Stop Demone per la Generazione del file Coda");
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
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

		odBusiness = new OggettoDigitaleBusiness(null);
		odBusiness.coda(getName(), testMode, log);
	}

	/**
	 * @param testMode the testMode to set
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

}
