/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public abstract class MDDemoniThred extends Thread {

	private Logger log = Logger.getLogger(MDDemoniThred.class);

	protected boolean testMode = false;

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniThred(Runnable target, String name) {
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
	protected abstract void execute();
	
	/**
	 * @param testMode the testMode to set
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

}
