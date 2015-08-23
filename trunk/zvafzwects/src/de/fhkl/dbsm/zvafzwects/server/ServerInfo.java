package de.fhkl.dbsm.zvafzwects.server;

/**
 *ServerInfo contains basic server parameters.
 * 
 * @author Andreas Baur
 * @version 1
 *
 */
public interface ServerInfo {
	
	/**
	 * the host to be used
	 */
	public final String HOST = "localhost";
	
	/**
	 * the database file to be used by the server
	 */
	public final String FILE = "db4o_database_file";
	
	/**
	 * the port to be used by the server
	 */
	public final int PORT = 1337;
	
	/**
	 * the user name for access control
	 */
	public final String USER = "db4oadmin";
	
	/**
	 * the password for access control
	 */
	public final String PASS = "admindb4o";
	
	/**
	 * initial value for the admin email
	 */
	final String ADMIN_EMAIL = "admin@dbs.ma";
	
	/**
	 * initial value for the admin password
	 */
	final String ADMIN_PASS = "root1337";
}
