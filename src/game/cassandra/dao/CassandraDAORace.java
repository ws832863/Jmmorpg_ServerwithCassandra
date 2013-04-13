package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.data.Race;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.log4j.Logger;

/**
 * a class for manipulate Map schema create schema prepopulate
 * 
 * insert value and retrieve value
 * 
 * @author wangshuo
 * 
 */
public class CassandraDAORace {

	private final static Logger logger = Logger
			.getLogger(CassandraDAORace.class.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	private final static String ColumnFamilyName = "race";

	private Vector<Race> vos;

	public Vector<Race> getVos() {
		return vos;
	}

	public CassandraDAORace() {
		vos = new Vector<Race>();
	}

	/**
	 * get race by rowkey
	 * 
	 * @param rowKey
	 * 
	 */
	public void selectByPk(int rowKey) {
		vos.removeAllElements();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspaceOperator, stringSerializer, stringSerializer,
				stringSerializer);
		String key = String.valueOf(rowKey);

		query.setKey(key).setColumnFamily(ColumnFamilyName);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, null, "\uFFFF", false);

		HashMap<String, String> tempResult = new HashMap<String, String>();
		
		while (iterator.hasNext()) {
			HColumn<String, String> c = iterator.next();
			tempResult.put(c.getName(), c.getValue());
		}
		
		vos.add(this.mappingHashMapIntoRaceObject(key, tempResult));

	}

	private Race mappingHashMapIntoRaceObject(String key,
			HashMap<String, String> h) {
		
		Race r = new Race();
		r.setId(Integer.valueOf(key));
		r.setRace(h.get("race"));
		return r;
	}

	/**
	 * 
	 * this function will create a schema with the name "race" in cassandra it
	 * will be only once invoked before a game server constructed
	 * 
	 * if the keyspace not exists, will create it if the keyspace has a
	 * columnfamily "race" already, will drop it
	 * 
	 */
	public static void createRaceSchema() {
		logger.info("====>Creating RaceSchema in Cassandra");

		BasicColumnDefinition columnRace = new BasicColumnDefinition();
		columnRace.setName(stringSerializer.toByteBuffer("race"));
		columnRace.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		// add all columnDefinition to columnfamily definition
		BasicColumnFamilyDefinition basicRaceDef = new BasicColumnFamilyDefinition();
		basicRaceDef.addColumnDefinition(columnRace);

		basicRaceDef.setName(ColumnFamilyName); // set a name for this
												// columnfamily
		basicRaceDef.setKeyspaceName(KeySpaceName);
		// set comparatortype of the columnfamily
		basicRaceDef.setComparatorType(ComparatorType.UTF8TYPE);

		ColumnFamilyDefinition CFRaceDef = new ThriftCfDef(basicRaceDef);
		// if the keyspace not exists, then create a keyspace
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(CFRaceDef));

		try {

			if (gameCluster.describeKeyspace(KeySpaceName) != null) {// if the
																		// keyspace

				gameCluster.dropColumnFamily(KeySpaceName, ColumnFamilyName,
						true);

			} else {
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());
			// he.printStackTrace();
		}
		gameCluster.addColumnFamily(CFRaceDef);// the new columnfamily into
												// cassandra
		logger.info(" RaceSchema created<====");
	}

	public static void prepopulateMapData() {

		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);

		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("race", "human"));
		mutator.addInsertion("2", ColumnFamilyName,
				HFactory.createStringColumn("race", "fairy"));

		mutator.execute();
	}

	public void testVectorRaces() {
		Race vo;
		Iterator<Race> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	// used for test this class
	public static void main(String[] str) {
		CassandraDAORace.createRaceSchema();
		CassandraDAORace.prepopulateMapData();
		CassandraDAORace dao=new CassandraDAORace();
		dao.selectByPk(1);
		dao.testVectorRaces();
	}
}
