package de.demoapp.command;

import java.util.HashMap;

import org.apache.log4j.Logger;


/** 
 *  Ermöglicht dem Servlet mittels der lookUp()-Methode das passende Command
 *  zu einem Request zu finden. Dabei hat der Commandbroker von jedem
 *  registrierten Command bereits eine Instanz in der commandList
 *    
 *  @author Stefan Engel
 *  @version 2007-08-17
 */
public class CommandBroker {
	
  
	/** Einzige Instanz (der Broker ist ein Singleton) */
	private static CommandBroker instance = null;

	/** Liste aller Commands (Commandname - Commandobjekt) */
	private HashMap<String, Command> commandList = new HashMap<String, Command>();
	
	private static Logger logger = Logger.getLogger( CommandBroker.class );

  
	private CommandBroker() {
	  
		logger.info( "CommandBroker() begin" );

		
		//Vorhandene Commands instanziieren und registrieren
		this.register( "index", ( new CommandIndex() ) );
		this.register( "hello", ( new CommandHello() ) );
		
		logger.info( "CommandBroker() end" );

	}//Konstruktor

	/**
	 * Holt das passende Command-Objekt
	 * 
	 * @param commandName Name des geforderten Commands
	 * @return Referenz zum Command-Objekt
	 */
	public Command lookUp( String commandName ) throws Exception {

		logger.info( "lookUp(" + commandName + ") begin" );
		logger.info( "lookUp(" + commandName + ") end" );

		if ( commandList.get( commandName ) == null ){
			
			throw new Exception( "Command " + commandName + " not found!" );
		
		}//if
		
		return (Command)( commandList.get( commandName ) );

	}//lookUp

	/**
	 * Nimmt ein Command-Objekt in die Liste auf
	 * 
	 * @param name Name des Commands
	 * @param command Command-Objekt
	 */
	public void register( String name, Command command ) {
	  
		logger.info( "register(...) begin" );

		commandList.put( name, command );
		
		logger.info( "register(...) end" );
	  
	}//register
	
	public static CommandBroker getInstance() {
		
		logger.info( "getInstance() begin" );

		if ( instance == null ){
			
			instance = new CommandBroker();
			
		}

		logger.info( "getInstance() end" );

		return instance;
		
	}//getInstance
  
}//CommandBroker