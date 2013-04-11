package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

public class Index_Helper {
	private static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();
	private final static Keyspace keyspace = HFactory.createKeyspace(
			KeySpaceName, gameCluster);
	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static String UserNameForUUID = "UserNameForUUID";

	/**
	 * if a user created, it's uuid and password will insert to this cf it helps
	 * us to search a user from cf by using username get uuid
	 * 
	 * @param username
	 * @param id
	 * @param password
	 */
	public static void insertUserNameForUUID(String username, UUID uuid,
			String password) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspace, UserNameForUUID, stringSerializer, stringSerializer);
		Mutator<String> mut = columnFamilyTemplate.createMutator();

		mut.insert(username, UserNameForUUID, HFactory.createColumn("uuid", uuid));
		mut.insert(username, UserNameForUUID,
				HFactory.createColumn("userpassword", password));

	}
	public static void getAllDateFromTheColumnFamily(){
		
	}
}
