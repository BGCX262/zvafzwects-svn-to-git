package de.demoapp.command;

import org.apache.log4j.Logger;

/** 
 *  Verweist nur auf die Loginseite. Wird immer aufgerufen wenn das
 *  Servlet einen Request ohne spezielle Parameter erhält.
 *  
 *  @author Stefan Engel
 *  @version 2007-08-17
 */
public class CommandIndex extends Command {

	private static Logger logger = Logger.getLogger( CommandIndex.class );
	
	/** Konstruktor */
	public CommandIndex() {
		
		logger.info( "CommandIndex() begin" );
		logger.info( "CommandIndex() end" );
		
	}//Konstruktor
	
	/**
	 * Verweist nur auf die Hello Seite
	 * 
     * @param input Eingabeparameter als CommandInput-Objekt
     * @return Ergebnisdaten als CommandOutput-Objekt
	 * @throws Exception 
	 */
	public CommandOutput execute( CommandInput input ) throws Exception {

		logger.info( "execute(...) begin" );

		//Ergebnisobjekt anlegen
		CommandOutput output = new CommandOutput();
		
		output.setTargetView( "welcome" );
			
		logger.info( "execute(...) end" );

		return output;

	}//execute

}//CommandIndex