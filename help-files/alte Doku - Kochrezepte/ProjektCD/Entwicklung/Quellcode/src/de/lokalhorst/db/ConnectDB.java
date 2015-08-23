package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * Klasse zur Datenbank-Verbindungsverwaltung Es werden Methoden mit und ohne
 * Unterstützung eines Connection Pools angeboten
 * 
 * @author Christian Zöllner
 * @version 23.10.2010
 */

public class ConnectDB
{

    // DataSource zum Connection Pool vom Tomcat, ist Singleton
    private static DataSource ds = null;

    /**
     * Liefert eine Datenbankverbindung mit deaktiviertem Autocommit
     * 
     * @see getConnection(boolean autocommit)
     * @return Connection Objekt
     */
    public static Connection getConnection()
    {
	Connection cn = null;

	cn = getConnection(false);

	return cn;

    }

    /**
     * Methode die auf den ConnectionPool von Tomcat zugreift und eine
     * Verbindung zur Datenbank zurückgibt, AutoCommit ist deaktiviert
     * 
     * @see getPoolConnection(boolean autocommit)
     * @return Connection Object
     */
    public static Connection getPoolConnection()
    {
	Connection cn = null;

	cn = getPoolConnection(false);

	return cn;
    }

    /**
     * Methode die auf den ConnectionPool von Tomcat zugreift und eine
     * Verbindung zur Datenbank zurückgibt
     * 
     * @return Connection Objekt
     * @exception null
     *                als Rückgabewert
     */
    public static synchronized Connection getPoolConnection(boolean autoCommit)
    {
	Connection cn = null;

	if (ds == null) // Initialisierung
	{
	    try
	    {
		Context initCtx = new InitialContext();
		// Umgebungsvariablen beziehen, java:comp/env ist der Ort wo
		// diese liegen
		Context envCtx = (Context) initCtx.lookup("java:comp/env");

		// Zugriff auf die Ressource
		ds = (DataSource) envCtx.lookup("jdbc/KochrezepteDB");
	    } catch (Exception e)
	    {
		System.err.println("Fehler DatasSourceebeschaffung: " + e);

	    }
	}

	// Erstellung der eigentlichen Datenbankverbindung
	try
	{
	    cn = ds.getConnection();
	    cn.setAutoCommit(autoCommit);
	} catch (Exception e)
	{
	    System.err.println("Fehler bei DB CONNECT: " + e);

	}

	return cn;
    }

    /**
     * Liefert eine Datenbankverbindung
     * 
     * @param commit
     *            , AutoCommit An/Aus
     * @return Connection Objekt
     */
    public static Connection getConnection(boolean autocommit)
    {
	Connection cn = null;

	try
	{ // dbverbindung TBD mit ConnectionPool
	    Class.forName("com.sap.dbtech.jdbc.DriverSapDB");
	    cn = DriverManager
		    .getConnection("jdbc:sapdb://10.0.107.19/info_102",
			    "gruppe_a", "gruppe_a");
	    cn.setAutoCommit(autocommit);

	} catch (Exception e)
	{
	    System.err.println("Fehler bei DB CONNECT: " + e);
	}

	return cn;

    }

    /**
     * Schließt die übergebene Datenbankverbindung
     * 
     * @param cn
     *            Die zu schließende Verbindung
     */
    public static void closeConnection(Connection cn)
    {
	try
	{
	    cn.close();
	} catch (Exception e)
	{
	    System.err.println("FEHLER beim Schliessen der Verbindung: " + e);
	}
    }
}
