/**
 * 
 */
package it.bncf.magazziniDigitali.demoni.exception;

/**
 * @author massi
 *
 */
public class MDDemoniException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7561210681988282066L;

	/**
	 * @param arg0
	 */
	public MDDemoniException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MDDemoniException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
