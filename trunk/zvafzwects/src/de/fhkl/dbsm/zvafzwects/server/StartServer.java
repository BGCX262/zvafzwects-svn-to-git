package de.fhkl.dbsm.zvafzwects.server;

import java.text.NumberFormat;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.messaging.MessageContext;
import com.db4o.messaging.MessageRecipient;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOUser;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.utils.hash.Whirlpool;

/**
 * Class for starting the server.
 * 
 * @author Andreas Baur
 * @version 1
 */
public class StartServer implements ServerInfo, MessageRecipient {

	/**
	 * setting the value to true denotes that the server should be closed
	 */
	private boolean stop = false;

	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();
		System.out.println("max mem : "
				+ NumberFormat.getInstance().format(rt.maxMemory() / 1024)
				+ " MB in use");
		new StartServer().runServer();
	}

	/**
	 * start the server.
	 */
	public void runServer() {
		synchronized (this) {
			// get the configuration for the database
			ServerConfiguration config = Configuration.getServerConfiguration();
			// using the messaging functionality to redirect all messages to
			// this.processMessage
			config.networking().messageRecipient(this);
			// open the server
			ObjectServer db4oServer = Db4oClientServer.openServer(config, FILE,
					PORT);
			// grant access for user
			db4oServer.grantAccess(USER, PASS);
			
			// to identify the thread in a debugger
			Thread.currentThread().setName(this.getClass().getName());
			// db4o server has it's own thread, therefore low priority can be
			// used
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

			System.out.println("server runs @" + HOST + ":" + PORT);

			// injects a default admin
			injectAdmin(db4oServer);
			
			try {
				if (!stop) {
					// wait forever for notify() from close()
					this.wait(Long.MAX_VALUE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			db4oServer.close();
		}
	}

	/**
	 * messaging callback.
	 * 
	 * @param con
	 *            the message context
	 * @param message
	 *            the message object
	 * 
	 * @see com.db4o.messaging.MessageRecipient#processMessage(MessageContext,Object)
	 */
	public void processMessage(MessageContext con, Object message) {
		if (message instanceof StopServer) {
			close();
		}
	}

	/**
	 * closes the server.
	 */
	public void close() {
		synchronized (this) {
			stop = true;
			this.notify();
		}
	}
	
	/**
	 * checks if a special admin is in db, if not, it will be created
	 * 
	 * @param db4oServer the object server
	 */
	private void injectAdmin(ObjectServer db4oServer) {
		ObjectContainer connection = db4oServer.openClient();
		Configuration.initializeDB(connection);
		
		String hashedPw = Whirlpool.hash(ADMIN_PASS);
		try {
			DAOUser.getUserByCredentials(connection, ADMIN_EMAIL, hashedPw);
			System.out.printf("Default admin found with \"%s\", \"%s\"\n", ADMIN_EMAIL, ADMIN_PASS);
		} catch (DatabaseObjectNotFoundException e) {
			try {
				 DAOUser.insertUser(connection, new DBUser("", "", ADMIN_EMAIL, hashedPw, null, 'm', true));
				System.out.printf("Default admin created with \"%s\", \"%s\"\n", ADMIN_EMAIL, ADMIN_PASS);
			} catch (UniqueFieldValueConstraintViolationException ex) {
			} catch (IllegalArgumentException ex) {
			} catch (GeneralDatabaseException ex) {
			}
		} catch (GeneralDatabaseException e) {
		} finally {
			connection.commit();
			connection.close();
		}
	}
}
