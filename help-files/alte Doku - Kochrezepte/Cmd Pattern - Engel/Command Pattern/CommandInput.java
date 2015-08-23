package de.demoapp.command;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class CommandInput {

	/** Die Parameter des Requests als Key-Value-Paare */
	private HashMap<String, Object> params = null;

	/** Das Session-Objekt des Benutzers */
	private HttpSession session;

	/** Locale-Objekt welches aus dem Request erzeugt wird (zwecks Lokalisierung) */
	private Locale locale;
	
	/** Logging-Objekt */
	private static Logger logger = Logger.getLogger( CommandInput.class );


	/**
	 * Konstruktor
	 * @param params Vorgegebene Parametermap
	 * @param session Vorgegebene Session
	 */
	public CommandInput( HashMap<String, Object> _params, HttpSession _session, Locale _locale ) {

		logger.debug( "CommandInput() start" );
		
		this.params = _params;
		this.session = _session;
		this.locale = _locale;

		logger.debug( "CommandInput() end" );

	}//ctor
	
	/**
	 * Konstruktor
	 * Muss die Parameter im Request-Objekt in die
	 * Map des CommandInput laden
	 * 
	 * @param req Das Request-Objekt mit den Eingabeparametern
	 */
	@SuppressWarnings("unchecked")
	public CommandInput( HttpServletRequest req ){
				
		logger.debug( "CommandInput() start" );

		//Parameter automatisch in die Map laden
		this.params = new HashMap<String, Object>( (HashMap<String, Object>)req.getParameterMap() );
		
		//Session speichern
		this.session = req.getSession();
		
		this.locale = req.getLocale();

		logger.debug( "CommandInput() end" );

	}//ctor
	
	/**
	 * Konstruktor
	 * Erzeugt ein CommandInput direkt aus einem CommandOutput
	 * (für CommandChains)
	 * @param output Das Output-Objekt vom vorher ausgeführten Command
	 * @param _session Session muss extra übergeben werden
	 * @param _locale Locale muss extra übergeben werden
	 */
	public CommandInput( CommandOutput output, HttpSession _session, Locale _locale ) {
		
		logger.debug( "CommandInput() start" );

		this.params = new HashMap<String, Object>( output.getResults() );
		this.session = _session;
		this.locale = _locale;
		
		logger.debug( "CommandInput() end" );

	}//ctor


	public HashMap<String, Object> getParams() {

		return params;
		
	}

	public void setParams( HashMap<String, Object> params ) {

		this.params = params;
		
	}

	public HttpSession getSession() {

		return session;
		
	}

	public void setSession( HttpSession session ) {

		this.session = session;
		
	}

	public Locale getLocale() {

		return locale;
		
	}

	public void setLocale( Locale locale ) {

		this.locale = locale;
		
	}
	
	/**
	 * Holt einen Parameter welcher im Request enthalten war.
	 * Diese Methode erleichtert den Zugriff da die ParameterMap String-Arrays enthält!
	 * @param key Name des Parameters
	 * @return Parameter als String
	 */
	public String getParam( String key ) {
		
		logger.debug( "getParam() start" );

		String returnValue = null;
		
		//Die Parameter kommen wirklich als Stringarray - warum auch immer!
		String[] myArray = null;
		myArray = (String[])this.params.get( key );
		
		if ( myArray != null && myArray.length > 0 ) {
			
			returnValue = myArray[0];
			
		}//if
		
		logger.debug( "getParam() end" );

		return returnValue;
		
	}//getParam

}// CommandInput
