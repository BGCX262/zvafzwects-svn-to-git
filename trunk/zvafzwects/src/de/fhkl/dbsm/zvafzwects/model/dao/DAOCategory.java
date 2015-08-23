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
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Data Access Object Class for Category.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 6
 * @category DAO
 */
public class DAOCategory {
	/**
	 * Method for inserting a new category into the database.
	 * 
	 * @param con the object container
	 * @param category the category to add
	 * @throws UniqueFieldValueConstraintViolationException if the category name
	 *             already exists
	 * @throws IllegalArgumentException if category is null
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	public static void insertCategory(ObjectContainer con, DBCategory category)
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		if (category == null) {
			throw new IllegalArgumentException("category must not be null");
		}
		// perform the query
		try {
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqCategoryId")).next();
			category.setCategoryId(sequence.nextVal());
			con.store(sequence);
			con.commit();
			con.store(category);
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
			throw new UniqueFieldValueConstraintViolationException(category,
					"categoryName", category.getCategoryName());
		}
	} // insertCategory

	/**
	 * Method for getting a category by its id.
	 * 
	 * @param con the object container
	 * @param categoryId the category id to look up
	 * @return the category found by the id
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if there is no matching catchword
	 */
	public static DBCategory getCategoryById(ObjectContainer con, int categoryId)
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		DBCategory category = new DBCategory();
		category.setCategoryId(categoryId);
		// perform the query
		try {
			ObjectSet<DBCategory> result = con.queryByExample(category);
			if (result.isEmpty()) {
				throw new DatabaseObjectNotFoundException(
						"No category with matching id.");
			}
			return result.next();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getCategoryById

	/**
	 * Method for getting all categories.
	 * 
	 * @param con the object container
	 * @return a linked list containing all found categories
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBCategory> getAllCategories(ObjectContainer con)
			throws GeneralDatabaseException {
		// perform the query
		try {
			// use SODA
			Query query = con.query();
			query.constrain(DBCategory.class);
			query.descend("categoryName").orderAscending();
			// execute the query and return the result
			return query.execute();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAllCategories

	/**
	 * Retrieve all categories which are referenced to and from alba.
	 * 
	 * @param con the object container
	 * @return a linked list containing all categories in use
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBCategory> getAllCategoriesInUse(ObjectContainer con)
			throws GeneralDatabaseException {
		// perform the query
		try {
			// use SODA
			Query query = con.query();
			query.constrain(DBCategory.class);
			query.descend("alba").constrain(DBAlbum.class);
			query.descend("categoryName").orderAscending();
			// execute the query and return the result
			return query.execute();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAllCategoriesInUse

	/**
	 * Deletes a category from the database
	 * 
	 * @param con the object container
	 * @param categoryId the id of the category to delete
	 * @throws DatabaseObjectNotFoundException if the category to delete could
	 *             not be found in the database
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static void deleteCategory(ObjectContainer con, int categoryId)
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		DBCategory category;
		// perform the query
		try {
			category = DAOCategory.getCategoryById(con, categoryId);

			con.delete(category);
			con.commit();
		} catch (GeneralDatabaseException e) {
			throw e;
		} catch (DatabaseObjectNotFoundException e) {
			throw new DatabaseObjectNotFoundException("category not found");
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		}
	} // deleteCategory

	/**
	 * Updates a category from the database
	 * 
	 * @param con the object container
	 * @param category the category to update
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static void updateCategory(ObjectContainer con, DBCategory category)
			throws GeneralDatabaseException {
		// perform the query
		try {
			con.store(category);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // updateCategory
}
