/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @author massi
 *
 */
public class QuartzTools {

	/**
	 * 
	 */
	public QuartzTools() {
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends Job> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params) 
			throws SchedulerException{
		return startJob(scheduler, jClass, jobGroup, jobName, triggerGroup, triggerName, params, null);
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends Job> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params,
			ScheduleBuilder<?> schedBuilder) 
			throws SchedulerException{
		JobDetail job = null;
		Trigger trigger = null;
		Set<Trigger> triggers = null;
		Enumeration<String> keys = null;
		String key = null;

		try {
			job = newJob(jClass).
					withIdentity(jobName, jobGroup)
					
					.build();

			if (params != null){
				keys = params.keys();
				while (keys.hasMoreElements()){
					key = keys.nextElement();
					job.getJobDataMap().put(key, params.get(key));
				}
			}

			if (schedBuilder != null){
				trigger = newTrigger()
						.withIdentity(triggerName, triggerGroup)
						.withSchedule(schedBuilder)
						.startNow()
						.build();
			} else {
				trigger = newTrigger()
						.withIdentity(triggerName, triggerGroup)
						.startNow()
						.build();
				
			}
			triggers = new HashSet<Trigger>();
			triggers.add(trigger);
			scheduler.scheduleJob(job, triggers, false);
		} catch (SchedulerException e) {
			throw e;
		}
		return job.getKey();
	}

	public static boolean checkJob(JobExecutionContext context) throws SchedulerException{
		List<JobExecutionContext> jobs = null;
		int conta = 0;
		boolean result = false;

		try {
			if (context.getScheduler().isShutdown()){
				result = false;
			} else if (context.getScheduler().isStarted()){
				jobs = context.getScheduler().getCurrentlyExecutingJobs();
	//			System.out.println("\t Jobs: "+jobs.size());
				if (jobs != null && jobs.size()>0){
					for(JobExecutionContext job : jobs){
						if (job.getJobDetail().getKey().getGroup().equals(context.getJobDetail().getKey().getGroup()) &&
								job.getJobDetail().getKey().getName().equals(context.getJobDetail().getKey().getName()) &&
								job.getTrigger().getKey().getGroup().equals(context.getTrigger().getKey().getGroup()) &&
								job.getTrigger().getKey().getName().equals(context.getTrigger().getKey().getName()) ){
							conta++;
						}
					}
				}
				result = conta<=1;
			}
		} catch (SchedulerException e) {
			throw e;
		}
//		System.out.println("conta: "+conta);
		return result;
	}

}
