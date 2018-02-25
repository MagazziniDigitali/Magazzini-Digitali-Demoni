/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.businessLogic.HashTable;
import it.bncf.magazziniDigitali.businessLogic.filesTmp.MDFilesTmpBusiness;
import it.bncf.magazziniDigitali.database.dao.MDStatoDAO;
import it.bncf.magazziniDigitali.database.entity.MDFilesTmp;

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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.digest.SHA1;
import mx.randalf.hibernate.FactoryDAO;

/**
 * @author massi
 *
 */
public class GeoReplicaProces extends Thread {

	private Logger log = Logger.getLogger(getClass());

	private boolean testMode = false;

	private HibernateTemplate hibernateTemplate;

	private Hashtable<String, ExecuteProcer> lProcess = null;

	private String fileConf = null;
	/**

	 * @param group
	 * @param name
	 */
	public GeoReplicaProces(ThreadGroup group, String name, boolean testMode, 
			HibernateTemplate hibernateTemplate, String fileConf) {
		super(group, name);
		this.testMode = testMode;
		this.hibernateTemplate = hibernateTemplate;
		this.fileConf = fileConf;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		File pCoda = null;
		File[] codas = null;

		try {
			pCoda = new File(Configuration.getValue("demoni.Coda.path"));
			if (pCoda.exists()){
				codas = pCoda.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {
						boolean ris = false;
						File fElab = null;
						if (pathname.isFile()){
							if (pathname.getName().toLowerCase().endsWith(".coda")){
								fElab = new File(pathname.getAbsolutePath()+".elab");
								if (!fElab.exists()){
									ris = true;
								}
							}
						}
						return ris;
					}
				});

				Arrays.sort(codas);
				for (File coda: codas){
					elabCoda(coda);
					if (testMode){
						break;
					}
				}
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
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
					esito = checkProces(esito, false);
					execute(st[0]);
					if (testMode){
						break;
					}
				}
			}
			esito = checkProces(esito, true);
			if (esito && ! testMode){
				writeElab(coda);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
		} catch (NamingException e) {
			log.error(e.getMessage(),e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(),e);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(),e);
		} catch (InterruptedException e) {
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

	private synchronized boolean checkProces(boolean esito, boolean closeAll) throws NumberFormatException, 
			ConfigurationException, InterruptedException, HibernateException, NamingException{
		int conta= 0;
		Enumeration<String> keys = null;
		String key = null;
		int numThread=1;

		try {
			if (lProcess!= null){
				if (Configuration.getValue("demoni.GeoReplica.numberThread") != null){
					numThread = Integer.valueOf(Configuration.getValue("demoni.GeoReplica.numberThread"));
				}
				while(true){
					conta =0;
					keys =lProcess.keys();
					while(keys.hasMoreElements()){
						key = keys.nextElement();
						if (!lProcess.get(key).isAlive()){
							if (!isArchive(key)){
								esito=false;	
							}
							lProcess.remove(key);
						} else {
							conta++;
						}
					}
					if (closeAll){
						if (conta==0){
							break;
						} else {
							Thread.sleep(5000);
						}
					} else {
						if (conta<numThread){
							break;
						} else {
							Thread.sleep(5000);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} catch (NamingException e) {
			throw e;
		}
		return esito;
	}

	private boolean isArchive(String objectIdentifierPremis) throws 
			HibernateException, NamingException, ConfigurationException{
		MDFilesTmpBusiness mdFileTmp = null;
		MDFilesTmp mdFilesTmp = null;
		boolean esito = false;
		String stato = null;

		try {
			mdFileTmp = new MDFilesTmpBusiness(hibernateTemplate);

			mdFilesTmp = mdFileTmp.findPremis(objectIdentifierPremis);

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
		} catch (NamingException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		}
		return esito;
	}

	private void execute(String objectIdentifierPremis) throws 
			IOException, ConfigurationException{
		ExecuteProcer executeProc = null;
		Vector<String> command = null;

		try {
			if (lProcess ==null){
				lProcess = new HashTable<String, ExecuteProcer>();
			}
			command=new Vector<String>();
			command.add("GeoReplica");
			command.add(objectIdentifierPremis);
			executeProc = new ExecuteProcer(command, "GeoReplica", objectIdentifierPremis, fileConf);
			executeProc.start();
			lProcess.put(objectIdentifierPremis, executeProc);
		} catch (IOException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		}
	}

	private void writeElab(File coda){
		BufferedWriter bw = null;
		FileWriter fw = null;
		SHA1 sha1 = null;
		try {
			fw = new FileWriter(coda.getAbsolutePath()+".elab");
			bw = new BufferedWriter(fw);
			sha1 = new SHA1();
			bw.write(sha1.getDigest(coda));
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
}
