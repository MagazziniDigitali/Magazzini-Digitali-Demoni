package it.bncf.magazziniDigitali.demoni.quartz.genTicket;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.ticket.OggettoDigitaleTicketBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;

public class JGenTicket implements Job {

	private Logger log = Logger.getLogger(JGenTicket.class);

	public JGenTicket() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		OggettoDigitaleTicketBusiness odBusiness = null;

		try {
			log.debug("Inizio la Pacchettizzazione dei Ticket");
			odBusiness = new OggettoDigitaleTicketBusiness();
			odBusiness.ticket(log, 
					MDDemoniQuartz.mdConfiguration);
		} catch (MDConfigurationException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JobExecutionException(e.getMessage(), e);
		} finally {
			log.debug("Fine della Pacchettizzazione dei Ticket");
		}
	}

}
