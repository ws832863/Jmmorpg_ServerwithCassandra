package game.cassandra.conn;

import game.cassandra.dao.CassandraDAOGamePlayer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.log4j.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.NameNotBoundException;

public class CassandraHelper {
	private static CassandraHelper singletonHelper = null;

	private final static Logger logger = Logger
			.getLogger(CassandraDAOGamePlayer.class.getName());

	private Cluster gameCluster;

	private String KeySpaceName;

	private StringSerializer stringSerializer = StringSerializer.get();

	/*
	 * only one instance , for the all game cluster
	 */
	public static CassandraHelper getCassandraHelperInstance() {
		if (singletonHelper == null) {
			logger.info("===>creating new CassandraHelper===");
			singletonHelper = new CassandraHelper();
		}
		return singletonHelper;

	}

	private CassandraHelper() {
		ManagedConfigurationHelper helper = null;

		try {
			helper = (ManagedConfigurationHelper) AppContext.getDataManager()
					.getBinding("CONFIG");
		} catch (NameNotBoundException ex) {
			helper = new ManagedConfigurationHelper("", "");
			setHelper(helper);
		}
		String clusterName = helper.getClusterName();
		String clusterIp = helper.getClusterIp();
		String host = helper.getHost();

		KeySpaceName = helper.getKeyspaceName();

		if (clusterIp == null || clusterIp.trim().length() == 0) {
			logger.info("===>creating single node cluster===");
			gameCluster = HFactory.getOrCreateCluster(clusterName, host);
		} else {
			gameCluster = createMultiNodesCuster(clusterName, clusterIp);
		}

	}

	private Cluster createMultiNodesCuster(String cname, String ips) {
		logger.info("===>creating multi node cluster===" + ips);
		CassandraHostConfigurator cassandraHostConfigurator = new CassandraHostConfigurator(
				ips);
		gameCluster = HFactory.getOrCreateCluster(cname,
				cassandraHostConfigurator);
		return gameCluster;

	}

	/*
	 * get the cassandra cluster
	 */
	public Cluster getGameCluster() {
		return gameCluster;
	}

	/*
	 * get keyspacename
	 */
	public String getKeySpaceName() {
		return KeySpaceName;
	}

	public StringSerializer getStringSerializer() {
		return stringSerializer;
	}

	private void setHelper(ManagedConfigurationHelper hp) {
		AppContext.getDataManager().setBinding("DB", hp);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
