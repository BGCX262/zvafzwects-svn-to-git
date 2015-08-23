package de.demoapp.command;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Kapselt die Ausgaben eines Commands
 * 
 * @author Stefan Engel
 * @version 2008-06-30
 */
public class CommandOutput {

	private HashMap<String, Object> results;

	/** Name der Zielseite ( "setTemplate" führt z.B. auf setTemplate.jsp ) */
	private String targetView;
	
	/** Optionale Fehlermeldung */
	private String errorMsg;
	
	/** Optionale Infomeldung */
	private String infoMsg;
		
	/** Logging-Objekt */
	private static Logger logger = Logger.getLogger( CommandOutput.class );

	public CommandOutput() {
		
		logger.debug( "CommandOutput() start" );

		this.results = new HashMap<String, Object>();
		this.targetView = null;

		logger.debug( "CommandOutput() end" );

	}//ctor

	
	public HashMap<String, Object> getResults() {
	
		return results;
		
	}

	
	public void setResults( HashMap<String, Object> results ) {
	
		this.results = results;
		
	}
		
	public void addValue( String key, Object value ) {
		
		this.results.put( key, value );
		
	}
	
	public String getTargetView() {
	
		return targetView;
		
	}
	
	public void setTargetView( String targetView ) {
	
		this.targetView = targetView;
		
	}

	public String getErrorMsg() {
	
		return errorMsg;
	}
	
	public void setErrorMsg( String errorMsg ) {
	
		this.errorMsg = errorMsg;
	}
	
	public String getInfoMsg() {
	
		return infoMsg;
	}
	
	public void setInfoMsg( String infoMsg ) {
	
		this.infoMsg = infoMsg;
	}
	
}//CommandOutput
