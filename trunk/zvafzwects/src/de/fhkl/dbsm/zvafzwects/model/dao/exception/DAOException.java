package de.fhkl.dbsm.zvafzwects.model.dao.exception;

/**
 * General Data Access Object Exceptions
 * 
 * @author Markus Henn
 */
public abstract class DAOException extends Exception {
	private static final long serialVersionUID = 1975938936172847721L;

	/**
	 * 
	 */
	public DAOException() {
		super();
	}

	/**
	 * @param message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

}
