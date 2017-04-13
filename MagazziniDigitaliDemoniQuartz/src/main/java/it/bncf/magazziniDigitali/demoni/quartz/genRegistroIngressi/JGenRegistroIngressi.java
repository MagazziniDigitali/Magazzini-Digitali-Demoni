package it.bncf.magazziniDigitali.demoni.quartz.genRegistroIngressi;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.registroIngressi.OggettoDigitaleRegistroIngressiBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

public class JGenRegistroIngressi implements Job {

	private Logger log = Logger.getLogger(JGenRegistroIngressi.class);

	public JGenRegistroIngressi() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleRegistroIngressiBusiness odBusiness = null;

		try {
			log.debug("Inizio la Pacchettizzazione dei Registri di ingressi");
			odBusiness = new OggettoDigitaleRegistroIngressiBusiness();
			odBusiness.registroIngressi(log, 
					MDDemoniQuartz.mdConfiguration);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Pacchettizzazione dei Registri di ingressi");
		}
	}

}
