package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.helper.EncodePwd;


/**
 * Testdatengenerator der noch net ganz fertig ist ;-)
 * 
 * @author Christian Zöllner
 * @version 11.11.2010
 *
 */
public class DataGenerator
{
    private static int makeKategories(Connection cn, int amount, boolean file) throws SQLException, IOException
    {
	String name = "TEST-KAT-";
	int count = 0;
	
	if( !file )
	{
	    String qry = "insert into Category values (cat_id_seq.nextval, ? )";

	    PreparedStatement ps = cn.prepareStatement( qry );
	
	    for( int i = 1; i <= amount; i++ )
	    {
		ps.setString(1, (name+i));
		ps.executeUpdate();
		
		count++;
	    }
	}
	
	if( file)
	{
	    String qry = "insert into Category values (cat_id_seq.nextval, '?' )";
	    
	    Writer fw = null;
	
	    fw = new FileWriter("./make_kategories.sql");
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    for( int i = 1; i <= amount; i++ )
	    {
		bw.append(qry.replace("?", name+i));
		bw.append(";\n");
		
		count++;
	    }
	    
	    bw.close();
	    fw.close();
	}
	
	return count;
    }
    
    @SuppressWarnings("unused")
    private static int deleteKategories(Connection cn) throws SQLException
    {
	String qry = "delete from CATEGORY where CATEGORY.CAT_NAME like 'TEST%'";
	PreparedStatement ps = cn.prepareStatement(qry);
	
	return (ps.executeUpdate());

    }
    
  
    
    private static int makeIngredients(Connection cn, int amount, boolean file) throws SQLException, IOException
    {
	String name = "TEST-ING-";
	int count = 0;
	Random r = new Random();
	
	if( !file )
	{
	    String qry = "INSERT INTO Ingredient VALUES(ingr_id_seq.nextval, ?, ? )";

	    PreparedStatement ps = cn.prepareStatement( qry );
	
	    for( int i = 1; i <= amount; i++ )
	    {
		ps.setString(1, (name+i));
		ps.setInt(2, (r.nextInt(4) +1));
		ps.executeUpdate();
		count++;
	    }
	}
	
	return count;
    }
    
    @SuppressWarnings("unused")
    private static int deleteIngredients(Connection cn) throws SQLException
    {
	String qry = "delete from INGREDIENT where INGREDIENT.INGR_NAME like 'TEST%'";
	PreparedStatement ps = cn.prepareStatement(qry);
	
	return (ps.executeUpdate());

    }
    
    private static int makeUsers(Connection cn, int amount, boolean file) throws SQLException, IOException
    {
	String name = "TEST-COO-";
	int count = 0;
	
	if( !file )
	{
	    String qry = "insert into COOK values ( cook_id_seq.nextval, ? , ? , ?, ? , ? )";	

	    PreparedStatement ps = cn.prepareStatement( qry );
	
	    for( int i = 1; i <= amount; i++ )
	    {
		ps.setString(1, (name+i));
		ps.setString(2, EncodePwd.encode(name+i));
		ps.setBoolean(3, false);
		ps.setString(4, (name+i));
		ps.setString(5, (name+i));
		
		ps.executeUpdate();
		count++;
	    }
	}
	
	return count;
    }
    
    @SuppressWarnings("unused")
    private static int deleteUsers(Connection cn) throws SQLException
    {
	String qry = "delete from COOK where COOK.COOK_NAME like 'TEST%'";
	PreparedStatement ps = cn.prepareStatement(qry);
	
	return (ps.executeUpdate());

    }

    private static int makeRecipes(Connection cn, int amount, boolean file) throws SQLException, IOException
    {
	String name = "TEST-REC-";
	int count = 0;
	Random r = new Random();
	
	if( !file )
	{
	    String qry = "insert into RECIPE values (recp_id_seq.nextval, ? , ? ,'2999-09-09','2999-02-02','2999-12-12', ? ,true , ? , ? , ?	)";
	    PreparedStatement ps = cn.prepareStatement( qry );
	
	    for( int i = 1; i <= amount; i++ )
	    {
		ps.setString(1, (name+i));
		ps.setString(2, ("TEST-BESCHREIBUNG-"+i));
		ps.setInt(3, r.nextInt(60)+1);
		ps.setInt(4, r.nextInt(3)+1);//schwierigkeitsgrad
		ps.setInt(5, r.nextInt(100)+1); //kategorie
		ps.setInt(6, r.nextInt(5000)+1); //cook id
		
		ps.executeUpdate();
		count++;
	    }
	}
	
	for( int i = 1; i <= amount; i++ )
	{    
	    String qry = "insert into PREPARATION values (?, ?, ?)";
	    PreparedStatement ps = cn.prepareStatement( qry );
	    String qry2 = "insert into USES values (?, ?, ?)"; 
	    PreparedStatement ps2 = cn.prepareStatement( qry2 );
	    int zid = r.nextInt(4796)+1; //zutaten id

	    for( int j = 1; j <= 3 ; j++)
	    {
		ps.setInt(1, j);//schritt
		ps.setInt(2, i);//recp id
		ps.setString(3, i+" .Schritt: TEST-ZUBEREITUNGSSCHRITT");
		
		ps2.setInt(1, i); //recp id
		ps2.setInt(2, (zid+j)); //zutaten id
		ps2.setInt(3, r.nextInt(100)+1);//zutatenmenge
		
		ps.executeUpdate();
		ps2.executeUpdate();
	    }
	    
	
	  
	} 
	return count;
    }
    
    private static void insertBaseValues(Connection cn) throws SQLException
    {
	Statement s1 = cn.createStatement();
	s1.executeUpdate("insert into DIFFICULTY values (1, 'leicht')");
	s1.executeUpdate("insert into DIFFICULTY values (2, 'mittel')");
	s1.executeUpdate("insert into DIFFICULTY values (3, 'schwer')");
	
	s1.executeUpdate("insert into UNIT values (unit_id_seq.nextval, 'ml')");
	s1.executeUpdate("insert into UNIT values (unit_id_seq.nextval, 'mg')");
	s1.executeUpdate("insert into UNIT values (unit_id_seq.nextval, 'Stk')");
	s1.executeUpdate("insert into UNIT values (unit_id_seq.nextval, ' ')");
	
		
    }
    /**
     * @param args
     */
        
    public static void main(String[] args)
    {
	try
	{
	    Connection cn = null;
	    Class.forName("com.sap.dbtech.jdbc.DriverSapDB");
	    cn = DriverManager.getConnection("jdbc:sapdb://10.0.107.19/info_102", "gruppe_a", "gruppe_a");
	    cn.setAutoCommit(true);
	    
	    //deleteKategories(cn);
	    insertBaseValues(cn);
	    System.out.println( makeKategories(cn, 100, false) );
	    System.out.println(makeIngredients(cn, 5000, false));
	    System.out.println(makeUsers(cn, 5000, false));
	    System.out.println(makeRecipes(cn,5000 , false));
	  
	    ConnectDB.closeConnection(cn);
	    
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} catch (ClassNotFoundException e)
	{
	    e.printStackTrace();
	} catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

}
