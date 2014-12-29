/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.OggettoDigitaleBusiness;
import it.bncf.magazziniDigitali.database.dao.MDStatoDAO;
import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;
import it.bncf.magazziniDigitali.database.entity.MDStato;

import java.io.IOException;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.FactoryDAO;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * @author massi
 *
 */
public class MDDemoniController extends MDDemoniThred {

	private boolean validate = false;
	private boolean publish = false;
	private boolean coda = false;
	private boolean geoReplica = false;
	private boolean solrIndex = false;
	private Date lastCoda = null;
	private SocketServer socket = null;
	private ExecuteProcer pCoda = null;
	private String fileConf = null;
	private GeoReplicaProces pGeoReplica = null;

	private Hashtable<String, Hashtable<String,ExecuteProcer>> lProcess = null;
	
	private Logger log =Logger.getLogger(MDDemoniController.class);

	/**
	 * @param target
	 * @param name
	 * @throws ConfigurationException 
	 * @throws SocketException 
	 */
	public MDDemoniController(Runnable target, String name, String fileConf) throws ConfigurationException, SocketException {
		super(target, name, true);
		try {
			if (Configuration.getValue("demoni.Validate") != null){
				validate = Configuration.getValue("demoni.Validate").equalsIgnoreCase("true");
			}else {
				validate = false;
			}
			if (Configuration.getValue("demoni.Publish") != null){
				publish = Configuration.getValue("demoni.Publish").equalsIgnoreCase("true");
			}else {
				publish = false;
			}
			if (Configuration.getValue("demoni.Coda") != null){
				coda = Configuration.getValue("demoni.Coda").equalsIgnoreCase("true");
			}else {
				coda = false;
			}
			if (Configuration.getValue("demoni.GeoReplica") != null){
				geoReplica = Configuration.getValue("demoni.GeoReplica").equalsIgnoreCase("true");
			}else {
				geoReplica = false;
			}
			if (Configuration.getValue("demoni.SolrIndex") != null){
			solrIndex = Configuration.getValue("demoni.SolrIndex").equalsIgnoreCase("true");
			}else {
				solrIndex = false;
			}

			this.fileConf = fileConf;
			socket = new SocketServer(this);
			socket.start();
		} catch (ConfigurationException e) {
			throw e;
		}
	}

