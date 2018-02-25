/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.batch;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.tools.MDDemoniQuartzTools;
import it.depositolegale.configuration.MDConfiguration;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * @author massi
 *
 */
public abstract class MDDemoniQuartzBatch <MDQ extends MDDemoniQuartzTools> {

	public static MDConfiguration mdConfiguration = null;

	private Logger log = Logger.getLogger(MDDemoniQuartzBatch.class);

	private String nomeSW = null;

	/**
	 * 
	 */
	public MDDemoniQuartzBatch(String nomeSW) {
		this.nomeSW =nomeSW;
	}

	public void esegui(String[] args) {
		MDQ mdq = null;
		Integer socketPort = null;
		boolean closeSocket = false;
		boolean scheduling = false;
		boolean processing = false;
		boolean rescheduling = false;

		try {
			mdConfiguration = new MDConfiguration(nomeSW, "file:///"+args[0]);
			if (mdConfiguration.getSoftware().getErrorMsg() ==null ||
					mdConfiguration.getSoftware().getErrorMsg().length==0){
				
				if (args[1].equalsIgnoreCase("stop")){
					closeSocket = true;
					scheduling = true;
				} else if (args[1].equalsIgnoreCase("start")){
					closeSocket = false;
					processing = true;
					scheduling = true;
				} else if (args[1].equalsIgnoreCase("rescheduling")){
					rescheduling = true;
					scheduling = true;
				} else {
					printHelp();
					System.exit(-1);
				}

				try {
					socketPort = mdConfiguration.getSoftwareConfigInteger("socketPort");
				} catch (MDConfigurationException e) {
				}
				if (socketPort== null){
					socketPort=9000;
				}

				if (processing){
					log.info("\nRimango in attesa per l'elaborazione dei Jobs schedulati\n"
							+ "Porta per la chiusura del programma: "+socketPort);
				}

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mdq = initScheduler(processing, 
						args[0]+"/"+
						Configuration.getValue("software."+nomeSW+".quartz"), 
						socketPort, 
						closeSocket, 
						rescheduling);
				if (!closeSocket){
					if (!rescheduling){
						if (scheduling){
							log.info("\nInizio l'attività di Scheduling.\nPorta per la chiusura del programma: "+socketPort);
							mdq.scheduling();
						}
					} else {
						log.info("\nInizio l'attività di Re-Scheduling.\nPorta per la chiusura del programma: "+socketPort);
						mdq.reScheduling();
						mdq.shutdown();
					}
				}
			} else {
				for (int x=0; x<mdConfiguration.getSoftware().getErrorMsg().length; x++){
					log.error("\n"+mdConfiguration.getSoftware().getErrorMsg(x).getMsgError());
				}
			}
		} catch (MDConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}

	}

	protected abstract MDQ initScheduler(boolean processing, 
			String fileQuartz, Integer socketPort, boolean closeSocket,
			boolean reScheduling) throws SchedulerException;

	protected static void printHelp(){
		System.out.println("E' necessario indicare i seguenti parametri:");
		System.out.println("1) Path files Configurazione");
		System.out.println("2) Azione (start/stop/rescheduling)");
	}

}
