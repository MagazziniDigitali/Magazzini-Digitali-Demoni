/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.solrIndex;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleSolrIndex;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class JSolrIndex extends JobExecute {

	private Logger log = Logger.getLogger(JSolrIndex.class);

	/**
	 * 
	 */
	public JSolrIndex() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleSolrIndex odSolrIndex = null;
		String id = null;
		String result = null;

		id = context.getJobDetail().
				getJobDataMap().getString("ID");
		log.debug("\n"+"Eseguo la indicizzazione in Solr ID: "+id);
		odSolrIndex = new OggettoDigitaleSolrIndex(log, "SolrIndex");
		odSolrIndex.esegui(id, 
				"SolrIndex",
				MDDemoniQuartzBatch.mdConfiguration);
		log.debug("\n"+"Fine della Indicizzazaione in Solr ID: "+id);
		result = "Indicizzazione su solr ID "+id+" eseguita corretamente";
		return result;
	}

}
