package de.fhkl.dbsm.zvafzwects.model.dao;


import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Data Access Object Class for Address.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 4
 * @category DAO
 */
public class DAOAddress {

	/**
	 * Gets an address by a given id
	 * 
	 * @param con the object container
	 * @param addressId the id to look up
	 * @return the found address object
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 * @throws DatabaseObjectNotFoundException if now matching address was found
	 */
	public static DBAddress getAddressById(ObjectContainer con, int addressId)
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		DBAddress address = new DBAddress();
		address.setAddressId(addressId);

		// perform the query
		try {
			ObjectSet<DBAddress> result = con.queryByExample(address);
			if (result.isEmpty())
				throw new DatabaseObjectNotFoundException(
						"No address with matching id.");

			return result.next();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // getAddressById

	/**
	 * Method for inserting an address into the database
	 * 
	 * @param con the object container
	 * @param address the address to store into the database
	 * @throws IllegalArgumentException if category is null
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	public static void insertAddress(ObjectContainer con, DBAddress address)
			throws IllegalArgumentException, GeneralDatabaseException {
		if (address == null)
			throw new IllegalArgumentException("address must not be null");

		// perform the query
		try {
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqAddressId")).next();
			address.setAddressId(sequence.nextVal());
			con.store(sequence);
			con.commit();
			con.store(address);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		}
	} // insertAddress
}
