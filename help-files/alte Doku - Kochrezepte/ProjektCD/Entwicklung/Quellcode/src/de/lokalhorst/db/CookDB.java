/**
 * 
 */
package de.lokalhorst.db;

import de.lokalhorst.db.dto.CookDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Klasse für den Zugriff auf die Cook-Tabelle
 * 
 * @author Jochen Pätzold, Christoph Antes
 * @version 11.11.2010
 */
public class CookDB
{
    private static final String changePwd_query = "UPDATE cook SET pwd=? WHERE cook_id=?";
    private static final String login_query = "SELECT * FROM cook WHERE cook_name=? AND pwd=?";
    private static final String exist_query = "SELECT cook_name FROM Cook WHERE LOWER(cook_name)=?";
    private static final String byName_query = "SELECT * FROM cook WHERE cook_name=?";
    private static final String byID_query = "SELECT * FROM cook WHERE cook_id=?";
    private static final String register_query = "INSERT INTO cook values(cook_id_seq.nextval, ?, ?, false,  ?, ?)";
    private static final String setAdminStatus = "UPDATE cook SET is_admin=? WHERE cook_id=?";
    private static final String deleteCook = "DELETE FROM cook WHERE cook_id = ?";

    /**
     * Ändert das Passwort zu einer CookID
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_id
     *            ID des Kochs im System
     * @param cook_pwd
     *            Passwort des Kochs im System
     * 
     * @return boolean true: Änderung erfolgreich, false: sonst
     */
    public static boolean changePassword(final Connection cn, int cook_id,
	    String cook_pwd)
    {
	int numUpdates = 0; // Anzahl geänderter Datensätze

	try
	{
	    PreparedStatement ps = cn.prepareStatement(changePwd_query);
	    ps.setString(1, cook_pwd);
	    ps.setInt(2, cook_id);
	    numUpdates = ps.executeUpdate();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numUpdates == 1); // Änderung erfolgreich,wenn numUpdate == 1
    }

