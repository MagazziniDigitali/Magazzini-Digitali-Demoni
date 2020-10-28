package it.bncf.magazziniDigitali.demoni.quartz.genTicket;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.ticket.OggettoDigitaleTicketBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.quartz.job.JobExecute;

public class JGenTicket extends JobExecute {

	private Logger log = LogManager.getLogger(JGenTicket.class);

	public JGenTicket() {
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleTicketBusiness odBusiness = null;
		String result = null;

		try {
			log.debug("\n"+"Inizio la Pacchettizzazione dei Ticket");
			odBusiness = new OggettoDigitaleTicketBusiness();
			odBusiness.ticket(log, 
					MDDemoniQuartzBatch.mdConfiguration);
			result = "Generazione Ticket eseguito regolarmente";
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"Fine della Pacchettizzazione dei Ticket");
		}
		return result;
	}

}
