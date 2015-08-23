package de.fhkl.dbsm.zvafzwects.model.dao;


import com.db4o.*;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Data Access Object Class for User.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 4.1
 * @category DAO
 */
public class DAOUser {

	/**
	 * Method for getting an user by its credentials.
	 * 
	 * @param con the object container
	 * @param email the email to look up
	 * @param password the password to look up
	 * @return the found DBUser object
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if no user object was found
	 */
	public static DBUser getUserByCredentials(ObjectContainer con,
			String email, String password)
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		DBUser user = new DBUser();
		user.setEmail(email);
		user.setPassword(password);

		// perform the query
		try {
			ObjectSet<DBUser> result = con.queryByExample(user);
			if (result.isEmpty()) {
				throw new DatabaseObjectNotFoundException("User not found");
			}
			return result.next();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getUserByCredentials

	/**
	 * Method for getting an user by its id.
	 * 
	 * @param con the object container
	 * @param id the id to look up
	 * @return the found DBUser object
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if no user object was found
	 */
	public static DBUser getUserById(ObjectContainer con, int userId)
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		DBUser user = new DBUser();
		user.setUserId(userId);

		// perform the query
		try {
			ObjectSet<DBUser> result = con.queryByExample(user);
			if (result.isEmpty()) {
				throw new DatabaseObjectNotFoundException("User not found");
			}

			return result.next();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getUserById

	/**
	 * Method for inserting a new user into the database.
	 * 
	 * @param con the object container
	 * @param user the user to add
	 * @throws UniqueFieldValueConstraintViolationException if the email already
	 *             exists
	 * @throws IllegalArgumentException if user is null
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	public static void insertUser(ObjectContainer con, DBUser user)
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		if (user == null) {
			throw new IllegalArgumentException("user must not be null");
		}
		// perform the query
		try {
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqUserId")).next();
			user.setUserId(sequence.nextVal());
			con.store(sequence);
			con.commit();
			con.store(user);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (com.db4o.constraints.UniqueFieldValueConstraintViolationException ex) {
			con.rollback();
			throw new UniqueFieldValueConstraintViolationException(user,
					"email", user.getEmail());
		}
	} // insertUser

	/**
	 * Method for updating a user.
	 * 
	 * @param con the object container
	 * @param user the user to update
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws IllegalArgumentException if user is null
	 * @throws UniqueFieldValueConstraintViolationException if the email is
	 *             already in use
	 */
	public static void updateUser(ObjectContainer con, DBUser user)
			throws GeneralDatabaseException, IllegalArgumentException,
			UniqueFieldValueConstraintViolationException {
		if (user == null)
			throw new IllegalArgumentException("user must not be null");

		// perform the query
		try {
			con.store(user);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (com.db4o.constraints.UniqueFieldValueConstraintViolationException ex) {
			con.rollback();
			throw new UniqueFieldValueConstraintViolationException(user,
					"email", user.getEmail());
		}
	} // updateUser
}
