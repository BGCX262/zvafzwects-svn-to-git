package de.lokalhorst.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stellt Funktionen für die Bearbeitung von Suchtermen zur Verfügung
 * @author Christoph Antes
 * @version 12.11.2010
 */
public class SearchTermPreparer
{
    public static String[] removeDuplicates(String[] input)
    {
	//Konvertierung des Arrays zu einer liste
	List<String> array_list = Arrays.asList(input);
	
	//Konvertierung zu einer Liste -> Duplikate werden direkt entfernt
	Set<String> set = new HashSet<String>(array_list);

	//Rückkonvertierung des Sets zu einem String-Array
	String[] output = new String[set.size()];
	set.toArray(output);
	
	return output;
    }
    
    
    /**
     * @author Christoph Antes
     * @param input
     * 		zu bearbeitendes Array
     * @param counter_not_space
     * 		Anzahl der Elemente mit "sinnvollen" Einträgen
     * @return
     */
    public static String[] prepareForSearch(String[] input, int counter_not_space)
    {
	//Neues Array: Die Anzahl der Elemente richtet sich nach der übergebenen Anzahl von nicht leeren Elementen
	String[] help_array = new String[counter_not_space];
	    
	//Füllen des Hilfsarrays mit den nicht leeren Elementen
	int counter_help=0;
	    
	 
	for(int counter=0; counter<input.length; counter++)
	{
	    if(!input[counter].equals(""))
	    {
		//Speichern im Hilfsarray. toLowerCase, da Suchbegriffe nicht Casesensitiv sind
		help_array[counter_help] = input[counter].toLowerCase();
		    
		counter_help++;
	    }
	}
	    
	    
	//Entfernen möglicher Duplicate aus dem Array
	String[] array_without_duplicates = SearchTermPreparer.removeDuplicates(help_array);
	
	return  array_without_duplicates;
    }
    
    
    /**
     * Prüft, ob Elemente eines Stringarrays weniger als 3 Zeichen enthalten
     * @param input
     * 		Stringarray
     * @return
     */
    public static boolean validTermLength(String[] input)
    {
	boolean valid = true;
	
	for(int counter=0; counter<input.length; counter++)
	{
	    if(input[counter].length() < 3)
	    {
		valid = false;
	    }
	}
	
	return valid;
    }
}
