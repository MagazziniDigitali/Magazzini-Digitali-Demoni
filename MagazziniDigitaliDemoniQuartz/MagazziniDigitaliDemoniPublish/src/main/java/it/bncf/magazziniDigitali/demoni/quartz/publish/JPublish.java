/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.publish;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitalePublish;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class JPublish extends JobExecute {

	private Logger log = Logger.getLogger(JPublish.class);

	/**
	 * 
	 */
	public JPublish() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitalePublish odBusiness = null;
		String id = null;
		String result = null;

		try {
			id = context.getJobDetail().
					getJobDataMap().getString("ID");
			log.debug("\n"+"Eseguo la Pubblicazione ID: "+id);
			odBusiness = new OggettoDigitalePublish(log, "Publish");
			odBusiness.esegui(id, 
					"Publish",
					MDDemoniQuartzBatch.mdConfiguration);
			result = "Pubblicazione ID "+id+" eseguita correttamente";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"Fine della Pubblicazione ID: "+id);
		}
		return result;
	}

}
