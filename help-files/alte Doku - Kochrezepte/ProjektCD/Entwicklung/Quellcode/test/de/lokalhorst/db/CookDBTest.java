package de.lokalhorst.db;

import static org.junit.Assert.*;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jochen Pätzold
 */
public class CookDBTest
{
    private static Connection cn = null;
    private final int COOK_ID = 314159265;
    private final String COOK_NAME = "TestCook_PI";
    private final String PWD = "TestCook_PI_PWD";
    private final boolean IS_ADMIN = false;
    private final String SEC_QUESTION = "TestCook_PI_SEC_Q";
    private final String SEC_ANSWER = "TestCook_PI_SEC_A";
    private CookDTO cook = new CookDTO(COOK_ID, COOK_NAME, PWD, IS_ADMIN,
	    SEC_QUESTION, SEC_ANSWER);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
	cn = ConnectDB.getConnection();
	if (null == cn)
	    throw new Exception();
    }

    @AfterClass
    public static void tearDownAfterClass()
    {
	ConnectDB.closeConnection(cn);
    }

    @Before
    public void setUp() throws SQLException
    {
	final String insert_query = "INSERT INTO Cook VALUES(?, ?, ?, ?,  ?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, COOK_ID);
	ps.setString(2, COOK_NAME);
	ps.setString(3, PWD);
	ps.setBoolean(4, IS_ADMIN);
	ps.setString(5, SEC_QUESTION);
	ps.setString(6, SEC_ANSWER);

	ps.executeUpdate();
    }

    @After
    public void tearDown() throws SQLException
    {
	final String delete_query = "DELETE FROM Cook WHERE cook_id=?";
	PreparedStatement statement = cn.prepareStatement(delete_query);
	statement.setInt(1, COOK_ID);

	statement.executeUpdate();
    }

    @Test
    public void testChangePassword()
    {
	String neuesPwd = "bekannt";
	CookDB.changePassword(cn, COOK_ID, neuesPwd);
	String changedPwd = CookDB.findByID(cn, COOK_ID).getPwd();
	assertTrue("testChangePassword", changedPwd.equals(neuesPwd));
    }

    @Test
    public void testLoginCook()
    {
	String correctPwd = PWD;
	CookDTO loginCook = CookDB.loginCook(cn, COOK_NAME, correctPwd);
	assertTrue("testLoginCook (pos)", loginCook != null);

	String falsePwd = "definitiv nicht das richtige Psswort!";
	loginCook = CookDB.loginCook(cn, COOK_NAME, falsePwd);
	assertTrue("testLoginCook (neg)", loginCook == null);
    }

    @Test
    public void testExistsName()
    {
	String validName = COOK_NAME;
	assertTrue("testExistsName (pos)", CookDB.existsName(cn, validName));

	String invalidName = "-Den!gibt's@nun\"wirklich(nicht";
	assertFalse("testExistsName (neg)", CookDB.existsName(cn, invalidName));
    }

    @Test
    public void testFindByName()
    {
	String validName = COOK_NAME;
	assertTrue("testFindByName (pos)",
		null != CookDB.findByName(cn, validName));

	String invalidName = "-Den!gibt's@nun\"wirklich(nicht";
	assertTrue("testFindByName (neg)",
		null == CookDB.findByName(cn, invalidName));
    }

    @Test
    public void testFindByID()
    {
	int validID = COOK_ID;
	assertTrue("testFindByID (pos)", null != CookDB.findByID(cn, validID));

	int invalidID = -4711; // anti "Eau de Cologne"
	assertTrue("testFindByID (neg)", null == CookDB.findByID(cn, invalidID));
    }

    @Test
    public void testRegisterCook()
    {
	CookDTO alreadyRegistered = CookDB.findByID(cn, COOK_ID);
	assertTrue("testRegisterCook", alreadyRegistered.equals(cook));
    }

    @Test
    public void testRegisterExistingName()
    {
	CookDTO twinCook = new CookDTO(0, COOK_NAME, "geheim", false,
		"kleiner Satz", "nicht wirklch");
	assertFalse("testRegisterExistingName",
		CookDB.registerNewCook(cn, twinCook));
    }

    @Test
    public void testSetAdminStatus()
    {
	boolean status = true;
	assertTrue("testSetAdminStatus (boolean)",
		CookDB.setAdminStatus(cn, status, COOK_ID));
	assertTrue("testSetAdminStatus (isAdmin)",
		status == CookDB.findByID(cn, COOK_ID).isIs_admin());
    }

    @Test
    public void testDeleteCook()
    {
	int validID = COOK_ID;
	assertTrue("testDeleteCook (pos)", CookDB.deleteCook(cn, validID));

	int invalidID = -42;
	assertFalse("testDeleteCook (neg)", CookDB.deleteCook(cn, invalidID));
    }

    @Test
    public void testSearchByName()
    {
	String[] names =
	    { COOK_NAME };
	LinkedList<CookDTO> cooks = (LinkedList<CookDTO>) CookDB.searchByName(
		cn, names);
	assertFalse("testSearchByName", cooks.isEmpty());
    }
}
