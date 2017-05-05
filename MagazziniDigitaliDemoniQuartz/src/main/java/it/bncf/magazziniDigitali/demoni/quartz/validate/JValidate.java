/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.validate;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitaleValidate;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class JValidate extends JobExecute {

	private Logger log = Logger.getLogger(JValidate.class);

	/**
	 * 
	 */
	public JValidate() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleValidate odBusiness = null;
		String id = null;
		String result = null;

		try {
			id = context.getJobDetail().
					getJobDataMap().getString("ID");
			log.info("Eseguo la Validazione ID: "+id);
			System.out.println("Eseguo la Validazione ID: "+id);
			odBusiness = new OggettoDigitaleValidate(log,
					"Validate");
			odBusiness.esegui(id, 
						"Validate",
						MDDemoniQuartz.mdConfiguration);
			result ="Validazione ID "+id+" completata correttamente";
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.info("Fine Validazione ID: "+id);
		}
		return result;
	}

}
