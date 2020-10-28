package it.bncf.magazziniDigitali.demoni.quartz.genRegistroIngressi;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.registroIngressi.OggettoDigitaleRegistroIngressiBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

public class JGenRegistroIngressi extends JobExecute {

	private Logger log = LogManager.getLogger(JGenRegistroIngressi.class);

	public JGenRegistroIngressi() {
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleRegistroIngressiBusiness odBusiness = null;
		String result = null;

		try {
			log.debug("\n"+"Inizio la Pacchettizzazione dei Registri di ingressi");
			odBusiness = new OggettoDigitaleRegistroIngressiBusiness();
			odBusiness.registroIngressi(log, 
					MDDemoniQuartzBatch.mdConfiguration);
			result = "Generazione registro di ingresso completata";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"Fine della Pacchettizzazione dei Registri di ingressi");
		}
		return result;
	}

}
