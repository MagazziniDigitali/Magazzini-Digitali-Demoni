/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.geoReplica;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzCodaGeoReplicaBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzCodaGeoReplicaBatch() {
		super("CodaGeoReplica");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzCodaGeoReplicaBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzCodaGeoReplicaBatch();
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
