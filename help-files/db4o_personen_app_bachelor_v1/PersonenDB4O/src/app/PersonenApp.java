package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Date;
import java.util.Scanner;

import com.db4o.*;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraint;

import dbklassen.*;

/*
 * @author
 * Andreas A. Baur
 */
public class PersonenApp {
	
	private static Scanner sIn = new Scanner(System.in);
	static BufferedReader bR = null;
	
	// private static DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.GERMANY);
	
	public static int readInt() throws InputMismatchException
	{
		return sIn.nextInt();
	} // end of readInt;a

	public static void printMenu()
	{
		System.out
				.print("\nBitte eine der folgenden Optionen waehlen:\n\n"
						+ "a - alle Personen anzeigen (nur Stammdaten)\n"
						+ "b - eine Person mit allen Daten anzeigen (ueber die persnr)\n"
						+ "c - alle Daten der Personen mit gleichem Namen anzeigen\n"
						+ "d - eine neue Person anlegen (komplett)\n"
						+ "e - eine Person komplett aus der PersonenDB loeschen\n"
						+ "f1 - Stammdaten aktualisieren\n"
						+ "f2 - neues Hobby/Adresse/Beruf/Ausbildung zu einer Person hinzufuegen\n"
						+ "f3 - Hobby/Adresse/Beruf/Ausbildung loeschen\n"
						+ "m - Menu anzeigen\n"
						+ "0 - Beenden\n\n");
	} // end of printMenu
	
	public static boolean printPersonList(ObjectContainer con)
	{
		LinkedList<DBPersonen> personOverview = DAOPersonen.selectAllPersons(con);
		if ( personOverview.size() == 0 )
		{
			System.out.println("Keine Personen in der Datenbank vorhanden!");
			return false;
		}
		for(DBPersonen p : personOverview)
		{
			p.printInfo();
		}
		return true;
	} // end of printPersonList

	public static void printPersonByPersnr(ObjectContainer con, int persnr)
	{
			DBPersonen person = DAOPersonen.selectPersonByPersnr(con, persnr);
			System.out.println("Person(en):");
			person.printInfo();
			System.out.println("\nAdresse(n):");
			person.printAdressen();
			System.out.println("\nAusbildung(en):");
			person.printAusbildungen();
			System.out.println("\nBeruf(e):");
			person.printBerufe();
			System.out.println("\nHobby(s):");
			person.printHobbies();
	} // end of printPersonByPersnr
	
	public static void printPersonByName(ObjectContainer con, String name)
	{
		LinkedList<DBPersonen> lperson = DAOPersonen.selectPersonByName(con, name);
		
		System.out.println("Person(en):");
		for(DBPersonen o : lperson)
		{
			System.out.println(o.getName());
			o.printInfo();
		}
		System.out.println("\nAdresse(n):");
		for(DBPersonen o : lperson)
		{
			o.printAdressen();
		}
		System.out.println("\nAusbildung(en):");
		for(DBPersonen o : lperson)
		{
			o.printAusbildungen();
		}
		System.out.println("\nBeruf(e):");
		for(DBPersonen o : lperson)
		{
			o.printBerufe();
		}
		System.out.println("\nHobby(s):");
		for(DBPersonen o : lperson)
		{
			o.printHobbies();
		}
	} // end of printPersonByName
	
