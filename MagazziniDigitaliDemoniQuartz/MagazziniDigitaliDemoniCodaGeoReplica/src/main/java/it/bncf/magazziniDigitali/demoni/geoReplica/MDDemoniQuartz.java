/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.geoReplica;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.quartz.geoReplica.JCodaGeoReplica;
import it.bncf.magazziniDigitali.demoni.tools.MDDemoniQuartzTools;
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
		
		Hashtable<String, Object> params = null;


		while(!this.isShutdown()){
			
			try {
				
				params = new Hashtable<String,Object>();
				addScheduler(JCodaGeoReplica.class, 
						"CodaGeoReplica", 
						null,
						"codaGeoReplica", params);
			} catch (HibernateException e) {
				log.error(e.getMessage(),e);
			} catch (ConfigurationException e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
			}
			try {
				Thread.sleep(10000);
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
		
		try {
			addReScheduler(JCodaGeoReplica.class, 
					"CodaGeoReplica");
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
//		} catch (HibernateUtilException e) {
//			log.error(e.getMessage(),e);
		}
	}

}
