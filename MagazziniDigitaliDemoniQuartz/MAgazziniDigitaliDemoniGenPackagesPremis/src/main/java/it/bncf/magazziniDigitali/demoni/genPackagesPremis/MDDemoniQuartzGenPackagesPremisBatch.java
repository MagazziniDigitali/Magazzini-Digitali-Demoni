/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.genPackagesPremis;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzGenPackagesPremisBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzGenPackagesPremisBatch() {
		super("GenPackagesPremis");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzGenPackagesPremisBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzGenPackagesPremisBatch();
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
