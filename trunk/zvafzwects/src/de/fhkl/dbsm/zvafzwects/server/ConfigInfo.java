package de.fhkl.dbsm.zvafzwects.server;

/**
 * ConfigInfo contains database related initial values.
 * 
 * @author Andreas Baur
 * @version 0.1
 */
public interface ConfigInfo {

	/**
	 * value for the activation depth. set the activation depth to 3 so that no
	 * mp3Track is loaded when an album is searched for or viewed unless the
	 * object is activated, same deal for cover (cover is loaded though, because
	 * it's loaded separately)
	 */
	final int ACTIVATION_DEPTH = 3;

	/**
	 * value for the update depth
	 */
	final int UPDATE_DEPTH = 15;

	/**
	 * initial value for the sequences
	 */
	final int SEQUENCE_INIT = 1;
}
