package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.database.map.vo.Map;
import game.database.race.vo.Race;

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

	/**
	 * construct a vector, all the Map stored in the database will be retrived
	 * and stored in this vector
	 */
	public CassandraDAOMap() {
		vos = new Vector<Map>();
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
	

	public void selectAll() throws Exception {
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
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(CFMapDef));

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
		gameCluster.addColumnFamily(CFMapDef);// the new columnfamily into
												// cassandra
		logger.info(" MapSchema created<====");

	}

	public static void prepopulateMapDate() {

		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);

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
		// CassandraDAOMap.createMapSchema();
		// CassandraDAOMap.prepopulateMapDate();
		CassandraDAOMap dao = new CassandraDAOMap();
		try {
			dao.selectAll();
			dao.testVectorMaps();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
