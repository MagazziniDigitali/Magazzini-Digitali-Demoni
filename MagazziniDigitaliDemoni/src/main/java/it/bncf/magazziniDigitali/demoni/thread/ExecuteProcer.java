/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.Vector;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
class ExecuteProcer extends Thread {
    private Process process;
    private Integer exit;
    private String name;
    private String id;
    private GregorianCalendar dStart;
    private GregorianCalendar dStop;
    private boolean esito = false;

    private Logger log = Logger.getLogger(ExecuteProcer.class);

	public ExecuteProcer(Vector<String> command, String name, String id, String fileConf) throws IOException, ConfigurationException {
    	File dir = null;
    	String[] comm = null;
    	Vector<String> commands = null;
        try {
        	dStart = new GregorianCalendar();
			dir = new File(Configuration.getValue("demoni.pathApplication"));
			comm = Configuration.getValue("demoni.javaCommand").split(" ");
			commands = new Vector<String>();
			for (int x=0; x<comm.length; x++){
				commands.add(comm[x]);
			}
			commands.add(fileConf);
			commands.addAll(command);
			this.process = Runtime.getRuntime().exec(commands.toArray(new String[commands.size()]), null, dir);
			this.name = name;
			this.id = id;
		} catch (IOException e) {
			log.error("Name: "+name+"\tId: "+id+"\n"+e.getMessage(),e);
			throw e;
		} catch (ConfigurationException e) {
			log.error("Name: "+name+"\tId: "+id+"\n"+e.getMessage(),e);
			throw e;
		}
    }

    public void run() {
    	BufferedReader errReader  = null;
    	BufferedReader inputReader = null;
    	String val = null;
    	String input = "";
    	String error = "";

    	try {
        	errReader = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));

            inputReader = new BufferedReader(new InputStreamReader(
            		process.getInputStream()));

			while ((val = inputReader.readLine()) != null) {
				input += (input.equals("")?"Name: "+name+"\tId: "+id+"\n":"\n")+val;
			}
			if (!input.equals("")){
				log.debug("\n"+input);
			}

			while ((val = errReader.readLine()) != null) {
				if (!val.trim().startsWith("Warning:") &&
						!val.trim().startsWith("Avvertenze del compilatore:") &&
						!val.trim().startsWith("WARNING:")){
					error += (error.equals("")?"Name: "+name+"\tId: "+id+"\n":"\n")+val;
				}
			}
			if (!error.equals("")){
				log.error("\n"+error);
			}

            exit = process.waitFor();
            
            if (exit==0){
            	esito=true;
            	log.debug("\n"+"Name: "+name+"\tId: "+id+"\n"+"Esito positivo");
            } else {
            	log.error("\n"+"Name: "+name+"\tId: "+id+"\n"+"Esito Negattivo ["+exit+"]");
            }
            dStop = new GregorianCalendar();
        } catch (InterruptedException ignore) {
        	log.error(name+" "+ignore.getMessage(), ignore);
        } catch (IOException e) {
        	log.error(name+" "+e.getMessage(), e);
		} finally {
        	if (process != null){
        		process.destroy();
        	}
        }
    }

    public long timeIsAlive(){
    	GregorianCalendar gc = null;
    	
    	gc = new GregorianCalendar();
    	return gc.getTimeInMillis()-dStart.getTimeInMillis();
    }

	/**
	 * @return the dStart
	 */
	public GregorianCalendar getdStart() {
		return dStart;
	}

	/**
	 * @return the dStop
	 */
	public GregorianCalendar getdStop() {
		return dStop;
	}

	/**
	 * @return the esito
	 */
	public boolean isEsito() {
		return esito;
	}
}
