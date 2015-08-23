package dbklassen;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Date;

public class DBPersonen
{
	private int persNr;
	private String name;
	private String vorName;
	private Date gebDat;
	private String geschlecht;
	private double einNahmen;
	private int bonus;
	
	private Collection<DBAdressen> adressenList;
	private Collection<DBBerufe> berufeList;
	private Collection<DBHobbies> hobbiesList;
	private Collection<DBAusbildungen> ausbildungenList;
	
	/**
	 * @param persNr
	 * @param name
	 * @param vorName
	 * @param gebDat
	 * @param geschlecht
	 */
	public DBPersonen (String name, String vorName, Date gebDat,
			String geschlecht )
	{
		super();
		this.name = name;
		this.vorName = vorName;
		this.gebDat = gebDat;
		this.geschlecht = geschlecht;
		
		adressenList = new LinkedList<DBAdressen>();
		berufeList = new LinkedList<DBBerufe>();
		hobbiesList = new LinkedList<DBHobbies>();
		ausbildungenList = new LinkedList<DBAusbildungen>();
	}
	
	public DBPersonen() {
		adressenList = new LinkedList<DBAdressen>();
		berufeList = new LinkedList<DBBerufe>();
		hobbiesList = new LinkedList<DBHobbies>();
		ausbildungenList = new LinkedList<DBAusbildungen>();
	}
	/**
	 * @return the persNr
	 */
	public int getPersNr()
	{
		return persNr;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name )
	{
		this.name = name;
	}
	/**
	 * @return the vorName
	 */
	public String getVorName()
	{
		return vorName;
	}
	/**
	 * @param vorName the vorName to set
	 */
	public void setVorName(String vorName )
	{
		this.vorName = vorName;
	}
	/**
	 * @return the gebDat
	 */
	public Date getGebDat()
	{
		return gebDat;
	}
	/**
	 * @param gebDat the gebDat to set
	 */
	public void setGebDat(Date gebDat )
	{
		this.gebDat = gebDat;
	}
	/**
	 * @return the geschlecht
	 */
	public String getGeschlecht()
	{
		return geschlecht;
	}
	/**
	 * @param geschlecht the geschlecht to set
	 */
	public void setGeschlecht(String geschlecht )
	{
		this.geschlecht = geschlecht;
	}
	/**
	 * @return the einNahmen
	 */
	public double getEinNahmen()
	{
		return einNahmen;
	}
	/**
	 * @return the bonus
	 */
	public int getBonus()
	{
		return bonus;
	}
	
	public LinkedList<DBAdressen> getAdressenList()
	{
		return (LinkedList<DBAdressen>)adressenList;
	}

	public void setAdressenList(LinkedList<DBAdressen> adressenList)
	{
		this.adressenList = adressenList;
	}

	public LinkedList<DBBerufe> getBerufeList()
	{
		return (LinkedList<DBBerufe>)berufeList;
	}

	public void setBerufeList(LinkedList<DBBerufe> berufeList)
	{
		this.berufeList = berufeList;
	}

	public LinkedList<DBHobbies> getHobbiesList()
	{
		return (LinkedList<DBHobbies>)hobbiesList;
	}

	public void setHobbiesList(LinkedList<DBHobbies> hobbiesList)
	{
		this.hobbiesList = hobbiesList;
	}

	public LinkedList<DBAusbildungen> getAusbildungenList()
	{
		return (LinkedList<DBAusbildungen>)ausbildungenList;
	}

	public void setAusbildungenList(LinkedList<DBAusbildungen> ausbildungenList)
	{
		this.ausbildungenList = ausbildungenList;
	}

	
	//methods to add and remove referring objects from a DBPersonen-Object
	public void addAdress(DBAdressen adress)
	{
		this.adressenList.add(adress);
	}
	public void addHobby(DBHobbies hobby)
	{
		this.hobbiesList.add(hobby);
	}
	public void addBeruf(DBBerufe beruf)
	{
		this.berufeList.add(beruf);
	}
	public void addAusbildung(DBAusbildungen ausbildung)
	{
		this.ausbildungenList.add(ausbildung);
	}
	
	public void deleteAdress(int adrnr)
	{
		for(DBAdressen currAdr : adressenList)
		{
			if(adressenList.isEmpty())
				return;
			
			if(currAdr.getAdrNr() == adrnr)
				adressenList.remove(currAdr);
		}
	}
	public void deleteHobby(int hnr)
	{
		for(DBHobbies currHobby : hobbiesList)
		{
			if(hobbiesList.isEmpty())
				return;
			
			if(currHobby.getHnr() == hnr)
				hobbiesList.remove(currHobby);
		}
	}
	public void deleteBeruf(int bnr)
	{
		for(DBBerufe currBeruf : berufeList)
		{
			if(berufeList.isEmpty())
				return;
			
			if(currBeruf.getbNr() == bnr)
				berufeList.remove(currBeruf);
		}
	}
	public void deleteAusbildung(int anr)
	{
		for(DBAusbildungen currAusbildung : ausbildungenList)
		{
			if(ausbildungenList.isEmpty())
				return;
			
			if(currAusbildung.getAnr() == anr)
				ausbildungenList.remove(currAusbildung);
		}
	}
	
	public void printInfo()
	{
		System.out.print(this.persNr + "  " +this.name+"  "+this.vorName+"  "+this.gebDat+"  "+this.geschlecht+"\n");
	}
	
	// TODO EXTRA ADDED
	public String printInfo2()
	{
		return (this.persNr + "  " +this.name+"  "+this.vorName+"  "+this.gebDat+"  "+this.geschlecht+"\n");
	}
	
	public void printAdressen()
	{
		DBAdressen a;
		Iterator<DBAdressen> it = this.adressenList.iterator();
		while(it.hasNext())
		{
			a = it.next();
			a.printInfo();
		}
	}
	public void printAusbildungen()
	{
		DBAusbildungen a;
		Iterator<DBAusbildungen> it = this.ausbildungenList.iterator();
		while(it.hasNext())
		{
			a = it.next();
			a.printInfo();
		}
	}
	public void printBerufe()
	{
		DBBerufe a;
		Iterator<DBBerufe> it = this.berufeList.iterator();
		while(it.hasNext())
		{
			a = it.next();
			a.printInfo();
		}
	}
	public void printHobbies()
	{
		DBHobbies a;
		Iterator<DBHobbies> it = this.hobbiesList.iterator();
		while(it.hasNext())
		{
			a = it.next();
			a.printInfo();
		}
	}
	public void printAll()
	{
		System.out.println("Person(en):");	printInfo();
		System.out.println("\nAdresse(n):");	printAdressen();
		System.out.println("\nAusbildung(en):");	printAusbildungen();
		System.out.println("\nBeruf(e):");	printBerufe();
		System.out.println("\nHobby(s):");	printHobbies();
	}
	public void setPersNr(int persnrToUpdate) {
		this.persNr = persnrToUpdate;		
	}
		
}//DBPersonen
