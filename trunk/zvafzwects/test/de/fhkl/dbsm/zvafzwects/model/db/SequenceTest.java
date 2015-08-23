package de.fhkl.dbsm.zvafzwects.model.db;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 */
public class SequenceTest {

	private static Sequence seq;

	private final static int INIT_VAL = 0;
	private final static String INIT_NAME = "Counter";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		seq = new Sequence(INIT_VAL, INIT_NAME);
	}

	@Test
	public void nextVal() {
		int nextval = seq.nextVal();
		Assert.assertEquals("Get", 0, nextval);
	}

}
