package de.demoapp.command;

import org.apache.log4j.Logger;

import de.demoapp.hibernate.dao.PersonHome;
import de.demoapp.hibernate.mapping.Person;

/** 
 *  Hello World Command
 *  
 *  @author Stefan Engel
 *  @version 2008-10-01
 */
public class CommandHello extends Command {

	private static Logger logger = Logger.getLogger( CommandHello.class );
	
	/** Konstruktor */
	public CommandHello() {
		
		logger.info( "CommandHello() begin" );
		logger.info( "CommandHello() end" );
		
	}//Konstruktor
	
	/**
	 * Läd eine Person aus der DB und verweist auf die Hello Seite
	 * 
     * @param input Eingabeparameter als CommandInput-Objekt
     * @return Ergebnisdaten als CommandOutput-Objekt
	 * @throws Exception 
	 */
	public CommandOutput execute( CommandInput input ) throws Exception {

		logger.info( "execute(...) begin" );

		//Ergebnisobjekt anlegen
		CommandOutput output = new CommandOutput();
		
		//Zielseite festlegen
		output.setTargetView( "hello" );
		
		//Person aus der DB laden
		PersonHome myPersonDAO = new PersonHome();
		Person myPerson = null;
		
		try {

			myPerson = myPersonDAO.findById(1);
		
		} catch ( Exception e ) {
			
			//Exceptionmeldung zum Anzeigen speichern
			output.setErrorMsg( "Fehler: " + e.getMessage() );
			
			//Exception nach oben weiterreichen
			throw e;
		
		}
		
		output.addValue( "person", myPerson );
		
		//Infomeldung (optional, es gibt ebenfalls output.setErrorMsg(...))
		output.setInfoMsg( "Ich bin eine Infomeldung. Z.B. \"Sie wurden ausgeloggt!\"" );
			
		output.addValue( "helloText", "Hello World!" );
		
		logger.info( "execute(...) end" );

		return output;

	}//execute

}//CommandHello