/**
 * 
 */
package app;

import javax.swing.*;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraint;
import dbklassen.DAOPersonen;
import dbklassen.DBAdressen;
import dbklassen.DBAusbildungen;
import dbklassen.DBBerufe;
import dbklassen.DBHobbies;
import dbklassen.DBPersonen;
import dbklassen.Sequence;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Andreas Baur
 *
 */
@SuppressWarnings("serial")
public class SwingTest extends JFrame implements ActionListener{

	private static Scanner sIn = new Scanner(System.in);
	static BufferedReader bR = null;
	private static int chosenPersnr = 0;
	private static int persnrToDelete, persnrToUpdate, persnrToExpand, persnrToDeleteFrom;
	private static int adrobjNrToDelete, bobjNrToDelete, hobjNrToDelete, ausobjNrToDelete;
	private static String chosenName = "";
	
	ObjectContainer con = null;
	
	private JTextField mResult = new JTextField(20);
	private JButton verbindung_aufbauen = new JButton();
	private JButton alle_anzeigen = new JButton();
	private JButton details_anzeigen = new JButton();
	private JButton gleiche_anzeigen = new JButton();
	private JButton neue_Person = new JButton();
	private JButton loesche_Person = new JButton();
	private JButton aktualisiere = new JButton();
	private JButton neues_Attribut = new JButton();
	private JButton loesche_Attribut = new JButton();
	private JButton zeige_menu = new JButton();
	private JButton verbindung_abbauen = new JButton();
	private JButton schliessen = new JButton();
	private JButton reset = new JButton();
	private JTextField input = new JTextField();
	// Personenattr
	private static JTextField name = new JTextField();
	private static JTextField vorname = new JTextField();
	private static JTextField geschlecht = new JTextField();
	private static JTextField gebdat = new JTextField();
	// Adressattr
	private static JTextField strasse = new JTextField();
	private static JTextField plz = new JTextField();
	private static JTextField ort = new JTextField();
	private static JTextField typ = new JTextField();
	// Berufattr
	private static JTextField bez = new JTextField();
	private static JTextField begonnen = new JTextField();
	private static JTextField beendet = new JTextField();
	private static JTextField gehalt = new JTextField();
	// Ausbildungattr
	private static JTextField beschr = new JTextField();
	private static JTextField von = new JTextField();
	private static JTextField bis = new JTextField();
	private static JTextField abschluss = new JTextField();
	private static JTextField datum = new JTextField();
	// Hobbieattr
	private static JTextField hobby = new JTextField();
		
	public SwingTest()
	{
		super("SwingTest");
		Container c = getContentPane();
		c.setLayout(new GridLayout(14,5));
		c.add(verbindung_aufbauen);		c.add(alle_anzeigen);
		c.add(details_anzeigen);		c.add(gleiche_anzeigen);
		c.add(neue_Person);				c.add(loesche_Person);
		c.add(aktualisiere);			c.add(neues_Attribut);
		c.add(loesche_Attribut);		c.add(zeige_menu);
		c.add(verbindung_abbauen);		c.add(schliessen);
		c.add(mResult);					c.add(input);
		c.add(new JLabel("<--Input-Field"));	
		c.add(reset);			c.add(new JLabel(""));			c.add(new JLabel(""));
		c.add(new JLabel("Person"));
		c.add(name);					c.add(vorname);
		c.add(gebdat);					c.add(geschlecht);				c.add(new JLabel(""));
		c.add(new JLabel("Adresse"));
		c.add(strasse);					c.add(plz);
		c.add(ort);						c.add(typ);						c.add(new JLabel(""));
		c.add(new JLabel("Beruf"));
		c.add(bez);						c.add(begonnen);
		c.add(beendet);					c.add(gehalt);					c.add(new JLabel(""));
		c.add(new JLabel("Ausbildung"));
		c.add(beschr);					c.add(von);
		c.add(bis);						c.add(abschluss);				c.add(datum);
		c.add(new JLabel("Hobbies"));
		c.add(hobby);
		
		dummy(c);
		
		mResult.setText("Es besteht keine Verbindung");
		mResult.setEditable(false);
		// Ereignisbehandlungen
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		verbindung_aufbauen.setAction(verbindung);
		verbindung_abbauen.setAction(CloseCon);
		schliessen.setAction(ExitAction);
		alle_anzeigen.setAction(allePersonen);
		details_anzeigen.setAction(details);
		gleiche_anzeigen.setAction(gleiche);
		neue_Person.setAction(neue);
		loesche_Person.setAction(loesche_p);
		aktualisiere.setAction(aktualisiere_s);
		zeige_menu.setAction(menu_zeigen);
		reset.setAction(null_setzen);
		neues_Attribut.setAction(neues_attr);
		loesche_Attribut.setAction(loesche_attr);
	}
	
