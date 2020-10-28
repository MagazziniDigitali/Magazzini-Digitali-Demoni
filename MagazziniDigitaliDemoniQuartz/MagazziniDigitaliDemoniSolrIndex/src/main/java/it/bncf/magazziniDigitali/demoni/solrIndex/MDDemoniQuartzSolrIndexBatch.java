/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.solrIndex;

import org.quartz.SchedulerException;

import com.amazonaws.SDKGlobalConfiguration;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzSolrIndexBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzSolrIndexBatch() {
		super("SolrIndex");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzSolrIndexBatch mdDemoniQuartzBatch = null;
		
		System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

		if (args.length == 2) {
			mdDemoniQuartzBatch = new MDDemoniQuartzSolrIndexBatch();
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
