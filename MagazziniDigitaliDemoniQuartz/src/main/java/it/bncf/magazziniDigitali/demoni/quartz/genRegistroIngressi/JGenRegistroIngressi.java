package it.bncf.magazziniDigitali.demoni.quartz.genRegistroIngressi;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.registroIngressi.OggettoDigitaleRegistroIngressiBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;
import mx.randalf.quartz.job.JobExecute;

public class JGenRegistroIngressi extends JobExecute {

	private Logger log = Logger.getLogger(JGenRegistroIngressi.class);

	public JGenRegistroIngressi() {
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleRegistroIngressiBusiness odBusiness = null;
		String result = null;

		try {
			log.debug("Inizio la Pacchettizzazione dei Registri di ingressi");
			odBusiness = new OggettoDigitaleRegistroIngressiBusiness();
			odBusiness.registroIngressi(log, 
					MDDemoniQuartz.mdConfiguration);
			result = "Generazione registro di ingresso completata";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Pacchettizzazione dei Registri di ingressi");
		}
		return result;
	}

}
