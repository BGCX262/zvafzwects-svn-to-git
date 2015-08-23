package de.fhkl.dbsm.zvafzwects.model.dao;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Query;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;


/**
 * Data Access Object Class for Album.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 6.1
 * @category DAO
 */
public class DAOAlbum {

	private static final int ALBUM_ACTIVATION_DEPTH = 5;
	private static final int CATCHWORD_ACTIVATION_DEPTH = 5;
	private static final int CATEGORY_ACTIVATION_DEPTH = 5;

	/**
	 * Method for getting all album in the database.
	 * 
	 * @param con the object container
	 * @return a list containing all found album
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBAlbum> getAllAlbum(ObjectContainer con)
			throws GeneralDatabaseException {
		// perform the query
		try {
			return con.queryByExample(DBAlbum.class);
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAllAlbum

	/**
	 * Method for getting a album by its id.
	 * 
	 * @param con the object container
	 * @param albumId the albumId to look up
	 * @return the DBAlbum object
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if now matching album was found
	 */
	public static DBAlbum getAlbumByID(ObjectContainer con, final int albumId)
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		// perform the query
		try {
			// use SODA
			Query query = con.query();
			query.constrain(DBAlbum.class);
			// define the search constraints
			query.descend("albumId").constrain(albumId).equal();
			// execute the query
			List<DBAlbum> alba = query.execute();
			if (alba.isEmpty()) {
				throw new DatabaseObjectNotFoundException(
						"No matching album found");
			}
			DBAlbum album = alba.get(0);
			con.activate(album, ALBUM_ACTIVATION_DEPTH);

			return album;
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAlbumByID

	/**
	 * Get an album by various search-criteria.
	 * 
	 * @param con the object container
	 * @param catchwords a list with catchwords to search for or null
	 * @param category the category to search for or null
	 * @param title the title to search for
	 * @param interpreter the interpreter to search for
	 * @return a list containing all found albums. If no album was found an
	 *         empty list is returned.
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static List<DBAlbum> getAlbumBySearch(ObjectContainer con,
			List<DBCatchword> catchwords, DBCategory category, String title,
			String interpreter) throws GeneralDatabaseException {

		try {
			List<DBAlbum> llalba;

			// no search-criteria specified, get all alba
			if (category == null && catchwords == null
					&& (title == null || "".equals(title))
					&& (interpreter == null || "".equals(interpreter))) {
				llalba = getAllAlbum(con);
			}
			// only category-criterion chosen
			else if (category != null && catchwords == null
					&& (title == null || "".equals(title))
					&& (interpreter == null || "".equals(interpreter))) {
				con.activate(category, CATEGORY_ACTIVATION_DEPTH);
				llalba = category.getAlba();
			}
			// only catchword-criterion chosen
			else if (catchwords != null && catchwords.size() == 1
					&& category == null && (title == null || "".equals(title))
					&& (interpreter == null || "".equals(interpreter))) {
				DBCatchword catchword = catchwords.get(0);
				con.activate(catchword, CATCHWORD_ACTIVATION_DEPTH);
				llalba = catchword.getAlba();
			}
			// use SODA-query considering every criterion with logical
			// and-composition
			else {
				Query query = con.query();
				query.constrain(DBAlbum.class);
				// define the search constraints
				if (title != null && !"".equals(title))
					query.descend("title").constrain(title).like();
				if (interpreter != null && !"".equals(interpreter))
					query.descend("interpreter").constrain(interpreter).like();
				if (category != null)
					query.descend("category").constrain(category);
				if (catchwords != null) {
					for (DBCatchword cw : catchwords) {
						query.descend("catchwords").constrain(cw);
					}
				}
				// set the sorting of the result
				query.descend("interpreter").orderAscending();
				query.descend("title").orderAscending();
				// execute the query
				llalba = query.execute();
			}
			// return the found list
			return llalba;
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAlbumBySearch

	/**
	 * Method for inserting a new album into the database.
	 * 
	 * @param con the object container
	 * @param album the album to add
	 * @throws IllegalArgumentException if album is null
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	public static void insertAlbum(ObjectContainer con, DBAlbum album)
			throws IllegalArgumentException, GeneralDatabaseException {
		if (album == null)
			throw new IllegalArgumentException("album must not be null");

		// perform the query
		try {
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqAlbumId")).next();
			album.setAlbumId(sequence.nextVal());

			// set the proper albumId for the cover
			if (album.getCover() != null)
				album.getCover().setAlbumId(album.getAlbumId());

			con.store(sequence);
			con.commit();
			con.store(album);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // insertAlbum

	/**
	 * Method for updating an album.
	 * 
	 * @param con the object container
	 * @param album the updated album
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	public static void updateAlbum(ObjectContainer con, DBAlbum album)
			throws GeneralDatabaseException {
		if (album == null)
			throw new IllegalArgumentException("album must not be null");

		// perform the query
		try {
			try {
				DBCover oldCover = DAOCover.getCoverByAlbumId(con,
						album.getAlbumId());
				if (oldCover != null)
					con.delete(oldCover);
			} catch (DatabaseObjectNotFoundException ex) {
			}

			con.store(album);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // updateAlbum
}
