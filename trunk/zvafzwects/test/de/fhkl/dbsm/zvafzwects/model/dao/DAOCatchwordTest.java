package de.fhkl.dbsm.zvafzwects.model.dao;

import static org.junit.Assert.*;

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

import de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 * @category JUnit
 */
public class DAOCatchwordTest implements TestServerInfo {

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
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * . Test if null as catchword-argument is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void insertCatchword_insertNull()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOCatchword.insertCatchword(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * . Test if new catchwords will be inserted
	 */
	@Test
	public final void insertCatchword_simpleInsert()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("CATCHME"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("GOTCHA"));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * . Insert a catchword that violates the unique field constraint
	 */
	@Test(expected = UniqueFieldValueConstraintViolationException.class)
	public final void insertCatchword_uniqueFieldConstraint()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("DOUBLETTE"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("DOUBLETTE"));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertCatchword_disconnectedClient()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		oContainer.close();
		DAOCatchword.insertCatchword(oContainer, new DBCatchword());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertCatchword_shutDownServer()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		db4oServer.close();
		DAOCatchword.insertCatchword(oContainer, new DBCatchword());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getCatchwordById(com.db4o.ObjectContainer, int)}
	 * . Test for known catchwordId (depends on
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#insertCatchword(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBCatchword)}
	 * )
	 * 
	 * @throws DatabaseObjectNotFoundException
	 * @throws GeneralDatabaseException
	 * @throws UniqueFieldValueConstraintViolationException
	 * @throws IllegalArgumentException
	 */
	@Test
	public final void getCatchwordById_simple()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException,
			IllegalArgumentException,
			UniqueFieldValueConstraintViolationException {
		// On a clean db first catchword should get id = 1
		DBCatchword first = new DBCatchword("TestCase");
		DAOCatchword.insertCatchword(oContainer, first);
		DBCatchword catchword = DAOCatchword.getCatchwordById(oContainer, 1);
		Assert.assertEquals("id mismatch", 1, catchword.getCatchwordId());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getCatchwordById(com.db4o.ObjectContainer, int)}
	 * . Tests for unknown catchwordId
	 * 
	 * @throws GeneralDatabaseException
	 */
	@Test(expected = DatabaseObjectNotFoundException.class)
	public final void getCatchwordById_unknownId()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		// Empty db contains no catchword, so any id has to fail
		DAOCatchword.getCatchwordById(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getCatchwordById(com.db4o.ObjectContainer, int)}
	 * . Test with disconnected client
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCatchwordById_disconnectedClient()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		oContainer.close();
		// missing client should throw exception
		DAOCatchword.getCatchwordById(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getCatchwordById(com.db4o.ObjectContainer, int)}
	 * . Test with shut down server
	 * 
	 * @throws DatabaseObjectNotFoundException
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCatchwordById_shutDownServer()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		db4oServer.close();
		// missing server should throw exception
		DAOCatchword.getCatchwordById(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getAllCatchwords(com.db4o.ObjectContainer)}
	 * . Test if you retrieve a list of catchwords (depends on
	 * DAOCatchword.insertCatchword())
	 */
	@Test
	public final void getAllCatchwords() throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		// insert more catchwords than could already there
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("ALPHA"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("BETA"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("GAMME"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("DELTA"));
		DAOCatchword.insertCatchword(oContainer, new DBCatchword("ERGO"));
		List<DBCatchword> llCatchword = DAOCatchword
				.getAllCatchwords(oContainer);
		// there should at least be 5 catchwords
		assertTrue(
				"Did not retrieve all catchwords with DAOCatchword.getAllCatchwords(...)",
				llCatchword.size() >= 5);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getAllCatchwords(com.db4o.ObjectContainer)}
	 * . Test with disconnected client
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAllCatchwords_disconnectedClient()
			throws GeneralDatabaseException {
		oContainer.close();
		// missing client should throw exception
		DAOCatchword.getAllCatchwords(oContainer);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword#getAllCatchwords(com.db4o.ObjectContainer)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAllCatchwords_shutDownServer()
			throws GeneralDatabaseException {
		db4oServer.close();
		// missing server should throw exception
		DAOCatchword.getAllCatchwords(oContainer);
	}

}
