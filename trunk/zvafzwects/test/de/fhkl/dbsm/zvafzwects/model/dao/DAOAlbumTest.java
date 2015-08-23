package de.fhkl.dbsm.zvafzwects.model.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;
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

import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;


/**
 * @author Andreas Baur
 * @version 1
 * @category JUnit
 */
public class DAOAlbumTest implements TestServerInfo {

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
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#insertAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test if null as album-argument is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void insertAlbum_insertNull() throws IllegalArgumentException,
			GeneralDatabaseException {
		DAOAlbum.insertAlbum(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#insertAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test if new alba will be inserted
	 */
	@Test
	public final void insertAlbum_simpleInsert()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title1", "Interpret1",
				new BigDecimal(3.99), "Label1", 42, null, null));
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title2", "Interpret2",
				new BigDecimal(13.50), "Label2", 21, null, null));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#insertAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertAlbum_disconnectedClient()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		oContainer.close();
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title", "Interpret",
				new BigDecimal(3.99), "Label", 42, null, null));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#insertAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void insertAlbum_shutDownServer()
			throws IllegalArgumentException,
			UniqueFieldValueConstraintViolationException,
			GeneralDatabaseException {
		db4oServer.close();
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title", "Interpret",
				new BigDecimal(3.99), "Label", 42, null, null));
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAlbumByID(com.db4o.ObjectContainer, int)} .
	 * Test for known albumId (depends on
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#insertAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * )
	 */
	@Test
	public final void getAlbumByID_simple() throws GeneralDatabaseException,
			DatabaseObjectNotFoundException, IllegalArgumentException,
			UniqueFieldValueConstraintViolationException {
		// On a clean db first album should get id = 1
		DBAlbum first = new DBAlbum("Title", "Interpret", new BigDecimal(3.99),
				"Label", 42, null, null);
		DAOAlbum.insertAlbum(oContainer, first);
		DBAlbum album = DAOAlbum.getAlbumByID(oContainer, 1);
		Assert.assertEquals("id mismatch", 1, album.getAlbumId());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAlbumByID(com.db4o.ObjectContainer, int)} .
	 * Test for unknown albumId
	 */
	@Test(expected = DatabaseObjectNotFoundException.class)
	public final void getAlbumByID_unknownId() throws GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		// Empty db contains no album, so any id has to fail
		DAOAlbum.getAlbumByID(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAlbumByID(com.db4o.ObjectContainer, int)} .
	 * Test with disconnected client
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAlbumByID_disconnectedClient()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		oContainer.close();
		// missing client should throw exception
		DAOAlbum.getAlbumByID(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAlbumByID(com.db4o.ObjectContainer, int)} .
	 * Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAlbumByID_shutDownServer()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		db4oServer.close();
		// missing server should throw exception
		DAOAlbum.getAlbumByID(oContainer, 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAllAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test if you retrieve a list of alba
	 */
	@Test
	public final void getAllAlbum() throws GeneralDatabaseException {
		// insert a bunch of alba
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title1", "Interpret1",
				new BigDecimal(3.99), "Label1", 42, null, null));
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title2", "Interpret2",
				new BigDecimal(3.99), "Label2", 42, null, null));
		DAOAlbum.insertAlbum(oContainer, new DBAlbum("Title3", "Interpret3",
				new BigDecimal(3.99), "Label3", 42, null, null));
		List<DBAlbum> llAlbum = DAOAlbum.getAllAlbum(oContainer);
		// there should at least be 3 alba
		assertTrue("Did not retrieve all alba", llAlbum.size() >= 3);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAllAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test with disconnected client
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAllAlbum_disconnectedClient()
			throws GeneralDatabaseException {
		oContainer.close();
		// missing client should throw exception
		DAOAlbum.getAllAlbum(oContainer);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#getAllAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAllAlbum_shutDownServer()
			throws GeneralDatabaseException {
		db4oServer.close();
		// missing server should throw exception
		DAOAlbum.getAllAlbum(oContainer);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#updateAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test if null as album-argument is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void updateAlbum_Null() throws IllegalArgumentException,
			GeneralDatabaseException,
			UniqueFieldValueConstraintViolationException {
		DAOAlbum.updateAlbum(oContainer, null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#updateAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test for successfully updating an album
	 */
	@Test
	public final void updateAlbum_success()
			throws UniqueFieldValueConstraintViolationException,
			IllegalArgumentException, GeneralDatabaseException,
			DatabaseObjectNotFoundException {
		DBAlbum album = new DBAlbum("Title1", "Interpret1",
				new BigDecimal(3.99), "Label", 42, null, null);
		DAOAlbum.insertAlbum(oContainer, album);

		album.setTitle("Title2");
		album.setInterpreter("Interpret2");

		DAOAlbum.updateAlbum(oContainer, album);

		// since we have only one album in the db the id should be 1
		DBAlbum updatedAlbum = DAOAlbum.getAlbumByID(oContainer, 1);

		Assert.assertTrue("Album not successfully updated", updatedAlbum
				.getTitle().compareTo("Title2") == 0);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#updateAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test with disconnected client
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void updateAlbum_disconnectedClient()
			throws GeneralDatabaseException {
		oContainer.close();
		// missing client should throw exception
		DAOAlbum.updateAlbum(oContainer, new DBAlbum());
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum#updateAlbum(com.db4o.ObjectContainer, de.fhkl.dbsm.zvafzwects.model.db.DBAlbum)}
	 * . Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void updateAlbum_shutDownServer()
			throws GeneralDatabaseException {
		db4oServer.close();
		// missing server should throw exception
		DAOAlbum.updateAlbum(oContainer, new DBAlbum());
	}

	// @Test
	// public final void getAlbumBySearch_success()
	// since this method is pretty complex and it would cost a lot of effort to
	// write that JUnit Test we skip that test and
	// tested it manually as detailed as we can

	/**
	 * Test method for {@link
	 * model.dao.DAOAlbum#getAlbumBySearch(com.db4o.ObjectContainer,
	 * List<model.db.DBCatchword>, List<model.db.DBCategory>, String, String)} .
	 * Test with disconnected client
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAlbumBySearch_disconnectedClient()
			throws GeneralDatabaseException {
		oContainer.close();
		// missing client should throw exception
		DAOAlbum.getAlbumBySearch(oContainer, null, null, "", "");
	}

	/**
	 * Test method for {@link
	 * model.dao.DAOAlbum#getAlbumBySearch(com.db4o.ObjectContainer,
	 * List<model.db.DBCatchword>, List<model.db.DBCategory>, String, String)} .
	 * Test with shut down server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getAlbumBySearch_shutDownServer()
			throws GeneralDatabaseException {
		db4oServer.close();
		// missing server should throw exception
		DAOAlbum.getAlbumBySearch(oContainer, null, null, "", "");
	}
}
