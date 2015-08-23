/**
 * 
 */
package de.fhkl.dbsm.zvafzwects.model.dao;

import java.math.BigDecimal;
import java.util.Date;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.io.MemoryStorage;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOUser;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrderItem;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 * @category JUnit
 */
public class DAOOrderTest implements TestServerInfo {

	private static ObjectServer db4oServer;
	private static ObjectContainer oContainer;

	private static void startUpClientConnection() {
		ClientConfiguration clientConfig = Configuration
				.getClientConfiguration();
		oContainer = Db4oClientServer.openClient(clientConfig, HOST, PORT,
				USER, PASS);
		Configuration.initializeDB(oContainer);
	}

	private ObjectServer createInMemoryServer() {
		ServerConfiguration config = Configuration.getServerConfiguration();
		config.file().storage(new MemoryStorage());
		ObjectServer server = Db4oClientServer.openServer(config, "In:Memory",
				PORT);
		server.grantAccess(USER, PASS);
		return server;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		db4oServer = createInMemoryServer();
		startUpClientConnection();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		db4oServer.close();
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder#insertOrder(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBOrder)}
	 * . Test if null as order-argument is rejected
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void InsertOrder_insertNull() throws IllegalArgumentException,
			GeneralDatabaseException, DatabaseObjectNotFoundException {
		DAOOrder.insertOrder(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder#insertOrder(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBOrder)}
	 * . Test if new orders will be inserted
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test
	public final void insertOrder_simpleInsert()
			throws IllegalArgumentException, GeneralDatabaseException,
			UniqueFieldValueConstraintViolationException,
			DatabaseObjectNotFoundException {
		DBUser user = new DBUser("surename", "forename", "email", "password",
				new Date(123456789), 'm', false);
		DAOUser.insertUser(oContainer, user);

		DBAddress address = new DBAddress("street", "42", "city", "1337");
		DAOAddress.insertAddress(oContainer, address);

		DBOrder order1 = new DBOrder();
		DBAlbum album = new DBAlbum("Title", "Interpreter", new BigDecimal(
				13.99), "Label", 13, null, null);
		DAOAlbum.insertAlbum(oContainer, album);
		new DBOrderItem(order1, album, 2, new BigDecimal(13.99));
		order1.setUser(user);
		order1.setDeliverAddress(address);
		order1.setInvoiceAddress(address);
		order1.setDate("01.01.1337");

		DAOOrder.insertOrder(oContainer, order1);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder#insertOrder(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBOrder)}
	 * . Test without client connection
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertOrder_disconnectedClient()
			throws IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		oContainer.close();
		DAOOrder.insertOrder(oContainer, new DBOrder());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder#insertOrder(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBOrder)}
	 * . Test with shut down server
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertOrder_shutDownServer()
			throws IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		db4oServer.close();
		DAOOrder.insertOrder(oContainer, new DBOrder());
	}
}
