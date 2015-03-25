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

	private boolean running = true;

	private boolean repeater = false;
	
	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniThred(Runnable target, String name, boolean repeater) {
		super(target, name);
		this.repeater = repeater;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			log.info("Start Demone ["+getName()+"]");
			while (running) {
				execute();
				if (testMode|| ! repeater || !running){
					break;
				}
				if (Configuration.getValue("demoni."+getName()+".timeOut")==null){
					Thread.sleep(10000);
				} else {
					Thread.sleep(Long.parseLong(Configuration.getValue("demoni."+getName()+".timeOut")));
				}
			}
			finalize();
			log.info("Stop Demone ["+getName()+"]");
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività finali
	 * 
	 */
	protected abstract void finalize();

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

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

}
