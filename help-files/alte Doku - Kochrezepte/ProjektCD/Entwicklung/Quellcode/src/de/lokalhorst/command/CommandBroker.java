package de.lokalhorst.command;

import java.util.HashMap;

/**
 * Ermöglicht dem Servlet mittels der lookUp()-Methode das passende Command zu
 * einem Request zu finden. Dabei hat der CommandBroker von jedem registrierten
 * Command bereits eine Instanz in der CommandList
 * 
 * @author Christian Zöllner
 * @version 12.11.2010
 * 
 */
public class CommandBroker
{

    /** Einzige Instanz (der Broker ist ein Singleton) */
    private static CommandBroker instance = null;

    /** Liste aller Commands (Commandname - Commandobjekt) */
    private HashMap<String, Command> commandList = new HashMap<String, Command>();

    private CommandBroker()
    {
	// Vorhandene Commands instanziieren und registrieren
	this.register("AddRecipeToBuyList", new CommandAddRecipeToBuyList());
	this.register("CalculateAmounts", (new CommandCalculateAmounts()));
	this.register("ClearBuyList", new CommandClearBuyList());
	this.register("CreateRecipe", new CommandCreateRecipe());
	this.register("DeleteCook", new CommandDeleteCook());
	this.register("EditCook", new CommandEditCook());
	this.register("EditRecipe", new CommandEditRecipe());
	this.register("LoginCook", new CommandLogin());
	this.register("LogoutCook", new CommandLogout());
	this.register("PrintSite", new CommandPrintSite());
	this.register("Redirect", new CommandRedirect());
	this.register("RegisterCook", new CommandRegister());
	this.register("SaveBuyList", new CommandSaveBuyList());
	this.register("SearchCook", new CommandSearchCook());
	this.register("SearchOwnRecipe", new CommandSearchOwnRecipe());
	this.register("SearchRecipe", (new CommandSearchRecipe()));
	this.register("SecretName", new CommandSecretName());
	this.register("SecretQuestion", new CommandSecretQuestion());
	this.register("ShowBuyList", new CommandShowBuyList());
	this.register("ShowRecipe", new CommandShowRecipe());
    }

    /**
     * Holt das passende Command-Objekt
     * 
     * @param commandName
     *            Name des geforderten Commands
     * @return Referenz zum Command-Objekt
     */
    public Command lookUp(String commandName) throws Exception
    {
	if (commandList.get(commandName) == null)
	{
	    throw new Exception("Command " + commandName + " not found!");
	}

	return (Command) (commandList.get(commandName));

    }

    /**
     * Nimmt ein Command-Objekt in die Liste auf
     * 
     * @param name
     *            Name des Commands
     * @param command
     *            Command-Objekt
     */
    public void register(String name, Command command)
    {
	commandList.put(name, command);

    }

    /**
     * Gibt eine Intanz des CommandBrokers zurück
     * 
     * @return Instanz des CommandBrokers
     */
    public static CommandBroker getInstance()
    {

	if (instance == null)
	{
	    instance = new CommandBroker();
	}

	return instance;

    }

}