	public static void menu(ObjectContainer con) throws NumberFormatException, IOException{
		printMenu();
		
		while (true)
		{
			System.out.print("\nAuswahl (m fuer Menue): ");

			// read user-input
			String input = bR.readLine();
			char[] inputArr = input.toCharArray();
			char userChoice;

			// catch f-options
			if (inputArr[0] == 'f')
				userChoice = inputArr[1];
			else
				userChoice = inputArr[0];

			// MENU-OPTIONS
			switch (userChoice)
			{
			case 'a': 	//Liste mit Personenstammdaten
						System.out.print("\n");
						printPersonList(con);
						break;
			case 'b': 	//Person über persnr aussuchen und ausgeben
						System.out.println("\nBitte eine der folgenden Personen auswählen:\n");
						if ( printPersonList(con) )
						{
							System.out.print("\nAuswahl: ");

							int chosenPersnr = Integer.parseInt(bR.readLine());

							DAOPersonen.selectPersonByPersnr(con, chosenPersnr).printAll();
						}
						break;

			case 'c': 	//Liste von Personen mit "name"
						System.out.print("\nBitte gesuchten Namen eingeben: ");
						String chosenName = bR.readLine();
						Collection<DBPersonen> personsWithName = DAOPersonen.selectPersonByName(con, chosenName);
						while (personsWithName.isEmpty())
						{
							System.err.print("FEHLER: Es sind keine Personen unter diesem Namen zu finden!");
							System.out.print("Bitte waehlen Sie aus folgenden Moeglichkeiten:\n");
							if ( !printPersonList(con) )
								break;
							chosenName = bR.readLine();
							personsWithName = DAOPersonen.selectPersonByName(con,chosenName);
						}
						for(DBPersonen p : personsWithName)
							p.printInfo();
						break;

			case 'd': 	//Person einfuegen
						System.out.print("Geben Sie nun die gewünschten Daten zur Person ein:\n");
					
						DBPersonen thePerson = buildPersonFromInput();
																		
						System.out.println("\nGeben Sie nun die Adressdaten ein:");
						do
						{
							System.out.print("Weitere Adresse hinzufuegen? [j/n]: ");
							if(bR.readLine().equals("j") || bR.readLine().equals("J"))
							{
								thePerson.addAdress(buildAdressFromInput());
								continue;
							}
							else
								break;
						}while (true);
					
						System.out.println("\nGeben Sie nun die Berufsdaten ein:");
						do
						{						
							System.out.print("Weiteren Beruf hinzufuegen? [j/n]: ");
							if(bR.readLine().equals("j") || bR.readLine().equals("J"))
							{
								thePerson.addBeruf(buildBerufFromInput());
								continue;
							}
							else
								break;
						}while (true);
						
						System.out.println("\nGeben Sie nun die Ausbildungsdaten ein:");
						do
						{
							System.out.print("Weitere Ausbildung hinzufuegen? [j/n]: ");
							if(bR.readLine().equals("j") || bR.readLine().equals("J"))
							{
								thePerson.addAusbildung(buildAusbildungenFromInput());
								continue;
							}
							else
								break;
						}while (true);
						
						System.out.println("\nGeben Sie nun die Hobbydaten ein:");
						do
						{
							System.out.print("Weiteres Hobby hinzufuegen? [j/n]: ");
							if(bR.readLine().equals("j") || bR.readLine().equals("J"))
							{
								thePerson.addHobby(buildHobbiesFromInput());
								continue;
							}
							else
								break;
						}while (true);
						
						System.out.print("\nSollen die Angaben so uebernommen werden?\n\n");
						
						thePerson.printAll();
						System.out.print("\nAuswahl [j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							System.out.println("Schreibe Personendaten in Datenbank...");
							DAOPersonen.insertPerson(con, thePerson);
							System.out.println(thePerson.getName()+", "+thePerson.getVorName()+" erfolgreich eingefuegt.");
						}
						break;
		
			case 'e': //Person loeschen
						printPersonList(con);
						System.out.println("Welche Person soll geloescht werden?\nAuswahl(persnr): ");
						int persnrToDelete = Integer.parseInt(bR.readLine());
						DAOPersonen.deletePersonByPersnr(con, persnrToDelete);
						if(persnrToDelete == 1)
							System.out.println("Person geloescht.");
						else
							System.err.println("Person konnte nicht geloescht werden!");
									
						break;
					
			case '1': //Personen-Stammdaten aendern
						printPersonList(con);
						System.out.print("Welche Person soll geaendert werden?\nAuswahl(persnr): ");	
						int persnrToUpdate = Integer.parseInt(bR.readLine());
						DBPersonen updatedPerson = buildPersonFromInput();
						updatedPerson.setPersNr(persnrToUpdate);
						DAOPersonen.updatePersonStammdaten(con, updatedPerson);
						System.out.print("Person aktualisiert.");
						break;
					
			case '2': //neues Hobby/Beruf/Ausbildung....
						System.out.println("Welcher Person soll ein neues Attribut hinzugefuegt werden?");
						printPersonList(con);
						int persnrToExpand = Integer.parseInt(bR.readLine());
						DBPersonen personToEdit = DAOPersonen.selectPersonByPersnr(con, persnrToExpand);
						System.out.print("neue Adresse hinzufuegen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), buildAdressFromInput());						
						}
						System.out.print("neuen Beruf hinzufuegen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), buildBerufFromInput());
						}
						System.out.print("neue Ausbildung hinzufuegen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), buildAusbildungenFromInput());
						}
						System.out.print("neue Hobby hinzufuegen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), buildHobbiesFromInput());
						}
						break;
						
			case '3': //Loeschen eines Hobbies/Berufs/....
						System.out.println("Von welcher Person soll ein Attribut geloescht werden?");
						printPersonList(con);
						
						int persnrToDeleteFrom = Integer.parseInt(bR.readLine());
						System.out.println("gewaehlte Personennummer: "+persnrToDeleteFrom);
						DBPersonen personToDeleteFrom = DAOPersonen.selectPersonByPersnr(con, persnrToDeleteFrom);
						personToDeleteFrom.printAll();
						
						int objNrToDelete = 0;
						
						System.out.print("Adresse loeschen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							System.out.print("Nummer der zu loeschenden Adresse: ");
							objNrToDelete = Integer.parseInt(bR.readLine());
							LinkedList<DBAdressen> aL = personToDeleteFrom.getAdressenList();
							for(DBAdressen a : aL)
							{
								if (a.getAdrNr() == objNrToDelete)
								{
									aL.remove(a);
								}
							}		
						}
						System.out.print("Beruf loeschen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							objNrToDelete = Integer.parseInt(bR.readLine());
							LinkedList<DBBerufe> bL = personToDeleteFrom.getBerufeList();
							for(DBBerufe b : bL)
							{
								if (b.getbNr() == objNrToDelete)
								{
									bL.remove(b);
								}
							}		
						}
						System.out.print("Ausbildung loeschen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							objNrToDelete = Integer.parseInt(bR.readLine());
							LinkedList<DBAusbildungen> aL = personToDeleteFrom.getAusbildungenList();
							for(DBAusbildungen a : aL)
							{
								if (a.getAnr() == objNrToDelete)
								{
									aL.remove(a);
								}
							}		
						}
						System.out.print("Hobby loeschen?[j/n]: ");
						if(bR.readLine().equals("j") || bR.readLine().equals("J"))
						{
							objNrToDelete = Integer.parseInt(bR.readLine());
							LinkedList<DBHobbies> hL = personToDeleteFrom.getHobbiesList();
							for(DBHobbies h : hL)
							{
								if (h.getHnr() == objNrToDelete)
								{
									hL.remove(h);
								}
							}		
						}
						break;
					
			case '0':	con.close();
						System.out.println("Verbindung wurde beendet.");
						System.exit(userChoice);
						break;
			case 'm':	printMenu();
						break;
			default:	System.out.print("Auswahl nicht korrekt! Bitte wiederholen. ");
				
			}// switch
		} // while true
	} // end of menu
	
