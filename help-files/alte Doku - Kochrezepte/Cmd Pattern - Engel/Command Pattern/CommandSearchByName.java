package command;

/**
 * Command soll Rezept nach bestimmtem Namen suchen und dann anzeigen
 * 
 * @author Chrsitan Zöllner
 * @version 0.1
 */
public class CommandSearchByName extends Command
{
	
	
	/** Konstruktor */
	public CommandSearchByName() 
	{
		
	}//Konstruktor
	
	/**
	 * Verweist nur auf die Seite zur Rezeptanzeige :-)
	 * 
     * @param input Eingabeparameter als CommandInput-Objekt
     * @return Ergebnisdaten als CommandOutput-Objekt
	 * @throws Exception 
	 */
	public CommandOutput execute( CommandInput input ) throws Exception 
	{
	//Hier wird nun in der DB nach dem namen gesucht
		
		//TBD
		//Aufruf der DB Klasse die eine Collection aus Rezepten zurückgibt
		//
		
		//Ergebnisobjekt anlegen
		CommandOutput output = new CommandOutput();
		
		//TBD
		//Suchbegriff steht im InputObjekt
		//Die Collection von Rezepten wird in das CommandOutput Objekt geschrieben
		//output.setResults(Die RezeptCollection)
		//oder
		//output.addValue("rezepte", DieRezeptCollection);
		//TBD , behalten wir hier die Hashmap für die Results oder nehmen wir andere Collection ???? 
		output.setTargetView( "RezepteMitName.jsp" );

		return output;

	}

}
