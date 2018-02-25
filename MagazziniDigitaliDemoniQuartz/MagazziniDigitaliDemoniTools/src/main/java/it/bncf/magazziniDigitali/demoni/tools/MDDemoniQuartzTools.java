/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.tools;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;


import org.quartz.TriggerKey;

import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.quartz.QuartzScheduler;
import mx.randalf.quartz.QuartzTools;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public abstract class MDDemoniQuartzTools extends QuartzScheduler {

	private Logger log = Logger.getLogger(MDDemoniQuartzTools.class);
	/**
	 * @param processiong
	 * @param fileQuartz
	 * @param socketPort
	 * @param closeSocket
	 * @throws SchedulerException
	 */
	public MDDemoniQuartzTools(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket, boolean reScheduling)
			throws SchedulerException {
		super(processing, fileQuartz, socketPort, closeSocket, reScheduling);
	}

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	public abstract void scheduling() throws SchedulerException;

	/**
	 * @see mx.randalf.quartz.QuartzScheduler#scheduling()
	 */
	public abstract void reScheduling() throws SchedulerException;

	protected void addScheduler(Class<? extends JobExecute> jClass, String tPrefix, ScheduleBuilder<?> schedBuilder,
			String cPrefix, Hashtable<String, Object> params) throws ConfigurationException {
		addScheduler(jClass, tPrefix, schedBuilder, cPrefix, params, false);
	}

	protected void addScheduler(Class<? extends JobExecute> jClass, String tPrefix, ScheduleBuilder<?> schedBuilder,
			String cPrefix, Hashtable<String, Object> params, boolean reSchedulerJobComplete) throws ConfigurationException {
		if (Configuration.getValueDefault(cPrefix+".enable", "true").equalsIgnoreCase("true")){
			if (Configuration.getValueDefault(cPrefix+".test", "false").equalsIgnoreCase("true")){
				addScheduler(jClass, tPrefix+"Test", null, params, reSchedulerJobComplete);
			} else {
				addScheduler(jClass, tPrefix, schedBuilder, params, reSchedulerJobComplete);
			}
		}
	}

	protected void addScheduler(Class<? extends JobExecute> jClass, 
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
				} else if (triggerState.equals(TriggerState.NONE)) {
					scheduler.deleteJob(jobKey);
					QuartzTools.startJob(scheduler, jClass, jobGroup, jobName, triggerGroup,
							triggerName, params, schedBuilder);
				}
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
		}
	}

	protected void addScheduler(Class<? extends JobExecute> jClass, List<MDFilesTmp> mdFilesTmps,
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
						log.info("\n"+"Schedulazione: "+jobName+"/"+jobGroup);
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
	
	protected void addReScheduler(Class<? extends Job> jClass, 
			String tPrefix) {
		JobKey jobKey = null;

		String jobGroup = null;
		String jobName = null;
		String triggerGroup = null;
		String triggerName	= null;
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

	protected void addReScheduler(Class<? extends Job> jClass, List<MDFilesTmp> mdFilesTmps,
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
