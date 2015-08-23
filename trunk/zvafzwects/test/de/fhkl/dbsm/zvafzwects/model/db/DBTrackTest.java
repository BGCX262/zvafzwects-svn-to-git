package de.fhkl.dbsm.zvafzwects.model.db;

import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBAudioFile;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBTrack;

/**
 * @author Andreas Baur
 * @verion 2
 */
public class DBTrackTest {

	private final static DBCD INIT_CD = new DBCD();
	private final static int INIT_TRACKNUMBER = 1;
	private final static String INIT_TITLE = "TestTitle";
	private final static long INIT_DURATION = 183;
	private final static DBAudioFile INIT_MP3TRACK = new DBAudioFile();

	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void createObject() throws Exception {
		new DBTrack(INIT_CD, INIT_TRACKNUMBER, INIT_TITLE, INIT_DURATION,
				INIT_MP3TRACK);
	}
}
