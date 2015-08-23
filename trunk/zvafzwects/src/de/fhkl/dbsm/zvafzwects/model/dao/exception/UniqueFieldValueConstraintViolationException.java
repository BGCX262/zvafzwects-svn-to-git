package de.fhkl.dbsm.zvafzwects.model.dao.exception;

/**
 * This Exception should be thrown when a unique field value constraint
 * violation occurs.
 * 
 * @author Markus Henn
 */
public class UniqueFieldValueConstraintViolationException extends DAOException {
	private static final long serialVersionUID = -7934709172406811834L;

	private final Object object;
	private final String field;
	private final String value;

	/**
	 * @param object
	 *            object, in which the field exists
	 * @param field
	 *            field of the object, which violates the unique field
	 *            constraint
	 * @param value
	 *            value of the field
	 */
	public UniqueFieldValueConstraintViolationException(Object object,
			String field, String value) {
		super("Field '" + field + "' with value '" + value + "' in object '"
				+ object + "'");
		this.object = object;
		this.field = field;
		this.value = value;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
