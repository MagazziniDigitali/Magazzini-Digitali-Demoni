/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.publish;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzPublishBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzPublishBatch() {
		super("Publish");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzPublishBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzPublishBatch();
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
