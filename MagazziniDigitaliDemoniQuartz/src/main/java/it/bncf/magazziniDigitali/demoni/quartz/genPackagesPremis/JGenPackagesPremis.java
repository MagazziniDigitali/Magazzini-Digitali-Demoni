package it.bncf.magazziniDigitali.demoni.quartz.genPackagesPremis;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitalePackagesPremisBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

public class JGenPackagesPremis implements Job {

	private Logger log = Logger.getLogger(JGenPackagesPremis.class);

	public JGenPackagesPremis() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitalePackagesPremisBusiness odBusiness = null;

		try {
			log.debug("Inizio la Pacchettizzazione dei tacciati Premis");
			odBusiness = new OggettoDigitalePackagesPremisBusiness();
			odBusiness.packagePremis(log, 
					MDDemoniQuartz.mdConfiguration);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Pacchettizzazione dei tacciati Premis");
		}
	}

}
