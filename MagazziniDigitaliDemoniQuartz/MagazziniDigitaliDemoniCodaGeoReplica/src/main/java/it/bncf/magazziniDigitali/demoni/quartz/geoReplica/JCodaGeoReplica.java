package it.bncf.magazziniDigitali.demoni.quartz.geoReplica;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.coda.OggettoDigitaleCodaBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

public class JCodaGeoReplica extends JobExecute {

	private Logger log = Logger.getLogger(JCodaGeoReplica.class);

	public JCodaGeoReplica() {
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleCodaBusiness odBusiness = null;
		String result = null;

		try {
			log.debug("\n"+"Inizio della Coda GeoReplica");
			odBusiness = new OggettoDigitaleCodaBusiness();
			odBusiness.coda(log, 
					MDDemoniQuartzBatch.mdConfiguration, context);
			result = "Coda generata correttamente";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"Fine della Coda GeoReplica");
		}
		return result;
	}

}
