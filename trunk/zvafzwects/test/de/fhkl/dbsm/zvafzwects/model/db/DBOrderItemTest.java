package de.fhkl.dbsm.zvafzwects.model.db;

import java.math.BigDecimal;

import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrderItem;

/**
 * @author Andreas Baur
 * @version 1
 */
public class DBOrderItemTest {
	
	private final static DBOrder INIT_ORDER = new DBOrder();
	private final static DBAlbum INIT_ALBUM = new DBAlbum();
	private final static int INIT_AMOUNT = 2;
	private final static BigDecimal INIT_PRICE = new BigDecimal(3.14);
	
	@Test
	public void createObject() {
		new DBOrderItem(INIT_ORDER, INIT_ALBUM, INIT_AMOUNT, INIT_PRICE);
	}
}
