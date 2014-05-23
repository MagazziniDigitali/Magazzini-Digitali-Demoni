/**
 * 
 */
package it.bncf.magazziniDigitali.demoni;

import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;
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
			if (args.length==1){
				demoni = new MDDemoni();
				demoni.start(args[0]);
			} else {
				System.out.println("Per eseguire questo programma sono necessari i seguenti parametri:");
				System.out.println("1) Path dei file di properties");
			}
		} catch (MDDemoniException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void start(String pathProperties) throws MDDemoniException{
		
		try {
			Configuration.init(pathProperties);
			
			if (Configuration.getValue("demoni.Validate") != null &&
					Configuration.getValue("demoni.Validate").equalsIgnoreCase("true")){
				validate = new MDDemoniValidate(Thread.currentThread(), "Validate");
				validate.start();
			}
			
			while (true){
				if (validate != null &&
						validate.isAlive()){
					Thread.sleep(10000);
				} else {
					break;
				}
			}
		} catch (ConfigurationException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		}
	}
}