	/**
	 * @see it.bncf.magazziniDigitali.demoni.thread.MDDemoniThred#execute()
	 */
	@Override
	protected void execute() {
		List<MDFilesTmp> elab = null;
		OggettoDigitaleBusiness odb = null;
		MDStatoDAO mdStatoDAO = null;
		int numRec = 1;
		Date myDate = null;
		GregorianCalendar gc = null;
		Vector<String> command = null;
		MDFilesTmp mdFileTmp = null;
		Enumeration<String> keys = null;
		String key = null;

		try {
			log.info("Inizio esecuzione processo per il Controller");
			odb = new OggettoDigitaleBusiness(null);
			mdStatoDAO = new MDStatoDAO(null);
			if (validate){
				numRec = 1;
				if (Configuration.getValue("demoni.Validate.numberThread")!= null){
					numRec = Integer.parseInt(Configuration.getValue("demoni.Validate.numberThread"));
				}
				elab = odb.findStatus(null, new MDStato[] {
						mdStatoDAO.FINETRASF(), mdStatoDAO.INITVALID() }, 
						numRec, elab);
			}
			if (publish){
				numRec = 1;
				if (Configuration.getValue("demoni.Publish.numberThread")!= null){
					numRec = Integer.parseInt(Configuration.getValue("demoni.Publish.numberThread"));
				}
				elab = odb.findStatus(null, new MDStato[] {
						mdStatoDAO.FINEVALID(), mdStatoDAO.INITPUBLISH() }, 
						numRec, elab);
			}
			if (solrIndex){
				numRec = 10;
				if (Configuration.getValue("demoni.SolrIndex.numberSql")!= null){
					numRec = Integer.parseInt(Configuration.getValue("demoni.SolrIndex.numberSql"));
				}
				elab = odb.findStatus(null, new MDStato[] {
						mdStatoDAO.FINEARCHIVE(), mdStatoDAO.INITINDEX()}, 
						numRec, elab);
			}

			gc = new GregorianCalendar();
			gc.set(Calendar.HOUR_OF_DAY, 0);
			gc.set(Calendar.MINUTE, 0);
			gc.set(Calendar.SECOND, 0);
			gc.set(Calendar.MILLISECOND, 0);
			myDate = new Date();
			myDate.setTime(gc.getTimeInMillis());
			if (pCoda != null){
				if (!pCoda.isAlive()){
					log.info("Generazione Code terminata");
					pCoda = null;
				}
			}
			if (coda && pCoda ==null && (lastCoda == null || lastCoda.before(myDate))){
				log.info("Eseguo la generazione delle Code");
				lastCoda = myDate;
				command=new Vector<String>();
				command.add("Coda");
				pCoda = new ExecuteProcer(command, "Coda", fileConf);
				pCoda.start();
			}
			if (pGeoReplica != null){
				if (!pGeoReplica.isAlive()){
					log.info("Geo Replica terminata");
					pGeoReplica = null;
				}
			}
			if (geoReplica && pGeoReplica ==null){
				log.info("Eseguo una geo Replica");
				pGeoReplica = new GeoReplicaProces(this.getThreadGroup(), "pGeoReplica", testMode, null, fileConf);
				pGeoReplica.start();
			}
			if (elab != null){
				for (int x=0; x<elab.size(); x++){
					mdFileTmp = elab.get(x);
					FactoryDAO.initialize(mdFileTmp.getStato());
					if (mdFileTmp.getStato().getId().equals(MDStatoDAO.FINEARCHIVE) ||
							mdFileTmp.getStato().getId().equals(MDStatoDAO.INITINDEX)){
						checkProces("SolrIndex", false);
						execute("SolrIndex", mdFileTmp.getId());
					}
				}
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} finally{
			log.info("Fine esecuzione processo per il Controller");
			if (lProcess != null){
				keys = lProcess.keys();
				while(keys.hasMoreElements()){
					try {
						key = keys.nextElement();
						checkProces(key, true);
					} catch (NumberFormatException e) {
						log.error(e.getMessage(), e);
					} catch (HibernateException e) {
						log.error(e.getMessage(), e);
					} catch (ConfigurationException e) {
						log.error(e.getMessage(), e);
					} catch (InterruptedException e) {
						log.error(e.getMessage(), e);
					} catch (NamingException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}

	}

	/**
	 * Metodo utilizzato per testare lo stao dei processi
	 * 
	 * @param application
	 * @param closeAll
	 * @throws NumberFormatException
	 * @throws ConfigurationException
	 * @throws InterruptedException
	 * @throws HibernateException
	 * @throws NamingException
	 */
	private synchronized void checkProces(String application, boolean closeAll) throws NumberFormatException, 
			ConfigurationException, InterruptedException, HibernateException, NamingException{
		int conta= 0;
		Enumeration<String> keys = null;
		String key = null;
		int numThread=1;
		Hashtable<String, ExecuteProcer> lProcess =null;

		try {
			if (this.lProcess == null){
				this.lProcess = new Hashtable<String, Hashtable<String,ExecuteProcer>>();
			}
			if (this.lProcess.get(application)!= null){
				lProcess = this.lProcess.get(application);
			}
			if (lProcess!= null){
				if (Configuration.getValue("demoni."+application+".numberThread") != null){
					numThread = Integer.valueOf(Configuration.getValue("demoni."+application+".numberThread"));
				}
				System.out.println("numThread: "+numThread);
				while(true){
					conta =0;
					keys =lProcess.keys();
					while(keys.hasMoreElements()){
						key = keys.nextElement();
						if (!lProcess.get(key).isAlive()){
							lProcess.remove(key);
						} else {
							conta++;
						}
					}
					if (closeAll){
						if (conta==0){
							break;
						} else {
							System.out.println("Rimango in attesa");
							Thread.sleep(5000);
						}
					} else {
						System.out.println("conta: "+conta);
						if (conta<numThread){
							System.out.println("Esco ");
							break;
						} else {
							System.out.println("Rimango in attesa");
							Thread.sleep(5000);
						}
					}
				}
				System.out.println("Ci sono posti disponibili per i processi");
			}
		} catch (NumberFormatException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} finally {
			if (lProcess != null && lProcess.size()>0){
				this.lProcess.put(application, lProcess);
			} else {
				this.lProcess.remove(application);
			}
		}
	}

	/**
	 * Meteodo utilizzato per l'esecuzione dell'applicazione esterna
	 * 
	 * @param application
	 * @param objectIdentifierPremis
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	private void execute(String application, String objectIdentifierPremis) throws 
			IOException, ConfigurationException{
		ExecuteProcer executeProc = null;
		Vector<String> command = null;
		Hashtable<String, ExecuteProcer> lProcess = null;

		try {
			if (this.lProcess ==null){
				this.lProcess = new Hashtable<String, Hashtable<String, ExecuteProcer>>();
			}
			command=new Vector<String>();
			command.add(application);
			command.add(objectIdentifierPremis);
			executeProc = new ExecuteProcer(command, application, fileConf);
			executeProc.start();
			lProcess = new Hashtable<String, ExecuteProcer>();
			lProcess.put(objectIdentifierPremis, executeProc);
			this.lProcess.put(application, lProcess);
		} catch (IOException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		}
	}

}
