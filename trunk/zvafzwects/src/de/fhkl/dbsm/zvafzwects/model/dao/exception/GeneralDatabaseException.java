package de.fhkl.dbsm.zvafzwects.model.dao.exception;

/**
 * Exception class for general database errors, like I/O or connection issues.
 * 
 * @author Markus Henn
 */
public class GeneralDatabaseException extends DAOException {
	private static final long serialVersionUID = -6255052843714705528L;

	/**
	 * 
	 */
	public GeneralDatabaseException() {
		super();
	}

	/**
	 * @param message
	 */
	public GeneralDatabaseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public GeneralDatabaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GeneralDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

}
