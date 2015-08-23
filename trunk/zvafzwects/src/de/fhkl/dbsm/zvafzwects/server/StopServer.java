package de.fhkl.dbsm.zvafzwects.server;

import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.messaging.MessageSender;

/**
 * Class for stopping the server.
 * 
 * @author Andreas Baur
 * @version 1
 */
public class StopServer implements ServerInfo {

	/**
	 * @throws Exception
	 */
	public static void main(String[] args) {
		ObjectContainer con = null;
		try {
			ClientConfiguration config = Configuration.getClientConfiguration();
			con = Db4oClientServer.openClient(config, HOST, PORT, USER, PASS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (con != null) {
			// get the messageSender for the ObjectContainer
			MessageSender messageSender = con.ext().configure().clientServer()
					.getMessageSender();
			// send an instance of a StopServer object
			messageSender.send(new StopServer());
			try {
				con.close();
			} catch (DatabaseClosedException ex) {
			}

			System.out.println("server stopped");
		}
	}
}
