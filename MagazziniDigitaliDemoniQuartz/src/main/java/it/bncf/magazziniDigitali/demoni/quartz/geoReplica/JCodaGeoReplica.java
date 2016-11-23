package it.bncf.magazziniDigitali.demoni.quartz.geoReplica;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleCodaBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

public class JCodaGeoReplica implements Job {

	private Logger log = Logger.getLogger(JCodaGeoReplica.class);

	public JCodaGeoReplica() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleCodaBusiness odBusiness = null;

		try {
			log.debug("Fine della Coda GeoReplica");
			odBusiness = new OggettoDigitaleCodaBusiness();
			odBusiness.coda(log, 
					MDDemoniQuartz.mdConfiguration);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Coda GeoReplica");
		}
	}

}
