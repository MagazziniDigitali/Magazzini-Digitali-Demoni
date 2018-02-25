/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.quartz.geoReplica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import it.bncf.magazziniDigitali.businessLogic.filesTmp.MDFilesTmpBusiness;
import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.geoReplica.OggettoDigitaleGeoReplica;
import it.bncf.magazziniDigitali.businessLogic.oggettoDigitale.implement.OggettoDigitale;
import it.bncf.magazziniDigitali.configuration.exception.MDConfigurationException;
import it.bncf.magazziniDigitali.database.dao.MDStatoDAO;
import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;
import it.bncf.magazziniDigitali.demoni.batch.MDDemoniQuartzBatch;
import mx.randalf.digest.SHA1;
import mx.randalf.hibernate.FactoryDAO;
import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class JGeoReplica extends JobExecute {

	private Logger log = Logger.getLogger(JGeoReplica.class);

	/**
	 * 
	 */
	public JGeoReplica() {
	}

	private void elabCoda( File coda){
		BufferedReader br = null;
		FileReader fr = null;
		boolean esito = true;
		String line = null;
		String[] st = null;

		try {
			log.info("\n"+"Analizzo il file: " + coda.getAbsolutePath());
			fr = new FileReader(coda);
			br = new BufferedReader(fr);

			while((line = br.readLine())!= null){
				st = line.split("\t");
				if (!isArchive(st[0])){
					if (!execute(st[0])){
						esito = false;
					}
				}
			}
			if (esito){
				writeElab(coda);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(),e);
		} finally{
			try {
				if (br != null){
					br.close();
				}
				if (fr != null){
					fr.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	private boolean isArchive(String objectIdentifierPremis) throws 
			HibernateException, HibernateUtilException{
		MDFilesTmpBusiness mdFileTmp = null;
		MDFilesTmp mdFilesTmp = null;
		boolean esito = false;
		String stato = null;

		try {
			mdFileTmp = new MDFilesTmpBusiness();

			mdFilesTmp = mdFileTmp.findPremis(OggettoDigitale.genPathPremis(objectIdentifierPremis));

			if (mdFilesTmp != null){
				FactoryDAO.initialize(mdFilesTmp.getStato());
				stato = mdFilesTmp.getStato().getId();
				if (stato.equals(MDStatoDAO.FINEARCHIVE) ||
						stato.equals(MDStatoDAO.INITINDEX) ||
						stato.equals(MDStatoDAO.CHECKINDEX) ||
						stato.equals(MDStatoDAO.FINEINDEX) ||
						stato.equals(MDStatoDAO.ERRORINDEX)
						){
					esito = true;
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		return esito;
	}

	private void writeElab(File coda){
		BufferedWriter bw = null;
		FileWriter fw = null;
		SHA1 sha1 = null;
		try {
			fw = new FileWriter(coda.getAbsolutePath()+".elab");
			bw = new BufferedWriter(fw);
			sha1 = new SHA1(coda);
			bw.write(sha1.getDigest());
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		} finally {
			try {
				if (bw != null){
					bw.flush();
					bw.close();
				}
				if (fw != null){
					fw.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
	}
	
	private boolean execute(String id) throws HibernateUtilException
	{
		OggettoDigitaleGeoReplica odgr = null;
		boolean esito = false;

		try {
			log.debug("\n"+"Eseguo la Geo replica ID: "+id);
			odgr = new OggettoDigitaleGeoReplica(log,"GeoReplica");
			esito = odgr.esegui(id, 
//					"GeoReplica", 
					MDDemoniQuartzBatch.mdConfiguration
					);
		} catch (HibernateUtilException e) {
			throw e;
		} finally {
			log.debug("\n"+"Fine della Geo replica ID: "+id);
		}
		return esito;
	}

	@Override
	protected String jobExecute(JobExecutionContext context) throws JobExecutionException {
		File pCoda = null;
		String result = null;

		try {
			pCoda = new File(
					MDDemoniQuartzBatch.
						mdConfiguration.getSoftwareConfigString("coda.path"));
			if (pCoda.exists()){
				
				scanFolder(pCoda);
				
			}
			result = "Geo replica terminata correttamente";
		} catch (MDConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private void scanFolder(File pCoda) {
		File[] codas = null;

		codas = pCoda.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				boolean ris = false;
				File fElab = null;
				if (!pathname.getName().startsWith("._")) {
					if (pathname.isFile()){
						if (pathname.getName().toLowerCase().endsWith(".coda")){
							fElab = new File(pathname.getAbsolutePath()+".elab");
							if (!fElab.exists()){
								ris = true;
							}
						}
					} else if (pathname.isDirectory()) {
						ris = true;
					}
				}
				return ris;
			}
		});

		Arrays.sort(codas);
		for (File coda: codas){
			if (coda.isDirectory()) {
				scanFolder(coda);
			} else {
				elabCoda(coda);
			}
		}
	}

}
