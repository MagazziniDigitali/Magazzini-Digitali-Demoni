/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.solrIndex;

import org.quartz.SchedulerException;

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
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzSolrIndexBatch();
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
