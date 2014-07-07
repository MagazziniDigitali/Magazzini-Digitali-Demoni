/**
 * 
 */
package it.bncf.magazziniDigitali.demoni;

import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniPublish;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniValidate;

import org.apache.log4j.Logger;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * @author massi
 *
 */
public class MDDemoni {

	private static Logger log = Logger.getLogger(MDDemoni.class);

	private MDDemoniValidate validate = null;

	private MDDemoniPublish publish = null;
	
	/**
	 * 
	 */
	public MDDemoni() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoni demoni = null;
		
		try {
			if (args.length>=1){
				demoni = new MDDemoni();
				demoni.start(args[0],(args.length>1?args[1]:null));
			} else {
				System.out.println("Per eseguire questo programma sono necessari i seguenti parametri:");
				System.out.println("1) Path dei file di properties");
				System.out.println("2) --test (Opzionale) per eseguire l'applicazione in modalità Test");
			}
		} catch (MDDemoniException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void start(String pathProperties, String testMode) throws MDDemoniException{
		
		try {
			log.info("Start Demoni");
			if (testMode != null && testMode.equals("--test")){
				System.out.println("Applicazione in modalità Test");
			}
			
			Configuration.init(pathProperties);
			
			if (Configuration.getValue("demoni.Validate") != null &&
					Configuration.getValue("demoni.Validate").equalsIgnoreCase("true")){
				validate = new MDDemoniValidate(Thread.currentThread(), "Validate");
				if (testMode != null && testMode.equals("--test")){
					validate.setTestMode(true);
					validate.run();
				} else {
					validate.start();
				}
			}
			
			if (Configuration.getValue("demoni.Publish") != null &&
					Configuration.getValue("demoni.Publish").equalsIgnoreCase("true")){
				publish = new MDDemoniPublish(Thread.currentThread(), "Publish");
				if (testMode != null && testMode.equals("--test")){
					publish.setTestMode(true);
					publish.run();
				} else {
					publish.start();
				}
			}
			
			if (testMode != null && testMode.equals("--test")){
				System.out.println("Fine Test");
			} else {
				while (true){
					if ((validate != null &&
							validate.isAlive()) ||
						(publish != null &&
							publish.isAlive())){
						Thread.sleep(10000);
					} else {
						break;
					}
				}
			}
			log.info("Stop Demoni");
		} catch (ConfigurationException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		}
	}
}
