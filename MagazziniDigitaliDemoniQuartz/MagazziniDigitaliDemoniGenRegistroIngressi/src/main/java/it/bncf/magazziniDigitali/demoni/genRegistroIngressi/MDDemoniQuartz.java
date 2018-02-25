/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.genRegistroIngressi;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.quartz.genRegistroIngressi.JGenRegistroIngressi;
import it.bncf.magazziniDigitali.demoni.tools.MDDemoniQuartzTools;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * @author massi
 *
 */
public class MDDemoniQuartz extends MDDemoniQuartzTools {

	private static Logger log = Logger.getLogger(MDDemoniQuartz.class);

	/**
	 * @param processiong
	 * @param fileQuartz
	 * @param socketPort
	 * @param closeSocket
	 * @throws SchedulerException
	 */
	public MDDemoniQuartz(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket, boolean reScheduling)
			throws SchedulerException {
		super(processing, fileQuartz, socketPort, closeSocket, reScheduling);
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	public void scheduling() throws SchedulerException {


		while(!this.isShutdown()){
			
			try {
				
				addScheduler(JGenRegistroIngressi.class, 
				"GenRegistroIngressi", 
				CronScheduleBuilder.dailyAtHourAndMinute(
						Integer.parseInt(Configuration.getValueDefault("genRegistroIngressi.cronHour", "4")), 
						Integer.parseInt(Configuration.getValueDefault("genRegistroIngressi.cronMinute", "0"))), 
				"genRegistroIngressi", null);

			} catch (HibernateException e) {
				log.error(e.getMessage(),e);
			} catch (ConfigurationException e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	public void reScheduling() throws SchedulerException {

		addReScheduler(JGenRegistroIngressi.class, 
				"GenRegistroIngressi");

	}

}
