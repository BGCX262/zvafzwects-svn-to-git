package de.fhkl.dbsm.zvafzwects.model.dao.exception;

/**
 * Exception class for object not found errors, thrown by the search methods
 * 
 * @author Andreas Baur
 * @version 1
 */
public class DatabaseObjectNotFoundException extends DAOException {
	private static final long serialVersionUID = 5086254779598587911L;

	public DatabaseObjectNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public DatabaseObjectNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DatabaseObjectNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatabaseObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
