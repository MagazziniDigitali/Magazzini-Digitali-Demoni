/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.publish;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitalePublish;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

/**
 * @author massi
 *
 */
public class JPublish implements Job {

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
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitalePublish odBusiness = null;
		String id = null;

		try {
			id = context.getJobDetail().
					getJobDataMap().getString("ID");
			log.debug("Eseguo la Pubblicazione ID: "+id);
			System.out.println("Eseguo la Pubblicazione ID: "+id);
			odBusiness = new OggettoDigitalePublish(log, "Publish");
			odBusiness.esegui(id, 
					"Publish",
					MDDemoniQuartz.mdConfiguration);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Pubblicazione ID: "+id);
		}

	}

}
