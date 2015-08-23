package de.fhkl.dbsm.zvafzwects.server;


import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.CommonConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraint;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.reflect.jdk.JdkReflector;

import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrderItem;
import de.fhkl.dbsm.zvafzwects.model.db.DBTrack;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.model.db.Sequence;

/**
 * Class for the configuration of the database.
 * 
 * @author Andreas Baur
 * @version 3
 */
public final class Configuration implements ConfigInfo {
	/**
	 * Method for getting a server configuration
	 * 
	 * @return sever configuration
	 */
	public static ServerConfiguration getServerConfiguration() {
		try {
			ServerConfiguration config = Db4oClientServer
					.newServerConfiguration();

			setGeneralConfig(config.common());
			setUniqueFields(config);

			return config;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	} // getServerConfiguration

	/**
	 * Method for getting a client configuration.
	 * 
	 * @return client configuration
	 */
	public static ClientConfiguration getClientConfiguration() {
		try {
			ClientConfiguration config = Db4oClientServer
					.newClientConfiguration();

			setGeneralConfig(config.common());

			return config;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	} // getClientConfiguration

	/**
	 * Method for setting the UniqueFieldValueConstraints.
	 * 
	 * @param config the server configuration
	 */
	private static void setUniqueFields(ServerConfiguration config) {
		// for DBUser
		config.common().add(
				new UniqueFieldValueConstraint(DBUser.class, "userId"));
		config.common().add(
				new UniqueFieldValueConstraint(DBUser.class, "email"));
		// for DBAddress
		config.common().add(
				new UniqueFieldValueConstraint(DBAddress.class, "addressId"));
		// for DBOrder
		config.common().add(
				new UniqueFieldValueConstraint(DBOrder.class, "orderId"));
		// for DBAlbum
		config.common().add(
				new UniqueFieldValueConstraint(DBAlbum.class, "albumId"));
		// for DBCover
		config.common().add(
				new UniqueFieldValueConstraint(DBCover.class, "albumId"));
		// for DBCategory
		config.common().add(
				new UniqueFieldValueConstraint(DBCategory.class, "categoryId"));
		config.common()
				.add(new UniqueFieldValueConstraint(DBCategory.class,
						"categoryName"));
		// for DBCatchword
		config.common()
				.add(new UniqueFieldValueConstraint(DBCatchword.class,
						"catchwordId"));
		config.common().add(
				new UniqueFieldValueConstraint(DBCatchword.class,
						"catchwordName"));
	}

	/**
	 * Method for setting the general information for the configuration
	 * 
	 * @param config the db4o-configuration
	 */
	private static void setGeneralConfig(CommonConfiguration common) {
		// config for DBUser
		common.objectClass(DBUser.class).cascadeOnUpdate(true);
		common.objectClass(DBUser.class).objectField("userId").indexed(true);
		common.objectClass(DBUser.class).objectField("email").indexed(true);
		// config for DBAddress
		common.objectClass(DBAddress.class).objectField("addressId")
				.indexed(true);
		// config for DBOrder
		common.objectClass(DBOrder.class).cascadeOnUpdate(true);
		common.objectClass(DBOrder.class).objectField("orderId").indexed(true);
		// config for DBOrderItem
		common.objectClass(DBOrderItem.class).cascadeOnUpdate(true);
		common.objectClass(DBOrderItem.class).objectField("order")
				.indexed(true);
		common.objectClass(DBOrderItem.class).objectField("album")
				.indexed(true);
		// config for DBTrack
		common.objectClass(DBTrack.class).objectField("cd").indexed(true);
		common.objectClass(DBTrack.class).objectField("trackNumber")
				.indexed(true);
		// config for DBCD
		common.objectClass(DBCD.class).objectField("album").indexed(true);
		common.objectClass(DBCD.class).objectField("cdNumber").indexed(true);
		// config for DBAlbum
		common.objectClass(DBAlbum.class).cascadeOnUpdate(true);
		common.objectClass(DBAlbum.class).objectField("albumId").indexed(true);
		// config for DBCover
		common.objectClass(DBCover.class).objectField("albumId").indexed(true);
		// config for DBCategory
		common.objectClass(DBCategory.class).objectField("categoryId")
				.indexed(true);
		common.objectClass(DBCategory.class).objectField("categoryName")
				.indexed(true);
		// config for DBCatchword
		common.objectClass(DBCatchword.class).cascadeOnUpdate(true);
		common.objectClass(DBCatchword.class).objectField("catchwordId")
				.indexed(true);
		common.objectClass(DBCatchword.class).objectField("catchwordName")
				.indexed(true);
		// config for activation depths
		common.activationDepth(ACTIVATION_DEPTH);
		common.updateDepth(UPDATE_DEPTH);
		// fix for the issue
		// "java.lang.ClassCastException: model.db.Sequence cannot be cast to com.db4o.reflect.generic.GenericObject"
		common.reflectWith(new JdkReflector(Thread.currentThread()
				.getContextClassLoader()));
	}

	/**
	 * initializes the database with the sequences.
	 * 
	 * @param con the object container
	 */
	public static void initializeDB(ObjectContainer con) {
		try {
			ObjectSet<Sequence> allsequences = con
					.queryByExample(Sequence.class);
			if (allsequences.size() == 0) {
				con.store(new Sequence(SEQUENCE_INIT, "seqUserId"));
				con.store(new Sequence(SEQUENCE_INIT, "seqAddressId"));
				con.store(new Sequence(SEQUENCE_INIT, "seqOrderId"));
				con.store(new Sequence(SEQUENCE_INIT, "seqAlbumId"));
				con.store(new Sequence(SEQUENCE_INIT, "seqCategoryId"));
				con.store(new Sequence(SEQUENCE_INIT, "seqCatchwordId"));
				con.commit();
			}
			while (allsequences.hasNext()) {
				Sequence seq = allsequences.next();
				if (seq.getName().equals("seqUserId")) {
					con.store(seq);
				} else if (seq.getName().equals("seqAddressId")) {
					con.store(seq);
				} else if (seq.getName().equals("seqOrderId")) {
					con.store(seq);
				} else if (seq.getName().equals("seqAlbumId")) {
					con.store(seq);
				} else if (seq.getName().equals("seqCategoryId")) {
					con.store(seq);
				} else if (seq.getClass().equals("seqCatchwordId")) {
					con.store(seq);
				}
				con.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
