/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.geopReplica;

import org.quartz.SchedulerException;

import com.amazonaws.SDKGlobalConfiguration;

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
		
		System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

		if (args.length == 2) {
			mdDemoniQuartzBatch = new MDDemoniQuartzGeoReplicaBatch();
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
