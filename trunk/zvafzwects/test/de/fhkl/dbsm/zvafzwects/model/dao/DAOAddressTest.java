package de.fhkl.dbsm.zvafzwects.model.dao;


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
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;

/**
 * @author Jochen Pätzold
 * @version 2
 * @category JUnit
 * 
 */
public class DAOAddressTest implements TestServerInfo {

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
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress#insertAddress(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAddress)}
	 * . Test if null as address-argument is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void insertAddress_insertNull()
			throws IllegalArgumentException, GeneralDatabaseException {
		DAOAddress.insertAddress(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress#insertAddress(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAddress)}
	 * . Test if new address-object will be inserted
	 */
	@Test
	public final void insertAddress_simpleInsert()
			throws IllegalArgumentException, GeneralDatabaseException {
		DBAddress address = new DBAddress();
		DAOAddress.insertAddress(oContainer, address);
		address = new DBAddress("Delaware", "23a-c", "Bipontina", "66482");
		DAOAddress.insertAddress(oContainer, address);
		// address = null; // gain breakpoint after last instruction
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress#insertAddress(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAddress)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertAddress_disconnectedClient()
			throws IllegalArgumentException, GeneralDatabaseException {
		oContainer.close(); // disconnect client
		DBAddress address = new DBAddress();
		DAOAddress.insertAddress(oContainer, address);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress#insertAddress(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAddress)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertAddress_shutDownServer()
			throws IllegalArgumentException, GeneralDatabaseException {
		DBAddress address = new DBAddress();
		db4oServer.close(); // shut down server
		DAOAddress.insertAddress(oContainer, address);
	}
}
