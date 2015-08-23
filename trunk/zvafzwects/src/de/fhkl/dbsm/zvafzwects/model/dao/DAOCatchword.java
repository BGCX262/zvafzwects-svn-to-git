package de.fhkl.dbsm.zvafzwects.model.dao;

import java.util.List;


import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Query;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Data Access Object Class for Catchword.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 5
 * @category DAO
 */
public class DAOCatchword {
	/**
	 * Method for inserting a new catchword into the database.
	 * 
	 * @param con the object container
	 * @param catchword the catchword to add
	 * @throws IllegalArgumentException if catchword is null
	 * @throws UniqueFieldValueConstraintViolationException if the catchword
	 *             name already exists
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	public static void insertCatchword(ObjectContainer con,
			DBCatchword catchword) throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		if (catchword == null) {
			throw new IllegalArgumentException("catchword must not be null");
		}
		// perform the query
		try {
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqCatchwordId")).next();
			catchword.setCatchwordId(sequence.nextVal());
			con.store(sequence);
			con.commit();
			con.store(catchword);
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
			throw new UniqueFieldValueConstraintViolationException(catchword,
					"catchwordName", catchword.getCatchwordName());
		}
	} // insertCatchword

	/**
	 * Method for getting a catchword by its id.
	 * 
	 * @param con the object container
	 * @param catchwordId the catchword id to look up
	 * @return the catchword found by the id
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if there is no matching catchword
	 */
	public static DBCatchword getCatchwordById(ObjectContainer con,
			int catchwordId) throws GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		DBCatchword catchword = new DBCatchword();
		catchword.setCatchwordId(catchwordId);

		// perform the query
		try {
			ObjectSet<DBCatchword> result = con.queryByExample(catchword);
			if (result.isEmpty()) {
				throw new DatabaseObjectNotFoundException(
						"No catchword with matching id.");
			}
			return result.next();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getCatchwordById

	/**
	 * Method for getting all catchwords.
	 * 
	 * @param con the object container
	 * @return a linked list containing all found catchwords
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBCatchword> getAllCatchwords(ObjectContainer con)
			throws GeneralDatabaseException {
		// perform the query
		try {
			// use SODA
			Query query = con.query();
			query.constrain(DBCatchword.class);
			query.descend("catchwordName").orderAscending();

			// execute the query and return the result
			return query.execute();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAllCatchwords

	/**
	 * Retrieve all catchwords which are referenced to and from alba.
	 * 
	 * @param con the object container
	 * @return a linked list containing all catchwords in use
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBCatchword> getAllCatchwordsInUse(ObjectContainer con)
			throws GeneralDatabaseException {
		// perform the query
		try {
			// use SODA
			Query query = con.query();
			query.constrain(DBCatchword.class);
			query.descend("alba").constrain(DBAlbum.class);
			query.descend("catchwordName").orderAscending();

			// execute the query and return the result
			return query.execute();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAllCatchwordsInUse

	/**
	 * Deletes a catchword from the database
	 * 
	 * @param con the object container
	 * @param catchwordId the id of the catchword to delete
	 * @throws DatabaseObjectNotFoundException if the catchword to delete could
	 *             not be found in the database
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static void deleteCatchword(ObjectContainer con, int catchwordId)
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		DBCatchword catchword;
		// perform the query
		try {
			catchword = DAOCatchword.getCatchwordById(con, catchwordId);

			con.delete(catchword);
			con.commit();
		} catch (GeneralDatabaseException e) {
			throw e;
		} catch (DatabaseObjectNotFoundException e) {
			throw new DatabaseObjectNotFoundException("catchword not found");
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		}
	} // deleteCatchword

	/**
	 * Updates a catchword
	 * 
	 * @param con the object container
	 * @param catchword the catchword to update
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static void updateCatchword(ObjectContainer con,
			DBCatchword catchword) throws GeneralDatabaseException {
		// perform the query
		try {
			con.store(catchword);
			con.commit();
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		}
	} // updateCatchword
}
