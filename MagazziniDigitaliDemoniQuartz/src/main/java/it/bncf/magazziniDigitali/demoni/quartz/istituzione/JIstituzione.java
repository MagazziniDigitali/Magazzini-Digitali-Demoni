/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.istituzione;

import it.bncf.magazziniDigitali.demoni.quartz.QuartzTools;
import it.bncf.magazziniDigitali.demoni.quartz.istituzione.validate.JValidate;

import java.util.Hashtable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

/**
 * @author massi
 *
 */
public class JIstituzione implements Job {
	
	public static String IDISTITUTO= "idIstituto";

	/**
	 * 
	 */
	public JIstituzione() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobKey validate = null;
		JobKey publish = null;
		JobKey coda = null;
		JobKey geoReplica = null;
		JobKey genSolr = null;
		JobKey checkSolr = null;

		Hashtable<String, Object> params = null;

		try {
			if (QuartzTools.checkJob(context)){
				System.out.println("JIstituto - Start");
				params = new Hashtable<String, Object>();
				params.put(IDISTITUTO, context.getJobDetail().getJobDataMap().getString(JIstituzione.IDISTITUTO));
				validate = QuartzTools.startJob(context.getScheduler(), 
						JValidate.class, 
						"Validate", 
						context.getJobDetail().getKey().getName(), 
						"Validate", 
						context.getTrigger().getKey().getName(),
						params);
				while (true){
					Thread.sleep(10000);
					if (!context.getScheduler().checkExists(validate)){
						break;
					}
				}
				System.out.println("JIstituto - End");
			} else {
				throw new JobExecutionException("Job.group: "+context.getJobDetail().getKey().getGroup()+
						"\t Job.name: "+context.getJobDetail().getKey().getName()+
						"\t Trigger.group: "+context.getTrigger().getKey().getGroup()+
						"\t Trigger.name: "+context.getTrigger().getKey().getName()+
						"\t In esecuzione: ",false);
			}
		} catch (JobExecutionException e) {
			throw e;
		} catch (SchedulerException e) {
			throw new JobExecutionException(e.getMessage(),e, false);
		} catch (InterruptedException e) {
			throw new JobExecutionException(e.getMessage(),e, false);
		}
	}

}
