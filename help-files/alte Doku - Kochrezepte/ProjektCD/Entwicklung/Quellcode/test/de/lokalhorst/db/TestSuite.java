package de.lokalhorst.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Christian Zöllner
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(
    { BuyListedDBTest.class, CategoryDBTest.class, CookDBTest.class,
	    DifficultyDBTest.class, IngredientDBTest.class, UnitDBTest.class,
	    UsesDBTest.class

    })
public class TestSuite
{
    // Bleibt leer
}
