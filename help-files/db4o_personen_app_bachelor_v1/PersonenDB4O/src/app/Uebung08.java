package app;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraint;
import com.db4o.ext.Db4oException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

import dbklassen.DBAdressen;
import dbklassen.DBAusbildungen;
import dbklassen.DBBerufe;
import dbklassen.DBHobbies;
import dbklassen.DBPersonen;

public class Uebung08
{
	static ObjectContainer objCon = null;

	public static void main(String[] args )
	{
		objCon = getConnection();
		try
		{
			System.out.println("##### QueryApp #####\n");

			System.out.println("--Aufgabe 1\nAlle Personen:");
			System.out.println("per QBE:");
			printCollection(selectAllPersonsQBE(objCon));
			System.out.println("per NQ:");
			printCollection(selectAllPersonsNQ(objCon));
			System.out.println("per SODA:");
			printCollection(selectAllPersonsSODA(objCon));

			System.out.println("--Aufgabe 2\nAlle weiblichen Personen deren Name mit \"H\" beginnt:");
			System.out.println("per QBE:");
			printCollection(selectAllWomenWithHNameQBE(objCon));
			System.out.println("per NQ:");
			printCollection(selectAllWomenWithHNameNQ(objCon));
			System.out.println("per SODA:");
			printCollection(selectAllWomenWithHNameSODA(objCon));

			System.out.println("--Aufgabe 3\nAlle Personen mit dem Hobby Tennis und einer dienstlichen Adresse in Zweibrücken");
			System.out.println("per QBE:");
			printCollection(selectAllPersonsTennisZweibrueckenAbiturQBE(objCon));
			System.out.println("per NQ:");
			printCollection(selectAllPersonsTennisZweibrueckenAbiturNQ(objCon));
			System.out.println("per SODA:");
			printCollection(selectAllPersonsTennisZweibrueckenAbiturSODA(objCon));
			System.out.println("\n--- Ende ---");

		} finally
		{
			objCon.close();
		}
	}// main

	// Aufgabe 1

	public static ObjectSet<DBPersonen> selectAllPersonsQBE(ObjectContainer objCon )
	{
		try
		{
			return objCon.queryByExample(DBPersonen.class);
		} catch (Db4oException e)
		{
			e.printStackTrace();
			return null;
		}
	}// selectAllPersonsQBE

	public static ObjectSet<DBPersonen> selectAllPersonsNQ(ObjectContainer objCon )
	{
//		ObjectSet<DBPersonen> personenList = 
		return objCon.query(new Predicate<DBPersonen>()
				{ // Realisierung Predicate in generischer anonymer Klasse
					private static final long serialVersionUID = 6820363429340961660L;

					public boolean match(DBPersonen person)
					{
						return true;
					}
				});
//		return personenList;
	}// selectAllPersonsNQ

	public static ObjectSet<DBPersonen> selectAllPersonsSODA(ObjectContainer objCon )
	{	// The SODA query API is db4o's low level querying API, allowing direct
		// access to nodes of query graphs.
		// Since SODA uses strings to identify fields, it is neither perfectly
		// typesafe nor compile-time checked
		// and it also is quite verbose to write.
		Query query = objCon.query();
		query.constrain(DBPersonen.class);
		return query.execute();
	}// selectAllPersonsSODA

	// Aufgabe 2

	public static LinkedList<DBPersonen> selectAllWomenWithHNameQBE(ObjectContainer objCon )
	{
		ObjectSet<DBPersonen> personenSet;
		LinkedList<DBPersonen> result = new LinkedList<DBPersonen>();
		DBPersonen examplePerson = new DBPersonen();
		examplePerson.setGeschlecht("W");
		try
		{
			personenSet = objCon.queryByExample(examplePerson);
			for (DBPersonen p : personenSet)
			{
				if (p.getName().startsWith("V"))
				{
					result.add(p);
				}
			}
			return result;

		} catch (Db4oException e)
		{
			e.printStackTrace();
			return null;
		}
	}// selectAllWomenWithHNameQBE

