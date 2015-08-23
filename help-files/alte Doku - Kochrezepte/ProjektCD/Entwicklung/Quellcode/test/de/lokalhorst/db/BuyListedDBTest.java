package de.lokalhorst.db;

import java.sql.Connection;
import java.util.LinkedList;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.lokalhorst.helper.BuyList;
import de.lokalhorst.helper.BuyListEntry;
import de.lokalhorst.helper.IngredientHelper;

/**
 * @author Christian Zöllner
 * 
 */
public class BuyListedDBTest
{

    private static Connection cn = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
	cn = ConnectDB.getConnection();

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
	ConnectDB.closeConnection(cn);
    }

    @Test
    public void testClearBuylist()
    {
	int erg_cookid = BuyListedDB.clearBuylist(cn, 0);
	assertTrue("testClearBuylist: ungültige cook id", erg_cookid == 0);

	int erg_con = BuyListedDB.clearBuylist(null, 1);
	assertTrue("testClearBuylist: ungültige connection", erg_con == 0);

	int erg_con_cookid = BuyListedDB.clearBuylist(null, 0);
	assertTrue("testClearBuylist: ungültige connection und cookid",
		erg_con_cookid == 0);

    }

    @Test
    public void testSaveBuylist()
    {
	int cookID = 1000;

	BuyList buyList = new BuyList();
	LinkedList<IngredientHelper> ing1 = new LinkedList<IngredientHelper>();
	ing1.add(new IngredientHelper(2, "mg", "TEST-ING-1"));
	ing1.add(new IngredientHelper(5, "ml", "TEST-ING-2"));

	LinkedList<IngredientHelper> ing2 = new LinkedList<IngredientHelper>();
	ing2.add(new IngredientHelper(8, "Stk", "TEST-ING-10"));
	ing2.add(new IngredientHelper(27, "ml", "TEST-ING-9"));

	BuyListEntry ble1 = new BuyListEntry("TEST-REC-1", 1, 4, ing1);
	BuyListEntry ble2 = new BuyListEntry("TEST-REC-2", 2, 4, ing2);

	buyList.add(ble1);
	buyList.add(ble2);

	BuyListedDB.clearBuylist(cn, cookID);

	int erg_normal = BuyListedDB.saveBuyList(cn, cookID,
		buyList.getBuylist());
	int erg_normal_clear = BuyListedDB.clearBuylist(cn, cookID);
	assertTrue("testSaveBuylist: normale Daten",
		(erg_normal == 2 && erg_normal_clear == 2));

	int erg_con = BuyListedDB.saveBuyList(null, cookID,
		buyList.getBuylist());
	int erg_con_clear = BuyListedDB.clearBuylist(cn, cookID);
	assertTrue("testSaveBuylist: null connection",
		(erg_con == 0 && erg_con_clear == 0));

	int erg_cookid = BuyListedDB.saveBuyList(cn, 0, buyList.getBuylist());
	int erg_cookid_clear = BuyListedDB.clearBuylist(cn, 0);
	assertTrue("testSaveBuylist: ungültige cookid",
		(erg_cookid == 0 && erg_cookid_clear == 0));

	int erg_entries = BuyListedDB.saveBuyList(cn, cookID, null);
	int erg_entries_clear = BuyListedDB.clearBuylist(cn, cookID);
	assertTrue("testSaveBuylist: ungültige buylist",
		(erg_entries == 0 && erg_entries_clear == 0));

	int erg_buylist_empty = BuyListedDB.saveBuyList(cn, cookID,
		new BuyList().getBuylist());
	int erg_buylist_empty_clear = BuyListedDB.clearBuylist(cn, cookID);
	assertTrue("testSaveBuylist: leere buylist",
		(erg_buylist_empty == 0 && erg_buylist_empty_clear == 0));

    }

    @Test
    public void testLoadBuyList()
    {
	int cookID = 1000;

	BuyList buyList = new BuyList();
	LinkedList<IngredientHelper> ing1 = new LinkedList<IngredientHelper>();
	ing1.add(new IngredientHelper(2, "mg", "TEST-ING-1"));
	ing1.add(new IngredientHelper(5, "ml", "TEST-ING-2"));

	LinkedList<IngredientHelper> ing2 = new LinkedList<IngredientHelper>();
	ing2.add(new IngredientHelper(8, "Stk", "TEST-ING-10"));
	ing2.add(new IngredientHelper(27, "ml", "TEST-ING-9"));

	BuyListEntry ble1 = new BuyListEntry("TEST-REC-1", 1, 4, ing1);
	BuyListEntry ble2 = new BuyListEntry("TEST-REC-2", 2, 4, ing2);

	buyList.add(ble1);
	buyList.add(ble2);

	BuyListedDB.clearBuylist(cn, cookID);
	BuyListedDB.saveBuyList(cn, cookID, buyList.getBuylist());

	BuyList loaded = BuyListedDB.loadBuyList(cn, cookID);
	assertTrue("testlaveBuylist: normale buylist", !loaded.isEmpty());

	loaded = BuyListedDB.loadBuyList(null, cookID);
	assertTrue("testlaveBuylist: null connection", loaded.isEmpty());

	loaded = BuyListedDB.loadBuyList(cn, 0);
	assertTrue("testlaveBuylist: ungültige cookid", loaded.isEmpty());

    }

}
