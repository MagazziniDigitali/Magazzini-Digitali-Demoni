/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.solrIndex;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleSolrIndex;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

/**
 * @author massi
 *
 */
public class JSolrIndex implements Job {

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
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleSolrIndex odSolrIndex = null;
		String id = null;

		id = context.getJobDetail().
				getJobDataMap().getString("ID");
		log.debug("Eseguo la indicizzazione in Solr ID: "+id);
		System.out.println("Eseguo la indicizzazione in Solr ID: "+id);
		odSolrIndex = new OggettoDigitaleSolrIndex(log, "SolrIndex");
		odSolrIndex.esegui(id, 
				"SolrIndex",
				MDDemoniQuartz.mdConfiguration);
		log.debug("Fine della Indicizzazaione in Solr ID: "+id);
	}

}
