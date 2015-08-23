package de.fhkl.dbsm.zvafzwects.model.dao;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oIOException;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;


/**
 * Data Access Object for Cover class.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 1
 * @category DAO
 * 
 */
public class DAOCover {

	// Application works properly with activation depth of 5, but throws rarely
	// an Exception (java.lang.IndexOutOfBoundsException) from time to time.
	// Reason unknown?
	private static final int COVER_ACTIVATION_DEPTH = 5;

	/**
	 * Method for retrieving a cover for a given album id.
	 * 
	 * @param con the object container
	 * @param albumId the album id to look up
	 * @return the cover of the album
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if now matching cover was found
	 */
	public static DBCover getCoverByAlbumId(ObjectContainer con, int albumId)
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		DBCover cover = new DBCover();
		cover.setAlbumId(albumId);
		// perform the query
		try {
			ObjectSet<DBCover> result = con.queryByExample(cover);
			if (result.isEmpty()) {
				throw new DatabaseObjectNotFoundException(
						"No matching cover found");
			}
			DBCover returnCover = result.get(0);
			con.activate(returnCover, COVER_ACTIVATION_DEPTH);

			return returnCover;
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getCoverByAlbumId
}