	private void dummy(Container c) 
	{
		// Dummy Table
		JTextField dummies[] = new JTextField[18];
		for ( int i = 0 ; i < 18 ; i++ )
		{
			dummies[i] = new JTextField();
			dummies[i].setEditable(false);
		}

		dummies[0].setText("Name");	dummies[1].setText("Vorname");	dummies[2].setText("Geburtsdatum");	dummies[3].setText("Geschlecht");
		dummies[4].setText("Strasse");	dummies[5].setText("PLZ");	dummies[6].setText("Ort");	dummies[7].setText("Typ");
		dummies[8].setText("Bezeichnung");	dummies[9].setText("Begonnen");	dummies[10].setText("Beendet");	dummies[11].setText("Gehalt");
		dummies[12].setText("Beschreibung"); dummies[13].setText("Von");	dummies[14].setText("Bis");	dummies[15].setText("Abschluss");
		dummies[16].setText("Abschluss-Datum");	dummies[17].setText("Hobby");
		
		c.add(new JLabel(""));			c.add(new JLabel(""));			c.add(new JLabel(""));
		c.add(new JLabel(""));			c.add(new JLabel(""));			c.add(new JLabel(""));			
		c.add(new JLabel(""));			c.add(new JLabel(""));			c.add(new JLabel(""));
		c.add(new JLabel(""));
		c.add(new JLabel("Person"));
		for ( int i = 0 ; i < 4 ; i++ )
		{
			c.add(dummies[i]);
		}
		
		c.add(new JLabel(""));
		c.add(new JLabel("Adresse"));
		for ( int i = 4 ; i < 8 ; i++ )
		{
			c.add(dummies[i]);
		}
					
		c.add(new JLabel(""));
		c.add(new JLabel("Beruf"));
		for ( int i = 8 ; i < 12 ; i++ )
		{
			c.add(dummies[i]);
		}
				
		c.add(new JLabel(""));
		c.add(new JLabel("Ausbildung"));
		for ( int i = 12 ; i < 17 ; i++ )
		{
			c.add(dummies[i]);
		}

		c.add(new JLabel("Hobbies"));
		c.add(dummies[17]);
	}

	public static int readInt() throws InputMismatchException
	{
		return sIn.nextInt();
	} // end of readInt;

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
	
	public static void menu(ObjectContainer con, char userChoice) throws NumberFormatException, IOException{
		// printMenu();
		/*
		 * 	System.out.print("\nAuswahl (m fuer Menue): ");
		 *

			// read user-input
			String input = bR.readLine();
			char[] inputArr = input.toCharArray();
			char userChoice;

			// catch f-options
			if (inputArr[0] == 'f')
				userChoice = inputArr[1];
			else
				userChoice = inputArr[0];
		*/
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

							DAOPersonen.selectPersonByPersnr(con, chosenPersnr).printAll();
						}
						break;

