package de.fhkl.dbsm.zvafzwects.model.db;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;

/**
 * @author Andreas Baur
 * @version 2
 */
public class DBUserTest {

	private static DBUser user;

	private final static int INIT_USERID = 1;
	private final static String INIT_SURNAME = "Surname";
	private final static String INIT_FORENAME = "Forename";
	private final static String INIT_EMAIL = "test@test.test";
	private final static String INIT_PASSWORD = "password";
	private final static Date INIT_DATEOFBIRTH = new Date(123456789);
	private final static char INIT_GENDER = 'm';
	private final static List<DBAddress> INIT_ADDRESSES = new LinkedList<DBAddress>();
	private final static List<DBOrder> INIT_ORDERS = new LinkedList<DBOrder>();
	private final static boolean INIT_ISADMIN = true;

	private final static DBAddress INIT_ADDRESS = new DBAddress();
	private final static DBOrder INIT_ORDER = new DBOrder();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = new DBUser(INIT_SURNAME, INIT_FORENAME, INIT_EMAIL,
				INIT_PASSWORD, INIT_DATEOFBIRTH, INIT_GENDER, INIT_ISADMIN);
	}

	@Test
	public void setId() {
		user.setUserId(INIT_USERID);
		Assert.assertTrue("setId", INIT_USERID == user.getUserId());
	}

	@Test
	public void setAddresses() {
		user.setAddresses(INIT_ADDRESSES);
		Assert.assertEquals("setAddresses", INIT_ADDRESSES, user.getAddresses());
	}

	@Test
	public void addAddress() {
		user.addAddress(INIT_ADDRESS);
		Assert.assertEquals("addAddress", INIT_ADDRESS, user.getAddresses()
				.get(0));
	}

	@Test
	public void deleteAddress() {
		List<DBAddress> addresses = new LinkedList<DBAddress>();
		DBAddress addr = new DBAddress();
		addresses.add(addr);
		user.setAddresses(addresses);

		boolean Found = false;
		user.removeAddress(addr);
		for (DBAddress currAdr : addresses)
			if (currAdr.equals(addr))
				Found = true;
		Assert.assertFalse("deleteAddress failed", Found);
	}

	@Test
	public void setOrders() {
		user.setOrders(INIT_ORDERS);
		Assert.assertEquals("setOrders", INIT_ORDERS, user.getOrders());
	}

	@Test
	public void addOrder() {
		user.addOrder(INIT_ORDER);
		Assert.assertEquals("addOrder", INIT_ORDER, user.getOrders().get(0));
	}
}
