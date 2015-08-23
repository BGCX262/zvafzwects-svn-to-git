package de.lokalhorst.command;

import de.lokalhorst.helper.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Klasse zum Umrechen der Zutaten eines Rezepts
 * 
 * @Author: Christoph Antes
 * @version: 15.11.2010
 */
public class CommandCalculateAmounts extends Command
{

    public CommandOutput execute(CommandInput input) throws Exception
    {
	// Ergebnisobjekt anlegen
	CommandOutput output = new CommandOutput();

	// Stringarrays für Rezeptattribute, Zutaten und Zubereitungsschritte
	// erstellen. Trennzeichen ist "_"
	String[] recipeattributes = input.getParam("recipeattributes").split(
		"_");
	String[] ingredients = input.getParam("ingredients").split("_");
	String[] prepsteps = input.getParam("prepsteps").split("_");

	// Rezepthilfsklasse anlegen
	RecipeHelper result = new RecipeHelper(recipeattributes, ingredients,
		prepsteps);

	/*
	 * Mit der "NumberFormatException" wird geprüft, ob die Eingabe einer
	 * ganzen Zahl entspricht. Wird eine Exception ausgelöst, so weiß man,
	 * dass eine "falsche" Eingabe erfolgte.
	 */
	try
	{
	    // Personenanzahl
	    String per = input.getParam("persons");
	    int persons_new = new Integer(Integer.parseInt(per));

	    if (persons_new <= 0)
	    {
		output.setInfoMsg("Bitte geben Sie eine sinnvolle Personenzahl an");
	    } else
	    {
		// neue Mengen werden mittels der Funktion "calculateAmounts"
		// berechnet und gesetzt
		result.setIngredients(calculateAmounts(result.getIngredients(),
			result.getPersons(), persons_new));

		// neue Personenzahl setzen
		result.setPersons(persons_new);
	    }
	} catch (NumberFormatException e)
	{
	    System.err.println("Ein Fehler ist aufgetreten:" + e.toString());

	    // Errormessage wird gesetzt, um den Anwender auf seine falsche
	    // Eingabe aufmerksam zu machen
	    output.setInfoMsg("Bitte geben Sie zur Umrechnung eine ganze Zahl ein");
	}

	// Result wird zu dem Outputobjekt hinzugefügt
	output.addValue("recipe", result);

	output.setTargetView("ShowRecipe");

	return output;

    }

    private Collection<IngredientHelper> calculateAmounts(
	    Collection<IngredientHelper> input, int persons_old, int persons_new)
    {
	LinkedList<IngredientHelper> output = new LinkedList<IngredientHelper>();

	// Verhältnis der gewünschten Personenzahl zur "alten" Personenzahl
	double ratio = ((double) persons_new / persons_old);

	Iterator<IngredientHelper> iter = input.iterator();

	while (iter.hasNext())
	{
	    // obj referenziert das IngredientHelper-Objekt, auf das der
	    // Iterator gerade "zeigt"
	    IngredientHelper obj = (IngredientHelper) iter.next();

	    /*
	     * neue Zutatenmenge: wird berechnet aus "alter" Menge multipliziert
	     * mit dem Verhältnisfaktor "ratio". Es wird immer aufgerundet
	     */
	    double help_amount = Math.ceil(obj.getAmount() * ratio);

	    int amount = (int) (help_amount);

	    // neue Zutatenmenge wird im lokalen Hilfsobjekt gesetzt
	    obj.setAmount(amount);

	    // Hinzufügen des lokalen Hilfsobjekts zu der Liste
	    output.add(obj);
	}

	return output;
    }

}
