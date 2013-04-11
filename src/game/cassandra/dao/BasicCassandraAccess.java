package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Row;

public class BasicCassandraAccess {
	
	//get the cluster 
	private static Cluster cluster=CassandraConnection.getCluster();
	// retrieve the whole columnFamily
	
	public static HashMap<UUID, Map<String, String>> getSliceQuery() {
		return null;
	}
	public static Row getSingleRowByKey(Keyspace keyspace){
		
		return null;
	}
	

}
