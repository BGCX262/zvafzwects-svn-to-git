package de.fhkl.dbsm.zvafzwects.utils;

import java.util.Collection;

/**
 * Convenient method for usage in JSTL. 
 * 
 * @author Markus Henn
 *
 */
public class CollectionUtils {
	
	public static boolean contains(Collection<?> collection, Object o) {
		if (collection == null) {
			throw new IllegalArgumentException("collection must not be null");
		}
		return collection.contains(o);
	}
	
}
