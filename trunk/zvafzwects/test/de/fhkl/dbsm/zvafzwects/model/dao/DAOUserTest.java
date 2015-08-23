package de.fhkl.dbsm.zvafzwects.model.dao;


import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.io.MemoryStorage;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOUser;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 * @category JUnit
 */
public class DAOUserTest implements TestServerInfo {

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
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#insertUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)}
	 * . Test if null as user-argument is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void insertUser_insertNull()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		DAOUser.insertUser(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#insertUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)}
	 * . Test if new users will be inserted
	 */
	@Test
	public final void insertUser_simpleInsert()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		DAOUser.insertUser(oContainer, new DBUser("Wurst", "Hans",
				"test.test@test.test", "superpw", new Date(123456789), 'm', false));
		DAOUser.insertUser(oContainer, new DBUser("Steier", "Mark",
				"test@web.test", "notsosuperpw", new Date(987654321), 'm', true));

	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#insertUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)}
	 * . Insert a new user that violates the unique field constraint
	 */
	@Test(expected = UniqueFieldValueConstraintViolationException.class)
	public final void insertUser_uniqueFieldConstraint()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		DAOUser.insertUser(oContainer, new DBUser("FN", "SN", "Mail", "PW",
				new Date(123456789), 'w', false));
		DAOUser.insertUser(oContainer, new DBUser("FN", "SN", "Mail", "PW",
				new Date(987654321), 'w', false));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#insertUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertUser_disconnectedClient()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		oContainer.close();
		DAOUser.insertUser(oContainer, new DBUser());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#insertUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertUser_shutdownServer()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		db4oServer.close();
		DAOUser.insertUser(oContainer, new DBUser());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#getUserByCredentials(com.db4o.ObjectContainer, String, String)}
	 * . Test for user that doesn't exist
	 */
	@Test(expected = DatabaseObjectNotFoundException.class)
	public final void getUserByCredentials_mismatch()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		DBUser user = new DBUser("FN", "SN", "TestMail", "TestPW", new Date(123456789), 'w',
				false);
		DAOUser.insertUser(oContainer, user);
		DBUser foundUser = DAOUser.getUserByCredentials(oContainer, "TestMail",
				"PW");
		Assert.assertTrue("User found, but none expected", foundUser
				.getSurname().compareToIgnoreCase(user.getSurname()) != 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#getUserByCredentials(com.db4o.ObjectContainer, String, String)}
	 * . Test for user with known email and password
	 */
	@Test
	public final void getUserByCredentials_match()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		DBUser user = new DBUser("FN", "SN", "KnownMail", "KnownPW", new Date(123456789),
				'w', false);
		DAOUser.insertUser(oContainer, user);
		DBUser foundUser = DAOUser.getUserByCredentials(oContainer,
				"KnownMail", "KnownPW");
		Assert.assertTrue("User not found, but one was expected", foundUser
				.getSurname().compareToIgnoreCase(user.getSurname()) == 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#getUserByCredentials(com.db4o.ObjectContainer, String, String)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getUserByCredentials_disconnectedClient()
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		oContainer.close();
		DAOUser.getUserByCredentials(oContainer, "", "");
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#getUserByCredentials(com.db4o.ObjectContainer, String, String)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getUserByCredentials_shutDownServer()
			throws DatabaseObjectNotFoundException, GeneralDatabaseException {
		db4oServer.close();
		DAOUser.getUserByCredentials(oContainer, "", "");
	}

/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#updateUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)
	 * . Test if null as user-argument is rejected
	 * @throws UniqueFieldValueConstraintViolationException 
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void updateUser_Null() throws IllegalArgumentException,
			GeneralDatabaseException,
			UniqueFieldValueConstraintViolationException {
		DAOUser.updateUser(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#updateUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)
	 * . Test for successful updating an user
	 */
	@Test
	public final void updateUser_success()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		DBUser user = new DBUser("FN", "SN", "UpdateMail", "UpdatePW", new Date(123456789),
				'w', true);
		DAOUser.insertUser(oContainer, user);

		user.setGender('m');
		user.setIsAdmin(false);

		DAOUser.updateUser(oContainer, user);

		DBUser updatedUser = DAOUser.getUserByCredentials(oContainer,
				"UpdateMail", "UpdatePW");

		Assert.assertTrue("User not successfully updated",
				updatedUser.getGender() == 'm');
	}

/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#updateUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)
	 * . Test without client connection
	 * @throws UniqueFieldValueConstraintViolationException 
	 * @throws IllegalArgumentException 
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void updateUser_disconnectedClient()
			throws DatabaseObjectNotFoundException, GeneralDatabaseException,
			IllegalArgumentException,
			UniqueFieldValueConstraintViolationException {
		oContainer.close();
		DAOUser.updateUser(oContainer, new DBUser());
	}

/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOUser#updateUser(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBUser)
	 * . Test with shut down server
	 * @throws UniqueFieldValueConstraintViolationException 
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void updateUser_shutdownServer()
			throws GeneralDatabaseException, IllegalArgumentException,
			UniqueFieldValueConstraintViolationException {
		db4oServer.close();
		DAOUser.updateUser(oContainer, new DBUser());
	}
}
