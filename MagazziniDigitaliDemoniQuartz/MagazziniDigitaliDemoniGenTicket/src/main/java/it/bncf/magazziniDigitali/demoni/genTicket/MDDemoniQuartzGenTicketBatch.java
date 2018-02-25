/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.genTicket;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzGenTicketBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzGenTicketBatch() {
		super("GenTicket");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzGenTicketBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzGenTicketBatch();
			mdDemoniQuartzBatch.esegui(args);
		} else {
			printHelp();
		}
	}

	@Override
	protected MDDemoniQuartz initScheduler(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket,
			boolean reScheduling) throws SchedulerException {
		return new MDDemoniQuartz(processing, fileQuartz, socketPort, closeSocket, reScheduling);
	}

}
