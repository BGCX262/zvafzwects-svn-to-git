/**
 * This class was taken from the db4o reference documentation:
 * http://community.versant.com/documentation/reference/db4o-8.1/java/reference/Content/CodeExamples/clientserver/pooling/Example-clientserver-pooling-java.zip
 *  
 */

package com.db4odoc.clientserver.pooling;

import com.db4o.ObjectContainer;
import com.db4o.ext.Db4oIOException;

public interface ClientConnectionFactory {
	ObjectContainer connect() throws Db4oIOException;
}
