package de.demoapp.command;

/** 
 *  Verbindliches Interface für alle Commands.
 *  
 *  @author Stefan Engel
 *  @version 2007-08-17
 */
public interface CommandInterface {
  
  /**
   * Führt das Command aus. Dabei wird das evtl. Model manipuliert
   * und die Ergebnisdaten in ein CommandOutput geschrieben
   * @param input Eingabeparameter als CommandInput-Objekt
   * @return Ergebnisdaten als CommandOutput-Objekt
   */
  public CommandOutput execute( CommandInput input ) throws Exception;

}