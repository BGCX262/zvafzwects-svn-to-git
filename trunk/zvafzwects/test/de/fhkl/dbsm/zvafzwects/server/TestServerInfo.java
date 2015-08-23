package de.fhkl.dbsm.zvafzwects.server;

/**
 * An interface with the basic server information only for JUnit testing
 * purposes
 * 
 * @author Jochen Pätzold
 * @version 1
 */
public interface TestServerInfo {

	// the host to be used
	public final String HOST = "localhost";

	// the database file to be used by the server
	public final String FILE = "junit_db4o_test_file";

	// the port to be used by the server
	public final int PORT = 1338;

	// the user name for access control
	public final String USER = "junit";

	// the password for access control
	public final String PASS = "junit";
}