	private static DBPersonen buildPersonFromInput() {
		try
		{
			System.out.print("\nName: ");
			String name = bR.readLine();			
			System.out.print("Vorname: ");
			String vorName = bR.readLine();			
			System.out.print("Geburtsdatum [JJJJ-MM-TT]: ");
			Date gebDat = buildDateFromInput();			
			System.out.print("Geschlecht [m/w]: ");
			String geschlecht = bR.readLine();
			
			return new DBPersonen(name, vorName, gebDat, geschlecht);
		} catch (IOException e)
		{
			System.err.println("Methode buildPersonFromInput() fehlgeschlagen.");
			return null;
		}
	} // buildPersonFromInput()
	
	public static DBAdressen buildAdressFromInput()
	{
		try
		{
			System.out.print("\nPLZ: ");
			String plz = bR.readLine();		
			System.out.print("Ort: ");
			String ort = bR.readLine();		
			System.out.print("Strasse: ");
			String str = bR.readLine();			
			System.out.print("Typ[p/d]: ");
			String typ = bR.readLine();
			
			return new DBAdressen(plz, ort, str, typ);
		} catch (IOException e)
		{
			System.err.println("Methode buildAdressFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildAdressFromInput()
	
	public static DBBerufe buildBerufFromInput()
	{
		try
		{
			System.out.print("\nBeschreibung: ");
			String beschreibung = bR.readLine();	
			System.out.print("Begonnen [JJJJ-MM-TT]: ");
			Date begonnen = buildDateFromInput();		
			System.out.print("Beendet [JJJJ-MM-TT]: ");
			Date beendet = buildDateFromInput();		
			System.out.print("Gehalt: ");
			double gehalt = Double.parseDouble(bR.readLine());
			
			return new DBBerufe(beschreibung, begonnen, beendet, gehalt);
		} catch (IOException e)
		{
			System.err.println("Methode buildBerufFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildBerufFromInput()
	
	public static DBHobbies buildHobbiesFromInput()
	{
		try
		{
			System.out.print("\nHobby: ");
			String hobby = bR.readLine();
			
			return new DBHobbies(hobby);
		} catch (IOException e)
		{
			System.err.println("Methode buildHobbiesFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildHobbiesFromInput()
	
	public static DBAusbildungen buildAusbildungenFromInput()
	{
		try
		{
			System.out.print("\nBeschreibung: ");
			String beschreibung = bR.readLine();		
			System.out.print("Von [JJJJ-MM-TT]: ");
			Date von = buildDateFromInput();			
			System.out.print("Bis [JJJJ-MM-TT]: ");
			Date bis = buildDateFromInput();			
			System.out.println("Abschluss: ");
			String abschluss = bR.readLine();			
			System.out.print("Abschluss-Datum [JJJJ-MM-TT]: ");
			Date datum = buildDateFromInput();
			
			return new DBAusbildungen(beschreibung, von, bis, abschluss, datum);
		} catch (IOException e)
		{
			System.err.println("Methode buildAusbildungenFromInput() fehlgeschlagen.");
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Date buildDateFromInput()
	{
		try
		{
			String dateString = bR.readLine();
			Date date = null;
			if(!dateString.isEmpty())
			{
				int year = Integer.parseInt(dateString.substring(0,4)); 		//yeard
				int month = Integer.parseInt(dateString.substring(5,7));		//month
				int day = Integer.parseInt(dateString.substring(8,10));			//day
				date = new Date(year - 1900, month - 1, day);
			}
			return date;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}//buildSQLDateFromInput()

	protected static ObjectContainer getConnection(String filename)
	{
		try{				
			EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
			
			// config DBPersonen
			config.common().objectClass(DBPersonen.class).cascadeOnUpdate(true);
			config.common().objectClass(DBPersonen.class).cascadeOnDelete(true);
			
			config.common().objectClass(DBPersonen.class).objectField("persNr").indexed(true);
			config.common().add(new UniqueFieldValueConstraint(DBPersonen.class, "persNr"));
			config.common().activationDepth(5);
			// config DBAdressen
			config.common().objectClass(DBAdressen.class).objectField("adrNr").indexed(true);
			config.common().add(new UniqueFieldValueConstraint(DBAdressen.class, "adrNr"));
			// config DBAusbildungen
			config.common().objectClass(DBAusbildungen.class).objectField("anr").indexed(true);
			config.common().add(new UniqueFieldValueConstraint(DBAusbildungen.class, "anr"));
			// config DBBerufe
			config.common().objectClass(DBBerufe.class).objectField("bNr").indexed(true);
			config.common().add(new UniqueFieldValueConstraint(DBBerufe.class, "bNr"));
			// config DBHobbies
			config.common().objectClass(DBHobbies.class).objectField("hnr").indexed(true);
			config.common().add(new UniqueFieldValueConstraint(DBHobbies.class, "hnr"));
					
			return Db4oEmbedded.openFile(config, filename);
		}
		catch (Exception e)
		{
			System.out.println("Verbindung konnte nicht hergestellt werden!");
			return null;
		}
	} // end of -sOC getConnection
	
	protected static void initializeDB(ObjectContainer con)
	{
		try
		{
			System.out.println("Lade Sequenzen...");
			ObjectSet<Sequence> allsequences = con.queryByExample(Sequence.class);
			if (allsequences.size() == 0)
			{
				con.store(new Sequence(0, "seq_persnr"));
				con.store(new Sequence(0, "seq_adrnr"));
				con.store(new Sequence(0, "seq_bnr"));
				con.store(new Sequence(0, "seq_hnr"));
				con.store(new Sequence(0, "seq_anr"));
				con.commit();
			}
			while(allsequences.hasNext())
			{
				Sequence seq = allsequences.next();
				if (seq.getName().equals("seq_persnr"))
				{
					seq.print();
					con.store(seq);
				}
				else if (seq.getName().equals("seq_adrnr"))
				{
					seq.print();
					con.store(seq);
				}
				else if (seq.getName().equals("seq_hnr"))
				{
					seq.print();
					con.store(seq);
				}
				else if (seq.getName().equals("seq_bnr"))
				{
					seq.print();
					con.store(seq);
				}
				else if (seq.getName().equals("seq_anr"))
				{
					seq.print();
					con.store(seq);
				}
				con.commit();
			}
			System.out.println("Sequenzen hergestellt");
		}catch (Exception e)
		{
			System.out.println("Fehler beim erzeugen/laden der Sequence(s)!");
			e.printStackTrace();
		}
//		// andy
//		DBPersonen andy = new DBPersonen("Baur", "Andreas", new Date(1987 - 1900,1 - 1,13), "M" );
//		DBAdressen andyadr1 = new DBAdressen("66953", "Pirmasens", "Strobelallee 1", "P");
//		DBBerufe andyber1 = new DBBerufe("Nachhilfe", new Date(2009 - 1900,10 - 1,1), null, 150);
//		DBAusbildungen andyaus1 = new DBAusbildungen("Sicherheitsoffizier", 
//									new Date(2006 - 1900,10 - 1, 5), new Date(2006 - 1900,12 - 1, 20), 
//									"Sicherheitsoffizier", new Date(2006 - 1900,12 - 1, 20));
//		DBAusbildungen andyaus2 = new DBAusbildungen("Abitur", null, null, "Abitur", new Date(2006 - 1900,3 - 1, 24));
//		DBHobbies andyhob1 = new DBHobbies( "Billard");
//		DBHobbies andyhob2 = new DBHobbies("Mathematik");
//		
//		// rabea
//		DBPersonen rabea = new DBPersonen("Vogt", "Rabea", new Date(1988 - 1900,11 - 1,30), "W" );
//		DBAdressen rabeaadr1 = new DBAdressen( "66954", "Pirmasens", "Sonnenstraße 6", "P");
//		DBBerufe rabeaber1 = new DBBerufe( "Friseur", new Date(2010 - 1900,5 - 1,1), null, 250);
//		DBAusbildungen rabeaaus1 = new DBAusbildungen( "Abitur", null, null, "Abitur", new Date(2009 - 1900,3 - 1, 24));
//		DBHobbies rabeahob1 = new DBHobbies("Theater");
//		DBHobbies rabeahob2 = new DBHobbies( "Tanzen");		
//		
//		andy.addAdress(andyadr1);
//		andy.addBeruf(andyber1);
//		andy.addAusbildung(andyaus1);
//		//andy.addAusbildung(andyaus2);
//		andy.addHobby(andyhob1);
//		//andy.addHobby(andyhob2);
//		
//		rabea.addAdress(rabeaadr1);
//		rabea.addBeruf(rabeaber1);
//		rabea.addAusbildung(rabeaaus1);
//		rabea.addHobby(rabeahob1);
//		rabea.addHobby(rabeahob2);

//		DAOPersonen.insertPerson(con, andy);
//		DAOPersonen.insertPerson(con, rabea);
		
//		con.commit();
	}

	public static void main(String args[]) throws NumberFormatException, IOException, InterruptedException 
	{
		System.out.println("Stelle Verbindung her und erstelle Datei...");

		Thread.sleep(750);
		
		ObjectContainer con = getConnection("db4o_database_file");
		initializeDB(con);
		
		try {
			// con = a.getConnection();
			System.out.println("Verbindung hergestellt.\n");
			
			Thread.sleep(750);
			
			bR = new BufferedReader(new InputStreamReader(System.in));
			
			menu(con);
					
			con.close();
		} catch ( Exception e) {
			System.out.println("FEHLER!");
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\nVerbindung wurde erfolgreich beendet.");
		
	} // end of main
} // end of class