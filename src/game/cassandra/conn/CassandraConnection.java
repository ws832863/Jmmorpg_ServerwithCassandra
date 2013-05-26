package game.cassandra.conn;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

public class CassandraConnection {

	private final static Cluster gameCluster = HFactory.getOrCreateCluster(
			"MMORPGCluster", "127.0.0.1:9160");

	private final static String KEYSPACE_JMMORPG = "JMMORPG";

	private final static Logger logger = Logger
			.getLogger(CassandraConnection.class.getName());
	public static String CurrentDateString = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss").format(new Date()).toString();

	public final static String getKeySpaceName() {
		return KEYSPACE_JMMORPG;
	}

	public final static Cluster getCluster() {
		return gameCluster;
	}

	private static void createKeySpace() {
	
		KeyspaceDefinition JKeyspace = HFactory.createKeyspaceDefinition(
				KEYSPACE_JMMORPG, ThriftKsDef.DEF_STRATEGY_CLASS, 1,
				null);
		gameCluster.addKeyspace(JKeyspace);
	}

	/**
	 * initialize keyspace
	 * 
	 * @param str
	 */
	public static void main(String[] str) {

		logger.info("First time starting a gameserver, creating a Keyspace with name JMMORPG");
		KeyspaceDefinition kd = gameCluster.describeKeyspace(KEYSPACE_JMMORPG);

		if (kd == null) {

			createKeySpace();
		} else {
			logger.info("the Keyspace JMMORPG already exists, drop it");

			gameCluster.dropKeyspace(KEYSPACE_JMMORPG);
			createKeySpace();
		}
		logger.info("create JMMORPG sucessfully");

	}

}