    /**
     * Liefert KochDTO wenn Name und Passwort zueinander passen, sonst null
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_name
     *            Name des Kochs im System
     * @param cook_pwd
     *            Passwort des Kochs im System
     * 
     * @return CookDTO mit Kochdaten oder null bei Misserfolg
     */
    public static CookDTO loginCook(final Connection cn, String cook_name,
	    String cook_pwd)
    {
	CookDTO cook = null;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(login_query);
	    ps.setString(1, cook_name);
	    ps.setString(2, cook_pwd);
	    ResultSet rs = ps.executeQuery(); // DB abfragen
	    if (rs.next()) // Prüfe auf Ergebnismennge
	    {
		cook = new CookDTO(rs);
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return cook;
    }

    /**
     * Prüft ob ein Koch(name) bereits existiert, wobei der Namensvergleich
     * nicht case-sensitive ist.
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_name
     *            Name des zu suchende Kochs
     * @return boolean
     */
    public static boolean existsName(final Connection cn, String cook_name)
    {
	boolean exists = false;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(exist_query);
	    ps.setString(1, cook_name.toLowerCase());
	    ResultSet rs = ps.executeQuery(); // DB abfragen
	    if (rs.next()) // Prüfe auf Ergebnismennge
	    {
		exists = true;
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return exists;
    }

    /**
     * Liefert ein CookDTO zu einem Namen, sonst null
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_name
     *            Name des zu suchende Kochs
     * @return CookDTO oder null
     */
    public static CookDTO findByName(final Connection cn, String cook_name)
    {
	CookDTO cook = null;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(byName_query);
	    ps.setString(1, cook_name);
	    ResultSet rs = ps.executeQuery(); // DB abfragen
	    if (rs.next())
	    {
		cook = new CookDTO(rs);
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return cook;
    }

    /**
     * Liefert ein CookDTO zu einer ID
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_id
     *            ID des zu suchende Kochs
     * @return CookDTO
     */
    public static CookDTO findByID(final Connection cn, int cook_id)
    {
	CookDTO cook = null;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(byID_query);
	    ps.setInt(1, cook_id);
	    ResultSet rs = ps.executeQuery(); // DB abfragen
	    if (rs.next()) // Hole Koch aus Ergenismenge, falls vorhanden
	    {
		cook = new CookDTO(rs);
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return cook;
    }

    /**
     * Anlegen eines neuen Koches
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param newCook
     *            CookDTO mit den Anmeldedaten
     * @return Stausmeldung ob das ANlegen erfolgreich war
     */
    public static boolean registerNewCook(final Connection cn, CookDTO newCook)
    {
	int numInserted = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(register_query);
	    ps.setString(1, newCook.getCook_name());
	    ps.setString(2, newCook.getPwd());
	    ps.setString(3, newCook.getSec_question());
	    ps.setString(4, newCook.getSec_answer());

	    numInserted = ps.executeUpdate(); // Einfügen in DB

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numInserted != 0);
    }

    /**
     * Die Funktion "searchByName" sucht anhand von einem oder mehreren
     * Suchbegriffen die Datenbank nach Köchen ab, auf welche die Suchkriterien
     * zutreffen. Die Suche ist NICHT case-sensitive
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur Datenbank
     * @param search_term_array
     *            Array mit Suchbegriffen
     * @return Collection von CookDTOs
     */
    public static Collection<CookDTO> searchByName(final Connection cn,
	    final String[] search_term_array)
    {
	final LinkedList<CookDTO> result = new LinkedList<CookDTO>();

	String sql_query = "SELECT * FROM cook WHERE lower(cook_name) LIKE ?";

	// Je nach Anzahl der Elemente im übergebenen Array wird an den
	// Query-String ein weiteres Suchkriterium angefügt
	for (int counter = 1; counter < search_term_array.length; counter++)
	{
	    sql_query = sql_query + " OR lower(cook_name) LIKE ?";
	}

	try
	{
	    PreparedStatement statement = cn.prepareStatement(sql_query);

	    // Prepared Statement: Alle ? durch die Suchbegriffe des Arrays
	    // ersetzen
	    for (int counter = 0; counter < search_term_array.length; counter++)
	    {
		statement.setString(counter + 1, "%"
			+ search_term_array[counter].toLowerCase() + "%");
	    }

	    ResultSet resultset = statement.executeQuery();

	    while (resultset.next())
	    {
		// Neues Objekt mit den Daten aus dem Results erstellen
		CookDTO dto = new CookDTO(resultset);

		// erstelltes Objekt zu der LinkedList hinzufügen
		result.add(dto);
	    }

	    resultset.close();

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	// Die LinkedList mit allen "gefundenen" Daten wird zurückgeliefert
	return result;
    }

    /**
     * Setzt den Adminstatus eines bestimmten Users
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur Datenbank
     * @param status
     *            neuer Status des Users
     * @param cook_id
     *            ID des zu ändernden Users
     * @return boolean true: Änderung erfolgreich, false: Änderungg nicht
     *         erfolgreich
     */
    public static boolean setAdminStatus(final Connection cn, boolean status,
	    int cook_id)
    {
	int numUpdates = 0; // Anzahl geänderter Datensätze

	try
	{
	    PreparedStatement statement = cn.prepareStatement(setAdminStatus);

	    // Prepared Statement: ? durch "status" und "cook_id" ersetzen
	    statement.setBoolean(1, status);
	    statement.setInt(2, cook_id);

	    numUpdates = statement.executeUpdate();

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numUpdates == 1); // Änderung erfolgreich,wenn numUpdate == 1
    }

    /**
     * Löscht den User mit der übergeben ID aus der Datenbank
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur DB
     * @param cook_id
     *            ID des zu löschenden Users
     * @return boolean true: Löschvorgang erfolgreich, false: Löschvorgang nicht
     *         erfolgreich
     * 
     */
    public static boolean deleteCook(final Connection cn, final int cook_id)
    {
	int numUpdates = 0; // Anzahl geänderter Datensätze

	try
	{
	    PreparedStatement statement = cn.prepareStatement(deleteCook);

	    // Prepared Statement: ? durch "cook_id" ersetzen
	    statement.setInt(1, cook_id);

	    numUpdates = statement.executeUpdate();

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numUpdates == 1); // Änderung erfolgreich,wenn numUpdate == 1
    }

}
