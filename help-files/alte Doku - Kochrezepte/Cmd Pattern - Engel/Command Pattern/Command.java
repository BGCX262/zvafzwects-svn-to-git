package de.demoapp.command;

/** 
 * Hiervon werden alle Command-Klassen abgeleitet. Jede muss die Methode execute überschreiben!
 *  
 * @author Stefan Engel
 * @version 2007-08-17
 */
public abstract class Command implements CommandInterface{
		
    /**
     * Führt das Command aus. Dabei wird evtl. das Model manipuliert
     * und die Ergebnisdaten in ein CommandOutput geschrieben.
     * Jedes Command muss diese Methode überschreiben!
     * 
     * @param input Eingabeparameter als CommandInput-Objekt
     * @return Ergebnisdaten als CommandOutput-Objekt
     * @throws Exception 
     */
    public CommandOutput execute( CommandInput input ) throws Exception {

    	return null;

    }//execute

}//Command