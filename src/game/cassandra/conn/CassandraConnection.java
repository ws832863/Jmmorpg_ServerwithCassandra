package game.cassandra.conn;
 
import java.text.SimpleDateFormat;
import java.util.Date;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

public class CassandraConnection {

	private final static Cluster gameCluster = HFactory.getOrCreateCluster(
			"test-cluster", "localhost:9160");

	private final static String KEYSPACE_JMMORPG = "JMMORPG";

	// static SimpleDateFormat sdf = ;

	public static String CurrentDateString = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss").format(new Date()).toString();

	public final static String getKeySpaceName() {
		return KEYSPACE_JMMORPG;
	}

	public final static Cluster getCluster() {
		return gameCluster;
	}

}
