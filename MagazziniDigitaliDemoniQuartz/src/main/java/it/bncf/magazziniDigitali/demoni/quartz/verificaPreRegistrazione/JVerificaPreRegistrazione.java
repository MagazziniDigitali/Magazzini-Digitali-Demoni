/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.verificaPreRegistrazione;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.database.dao.MDPreRegistrazioneDAO;
import it.bncf.magazziniDigitali.demoni.quartz.MDDemoniQuartz;
import it.depositolegale.gestionale.verifica.preRegistrazione.ReadGoogle;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class JVerificaPreRegistrazione extends JobExecute {

	private Logger log = Logger.getLogger(JVerificaPreRegistrazione.class);

	/**
	 * 
	 */
	public JVerificaPreRegistrazione() {
	}

	/**
	 * @see mx.randalf.quartz.job.JobExecute#jobExecute(org.quartz.JobExecutionContext)
	 */
	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		ReadGoogle readGoogle = null;
		Integer nRow = 2;
		String login = null;
		String password = null;
		String urlConfirm = null;
		MDPreRegistrazioneDAO mdPreRegistrazioneDAO = null;
		
		try {
			login = MDDemoniQuartz.mdConfiguration.getSoftwareConfigString("send.email.login");
			password = MDDemoniQuartz.mdConfiguration.getSoftwareConfigString("send.email.password");
			urlConfirm = MDDemoniQuartz.mdConfiguration.getSoftwareConfigString("url.validate");
			readGoogle = new ReadGoogle(login, password, urlConfirm);

			mdPreRegistrazioneDAO = new MDPreRegistrazioneDAO();
			nRow = mdPreRegistrazioneDAO.max("progressivo");

			if (nRow == null || nRow ==0){
				nRow=2;
			}
			while (!context.getScheduler().isShutdown()){
				nRow = readGoogle.analizza(nRow);
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
				}
			}
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage(), e);
		} catch (MDConfigurationException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage(), e);
		}
		return "Verifica pre registrazioni completate correttamente";
	}

}
