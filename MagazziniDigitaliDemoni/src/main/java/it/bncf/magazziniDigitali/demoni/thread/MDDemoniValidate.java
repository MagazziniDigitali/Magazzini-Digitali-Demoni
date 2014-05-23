/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.thread;

import it.bncf.magazzimiDigitali.database.entity.MDFilesTmp;
import it.bncf.magazzimiDigitali.databaseSchema.sqlite.MDFilesTmpSqlite;
import it.bncf.magazziniDigitali.demoni.thread.validate.ValidateFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;

/**
 * @author massi
 * 
 */
public class MDDemoniValidate extends Thread {

	private Logger log = Logger.getLogger(getClass());

	/**
	 * @param target
	 * @param name
	 */
	public MDDemoniValidate(Runnable target, String name) {
		super(target, name);
	}

	@Override
	public void run() {
		try {
			while (true) {
				execute();
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Metodo utilizzato per l'esecuzione dell'attività di validazione
	 * 
	 */
	private void execute() {
		MDFilesTmpSqlite mdFileTmp = null;
		List<MDFilesTmp> rs = null;
		String fileObj = null;
		File fObj = null;
		ValidateFile validate = null;

		try {
			mdFileTmp = new MDFilesTmpSqlite();

			// Eseguo la ricerca di tutti i files che hanno finito il
			// trasferimento oppure che risultano in fase di validazione
			rs = mdFileTmp.findByStatus(new String[] {
					MDFilesTmpSqlite.FINETRASF, MDFilesTmpSqlite.INITVALID });
			if (rs != null && rs.size() > 0) {
				// Risulta almeno 1 record da elaborare
				validate = new ValidateFile();
				for (int x = 0; x < rs.size(); x++) {
					try {
						// calcolo il file da validare
						fileObj = Configuration.getValue("istituto."
								+ rs.get(x).getIdIstituto() + ".pathTmp");
						fileObj += File.separator;
						fileObj += rs.get(x).getNomeFile();
						fObj = new File(fileObj);
						log.debug("fileObj: " + fObj.getAbsolutePath());
						if (fObj.exists()) {
							// il file Esiste
							if (rs.get(x).getStato()
									.equals(MDFilesTmpSqlite.FINETRASF)) {
								log.debug("Inizio la validazione del file ["
										+ fObj.getAbsolutePath() + "]");
								mdFileTmp.updateStartValidate(rs.get(x).getId());
							} else {
								log.debug("Continuo la validazione del file ["
										+ fObj.getAbsolutePath() + "]");
							}
							validate.check(fObj);
							if (validate.isErrors()) {
								mdFileTmp.updateStopValidate(rs.get(x).getId(),
										null, false, validate.getErrors());
							} else {
								mdFileTmp.updateStopValidate(rs.get(x).getId(),
										validate.getXmlType().value(), true,
										null);
							}
						} else {
							mdFileTmp.updateStopValidate(rs.get(x).getId(),
									null, false, new String[] { "Il file ["
											+ fObj.getAbsolutePath()
											+ "] non è presente sul Server" });
						}
					} catch (ConfigurationException e) {
						mdFileTmp.updateStopValidate(
								rs.get(x).getId(),
								null,
								false,
								new String[] { e.getMessage() });
						log.error(e.getMessage(), e);
					} catch (SQLException e) {
						mdFileTmp.updateStopValidate(
								rs.get(x).getId(),
								null,
								false,
								new String[] { e.getMessage() });
						log.error(e.getMessage(), e);
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (mdFileTmp != null) {
					mdFileTmp.disconnect();
				}
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
