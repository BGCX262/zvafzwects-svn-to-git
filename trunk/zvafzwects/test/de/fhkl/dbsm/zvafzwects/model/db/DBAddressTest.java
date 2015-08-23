package de.fhkl.dbsm.zvafzwects.model.db;

import org.junit.Assert;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 */
public class DBAddressTest {

	private final static String INIT_STREET = "Test Street";
	private final static String INIT_HOUSENUMBER = "42b";
	private final static String INIT_CITY = "Test City";
	private final static String INIT_ZIPCODE = "1337";

	/**
	 * Test method for
	 * {@link de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress#hashCode()}
	 * . Test if hash differs for different addresses
	 */
	@Test
	public final void hashCode_differs() {
		DBAddress address = generateDefaultAddress();
		DBAddress addressId = generateDefaultAddress();
		addressId.setAddressId(100);
		DBAddress cityNull = generateDefaultAddress();
		cityNull.setCity(null);
		DBAddress city = generateDefaultAddress();
		city.setCity("otherCity");
		DBAddress houseNumber = generateDefaultAddress();
		houseNumber.setHouseNumber(address.getHouseNumber()+ 100);
		DBAddress streetNull = generateDefaultAddress();
		streetNull.setStreet(null);
		DBAddress street = generateDefaultAddress();
		street.setStreet("otherStreet");
		DBAddress zipcodeNull = generateDefaultAddress();
		zipcodeNull.setZipcode(null);
		DBAddress zipcode = generateDefaultAddress();
		zipcode.setZipcode("otherZipcode");
		
		int hash = address.hashCode();
		Assert.assertTrue("addressId differs, but same hash!", hash != addressId.hashCode());
		Assert.assertTrue("city differs, but same hash!", hash != cityNull.hashCode());
		Assert.assertTrue("city differs, but same hash!", hash != city.hashCode());
		Assert.assertTrue("houseNumber differs, but same hash!", hash != houseNumber.hashCode());
		Assert.assertTrue("street differs, but same hash!", hash != streetNull.hashCode());
		Assert.assertTrue("street differs, but same hash!", hash != street.hashCode());
		Assert.assertTrue("zipcode differs, but same hash!", hash != zipcodeNull.hashCode());
		Assert.assertTrue("zipcode differs, but same hash!", hash != zipcode.hashCode());
	}
	
	@Test
	public final void equals_success() {
		DBAddress adress1 = generateDefaultAddress();
		DBAddress adress2 = generateDefaultAddress();
		Assert.assertTrue("Same values but not recognized as equal", adress1.equals(adress2));
	}

	private final DBAddress generateDefaultAddress() {
		return new DBAddress(INIT_STREET, INIT_HOUSENUMBER, INIT_CITY,
				INIT_ZIPCODE);
	}
}
