package it.bncf.magazziniDigitali.demoni.quartz.genPackagesPremis;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.packegesPremis.OggettoDigitalePackagesPremisBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

public class JGenPackagesPremis extends JobExecute {

	private Logger log = Logger.getLogger(JGenPackagesPremis.class);

	public JGenPackagesPremis() {
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitalePackagesPremisBusiness odBusiness = null;
		String result = null;

		try {
			log.debug("\n"+"Inizio la Pacchettizzazione dei tacciati Premis");
			odBusiness = new OggettoDigitalePackagesPremisBusiness();
			odBusiness.packagePremis(log, 
					MDDemoniQuartzBatch.mdConfiguration);
			result = "Generazione pachetti premis completato";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"Fine della Pacchettizzazione dei tacciati Premis");
		}
		return result;
	}

}
