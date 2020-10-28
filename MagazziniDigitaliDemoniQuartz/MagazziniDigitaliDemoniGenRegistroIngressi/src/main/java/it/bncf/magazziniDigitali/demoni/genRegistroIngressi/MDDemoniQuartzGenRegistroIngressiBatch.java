/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.genRegistroIngressi;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzGenRegistroIngressiBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzGenRegistroIngressiBatch() {
		super("GenRegistroIngressi");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzGenRegistroIngressiBatch mdDemoniQuartzBatch = null;
		if (args.length == 2) {
			mdDemoniQuartzBatch = new MDDemoniQuartzGenRegistroIngressiBatch();
			mdDemoniQuartzBatch.esegui(args);
		} else {
			printHelp();
		}
	}

	@Override
	protected MDDemoniQuartz initScheduler(boolean processing, String fileQuartz, Integer socketPort,
			boolean closeSocket, boolean reScheduling, boolean quartzScheduler) throws SchedulerException {
		return new MDDemoniQuartz(processing, fileQuartz, socketPort, closeSocket, reScheduling, quartzScheduler);
	}

}
