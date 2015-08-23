package de.fhkl.dbsm.zvafzwects.model.dao;

import java.awt.image.BufferedImage;

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

import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCover;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.TestServerInfo;


/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 * @category JUnit
 */
public class DAOCoverTest implements TestServerInfo {

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
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCover#getCoverByAlbumId(com.db4o.ObjectContainer, int)}
	 * . Test with valid album id and cover found
	 */
	@Test
	public final void getCoverByAlbumId_validAlbumId()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		
		DBAlbum album = new DBAlbum();
		album.setAlbumId(1);

		DBCover cover = new DBCover(new BufferedImage(160, 160,
				BufferedImage.TYPE_INT_RGB));

		album.setCover(cover);
		DAOAlbum.insertAlbum(oContainer, album);

		DBCover result = DAOCover.getCoverByAlbumId(oContainer, 0);
		Assert.assertTrue("Cover not found, but one was expected",
				result != null);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCover#getCoverByAlbumId(com.db4o.ObjectContainer, int)}
	 * . Test with invalid album id or no cover found
	 */
	@Test(expected = DatabaseObjectNotFoundException.class)
	public final void getCoverByAlbumId_invalidAlbumId()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		DBAlbum album = new DBAlbum();
		album.setAlbumId(1);
		
		DBCover cover = new DBCover(new BufferedImage(160, 160,
				BufferedImage.TYPE_INT_RGB));

		album.setCover(cover);
		DAOAlbum.insertAlbum(oContainer, album);

		DAOCover.getCoverByAlbumId(oContainer, 2);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCover#getCoverByAlbumId(com.db4o.ObjectContainer, int)}
	 * . Test without client connection
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCoverByAlbumId_disconnectedClient()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		oContainer.close();
		DAOCover.getCoverByAlbumId(oContainer, 1);
	}

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOCover#getCoverByAlbumId(com.db4o.ObjectContainer, int)}
	 * . Test with shutdown server
	 */
	@Test(expected = GeneralDatabaseException.class)
	public final void getCoverByAlbumId_shutdownServer()
			throws GeneralDatabaseException, DatabaseObjectNotFoundException {
		db4oServer.close();
		DAOCover.getCoverByAlbumId(oContainer, 1);
	}

}
