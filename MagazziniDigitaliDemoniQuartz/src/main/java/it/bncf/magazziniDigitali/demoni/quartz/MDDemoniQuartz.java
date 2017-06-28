/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.database.dao.MDStatoDAO;
import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;
import it.bncf.magazziniDigitali.database.entity.MDStato;
import it.bncf.magazziniDigitali.demoni.quartz.genPackagesPremis.JGenPackagesPremis;
import it.bncf.magazziniDigitali.demoni.quartz.genRegistroIngressi.JGenRegistroIngressi;
import it.bncf.magazziniDigitali.demoni.quartz.genTicket.JGenTicket;
import it.bncf.magazziniDigitali.demoni.quartz.geoReplica.JCodaGeoReplica;
import it.bncf.magazziniDigitali.demoni.quartz.geoReplica.JGeoReplica;
import it.bncf.magazziniDigitali.demoni.quartz.publish.JPublish;
import it.bncf.magazziniDigitali.demoni.quartz.solrIndex.JSolrIndex;
import it.bncf.magazziniDigitali.demoni.quartz.validate.JValidate;
import it.bncf.magazziniDigitali.demoni.quartz.verificaPreRegistrazione.JVerificaPreRegistrazione;
import it.depositolegale.configuration.MDConfiguration;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.randalf.quartz.QuartzScheduler;
import mx.randalf.quartz.QuartzTools;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class MDDemoniQuartz extends QuartzScheduler {

	private static Logger log = Logger.getLogger(MDDemoniQuartz.class);

	public static MDConfiguration mdConfiguration = null;

	/**
	 * @param processiong
	 * @param fileQuartz
	 * @param socketPort
	 * @param closeSocket
	 * @throws SchedulerException
	 */
	public MDDemoniQuartz(boolean processiong, String fileQuartz, Integer socketPort, boolean closeSocket, boolean reScheduling)
			throws SchedulerException {
		super(processiong, fileQuartz, socketPort, closeSocket, reScheduling);
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	protected void scheduling() throws SchedulerException {
		OggettoDigitaleBusiness odb = null;

		List<MDFilesTmp> mdFilesTmps = null;
		MDStatoDAO mdStatoDAO = null;
		
		odb = new OggettoDigitaleBusiness();
		mdStatoDAO = new MDStatoDAO();
		Hashtable<String, Object> params = null;


		while(!this.isShutdown()){
			
			try {

				addScheduler(JVerificaPreRegistrazione.class,
						"VerificaPreRegistrazione",
						null,
						"verificaPreRegistrazione", null, true);
				
				params = new Hashtable<String,Object>();
				if (Configuration.getValue("codaGeoReplica.addGG") != null)params.put("addGG", Integer.parseInt(Configuration.getValue("codaGeoReplica.addGG")));
				if (Configuration.getValue("codaGeoReplica.setHour") != null)params.put("setHour", Integer.parseInt(Configuration.getValue("codaGeoReplica.setHour")));
				if (Configuration.getValue("codaGeoReplica.setMinute") != null)params.put("setMinute", Integer.parseInt(Configuration.getValue("codaGeoReplica.setMinute")));
				if (Configuration.getValue("codaGeoReplica.setSecond") != null)params.put("setSecond", Integer.parseInt(Configuration.getValue("codaGeoReplica.setSecond")));
				if (Configuration.getValue("codaGeoReplica.setMillisecond") != null)params.put("setMillisecond", Integer.parseInt(Configuration.getValue("codaGeoReplica.setMillisecond")));
				addScheduler(JCodaGeoReplica.class, 
						"CodaGeoReplica", 
						CronScheduleBuilder.dailyAtHourAndMinute(
								Integer.parseInt(Configuration.getValueDefault("codaGeoReplica.cronHour", "1")), 
								Integer.parseInt(Configuration.getValueDefault("codaGeoReplica.cronMinute", "0"))), 
						"codaGeoReplica", params);

				addScheduler(JGeoReplica.class, 
						"GeoReplica", 
						CronScheduleBuilder.dailyAtHourAndMinute(
								Integer.parseInt(Configuration.getValueDefault("geoReplica.cronHour", "2")), 
								Integer.parseInt(Configuration.getValueDefault("geoReplica.cronMinute", "0"))), 
						"geoReplica", null);

				addScheduler(JGenPackagesPremis.class, 
						"GenPackagesPremis", 
						CronScheduleBuilder.dailyAtHourAndMinute(
								Integer.parseInt(Configuration.getValueDefault("genPackagesPremis.cronHour", "3")), 
								Integer.parseInt(Configuration.getValueDefault("genPackagesPremis.cronMinute", "0"))), 
						"genPackagesPremis", null);

				addScheduler(JGenRegistroIngressi.class, 
						"GenRegistroIngressi", 
						CronScheduleBuilder.dailyAtHourAndMinute(
								Integer.parseInt(Configuration.getValueDefault("genRegistroIngressi.cronHour", "4")), 
								Integer.parseInt(Configuration.getValueDefault("genRegistroIngressi.cronMinute", "0"))), 
						"genRegistroIngressi", null);

				addScheduler(JGenTicket.class, 
						"GenTicket", 
						CronScheduleBuilder.dailyAtHourAndMinute(
								Integer.parseInt(Configuration.getValueDefault("genTicket.cronHour", "5")), 
								Integer.parseInt(Configuration.getValueDefault("genTicket.cronMinute", "0"))), 
						"genTicket", null);

				if (Configuration.getValueDefault("validate.enable", "true").equalsIgnoreCase("true")){
					mdFilesTmps = null;
					mdFilesTmps = odb.findStatus(null, new MDStato[] {
							mdStatoDAO.FINETRASF(), mdStatoDAO.INITVALID() }, 
							0, mdFilesTmps);
					addScheduler(JValidate.class, 
							mdFilesTmps,
							"Validate");
				}

				if (Configuration.getValueDefault("publisher.enable", "true").equalsIgnoreCase("true")){
					mdFilesTmps = null;
					mdFilesTmps = odb.findStatus(null, new MDStato[] {
							mdStatoDAO.FINEVALID(), mdStatoDAO.INITPUBLISH() }, 
							0, mdFilesTmps);
					addScheduler(JPublish.class, 
							mdFilesTmps,
							"Publish");
				}

				if (Configuration.getValueDefault("solrIndex.enable", "true").equalsIgnoreCase("true")){
					mdFilesTmps = null;
					mdFilesTmps = odb.findStatus(null, new MDStato[] {
							mdStatoDAO.FINEARCHIVE(), mdStatoDAO.INITINDEX() }, 
							0, mdFilesTmps);
					addScheduler(JSolrIndex.class, 
							mdFilesTmps,
							"SolrIndex");
				}
			} catch (HibernateException e) {
				log.error(e.getMessage(),e);
			} catch (HibernateUtilException e) {
				e.printStackTrace();
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

	private void addScheduler(Class<? extends JobExecute> jClass, String tPrefix, ScheduleBuilder<?> schedBuilder,
			String cPrefix, Hashtable<String, Object> params) throws ConfigurationException {
		addScheduler(jClass, tPrefix, schedBuilder, cPrefix, params, false);
	}

	private void addScheduler(Class<? extends JobExecute> jClass, String tPrefix, ScheduleBuilder<?> schedBuilder,
			String cPrefix, Hashtable<String, Object> params, boolean reSchedulerJobComplete) throws ConfigurationException {
		if (Configuration.getValueDefault(cPrefix+".enable", "true").equalsIgnoreCase("true")){
			if (Configuration.getValueDefault(cPrefix+".test", "false").equalsIgnoreCase("true")){
				addScheduler(jClass, tPrefix+"Test", null, params, reSchedulerJobComplete);
			} else {
				addScheduler(jClass, tPrefix, schedBuilder, params, reSchedulerJobComplete);
			}
		}
	}

	private void addScheduler(Class<? extends JobExecute> jClass, 
			String tPrefix, ScheduleBuilder<?> schedBuilder, Hashtable<String, Object> params, boolean reSchedulerJobComplete){
		JobKey jobKey = null;

		String jobGroup = null;
		String jobName = null;
		String triggerGroup = null;
		String triggerName	= null;
		TriggerKey triggerKey = null;
		TriggerState triggerState = null;

		jobGroup = "Job_" + tPrefix;
		jobName = tPrefix;
		triggerGroup = "Triggert_"+tPrefix;
		
		try {
			triggerName = scheduler.getSchedulerInstanceId();
			jobKey = new JobKey(jobName, jobGroup);
			triggerKey = new TriggerKey(triggerName, triggerGroup);
			if (!scheduler.checkExists(jobKey)){
				QuartzTools.startJob(scheduler, jClass, jobGroup, jobName, triggerGroup,
						triggerName, params, schedBuilder);
			} else {
				triggerState = scheduler.getTriggerState(triggerKey);
				if (triggerState.compareTo(TriggerState.COMPLETE)==0 && reSchedulerJobComplete){
					scheduler.deleteJob(jobKey);
					QuartzTools.startJob(scheduler, jClass, jobGroup, jobName, triggerGroup,
							triggerName, params, schedBuilder);
				}
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	private void addScheduler(Class<? extends JobExecute> jClass, List<MDFilesTmp> mdFilesTmps,
			String tPrefix){
		JobKey jobKey = null;

		String jobGroup = null;
		String jobName = null;
		String triggerGroup = null;
		String triggerName	= null;
		Hashtable<String, Object> params = null;

		if (mdFilesTmps != null && mdFilesTmps.size()>0){
			for (MDFilesTmp mdFilesTmp: mdFilesTmps){
				jobGroup = "Job_" + mdFilesTmp.getId();
				jobName = mdFilesTmp.getId();
				triggerGroup = tPrefix+"_"+mdFilesTmp.getId();
				try {
					triggerName = scheduler.getSchedulerInstanceId();
					jobKey = new JobKey(jobName, jobGroup);

					if (!scheduler.checkExists(jobKey)){
						log.info("Schedulazione: "+jobName+"/"+jobGroup);
						params = new Hashtable<String,Object>();
						params.put("ID", mdFilesTmp.getId());
						QuartzTools.startJob(scheduler, jClass, jobGroup, jobName, triggerGroup,
								triggerName, params);
					}
				} catch (SchedulerException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartz quartzDeskScheduler = null;
		Integer socketPort = null;
		boolean closeSocket = false;
		boolean processing = false;
		boolean scheduling = false;
		boolean rescheduling = false;

		try {
			
			if (args.length==3){

				MDDemoniQuartz.mdConfiguration = 
					new MDConfiguration(args[1], "file:///"+args[0]);

				if (MDDemoniQuartz.mdConfiguration.getSoftware().getErrorMsg() ==null ||
						MDDemoniQuartz.mdConfiguration.getSoftware().getErrorMsg().length==0){
					
					if (args[2].equalsIgnoreCase("stop")){
						closeSocket = true;
					} else if (args[2].equalsIgnoreCase("start")){
						closeSocket = false;
					} else if (args[2].equalsIgnoreCase("rescheduling")){
						rescheduling = true;
					} else {
						printHelp();
						System.exit(-1);
					}
	
					socketPort = MDDemoniQuartz.mdConfiguration.getSoftwareConfigInteger("socketPort");
					if (socketPort== null){
						socketPort=9000;
					}
					
					try {
						processing = MDDemoniQuartz.mdConfiguration.getSoftwareConfigBoolean("startScheduler");
					} catch (MDConfigurationException e) {
						processing = false;
					}
					
					try {
						scheduling = MDDemoniQuartz.mdConfiguration.getSoftwareConfigBoolean("scheduling");
					} catch (MDConfigurationException e) {
						scheduling = false;
					}
		
					if (processing){
						log.info("Rimango in attesa per l'elaborazione dei Jobs schedulati\n"
								+ "Porta per la chiusura del programma: "+socketPort);
					}
		
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					quartzDeskScheduler = new 
							MDDemoniQuartz(processing, 
									args[0]+"/quartz.properties", 
									socketPort, 
									closeSocket, 
									rescheduling);
					if (!closeSocket){
						if (!rescheduling){
							if (scheduling){
								log.info("Inizio l'attività di Scheduling.\nPorta per la chiusura del programma: "+socketPort);
								quartzDeskScheduler.scheduling();
							}
						} else {
							log.info("Inizio l'attività di Re-Scheduling.\nPorta per la chiusura del programma: "+socketPort);
							quartzDeskScheduler.reScheduling();
							quartzDeskScheduler.scheduler.shutdown();
						}
					}
				} else {
					for (int x=0; x<MDDemoniQuartz.mdConfiguration.getSoftware().getErrorMsg().length; x++){
						log.error(MDDemoniQuartz.mdConfiguration.getSoftware().getErrorMsg(x).getMsgError());
					}
				}

			} else {
				printHelp();
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
		} catch (MDConfigurationException e) {
			log.error(e.getMessage(),e);
		}
	}

	private static void printHelp(){
		System.out.println("E' necessario indicare i seguenti parametri:");
		System.out.println("1) Path files Configurazione");
		System.out.println("2) Codice identificativo del Software");
		System.out.println("3) Azione (start/stop/rescheduling)");
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	@Override
	protected void reScheduling() throws SchedulerException {
		OggettoDigitaleBusiness odb = null;

		List<MDFilesTmp> mdFilesTmps = null;
		MDStatoDAO mdStatoDAO = null;
		
		odb = new OggettoDigitaleBusiness();
		mdStatoDAO = new MDStatoDAO();

			
		try {
			addReScheduler(JGenRegistroIngressi.class, 
					"GenRegistroIngressi");

			addReScheduler(JGenPackagesPremis.class, 
					"GenPackagesPremis");

			addReScheduler(JCodaGeoReplica.class, 
					"CodaGeoReplica");

			addReScheduler(JGeoReplica.class, 
					"GeoReplica");

			mdFilesTmps = null;
			mdFilesTmps = odb.findStatus(null, new MDStato[] {
					mdStatoDAO.FINETRASF(), mdStatoDAO.INITVALID() }, 
					0, mdFilesTmps);
			addReScheduler(JValidate.class, 
					mdFilesTmps,
					"Validate");

			mdFilesTmps = null;
			mdFilesTmps = odb.findStatus(null, new MDStato[] {
					mdStatoDAO.FINEVALID(), mdStatoDAO.INITPUBLISH() }, 
					0, mdFilesTmps);
			addReScheduler(JPublish.class, 
					mdFilesTmps,
					"Publish");

			mdFilesTmps = null;
			mdFilesTmps = odb.findStatus(null, new MDStato[] {
					mdStatoDAO.FINEARCHIVE(), mdStatoDAO.INITINDEX() }, 
					0, mdFilesTmps);
			addReScheduler(JSolrIndex.class, 
					mdFilesTmps,
					"SolrIndex");
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(),e);
		}
	}

	private void addReScheduler(Class<? extends Job> jClass, 
			String tPrefix) {
		JobKey jobKey = null;

		String jobGroup = null;
		String jobName = null;
		String triggerGroup = null;
		String triggerName	= null;
//		Hashtable<String, Object> params = null;
		Trigger newTrigger = null;
		TriggerKey triggerKey = null;

		jobGroup = "Job_" + tPrefix;
		jobName = tPrefix;
		triggerGroup = "Triggert_"+tPrefix;
				
		try {
			triggerName = scheduler.getSchedulerInstanceId();
			jobKey = new JobKey(jobName, jobGroup);
			if (scheduler.checkExists(jobKey)){
				if (!TriggerState.NORMAL.equals(this.getStatoJob(jobGroup, jobName))){
					newTrigger = newTrigger()
							.withIdentity(triggerName, triggerGroup)
							.startNow()
							.build();
					triggerKey = new TriggerKey(triggerName, triggerGroup);
					scheduler.rescheduleJob(triggerKey, newTrigger);
				}
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
		}
	}

	private void addReScheduler(Class<? extends Job> jClass, List<MDFilesTmp> mdFilesTmps,
			String tPrefix) {
		JobKey jobKey = null;

		String jobGroup = null;
		String jobName = null;
		String triggerGroup = null;
		String triggerName	= null;
//		Hashtable<String, Object> params = null;
		Trigger newTrigger = null;
		TriggerKey triggerKey = null;

		if (mdFilesTmps != null && mdFilesTmps.size()>0){
			for (MDFilesTmp mdFilesTmp: mdFilesTmps){
				jobGroup = "Job_" + mdFilesTmp.getId();
				jobName = mdFilesTmp.getId();
				triggerGroup = tPrefix+"_"+mdFilesTmp.getId();
				
				try {
					triggerName = scheduler.getSchedulerInstanceId();
					jobKey = new JobKey(jobName, jobGroup);
					if (scheduler.checkExists(jobKey)){
						if (!TriggerState.NORMAL.equals(this.getStatoJob(jobGroup, jobName))){
							newTrigger = newTrigger()
									.withIdentity(triggerName, triggerGroup)
									.startNow()
									.build();
							triggerKey = new TriggerKey(triggerName, triggerGroup);
							scheduler.rescheduleJob(triggerKey, newTrigger);
						}
//						params = new Hashtable<String,Object>();
//						params.put("ID", mdFilesTmp.getId());
//						QuartzTools.startJob(scheduler, jClass, jobGroup, jobName, triggerGroup,
//								triggerName, params);
					}
				} catch (SchedulerException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
	}

}
