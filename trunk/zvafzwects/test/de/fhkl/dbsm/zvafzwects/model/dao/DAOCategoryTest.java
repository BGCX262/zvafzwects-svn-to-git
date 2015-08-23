package de.fhkl.dbsm.zvafzwects.model.dao;

import java.util.List;

import junit.framework.Assert;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.io.MemoryStorage;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;

/**
 * @author Jochen Pätzold
 * @version 2
 * @category JUnit
 */

/*
 * This class tests DAOCategory-related methods
 */
public class DAOCategoryTest implements TestServerInfo {

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

	// test null as category-argument
	@Test(expected = IllegalArgumentException.class)
	public void insertCategory_insertNull() throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOCategory.insertCategory(oContainer, null);
	}

	// just insert an category
	@Test
	public void insertCategory_simpleInsert()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		DAOCategory.insertCategory(oContainer, new DBCategory("ONE"));
	}

	// insert a category that violates the unique field constraint
	@Test(expected = UniqueFieldValueConstraintViolationException.class)
	public void insertCategory_uniqueFieldConstraint()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOCategory.insertCategory(oContainer, new DBCategory("TWO"));
		// the same object is inserted in order to raise the
		// UniqueFieldValueConstraintViolationException
		DAOCategory.insertCategory(oContainer, new DBCategory("TWO"));
	}

	// test category without client connection
	@Test(expected = GeneralDatabaseException.class)
	public void insertCategory_disconnectedClient()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		oContainer.close(); // disconnect client connection
		DAOCategory.insertCategory(oContainer, new DBCategory("closed"));
	}

	// test category with shut down server
	@Test(expected = GeneralDatabaseException.class)
	public void insertCategory_shutDownServer()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		db4oServer.close(); // shutdown-server
		DAOCategory.insertCategory(oContainer, new DBCategory("closed"));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory#getCategoryById(com.db4o.ObjectContainer, int)}
	 * . Test for known categoryId (depends on
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory#insertCategory(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCategory)}
	 * )
	 * 
	 * @throws GeneralDatabaseException
	 * @throws IllegalArgumentException
	 * @throws UniqueFieldValueConstraintViolationException
	 * @throws DatabaseObjectNotFoundException
	 * 
	 */
	@Test
	public final void getCategoryById_simple()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		// On a clean db first category should get id = 1
		DBCategory first = new DBCategory("TestCase");
		DAOCategory.insertCategory(oContainer, first);
		DBCategory category = DAOCategory.getCategoryById(oContainer, 1);
		Assert.assertEquals("id mismatch", 1, category.getCategoryId());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory#getCategoryById(com.db4o.ObjectContainer, int)}
	 * . Tests for unknown categoryId
	 * 
	 * @throws GeneralDatabaseException
	 */
	@Test(expected = DatabaseObjectNotFoundException.class)
	public final void getCategoryById_unknownId()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		// Empty db contains no category, so any id has to fail
		DAOCategory.getCategoryById(oContainer, 0);
	}
	
	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory#getCategoryById(com.db4o.ObjectContainer, int)}
	 * . Test without disconnected client
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCatchwordById_disconectedClient()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		oContainer.close();
		// missing server should throw exception
		DAOCategory.getCategoryById(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory#getCategoryById(com.db4o.ObjectContainer, int)}
	 * . Test without shut down server
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCatchwordById_shutDownServer()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		db4oServer.close();
		// missing server should throw exception
		DAOCategory.getCategoryById(oContainer, 0);
	}
	
	// retrieve known categories
	// (depends on "insertCategory()" working properly)
	@Test
	public void getAllCategories()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException {
		boolean three = false, four = false;
		// insert two items
		DAOCategory.insertCategory(oContainer, new DBCategory("THREE"));
		DAOCategory.insertCategory(oContainer, new DBCategory("FOUR"));
		List<DBCategory> catList = DAOCategory.getAllCategories(oContainer);
		// and retrieve them
		for (DBCategory cat : catList) {
			if (cat.getCategoryName().equals("THREE"))
				three = true;
			if (cat.getCategoryName().equals("FOUR"))
				four = true;
		}
		Assert.assertTrue("Category-List not successfully retrieved", three
				&& four);
	}

	// test getAllCategories without client connection
	@Test(expected = GeneralDatabaseException.class)
	public void getAllCategories_disconnectedClient()
			throws GeneralDatabaseException {
		oContainer.close(); // disconnect client connection
		@SuppressWarnings("unused")
		List<DBCategory> catList = DAOCategory.getAllCategories(oContainer);
	}

	// test getAllCategories with shut down server
	@Test(expected = GeneralDatabaseException.class)
	public void getAllCategories_shutDownServer()
			throws GeneralDatabaseException {
		db4oServer.close(); // disconnect client connection
		@SuppressWarnings("unused")
		List<DBCategory> catList = DAOCategory.getAllCategories(oContainer);
	}

}