			case 'c': 	//Liste von Personen mit "name"
						Collection<DBPersonen> personsWithName = DAOPersonen.selectPersonByName(con, chosenName);
						while (personsWithName.isEmpty())
						{
							System.err.print("FEHLER: Es sind keine Personen unter diesem Namen zu finden!");
							System.out.print("Bitte waehlen Sie aus folgenden Moeglichkeiten:\n");
							if ( !printPersonList(con) )
								break;
							personsWithName = DAOPersonen.selectPersonByName(con,chosenName);
						}
						for(DBPersonen p : personsWithName)
							p.printInfo();
						break;

			case 'd': 	//Person einfuegen
											
						DBPersonen thePerson = buildPersonFromInput();
						thePerson.addAdress(buildAdressFromInput());
						thePerson.addBeruf(buildBerufFromInput());
						thePerson.addAusbildung(buildAusbildungenFromInput());
						thePerson.addHobby(buildHobbiesFromInput());
						
				//		System.out.print("\nSollen die Angaben so uebernommen werden?\n\n");
						
						thePerson.printAll();
						System.out.println("Schreibe Personendaten in Datenbank...");
						DAOPersonen.insertPerson(con, thePerson);
						System.out.println("Person eingefügt");
						break;
		
			case 'e': //Person loeschen
						printPersonList(con);
//						System.out.println("Welche Person soll geloescht werden?\nAuswahl(persnr): ");
						
						if(DAOPersonen.deletePersonByPersnr(con, persnrToDelete) == 1)
							System.out.println("Person geloescht.");
						else
							System.err.println("Person konnte nicht geloescht werden!");
									
						break;
					
			case '1': //Personen-Stammdaten aendern
//						printPersonList(con);
//						System.out.print("Welche Person soll geaendert werden?\nAuswahl(persnr): ");	
						DBPersonen updatedPerson = buildPersonFromInput();
						updatedPerson.setPersNr(persnrToUpdate);
						DAOPersonen.updatePersonStammdaten(con, updatedPerson);
						System.out.print("Person aktualisiert.");
						break;
					
			case '2': //neues Hobby/Beruf/Ausbildung....
//						System.out.println("Welcher Person soll ein neues Attribut hinzugefuegt werden?");
						printPersonList(con);
						DBPersonen personToEdit = DAOPersonen.selectPersonByPersnr(con, persnrToExpand);
						DBAdressen adresse;
						DBBerufe beruf;
						DBAusbildungen ausbildung;
						DBHobbies hobby;
//						System.out.print("neue Adresse hinzufuegen?[j/n]: ");
						if( (adresse = buildAdressFromInput()) != null )
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), adresse);						
						}
//						System.out.print("neuen Beruf hinzufuegen?[j/n]: ");
						if( (beruf = buildBerufFromInput()) != null )
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), beruf);
						}
//						System.out.print("neue Ausbildung hinzufuegen?[j/n]: ");
						if( (ausbildung = buildAusbildungenFromInput()) != null )
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), ausbildung);
						}
//						System.out.print("neue Hobby hinzufuegen?[j/n]: ");
						if( (hobby = buildHobbiesFromInput()) != null )
						{
							DAOPersonen.updatePersonenObjekt(con, personToEdit.getPersNr(), hobby);
						}
						break;
						
			case '3': //Loeschen eines Hobbies/Berufs/....
//						System.out.println("Von welcher Person soll ein Attribut geloescht werden?");
//						printPersonList(con);
//						
//						int persnrToDeleteFrom = Integer.parseInt(bR.readLine());
//						System.out.println("gewaehlte Personennummer: "+persnrToDeleteFrom);
						DBPersonen personToDeleteFrom = DAOPersonen.selectPersonByPersnr(con, persnrToDeleteFrom);
						personToDeleteFrom.printAll();

//						System.out.print("Adresse loeschen?[j/n]: ");
						if( adrobjNrToDelete >= 0 )
						{
//							System.out.print("Nummer der zu loeschenden Adresse: ");
							LinkedList<DBAdressen> aL = personToDeleteFrom.getAdressenList();
							for(DBAdressen a : aL)
							{
								if (a.getAdrNr() == adrobjNrToDelete)
								{
									aL.remove(a);
									break;
								}
							}		
						}
