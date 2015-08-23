package de.fhkl.dbsm.zvafzwects.model.dao;


import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrderItem;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Data Access Object Class for Order.
 * 
 * @author Andreas Baur, Jochen Pätzold
 * @version 4
 * @category DAO
 */
public class DAOOrder {

	private final static int MIN_AMOUNT = 5;
	private final static int NEW_STOCK = 42;

	/**
	 * Method for inserting a new order into the database.
	 * 
	 * @param con the object container
	 * @param order the order to store into the database
	 * @throws IllegalArgumentException if order is null
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 * @throws DatabaseObjectNotFoundException if the album to update could not
	 *             be found
	 */
	public static void insertOrder(ObjectContainer con, DBOrder order)
			throws IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		if (order == null) {
			throw new IllegalArgumentException("order must not be null");
		}
		// perform the query
		try {
			// adjusting the album stock
			for (DBOrderItem item : order.getOrderItems()) {
				// !important! retrieve origin album from db
				DBAlbum album = DAOAlbum.getAlbumByID(con, item.getAlbum()
						.getAlbumId());
				if (album.getStock() - item.getAmount() < MIN_AMOUNT) {
					album.setStock(NEW_STOCK);
				} else {
					album.setStock(album.getStock() - item.getAmount());
				}
				// !important! replace album in orderitem with its origin to
				// avoid UniqueFieldValueConstraintVioloation
				item.setAlbum(album);
			}
			// storing the order (updates the album automatically because of
			// cascading update)
			Sequence sequence = (Sequence) con.queryByExample(
					new Sequence(0, "seqOrderId")).next();
			order.setOrderId(sequence.nextVal());
			con.store(sequence);
			con.store(order);
			con.commit();
		} catch (DatabaseReadOnlyException ex) {
			throw new GeneralDatabaseException("Database is read only", ex);
		} catch (Db4oIOException ex) {
			con.rollback();
			throw new GeneralDatabaseException("Db4oIOException", ex);
		} catch (DatabaseClosedException ex) {
			throw new GeneralDatabaseException("Database closed", ex);
		} catch (DatabaseObjectNotFoundException e) {
			throw new DatabaseObjectNotFoundException(
					"Album konnte nicht gefunden werden.");
		}
	} // insertOrder
}
