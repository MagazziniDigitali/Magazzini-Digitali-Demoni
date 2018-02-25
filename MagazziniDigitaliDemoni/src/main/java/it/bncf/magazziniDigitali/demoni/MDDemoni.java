/**
 * 
 */
package it.bncf.magazziniDigitali.demoni;

import it.bncf.magazziniDigitali.demoni.exception.MDDemoniException;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniCoda;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniController;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniGeoReplica;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniPublish;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniSolrIndex;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniThred;
import it.bncf.magazziniDigitali.demoni.thread.MDDemoniValidate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public class MDDemoni {

	private static Logger log = Logger.getLogger(MDDemoni.class);

	private MDDemoniThred process = null;
	
	public static int portSocket=11000;
//	private MDDemoniCoda coda = null;
//
//	private MDDemoniValidate validate = null;
//
//	private MDDemoniPublish publish = null;
	
	/**
	 * 
	 */
	public MDDemoni() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MDDemoni demoni = null;
		try {
			if (args.length>=1){
				
				demoni = new MDDemoni();
				demoni.start(args);
			} else {
				System.out.println("Per eseguire questo programma sono necessari i seguenti parametri:");
				System.out.println("1) Path dei file di properties");
				System.out.println("2) Indicare il tipo di Operazione (Validate, Publish) ");
				System.out.println("3) --test (Opzionale) per eseguire l'applicazione in modalità Test");
			}
		} catch (MDDemoniException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void start(String[] args) throws MDDemoniException{
		String pathProperties = null;
		String testMode = null;
		String operation = null;
		Vector<String> params = null;
		try {
			log.info("\n"+"Start Demoni");
			for (int x=0;x<args.length; x++){
				if (pathProperties==null){
					pathProperties = args[x];
				} else {
					if (args[x].trim().equalsIgnoreCase("--test")){
						testMode = args[x].trim().toLowerCase();
					} else if (operation == null) {
						operation = args[x];
					} else {
						if (params == null){
							params = new Vector<String>();
						}
						params.add(args[x]);
					}
				}
			}
			if (testMode != null && testMode.equals("--test")){
				System.out.println("Applicazione in modalità Test");
			}
			
			Configuration.init(pathProperties);

			if (operation == null || operation.equalsIgnoreCase("Start")){
				if (!available(portSocket)){
					throw new SocketException("Il servizio risultà già attivo");
				}
				process = new MDDemoniController(Thread.currentThread(), "Controller", pathProperties);
				process.setTestMode((testMode != null && testMode.equals("--test")));
				process.start();
				while (true){
					if (process != null &&
						process.isAlive()) {
						Thread.sleep(10000);
					} else {
						break;
					}
				}
			} else if (operation.equalsIgnoreCase("Stop")){
				if (available(portSocket)){
					throw new SocketException("Il controller non risulta attivo");
				}
				clientSocket("Stop");
//					process = new MDDemoniController(Thread.currentThread(), "Controller");
			} else {
				if (available(portSocket) && ! testMode.equals("--test")){
					throw new SocketException("Il controller non risulta attivo");
				}
				if (operation.equalsIgnoreCase("Validate")){
					if (!startValidate(params, operation)){
						throw new SocketException("Riscontrato un problema nella Validate");
					}
				} else if (operation.equalsIgnoreCase("Publish")){
					if (!startPublish(params, operation)){
						throw new SocketException("Riscontrato un problema nella Publish");
					}
				} else if (operation.equalsIgnoreCase("GeoReplica")){
					if (!startGeoReplica(params, operation)){
						throw new SocketException("Riscontrato un problema nella GeoRepplica");
					}
				} else if (operation.equalsIgnoreCase("SolrIndex")){
					if (!startSolrIndex(params, operation)){
						throw new SocketException("Riscontrato un problema nella SolrIndex");
					}
				} else {
					if (Configuration.getValue("demoni."+operation) != null &&
						Configuration.getValue("demoni."+operation).equalsIgnoreCase("true")){
						if (operation.equalsIgnoreCase("Coda")){
							process =new MDDemoniCoda(Thread.currentThread(), operation);
						}
						if (testMode != null && testMode.equals("--test")){
							process.setTestMode(true);
							process.run();
						} else {
							process.start();
						}
						while (true){
							if (process != null &&
								process.isAlive()) {
								Thread.sleep(10000);
							} else {
								break;
							}
						}

					}
				}
			}
			if (testMode != null && testMode.equals("--test")){
				System.out.println("Fine Test");
			}
			log.info("\n"+"Stop Demoni");
		} catch (ConfigurationException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (SocketException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (UnknownHostException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		} catch (IOException e) {
			throw new  MDDemoniException(e.getMessage(), e);
		}
	}

	private boolean startSolrIndex(Vector<String> params, String operation) 
			throws ConfigurationException, MDDemoniException{
		MDDemoniSolrIndex mdSolrIndex = null;
		boolean esito = false;
		
		try {
			if (operation.equals("SolrIndex") &&
					Configuration.getValue("demoni."+operation) != null &&
					Configuration.getValue("demoni."+operation).equalsIgnoreCase("true")){
				esito = true;
				mdSolrIndex = new MDDemoniSolrIndex();
				mdSolrIndex.execute(params, operation);
				
			}
		} catch (ConfigurationException e) {
			throw e;
		} catch (MDDemoniException e) {
			throw e;
		}
		return esito;
	}

	private boolean startValidate(Vector<String> params, String operation) 
			throws ConfigurationException, MDDemoniException{
		MDDemoniValidate mdValidate = null;
		boolean esito = false;
		try {
			if (operation.equals("Validate") &&
					Configuration.getValue("demoni."+operation) != null &&
					Configuration.getValue("demoni."+operation).equalsIgnoreCase("true")){
				esito = true;
				mdValidate = new MDDemoniValidate();
				mdValidate.execute(params, operation);
				
			}
		} catch (ConfigurationException e) {
			throw e;
		} catch (MDDemoniException e) {
			throw e;
		}
		return esito;
	}

	private boolean startPublish(Vector<String> params, String operation) 
			throws ConfigurationException, MDDemoniException{
		MDDemoniPublish mdPublish = null;
		boolean esito = false;
		try {
			if (operation.equals("Publish") &&
					Configuration.getValue("demoni."+operation) != null &&
					Configuration.getValue("demoni."+operation).equalsIgnoreCase("true")){
				esito = true;
				mdPublish = new MDDemoniPublish();
				mdPublish.execute(params, operation);
				
			}
		} catch (ConfigurationException e) {
			throw e;
		} catch (MDDemoniException e) {
			throw e;
		}
		return esito;
	}

	private boolean startGeoReplica(Vector<String> params, String operation) 
			throws ConfigurationException, MDDemoniException{
		MDDemoniGeoReplica mdGeoReplica = null;
		boolean esito = false;
		try {
			if (operation.equals("GeoReplica") &&
					Configuration.getValue("demoni."+operation) != null &&
					Configuration.getValue("demoni."+operation).equalsIgnoreCase("true")){
				esito = true;
				mdGeoReplica = new MDDemoniGeoReplica();
				mdGeoReplica.execute(params, operation);
				
			}
		} catch (ConfigurationException e) {
			throw e;
		} catch (MDDemoniException e) {
			throw e;
		}
		return esito;
	}
	
	private boolean available(int port) {

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    boolean ris = false;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        ris = true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return ris;
	}

	private void clientSocket(String msg) throws UnknownHostException, SocketException, IOException{
		Socket echoSocket = null;
		PrintWriter out = null;

		try {
			echoSocket = new Socket("127.0.0.1", MDDemoni.portSocket);
			echoSocket.setKeepAlive(true);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			out.print(msg);
		} catch (UnknownHostException e) {
			throw e;
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null){
					out.close();
				}
				if (echoSocket != null){
					echoSocket.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}
}
