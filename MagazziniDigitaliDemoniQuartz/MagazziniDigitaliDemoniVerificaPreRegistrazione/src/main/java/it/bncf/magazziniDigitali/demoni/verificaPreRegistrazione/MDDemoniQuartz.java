/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.verificaPreRegistrazione;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.quartz.verificaPreRegistrazione.JVerificaPreRegistrazione;
import it.bncf.magazziniDigitali.demoni.tools.MDDemoniQuartzTools;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * @author massi
 *
 */
public class MDDemoniQuartz extends MDDemoniQuartzTools {

	private static Logger log = LogManager.getLogger(MDDemoniQuartz.class);

	/**
	 * @param processiong
	 * @param fileQuartz
	 * @param socketPort
	 * @param closeSocket
	 * @throws SchedulerException
	 */
	public MDDemoniQuartz(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket,
			boolean reScheduling, boolean quartzScheduler) throws SchedulerException {
		super(processing, fileQuartz, socketPort, closeSocket, reScheduling, quartzScheduler);
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	public void scheduling() throws SchedulerException {

		while (!this.isShutdown()) {

			try {

				addScheduler(JVerificaPreRegistrazione.class, "VerificaPreRegistrazione", null,
						"verificaPreRegistrazione", null, true);
			} catch (HibernateException e) {
				log.error(e.getMessage(), e);
			} catch (ConfigurationException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	public void reScheduling() throws SchedulerException {

	}

}
