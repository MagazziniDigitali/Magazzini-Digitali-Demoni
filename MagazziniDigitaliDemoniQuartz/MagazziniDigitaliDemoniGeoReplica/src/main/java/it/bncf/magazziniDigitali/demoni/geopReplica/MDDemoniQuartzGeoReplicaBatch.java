/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.geopReplica;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzGeoReplicaBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzGeoReplicaBatch() {
		super("GeoReplica");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzGeoReplicaBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzGeoReplicaBatch();
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
