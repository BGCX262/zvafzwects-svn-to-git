/**
 * 
 */
package dbklassen;

import java.util.LinkedList;

import com.db4o.*;
import com.db4o.ext.DatabaseFileLockedException;

/**
 * @author AndreasBaur
 * 
 */
public class DAOPersonen
{
	public static LinkedList<DBPersonen> selectAllPersons(ObjectContainer con)
	{
		LinkedList<DBPersonen> llpersonen = new LinkedList<DBPersonen>();
		try
		{
			DBPersonen person = new DBPersonen(null, null, null, null);
			ObjectSet<DBPersonen> result = con.queryByExample(person);
			
			while ( result.hasNext() )
			{
				person = result.next();
				llpersonen.add(person);
			}
		}
		catch ( DatabaseFileLockedException e )
		{
			e.printStackTrace();
		}
		return llpersonen;		
	}// selectAllPersons

	public static DBPersonen selectPersonByPersnr(ObjectContainer con, int persnr)
	{
		DBPersonen proto = new DBPersonen();
		proto.setPersNr(persnr);
		ObjectSet<DBPersonen> result = con.queryByExample(proto);
		proto = (DBPersonen)result.next();
		return proto;
	}// selectPersonByPersnr()

	public static LinkedList<DBPersonen> selectPersonByName(ObjectContainer con, String name)
	{
		LinkedList<DBPersonen> llpersonen = new LinkedList<DBPersonen>();
		DBPersonen proto = new DBPersonen();
		proto.setName(name);
		ObjectSet<DBPersonen> result = con.queryByExample(proto);
		while ( result.hasNext() )
		{
			proto = result.next();
			llpersonen.add(proto);
		}
		return llpersonen;		
	}// selectPersonByName()

	public static void insertPerson(ObjectContainer con, DBPersonen person)
	{
//		DBPersonen pers = selectPersonByPersnr(con,person.getPersNr());
//	    if(pers != null)
//	    {
//	    	try
//	    	{
//		    	con.store(person);
//		    	con.commit();
//	    	}
//	    	catch (Exception e)
//	    	{
//	    		e.printStackTrace();
//	    	}
//	    }
		try {
			ObjectSet<Sequence> allsequences = con.queryByExample(Sequence.class);
			DBAdressen adr = (DBAdressen)person.getAdressenList().getLast();
			DBAusbildungen ausb = (DBAusbildungen)person.getAusbildungenList().getLast();
			DBHobbies hob = (DBHobbies)person.getHobbiesList().getLast();
			DBBerufe ber = (DBBerufe)person.getBerufeList().getLast();
			while(allsequences.hasNext())
			{
				Sequence seq = allsequences.next();
				if (seq.getName().equals("seq_persnr"))
				{
					person.setPersNr(seq.nextVal());
					con.store(seq);
				}
				else if (seq.getName().equals("seq_adrnr"))
				{
					adr.setAdrNr(seq.nextVal());
					con.store(seq);
				}
				else if (seq.getName().equals("seq_hnr"))
				{
					hob.setHnr(seq.nextVal());
					con.store(seq);
				}
				else if (seq.getName().equals("seq_bnr"))
				{
					ber.setBnr(seq.nextVal());
					con.store(seq);
				}
				else if (seq.getName().equals("seq_anr"))
				{
					ausb.setAnr(seq.nextVal());
					con.store(seq);
				}
				con.commit();
			}
			con.store(person);
			con.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}// insertPerson

	public static int deletePersonByPersnr(ObjectContainer con, int persnr)
	{
		DBPersonen pers = selectPersonByPersnr(con, persnr);
		try
		{
			if(pers != null)
			{
				con.delete(pers);
				con.commit();
				return 1;
			}
		}
		catch(Exception e)
		{
			con.rollback();
			con.ext().refresh(pers, Integer.MAX_VALUE);
			return 0;
		}
		return 0;
	}// deltePerson

	public static void updatePersonStammdaten(ObjectContainer con, DBPersonen person)
	{	
		DBPersonen personToUpdate = selectPersonByPersnr(con, person.getPersNr());
		personToUpdate.setName(person.getName());
		personToUpdate.setVorName(person.getVorName());
		personToUpdate.setGebDat(person.getGebDat());
		personToUpdate.setGeschlecht(person.getGeschlecht());
		con.store(personToUpdate);
		con.commit();
	}// updatePersonStammdaten()

	public static int updatePersonenObjekt(ObjectContainer con, int persnr, Object o)
	{
		if ( o instanceof DBPersonen || o == null || con == null )
			return -1;
		
		DBPersonen person = selectPersonByPersnr(con,persnr);
		if (person == null)
			return -1;
		
		if ( o instanceof DBAdressen )
		{
			Sequence seq = (Sequence)con.queryByExample(new Sequence(0,"seq_adrnr")).next();
			((DBAdressen) o).setAdrNr(seq.nextVal());
			con.store(seq);
    		person.addAdress((DBAdressen)o);
    		con.store(person);
    		con.commit();
    		return 1;
		}
		else if ( o instanceof DBBerufe )
		{
			Sequence seq = (Sequence)con.queryByExample(new Sequence(0,"seq_bnr")).next();
			((DBBerufe) o).setBnr(seq.nextVal());
			con.store(seq);
    		person.addBeruf((DBBerufe)o);
    		con.store(person);
    		con.commit();
    		return 1;
		}
		else if ( o instanceof DBAusbildungen )
		{
			Sequence seq = (Sequence)con.queryByExample(new Sequence(0,"seq_anr")).next();
			((DBAusbildungen) o).setAnr(seq.nextVal());
			con.store(seq);
    		person.addAusbildung((DBAusbildungen)o);
    		con.store(person);
    		con.commit();
    		return 1;
		}
		else if ( o instanceof DBHobbies )
		{
			Sequence seq = (Sequence)con.queryByExample(new Sequence(0,"seq_hnr")).next();
			((DBHobbies) o).setHnr(seq.nextVal());
			con.store(seq);
    		person.addHobby((DBHobbies)o);
    		con.store(person);
    		con.commit();
    		return 1;
		}	
		return 0;
	} // end of updatePersonenObjekt;
}// class
