/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniValidate extends Thread {

	private Logger log = Logger.getLogger(getClass());
	
	private boolean testMode = false;

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
			log.info("Start Demone per la Validazione");
			while (true) {
				execute();
				if (testMode){
					break;
				}
				Thread.sleep(Long.parseLong(Configuration.getValue("demoni.Validate.timeOut")));
			}
			log.info("Stop Demone per la Validazione");
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

		odBusiness = new OggettoDigitaleBusiness();
		odBusiness.validate(getName(), testMode, log);
	}

	/**
	 * @param testMode the testMode to set
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}
}