	public static ObjectSet<DBPersonen> selectAllWomenWithHNameNQ(ObjectContainer objCon )
	{
		ObjectSet<DBPersonen> personenList = objCon.query(new Predicate<DBPersonen>()
				{
					private static final long serialVersionUID = 3028596441069905122L;

					public boolean match(DBPersonen person )
					{
						return (person.getName().startsWith("V") && person.getGeschlecht().equalsIgnoreCase("W"));
					}

				});
		return personenList;
	}// selectAllWomenWithHNameNQ

	public static ObjectSet<DBPersonen> selectAllWomenWithHNameSODA(ObjectContainer objCon )
	{
		ObjectSet<DBPersonen> personenSet;
		Query query = objCon.query();
		query.constrain(DBPersonen.class);
		query.descend("name").constrain("V").startsWith(true);
		query.descend("geschlecht").constrain("W");
		personenSet = query.execute();
		return personenSet;
	}

	// --Aufgabe 3

	public static ObjectSet<DBPersonen> selectAllPersonsTennisZweibrueckenAbiturQBE(
			ObjectContainer objCon )
	{
		ObjectSet<DBPersonen> personenSet = null;
		DBPersonen examplePerson = new DBPersonen();
		DBHobbies exampleHobby = new DBHobbies("Schach");
		examplePerson.addHobby(exampleHobby);
		DBAdressen exampleAdresse = new DBAdressen();
		exampleAdresse.setOrt("Pirmasens");
		exampleAdresse.setTyp("p");
		examplePerson.addAdress(exampleAdresse);
		DBAusbildungen exampleAusbildung = new DBAusbildungen();
		exampleAusbildung.setAbschluss("Sicherheitsoffizier");
		examplePerson.addAusbildung(exampleAusbildung);
		try
		{
			personenSet = objCon.queryByExample(examplePerson);
			return personenSet;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static List<DBPersonen> selectAllPersonsTennisZweibrueckenAbiturNQ(ObjectContainer objCon )
	{
		List<DBPersonen> personenList = objCon.query(new Predicate<DBPersonen>()
				{
					private static final long serialVersionUID = 4048280430939114784L;

					public boolean match(DBPersonen p )
					{
						for (DBHobbies h : p.getHobbiesList())
						{
							if (h.getHobby().equalsIgnoreCase("Schach"))
							{
								for (DBAdressen a : p.getAdressenList())
								{
									if (a.getOrt().equalsIgnoreCase("Pirmasens") && a.getTyp().equalsIgnoreCase("p"))
									{
										for (DBAusbildungen aus : p.getAusbildungenList())
										{
											if(aus.getAbschluss().equalsIgnoreCase("Sicherheitsoffizier"))
											{
												return true;
											}
										}
									}
								}
							}
						}
						return false;
					}
				});
		return personenList;
	}

	public static ObjectSet<DBPersonen> selectAllPersonsTennisZweibrueckenAbiturSODA(ObjectContainer objCon )
	{
		ObjectSet<DBPersonen> personenSet;
		Query query = objCon.query();
		query.constrain(DBPersonen.class);

		Query adressenQuery = query.descend("adressenList");
		adressenQuery.constrain(DBAdressen.class);
		adressenQuery.descend("ort").constrain("Pirmasens");
		adressenQuery.descend("typ").constrain("p");

		Query hobbiesQuery = query.descend("hobbiesList");
		hobbiesQuery.constrain(DBHobbies.class);
		hobbiesQuery.descend("hobby").constrain("Schach");

		Query ausbildungenQuery = query.descend("ausbildungenList");
		ausbildungenQuery.constrain(DBAusbildungen.class);
		ausbildungenQuery.descend("abschluss").constrain("Sicherheitsoffizier");

		personenSet = query.execute();
		return personenSet;
	}

	// Utility-Methods

	public static ObjectContainer getConnection()
	{
		ObjectContainer objCon = null;

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

		try
		{
			objCon = Db4oEmbedded.openFile(config, "db4o_database_file");
			return objCon;
		} catch (Db4oException e)
		{
			e.printStackTrace();
			return null;
		}
	}// getConnection

	public static void printCollection(Collection<DBPersonen> c)
	{
		for (DBPersonen p : c)
			p.printAll();
	}

}