package de.fhkl.dbsm.zvafzwects.model.db;

/**
 * Utility class for sequence numbers in database
 * 
 * @author Andreas Baur, Jochen Pätzold
 */
public class Sequence {
	private int nextNumber;
	private String seqname;

	public Sequence(int startvalue, String name) {
		nextNumber = startvalue;
		seqname = name;
	}

	public int nextVal() {
		return nextNumber++;
	}

	public int currVal() {
		return nextNumber;
	}

	public String getName() {
		return this.seqname;
	}
}