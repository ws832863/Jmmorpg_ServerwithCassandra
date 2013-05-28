package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.data.Map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
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
public class CassandraDAOMap {

	private final static Logger logger = Logger.getLogger(CassandraDAOMap.class
			.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static String ColumnFamilyName = "map";

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	/** The vector used to store Map information **/
	private Vector<Map> vos;

	public Vector<Map> getVos() {
		return vos;

	}

	private static ConfigurableConsistencyLevel consistency() {
		// create a customized Consistency Level
		ConfigurableConsistencyLevel configurableConsistencyLevel = new ConfigurableConsistencyLevel();
		HashMap<String, HConsistencyLevel> clmap = new HashMap<String, HConsistencyLevel>();
		// define cl.one for columnfamily map
		clmap.put("map", HConsistencyLevel.ONE);
		// in this we use cl.one for read and writes
		configurableConsistencyLevel.setReadCfConsistencyLevels(clmap);
		configurableConsistencyLevel.setWriteCfConsistencyLevels(clmap);

		return configurableConsistencyLevel;
	}

	/**
	 * construct a vector, all the Map stored in the database will be retrived
	 * and stored in this vector
	 */
	public CassandraDAOMap() {
		vos = new Vector<Map>();
		// keyspaceOperator
		// .setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
	}

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

		vos.add(this.mappingHashMapIntoMapObject(key, tempResult));

	}

	public void selectAll() {
		// there is only one map in the database
		HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
		vos.removeAllElements();
		int row_count = 100;
		// KeySerializer ColumnNameSerializer and ValueSerializer are String
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(CassandraDAOMap.keyspaceOperator,
						stringSerializer, stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily(CassandraDAOMap.ColumnFamilyName);
		// null null denotes ,we want all key range returned
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);
		rangeSliceQuery.setRowCount(row_count);

		String last_key = null;

		while (true) {
			rangeSliceQuery.setKeys(last_key, null);
			QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery
					.execute();
			OrderedRows<String, String, String> rows = result.get();
			Iterator<Row<String, String, String>> rowsIterator = rows
					.iterator();

			while (rowsIterator.hasNext()) {
				// skip the first one ,since it is the same as the last one from
				// the
				// previous time we execute

				Row<String, String, String> row = rowsIterator.next();

				last_key = row.getKey();

				if (row.getColumnSlice().getColumns().isEmpty()) {
					continue;
				}

				ColumnSlice<String, String> cs = row.getColumnSlice();

				HashMap<String, String> tempresult = new HashMap<String, String>();

				for (HColumn<String, String> c : cs.getColumns()) {
					tempresult.put(c.getName(), c.getValue());
				}

				resultMap.put(row.getKey(), tempresult);
			}
			if (rows.getCount() < row_count)
				break;
		}
		logger.info("Retrieving all map :" + resultMap.toString());

		for (String key : resultMap.keySet()) {
			vos.add(mappingHashMapIntoMapObject(key, resultMap.get(key)));
		}

	}

	private Map mappingHashMapIntoMapObject(String key,
			HashMap<String, String> h) {
		Map m = new Map();
		m.setId(Integer.valueOf(key));
		m.setName(h.get("name"));
		m.setPosition(Integer.valueOf(h.get("position")));
		m.setSizeTile(Integer.valueOf(h.get("sizeTile")));
		m.setStartTileHeroPosX(Integer.valueOf(h.get("startTileHeroPosX")));
		m.setStartTileHeroPosY(Integer.valueOf(h.get("startTileHeroPosY")));
		m.setWidthInTiles(Integer.valueOf(h.get("widthInTiles")));
		m.setHeightInTiles(Integer.valueOf(h.get("heightInTiles")));
		return m;
	}

