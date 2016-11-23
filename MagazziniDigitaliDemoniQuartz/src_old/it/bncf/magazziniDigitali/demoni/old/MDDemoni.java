/**
 * 
 */
package it.bncf.magazziniDigitali.demoni;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import it.bncf.magazziniDigitali.database.dao.MDIstituzioneDAO;
import it.bncf.magazziniDigitali.database.entity.MDIstituzione;
import it.bncf.magazziniDigitali.demoni.quartz.QuartzTools;
import it.bncf.magazziniDigitali.demoni.quartz.istituzione.JIstituzione;
import it.bncf.magazziniDigitali.demoni.quartz.istituzione.validate.JValidate;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * Questa classe viene utilizzata per la gestione della Schedulazione delle attività del progetto
 * 
 * @author massi
 *
 */
public class MDDemoni {

	/**
	 * Variabile utilizzata per loggare l'applicazione
	 */
	private static Logger log = Logger.getLogger(MDDemoni.class);

	/**
	 * Variabile utilizzata per la gestione della schedurlazione delle attività
	 */
	private Scheduler scheduler = null;

	private Hashtable<String, JobKey> jIstituzioni = null;

	/**
	 * Costruttore
	 * @throws SchedulerException 
	 */
	public MDDemoni() throws SchedulerException {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			jIstituzioni = new Hashtable<String, JobKey>();
		} catch (SchedulerException e) {
			throw e;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public void start(String[] args) {
		String pathProperties = null;
		String testMode = null;

		try {
			log.info("Start Demoni");
			for (int x=0;x<args.length; x++){
				if (pathProperties==null){
					pathProperties = args[x];
				} else {
					if (args[x].trim().equalsIgnoreCase("--test")){
						testMode = args[x].trim().toLowerCase();
//				} else if (operation == null) {
//					operation = args[x];
//				} else {
//					if (params == null){
//						params = new Vector<String>();
//					}
//					params.add(args[x]);
					}
				}
			}
			if (testMode != null && testMode.equals("--test")){
				System.out.println("Applicazione in modalità Test");
			}
			
			Configuration.init(pathProperties);
			
			while(true){
				start();
				if (testMode != null && testMode.equals("--test")){
					break;
				}
				try {
					Thread.sleep(600000);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}

			if (testMode != null && testMode.equals("--test")){
				System.out.println("Fine Test");
			}
			log.info("Stop Demoni");
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void start(){
		MDIstituzioneDAO mdIstituzioneDAO = null;
		List<MDIstituzione> mdIstituzioni = null;
		MDIstituzione mdIstituzione = null;

		try {
			mdIstituzioneDAO = new MDIstituzioneDAO(null);
			
			mdIstituzioni = mdIstituzioneDAO.findAll();
			for (int x = 0; x<mdIstituzioni.size(); x++){
				try {
					mdIstituzione = mdIstituzioni.get(x);
					if (jIstituzioni.get(mdIstituzione.getId())== null ||
							scheduler.checkExists(jIstituzioni.get(mdIstituzione.getId()))){
						jIstituzioni.put(mdIstituzione.getId(), start(mdIstituzione));
					}
				} catch (SchedulerException e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Metodo utilizzato per l'esecuzione della scheduzione dell'attività relativo all'istituzione
	 * 
	 * @param mdIstituzione
	 * @return
	 * @throws SchedulerException
	 */
	private JobKey start(MDIstituzione mdIstituzione) throws SchedulerException{
		JobKey jobKey = null;
		Hashtable<String, Object> params = null;

		try {
			params = new Hashtable<String, Object>();
			params.put(JIstituzione.IDISTITUTO, mdIstituzione.getId());
			jobKey = QuartzTools.startJob(scheduler, 
					JIstituzione.class, 
					"Istituzione", 
					mdIstituzione.getId(), 
					"Istituzione", 
					mdIstituzione.getId(),
					params);
		} catch (SchedulerException e) {
			throw e;
		}
		return jobKey;
	}
}
