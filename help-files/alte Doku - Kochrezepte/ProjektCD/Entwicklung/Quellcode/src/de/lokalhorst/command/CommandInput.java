package de.lokalhorst.command;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Kapselt die zu übergebenden Eingaben eines Commands
 * 
 * @author Christian Zöllner
 * @version 8.11.2010
 */
public class CommandInput
{

    /** Die Parameter des Requests als Key-Value-Paare */
    private HashMap<String, Object> params = null;

    /** Das Session-Objekt des Benutzers */
    private HttpSession session;

    /**
     * Konstruktor
     * 
     * @param params
     *            Vorgegebene Parametermap
     * @param session
     *            Vorgegebene Session
     * @param cn
     *            Verbindungsobjekt zur Datenbank
     */
    public CommandInput(HashMap<String, Object> _params, HttpSession _session)
    {
	this.params = _params;
	this.session = _session;

    }

    @SuppressWarnings("unchecked")
    public CommandInput(HttpServletRequest req)
    {

	// Parameter automatisch in die Map laden
	this.params = new HashMap<String, Object>(
		(HashMap<String, Object>) req.getParameterMap());

	// Session speichern
	this.session = req.getSession();

    }

    /**
     * Konstruktor Erzeugt ein CommandInput direkt aus einem CommandOutput (für
     * CommandChains)
     * 
     * @param output
     *            Das Output-Objekt vom vorher ausgeführten Command
     * @param _session
     *            Session muss extra �bergeben werden
     * @param cn
     *            Verbindungsobjekt zur Datenbank
     */
    public CommandInput(CommandOutput output, HttpSession _session)
    {

	this.params = new HashMap<String, Object>(output.getResults());
	this.session = _session;

    }

    public HashMap<String, Object> getParams()
    {

	return params;

    }

    public void setParams(HashMap<String, Object> params)
    {

	this.params = params;

    }

    public HttpSession getSession()
    {

	return session;

    }

    public void setSession(HttpSession session)
    {

	this.session = session;

    }

    /**
     * Holt einen Parameter welcher im Request enthalten war. Diese Methode
     * erleichtert den Zugriff da die ParameterMap String-Arrays enth�lt!
     * 
     * @param key
     *            Name des Parameters
     * @return Parameter als String
     */
    public String getParam(String key)
    {

	String returnValue = null;

	// Die Parameter als Stringarray
	String[] myArray = null;
	myArray = (String[]) this.params.get(key);

	if (myArray != null && myArray.length > 0)
	{

	    returnValue = myArray[0];

	}

	return returnValue;

    }

}