	/**
	 * 
	 * this function will create a schema with the name "map" in cassandra it
	 * will be only once invoked before a game server constructed
	 * 
	 * if the keyspace not exists, will create it if the keyspace has a
	 * columnfamily "map" already, will drop it
	 * 
	 */
	public static void createMapSchema() {
		logger.info("=======>Creating MapSchema in Cassandra");

		BasicColumnDefinition columnMapName = new BasicColumnDefinition();
		columnMapName.setName(stringSerializer.toByteBuffer("name"));
		columnMapName.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnWidthInTiles = new BasicColumnDefinition();
		columnWidthInTiles.setName(stringSerializer
				.toByteBuffer("widthInTiles"));
		columnWidthInTiles.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnHeightInTiles = new BasicColumnDefinition();
		columnHeightInTiles.setName(stringSerializer
				.toByteBuffer("heightInTiles"));
		columnHeightInTiles.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnSizeTile = new BasicColumnDefinition();
		columnSizeTile.setName(stringSerializer.toByteBuffer("sizeTile"));
		columnSizeTile.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnStartTileHeroPosX = new BasicColumnDefinition();
		columnStartTileHeroPosX.setName(stringSerializer
				.toByteBuffer("startTileHeroPosX"));
		columnStartTileHeroPosX.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnStartTileHeroPosY = new BasicColumnDefinition();
		columnStartTileHeroPosY.setName(stringSerializer
				.toByteBuffer("startTileHeroPosY"));
		columnStartTileHeroPosY.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition columnPosition = new BasicColumnDefinition();
		columnPosition.setName(stringSerializer.toByteBuffer("position"));
		columnPosition.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// add all columnDefinition to columnfamily definition
		BasicColumnFamilyDefinition basicMapDef = new BasicColumnFamilyDefinition();
		// basicMapDef.addColumnDefinition(columnRowKeyId);
		basicMapDef.addColumnDefinition(columnMapName);
		basicMapDef.addColumnDefinition(columnWidthInTiles);
		basicMapDef.addColumnDefinition(columnHeightInTiles);
		basicMapDef.addColumnDefinition(columnStartTileHeroPosX);
		basicMapDef.addColumnDefinition(columnStartTileHeroPosY);
		basicMapDef.addColumnDefinition(columnSizeTile);
		basicMapDef.addColumnDefinition(columnPosition);

		basicMapDef.setName(ColumnFamilyName); // set a name for this
												// columnfamily

		basicMapDef.setKeyspaceName(KeySpaceName);
		// set comparatortype of the columnfamily
		basicMapDef.setComparatorType(ComparatorType.UTF8TYPE);
		ColumnFamilyDefinition CFMapDef = new ThriftCfDef(basicMapDef);
		// if the keyspace not exists, then create a keyspace
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 3,
						Arrays.asList(CFMapDef));

		try {

			// if the keyspace exists
			if (gameCluster.describeKeyspace(KeySpaceName) != null) {

				gameCluster.dropColumnFamily(KeySpaceName, ColumnFamilyName,
						true);
				gameCluster.addColumnFamily(CFMapDef);

			} else {
				logger.debug("Keyspace not exists, create it");
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());
			gameCluster.addColumnFamily(CFMapDef);
		}
		logger.info(" MapSchema created<=======");

	}

	public static void prepopulateMapData() {
		logger.info("Starting prepopulating data for CF Map");
		logger.info("consistency level" + keyspaceOperator);

		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		//set consistency policy as read one write one.
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		
		Mutator<String> mutator = columnFamilyTemplate.createMutator();

		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("name", "map_1"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("widthInTiles", "30"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("heightInTiles", "30"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("sizeTile", "32"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("startTileHeroPosX", "12"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("startTileHeroPosY", "9"));
		mutator.addInsertion("1", ColumnFamilyName,
				HFactory.createStringColumn("position", "0"));

		mutator.execute();
		logger.info("the data have been insterted to map");

	}

	public void testVectorMaps() {
		Map vo;
		Iterator<Map> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	// used for test this class
	public static void main(String[] str) {
		CassandraDAOMap.createMapSchema();
		CassandraDAOMap.prepopulateMapData();
		CassandraDAOMap dao = new CassandraDAOMap();
		dao.selectAll();
		dao.testVectorMaps();
	}
}
