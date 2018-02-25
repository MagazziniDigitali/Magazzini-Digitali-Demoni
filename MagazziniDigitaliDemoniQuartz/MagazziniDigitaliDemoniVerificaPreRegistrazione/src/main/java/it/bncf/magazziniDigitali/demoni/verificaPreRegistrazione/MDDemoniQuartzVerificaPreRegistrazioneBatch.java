/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.verificaPreRegistrazione;

import org.quartz.SchedulerException;

import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;

/**
 * @author massi
 *
 */
public class MDDemoniQuartzVerificaPreRegistrazioneBatch extends MDDemoniQuartzBatch<MDDemoniQuartz> {

	public MDDemoniQuartzVerificaPreRegistrazioneBatch() {
		super("VerificaPreRegistrazione");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoniQuartzVerificaPreRegistrazioneBatch mdDemoniQuartzBatch = null;
		if (args.length==2){
			mdDemoniQuartzBatch = new MDDemoniQuartzVerificaPreRegistrazioneBatch();
			mdDemoniQuartzBatch.esegui(args);
		} else {
			printHelp();
		}
	}

	@Override
	protected MDDemoniQuartz initScheduler(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket,
			boolean reScheduling) throws SchedulerException {
		return new MDDemoniQuartz(processing, fileQuartz, socketPort, closeSocket, reScheduling);
	}

}