//						System.out.print("Beruf loeschen?[j/n]: ");
						if(bobjNrToDelete >= 0)
						{
							LinkedList<DBBerufe> bL = personToDeleteFrom.getBerufeList();
							for(DBBerufe b : bL)
							{
								if (b.getbNr() == bobjNrToDelete)
								{
									bL.remove(b);
									break;
								}
							}		
						}
//						System.out.print("Ausbildung loeschen?[j/n]: ");
						if(ausobjNrToDelete >= 0)
						{
							LinkedList<DBAusbildungen> aL = personToDeleteFrom.getAusbildungenList();
							for(DBAusbildungen a : aL)
							{
								if (a.getAnr() == ausobjNrToDelete)
								{
									aL.remove(a);
									break;
								}
							}		
						}
//						System.out.print("Hobby loeschen?[j/n]: ");
						if( hobjNrToDelete >= 0 )
						{
							LinkedList<DBHobbies> hL = personToDeleteFrom.getHobbiesList();
							for(DBHobbies h : hL)
							{
								if (h.getHnr() == hobjNrToDelete)
								{
									hL.remove(h);
									break;
								}
							}		
						}
						con.store(personToDeleteFrom);
						con.commit();
						break;
					
			case '0':	System.exit(userChoice);
						break;
			case 'm':	printMenu();
						break;
			default:	System.out.print("Auswahl nicht korrekt! Bitte wiederholen. ");
				
			}// switch
	} // end of menu
	
	private static DBPersonen buildPersonFromInput() 
	{
		try
		{
			return new DBPersonen(
					name.getText(), 
					vorname.getText(), 
					buildDateFromInput(gebdat.getText()), 
					geschlecht.getText()
					);
		} catch (Exception e)
		{
			System.err.println("Methode buildPersonFromInput() fehlgeschlagen.");
			return null;
		}
	} // buildPersonFromInput()
	
	public static DBAdressen buildAdressFromInput()
	{
		try
		{			
			if ( (plz.getText().length() <= 1 || ort.getText().length() <= 1 || strasse.getText().length() <= 1 
					|| typ.getText().length() <= 1 ) )
				return null;
			else
				return new DBAdressen(
						plz.getText(), 
						ort.getText(), 
						strasse.getText(), 
						typ.getText()
						);
		} catch (Exception e)
		{
			System.err.println("Methode buildAdressFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildAdressFromInput()
	
	public static DBBerufe buildBerufFromInput()
	{
		try
		{
			if ( (bez.getText().length() <= 1 || begonnen.getText().length() <= 1 || beendet.getText().length() <= 1 
					|| gehalt.getText().length() <= 1 ) )
				return null;
			else
				return new DBBerufe(
						bez.getText(), 
						buildDateFromInput(begonnen.getText()), 
						buildDateFromInput(beendet.getText()), 
						Double.parseDouble(gehalt.getText())
						);
		
		} catch (Exception e)
		{
			System.err.println("Methode buildBerufFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildBerufFromInput()
	
	public static DBHobbies buildHobbiesFromInput()
	{
		try
		{
			if ( hobby.getText().length() > 1 )
				return new DBHobbies(hobby.getText());
			else
				return null;
			
		} catch (Exception e)
		{
			System.err.println("Methode buildHobbiesFromInput() fehlgeschlagen.");
			return null;
		}
	}//buildHobbiesFromInput()
	
	public static DBAusbildungen buildAusbildungenFromInput()
	{
		try
		{
			if ( (beschr.getText().length() <= 1 || von.getText().length() <= 1 || bis.getText().length() <= 1  
					|| abschluss.getText().length() <= 1 || datum.getText().length() <= 1 ) )
				return null;
			else
				return new DBAusbildungen(
						beschr.getText(), 
						buildDateFromInput(von.getText()), 
						buildDateFromInput(bis.getText()), 
						abschluss.getText(), 
						buildDateFromInput(datum.getText())
						);
		} catch (Exception e)
		{
			System.err.println("Methode buildAusbildungenFromInput() fehlgeschlagen.");
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Date buildDateFromInput(String dateString)
	{
		// TODO delete method ??
		try
		{
			Date date = null;
			if(!dateString.isEmpty())
			{	//yyyy-mm-dd
				int year = Integer.parseInt(dateString.substring(0,4)); 		//yeard
				int month = Integer.parseInt(dateString.substring(5,7));		//month
				int day = Integer.parseInt(dateString.substring(8,10));			//day
				date = new Date(year - 1900, month - 1, day);
			}
			return date;
		} catch (Exception e)
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
			e.printStackTrace();
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
	}
	 
	public void closeConnection(ObjectContainer con)
	{
		try {
			// con = a.getConnection();
			System.out.println("Verbindung wird geschlossen.\n");
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
	} // end of closeConnection
	
	public static void main(String[] args) throws InterruptedException
	{
		SwingTest f = new SwingTest();
		f.pack();
		f.setVisible(true);
		/*
		System.out.println("Stelle Verbindung her und erstelle Datei...");

		Thread.sleep(750);
		
		ObjectContainer con = getConnection("db4o_database_file");
		initializeDB(con);
		
		System.out.println("Verbindung hergestellt.\n");
		*/
	} // end of main

	private Action verbindung = new AbstractAction()
	{
		{
			putValue(Action.NAME, "Verbindung aufbauen");
		}
		public void actionPerformed(ActionEvent e)
		{
			verbindung_aufbauen.setDisabledSelectedIcon(null);
			System.out.println("Stelle Verbindung her und erstelle Datei...");
			con = getConnection("db4o_database_file");
			initializeDB(con);
			JOptionPane.showMessageDialog(SwingTest.this, "Verbindung wurde aufgebaut");
			
			mResult.setText("Verbindung steht");
			System.out.println("Verbindung hergestellt");
		}
	};	
	private Action CloseCon = new AbstractAction()
    {
		{
			putValue(Action.NAME, "Verbindung abbauen");
		}
        public void actionPerformed(ActionEvent e)
        {
        	if (con == null) return;
        	closeConnection(con);
        	JOptionPane.showMessageDialog(SwingTest.this, "Verbindung wurde abgebaut");
        	mResult.setText("Es besteht keine Verbindung");
        	con = null;
        }
    };   
    private Action ExitAction = new AbstractAction()
    {
		{
			putValue(Action.NAME, "Schliessen");
		}
        public void actionPerformed(ActionEvent e)
        {
        	if ( con != null )
        		closeConnection(con);
        	System.exit(0);
        }
    };
    private Action allePersonen = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Alle Personen ausgeben");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con != null )
    				menu(con, 'a');
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
    	}
    }; 
    private Action details = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Details anzeigen");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else if ( input.getText().length() > 0 )
    			{
    				try
    				{
    					chosenPersnr = Integer.parseInt(input.getText());
    					menu(con, 'b');
    				}
    				catch (Exception ec)
    				{
    					JOptionPane.showMessageDialog(SwingTest.this, "Falscher Parameter.");
    					input.setText("");
    				}
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es wurde keine Person ausgewählt.");
    				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
    	}
    };
    private Action gleiche = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Personen mit geleichem Namen ausgeben");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else if ( input.getText().length() > 0 )
    			{
    				try
    				{
    					chosenName = input.getText();
    					menu(con, 'c');
    				}
    				catch (Exception ec)
    				{
    					JOptionPane.showMessageDialog(SwingTest.this, "Parameter eingeben.");
    					input.setText("");
    				}
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es wurde keine Name eingegeben.");
    				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
    		
    	}
    };
    private Action neue = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Neue Person anlegen");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con != null )
    				menu(con, 'd'); 
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");  				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
    		
    	}
    };
    private Action loesche_p = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Person löschen");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con != null ){
    				persnrToDelete = Integer.parseInt(input.getText());
    				menu(con, 'e');
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");  				
			} catch (Exception ex) 
			{
				JOptionPane.showMessageDialog(SwingTest.this, "Person nicht vorhanden oder falscher Parameter");  
				ex.printStackTrace();
			}
    		
    	}
    }; 
    private Action menu_zeigen = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Menu anzeigen");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else
    			{
    				try
    				{
    					menu(con, 'm');
    				}
    				catch (Exception ec)
    				{
    					ec.printStackTrace();
    					input.setText("");
    				}
    			}
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}    		
    	}
    }; 
    private Action aktualisiere_s = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Stammdaten aktualisieren");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else if ( input.getText().length() > 0 )
    			{
    				try
    				{
    					persnrToUpdate = Integer.parseInt(input.getText());
    					menu(con, '1');
    				}
    				catch (Exception ec)
    				{
    					JOptionPane.showMessageDialog(SwingTest.this, "Falscher Parameter.");
    					input.setText("");
    				}
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es wurde keine Person ausgewählt.");
    				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}    		
    	}
    };
    private Action null_setzen = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Felder leeren");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		input.setText("");
    		// Personenattr
    		name.setText("");
    		vorname.setText("");
    		geschlecht.setText("");
    		gebdat.setText("");
    		// Adressattr
    		strasse.setText("");
    		plz.setText("");
    		ort.setText("");
    		typ.setText("");
    		// Berufattr
    		bez.setText("");
    		begonnen.setText("");
    		beendet.setText("");
    		gehalt.setText("");
    		// Ausbildungattr
    		beschr.setText("");
    		von.setText("");
    		bis.setText("");
    		abschluss.setText("");
    		datum.setText("");
    		// Hobbieattr
    		hobby.setText("");
    	}
    };
    private Action neues_attr = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Neues Attribut");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else if ( input.getText().length() > 0 )
    			{
    				try
    				{
    					persnrToExpand = Integer.parseInt(input.getText());
    					menu(con, '2');
    				}
    				catch (Exception ec)
    				{
    					JOptionPane.showMessageDialog(SwingTest.this, "Falscher Parameter.");
    					input.setText("");
    				}
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es wurde keine Person ausgewählt.");
    				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}    		
    	}
    };
    private Action loesche_attr = new AbstractAction()
    {
    	{
    		putValue(Action.NAME, "Lösche Attribut");
    	}
    	public void actionPerformed(ActionEvent e)
    	{
    		try 
    		{
    			if ( con == null )
    			{
    				JOptionPane.showMessageDialog(SwingTest.this, "Es besteht keine Verbindung.");
    			}
    			else if ( input.getText().length() > 0 )
    			{
    				try
    				{
    					persnrToDeleteFrom = Integer.parseInt(input.getText());
    					if( strasse.getText().length() > 0 ) adrobjNrToDelete = Integer.parseInt(strasse.getText());
    					if( bez.getText().length() > 0 ) bobjNrToDelete = Integer.parseInt(bez.getText());
    					if( hobby.getText().length() > 0 ) hobjNrToDelete = Integer.parseInt(hobby.getText());
    					if( beschr.getText().length() > 0 ) ausobjNrToDelete = Integer.parseInt(beschr.getText());
    					menu(con, '3');
    				}
    				catch (Exception ec)
    				{
    					ec.printStackTrace();
    					JOptionPane.showMessageDialog(SwingTest.this, "Falscher Parameter.");
    					// input.setText("");
    				}
    			}
    			else
    				JOptionPane.showMessageDialog(SwingTest.this, "Es wurde keine Person ausgewählt.");
    				
			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}    		
    	}
    };
    @Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
	}
	
} // end of class