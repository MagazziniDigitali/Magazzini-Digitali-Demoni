/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.publish;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;
import it.bncf.magazziniDigitali.database.dao.MDStatoDAO;
import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;
import it.bncf.magazziniDigitali.database.entity.MDStato;
import it.bncf.magazziniDigitali.demoni.quartz.publish.JPublish;
import it.bncf.magazziniDigitali.demoni.tools.MDDemoniQuartzTools;
import mx.randalf.hibernate.exception.HibernateUtilException;

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
		OggettoDigitaleBusiness odb = null;
		MDStatoDAO mdStatoDAO = null;
		List<MDFilesTmp> mdFilesTmps = null;

		odb = new OggettoDigitaleBusiness();
		mdStatoDAO = new MDStatoDAO();

		while(!this.isShutdown()){
			
			try {
				
				mdFilesTmps = null;
				mdFilesTmps = odb.findStatus(null, new MDStato[] {
						mdStatoDAO.FINEVALID(), mdStatoDAO.INITPUBLISH() }, 
						0, mdFilesTmps);
				addScheduler(JPublish.class, 
						mdFilesTmps,
						"Publish");
			} catch (HibernateException e) {
				log.error(e.getMessage(),e);
			} catch (HibernateUtilException e) {
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
		OggettoDigitaleBusiness odb = null;
		MDStatoDAO mdStatoDAO = null;
		List<MDFilesTmp> mdFilesTmps = null;

		odb = new OggettoDigitaleBusiness();
		mdStatoDAO = new MDStatoDAO();
		
		try {
			mdFilesTmps = null;
			mdFilesTmps = odb.findStatus(null, new MDStato[] {
					mdStatoDAO.FINEVALID(), mdStatoDAO.INITPUBLISH() }, 
					0, mdFilesTmps);
			addReScheduler(JPublish.class, 
					mdFilesTmps,
					"Publish");
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(),e);
		}
	}

}
