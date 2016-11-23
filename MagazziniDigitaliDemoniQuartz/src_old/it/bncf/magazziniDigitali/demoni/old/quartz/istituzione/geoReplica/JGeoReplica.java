/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.istituzione.geoReplica;

import it.bncf.magazziniDigitali.demoni.quartz.istituzione.JIstituzione;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author massi
 *
 */
public class JGeoReplica implements Job {

	/**
	 * 
	 */
	public JGeoReplica() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("JValidate - Start");
		for (int x=0; x<10; x++){
			try {
				System.out.print(x+" - 10");
				System.out.print("\t Job.group: "+context.getJobDetail().getKey().getGroup());
				System.out.print("\t Job.name: "+context.getJobDetail().getKey().getName());
				System.out.print("\t Trigger.group: "+context.getTrigger().getKey().getGroup());
				System.out.print("\t Trigger.name: "+context.getTrigger().getKey().getName());
				System.out.println("\t "+JIstituzione.IDISTITUTO+": "+context.getJobDetail().getJobDataMap().getString(JIstituzione.IDISTITUTO));
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("JValidate - End");
	}

}
