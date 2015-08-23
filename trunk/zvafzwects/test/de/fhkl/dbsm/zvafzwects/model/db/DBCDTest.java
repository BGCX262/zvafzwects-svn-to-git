package de.fhkl.dbsm.zvafzwects.model.db;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBTrack;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2.1
 */
public class DBCDTest {

	private static DBCD cd;

	private final static DBAlbum INIT_ALBUM = new DBAlbum();
	private final static int INIT_CDNUMBER = 1;
	private static DBTrack INIT_TRACK = new DBTrack();

	@BeforeClass
	public static void setUpBeforeClass() {
		cd = new DBCD(INIT_ALBUM, INIT_CDNUMBER);
	}
	
	@Test
	public void addTrack() {
		cd.addTrack(INIT_TRACK);
		Assert.assertEquals("assTrack", INIT_TRACK, cd.getTrack(0));
		Assert.assertTrue("trackCount", cd.getNumberOfTracks() == 1);
	}

}
