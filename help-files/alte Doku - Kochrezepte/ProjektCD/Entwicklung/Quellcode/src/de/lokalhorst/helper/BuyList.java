package de.lokalhorst.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Klasse die ein e Einkaufsliste abbildet, sie beinhaltet die hinzugefügten
 * Rezepte und eine Aufsummierung aller benötigten Zutaten für die Rezepte
 * 
 * @author Christian Zöllner
 * @version 09.11.2010
 */

public class BuyList
{
    private ArrayList<BuyListEntry> buylist; // rezepte
    private TreeMap<String, IngredientHelper> summap; // aufsummierung

    public BuyList()
    {
	this.buylist = new ArrayList<BuyListEntry>(20);
	this.summap = new TreeMap<String, IngredientHelper>();
    }

    /**
     * Methode die, die Gesamtzahl der jeweils benötigten Zutaten um das
     * übergebene Zutatenobjekt ergänzt. Hierbei werden, falls die übergebene
     * Zutat schon auf der Liste steht, die bisherige Anzahl und die übegebene
     * Anzahl addiert. Falls die Zutat noch nicht vorhandne ist, wird die Liste
     * um die neue Zutat ergänzt
     * 
     * @param ingred
     *            die neue Zutat
     */
    public void sumUp(IngredientHelper ingred)
    {
	IngredientHelper ingred_old = this.summap.get(ingred.getIngr_name());

	if (ingred_old != null)// zutat schon vorhanden
	{// umkopieren der alten zutat, wegen call by reference ^^
	    IngredientHelper ingred_new = new IngredientHelper(
		    ingred.getAmount() + ingred_old.getAmount(),
		    ingred.getUnit_name(), ingred.getIngr_name());
	    this.summap.remove(ingred_old.getIngr_name());
	    this.summap.put(ingred_new.getIngr_name(), ingred_new);
	} else
	// neue zutat
	{
	    // neues Objekt, sonst in summap und buylist die selben objekte =>
	    // seiteneffekte
	    this.summap.put(
		    ingred.getIngr_name(),
		    new IngredientHelper(ingred.getAmount(), ingred
			    .getUnit_name(), ingred.getIngr_name()));
	}

    }

    /**
     * Methode die, die Gesamtzahl der jeweils benötigten Zutaten um die im
     * übergebenen BuyListEntry Objekt enthaltenen Zutaten ergänzt. Hierbei
     * werden, falls die übergebene Zutat schon auf der Liste steht, die
     * bisherige Anzahl und die übegebene Anzahl addiert. Falls die Zutat noch
     * nicht vorhandne ist, wird die Liste um die neue Zutat ergänzt
     * 
     * @param entry
     *            Einkaufslisteintrag
     */
    public void sumUp(BuyListEntry entry)
    {
	Iterator<IngredientHelper> iter = entry.getIngredients().iterator();

	while (iter.hasNext())
	{
	    this.sumUp(iter.next()); // summieren über alle zutaten
	}
    }

    /**
     * 
     * Methode fügt einen Rezeptnamen und Rezeptzutaten der Einkaufsliste hinzu.
     * Auf der Liste können maximal 20 Rezepte stehen. Ist ein Rezept schon
     * vorhanden, werden die Zutaten aufsummiert
     * 
     * @param entry_new
     *            der hinzuzufügende Eintrag
     * @param buylist
     *            die Einkaufsliste
     * @return int 1 wenn Liste schon voll ist 0 wenn hinzufügen erfolgreich -1
     *         wenn hinzufügen gescheitert
     */
    public int add(BuyListEntry entry_new)
    {
	for (int i = 0; i < this.buylist.size(); i++)
	{
	    // Prüfe ob Rezept schon vorhanden
	    if (this.buylist.get(i).getRecp_id() == entry_new.getRecp_id())
	    {

		BuyListEntry entry_old = this.buylist.get(i);
		entry_old.setQuantity(entry_old.getQuantity()
			+ entry_new.getQuantity()); // personenzahl hochzählen

		Iterator<IngredientHelper> iter_entry_old = entry_old
			.getIngredients().iterator();
		Iterator<IngredientHelper> iter_entry_new = entry_new
			.getIngredients().iterator();

		// Falls Rezept schon vorhanden werden die Rezeptzutaten
		// aufsummiert
		while (iter_entry_old.hasNext())
		{
		    IngredientHelper ingred_old = iter_entry_old.next();
		    IngredientHelper ingred_new = iter_entry_new.next();

		    ingred_old.setAmount(ingred_old.getAmount()
			    + ingred_new.getAmount());
		    // Übergabe von ingred_new weil die summap nur um die Anzahl
		    // von ingred_new summiert werden muss
		    this.sumUp(ingred_new);
		}
		return 0;
	    }
	}
	// Prüfe ob Liste voll
	if (this.buylist.size() == 20)
	{
	    return 1;
	}
	// Hinzufügen wenn Rezept neu
	if (this.buylist.add(entry_new))
	{
	    // Summieren für die Gesamtzahl der zutaten
	    this.sumUp(entry_new);
	    return 0;
	}

	return -1; // Wenn nicht hinzugefügt

    }

    /**
     * Methode leert gesamte Einkaufsliste
     */
    public void clear()
    {
	this.buylist.clear();
	this.summap.clear();
    }

    public boolean isEmpty()
    {
	return this.buylist.isEmpty();
    }

    public ArrayList<BuyListEntry> getBuylist()
    {
	return buylist;
    }

    public void setBuylist(ArrayList<BuyListEntry> buylist)
    {
	this.buylist = buylist;
    }

    public TreeMap<String, IngredientHelper> getSummap()
    {
	return summap;
    }

    public void setSummap(TreeMap<String, IngredientHelper> summation)
    {
	this.summap = summation;
    }

}
