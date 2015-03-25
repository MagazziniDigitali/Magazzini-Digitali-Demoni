/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazziniDigitali.demoni.MDDemoni;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
class SocketServer extends Thread {
	private MDDemoniController controller = null;
	
	private Logger log = Logger.getLogger(SocketServer.class);

	public SocketServer(MDDemoniController controller){
		this.controller = controller;
	}
	
	public void run(){
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
//		PrintWriter out = null;
		BufferedReader in = null;
		String line=null;

		try {
			serverSocket = new ServerSocket(MDDemoni.portSocket);
			
			while(true){
				clientSocket = serverSocket.accept();
//				out =
//				    new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				line = in.readLine();
				
				if (in != null){
					in.close();
				}
				if (clientSocket != null){
					clientSocket.close();
				}

				if (line.equalsIgnoreCase("stop")){
					controller.setRunning(false);
					break;
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
//				if (out != null){
//					out.close();
//				}
				if (serverSocket != null){
					serverSocket.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
