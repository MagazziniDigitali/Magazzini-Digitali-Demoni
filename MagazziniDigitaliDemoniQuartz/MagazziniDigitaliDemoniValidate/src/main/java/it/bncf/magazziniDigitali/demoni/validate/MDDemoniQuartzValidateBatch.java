/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.validate;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzValidateBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzValidateBatch() {
		super("Validate");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzValidateBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzValidateBatch();
			mdDemoniQuartzBatch.esegui(args);
		} else {
			printHelp();
		}
	}

	@Override
	protected MDDemoniQuartz initScheduler(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket,
			boolean reScheduling, boolean quartzScheduler) throws SchedulerException {
		return new MDDemoniQuartz(processing, fileQuartz, socketPort, closeSocket, reScheduling,  quartzScheduler);
	}

}
