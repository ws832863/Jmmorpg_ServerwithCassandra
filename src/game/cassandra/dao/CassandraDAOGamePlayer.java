package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.data.GamePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.log4j.Logger;

public class CassandraDAOGamePlayer {
	private final static Logger logger = Logger
			.getLogger(CassandraDAOGamePlayer.class.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	// only create login, player not include
	private final static String ColumnFamilyName = "GamePlayer";

	private Vector<GamePlayer> vos;

	public Vector<GamePlayer> getVos() {
		return vos;
	}

	private static ConfigurableConsistencyLevel consistency() {
		// create a customized Consistency Level
		ConfigurableConsistencyLevel configurableConsistencyLevel = new ConfigurableConsistencyLevel();
		HashMap<String, HConsistencyLevel> clmap = new HashMap<String, HConsistencyLevel>();
		// define cl.one for columnfamily map
		clmap.put(ColumnFamilyName, HConsistencyLevel.ONE);
		// in this we use cl.one for read and writes
		configurableConsistencyLevel.setReadCfConsistencyLevels(clmap);
		configurableConsistencyLevel.setWriteCfConsistencyLevels(clmap);

		return configurableConsistencyLevel;
	}

	public CassandraDAOGamePlayer() {
		vos = new Vector<GamePlayer>();
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
	}

	public void selectByPk(int rowKey) {
		String key = String.valueOf(rowKey);
		vos.removeAllElements();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspaceOperator, stringSerializer, stringSerializer,
				stringSerializer);

		query.setKey(key).setColumnFamily(ColumnFamilyName);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, null, "\uFFFF", false);

		HashMap<String, String> tempResult = new HashMap<String, String>();

		while (iterator.hasNext()) {
			HColumn<String, String> c = iterator.next();
			tempResult.put(c.getName(), c.getValue());
		}
		logger.info("get a user by pk " + tempResult.toString());
		vos.add(this.mappingHashMapIntoGamePlayerObject(key, tempResult));

	}

	public void selectByLoginName(String Loginname) {

		// loginname is hero name, so we search the heroname in player column
		// family

		vos.removeAllElements();
		int row_count = 10;
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspaceOperator, stringSerializer,
						stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily(ColumnFamilyName);
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);
		rangeSliceQuery.setRowCount(row_count);
		rangeSliceQuery.setKeys(null, null);
		rangeSliceQuery.addEqualsExpression("username", Loginname);

		QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery
				.execute();
		OrderedRows<String, String, String> rows = result.get();

		if (rows.getCount() != 0) {// at least one rows returned

			Iterator<Row<String, String, String>> rowsIterator = rows
					.iterator();

			// System.out.println("has next " + rowsIterator.hasNext());
			while (rowsIterator.hasNext()) {
				Row<String, String, String> row = rowsIterator.next();
				ColumnSlice<String, String> cs = row.getColumnSlice();

				HashMap<String, String> tempMap = new HashMap<String, String>();
				for (HColumn<String, String> c : cs.getColumns()) {// add all
					tempMap.put(c.getName(), c.getValue());

				}
				logger.info("get the roller by specifying a heroname(username) "
						+ Loginname);
				vos.add(this.mappingHashMapIntoGamePlayerObject(row.getKey(),
						tempMap));

			}
		}

	}

	public boolean selectByLoginPassword(String username, String password) {
		vos.removeAllElements();
		int row_count = 10;
		boolean userverified = false;
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspaceOperator, stringSerializer,
						stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily(ColumnFamilyName);
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);// get
																		// 1000
																		// columns
		rangeSliceQuery.setRowCount(row_count);
		rangeSliceQuery.setKeys(null, null);
		rangeSliceQuery.addEqualsExpression("username", username);

		QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery
				.execute();
		OrderedRows<String, String, String> rows = result.get();

		if (rows.getCount() != 0) {// at least one rows returned

			Iterator<Row<String, String, String>> rowsIterator = rows
					.iterator();

			// System.out.println("has next " + rowsIterator.hasNext());
			while (rowsIterator.hasNext()) {
				Row<String, String, String> row = rowsIterator.next();
				ColumnSlice<String, String> cs = row.getColumnSlice();

				HashMap<String, String> tempMap = new HashMap<String, String>();
				for (HColumn<String, String> c : cs.getColumns()) {// add all
					tempMap.put(c.getName(), c.getValue());

				}

				logger.info("searching database with rowkey :" + row.getKey()
						+ " get the resultmap = " + tempMap.toString());

				if (tempMap.get("userpassword").equals(password)) {
					logger.info("user verified");
					userverified = true;
					// vos.add(this.mappingHashMapIntoGamePlayerObject(
					// row.getKey(), tempMap));

				}
			}
			if (!userverified) {

				System.out.println("User gives Wrong Password");
				userverified = false;
			}

		} else {// no result returnd
			System.out.println("No User was found in Cassandra");
			userverified = false;
		}
		return userverified;
	}

	public void selectAll() {
		// once get 100 rows
		int row_count = 100;
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspaceOperator, stringSerializer,
						stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily(ColumnFamilyName);
		// get all columns and all row in the cassandra, this is not good, but
		// for test is ok
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);
		rangeSliceQuery.setRowCount(row_count);
		String last_key = null;
		vos.removeAllElements();

		while (true) {
			rangeSliceQuery.setKeys(last_key, null);
			// System.out.println(">" + last_key);
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

				HashMap<String, String> tempMap = new HashMap<String, String>();

				for (HColumn<String, String> c : cs.getColumns()) {
					tempMap.put(c.getName(), c.getValue());

				}
				System.out.println(tempMap);
				vos.add(this.mappingHashMapIntoGamePlayerObject(row.getKey(),
						tempMap));

				// resultMap.put(row.getKey(), tempMap);
			}
			if (rows.getCount() < row_count)
				break;
		}

	}

	public void addNewGamePlayer(List<GamePlayer> listGp) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		int i = 0;
		for (GamePlayer gp : listGp) {

			CassandraDAOGamePlayer.addInsseration(mutator, gp.getUUIDString(),
					gp.getUserName(), gp.getUserPassword(), gp.getRegistDate(),
					gp.getLastActiveIp(), gp.getLastActiceDate(),
					gp.getTrueName(), gp.getEmail(), gp.getBirth(),
					String.valueOf(gp.getMapId()), gp.getHeroClass(),
					gp.getHeroRace(), String.valueOf(gp.getCurrHp()),
					String.valueOf(gp.getMaxHp()),
					String.valueOf(gp.getCurrExp()),
					String.valueOf(gp.getMaxExp()),
					String.valueOf(gp.getStrength()),
					String.valueOf(gp.getAttack()),
					String.valueOf(gp.getDefense()),
					String.valueOf(gp.getClassId()),
					String.valueOf(gp.getRaceId()));
			i++;
			if (i % 100 == 0) {
				System.out.println(mutator.execute().getExecutionTimeMicro());
			}
		}
		mutator.execute();

	}

	public void addNewGamePlayer(GamePlayer gp) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();

		CassandraDAOGamePlayer
				.addInsseration(mutator, gp.getUUIDString(), gp.getUserName(),
						gp.getUserPassword(), gp.getRegistDate(),
						gp.getLastActiveIp(), gp.getLastActiceDate(),
						gp.getTrueName(), gp.getEmail(), gp.getBirth(),
						String.valueOf(gp.getMapId()), gp.getHeroClass(),
						gp.getHeroRace(), String.valueOf(gp.getCurrHp()),
						String.valueOf(gp.getMaxHp()),
						String.valueOf(gp.getCurrExp()),
						String.valueOf(gp.getMaxExp()),
						String.valueOf(gp.getStrength()),
						String.valueOf(gp.getAttack()),
						String.valueOf(gp.getDefense()),
						String.valueOf(gp.getClassId()),
						String.valueOf(gp.getRaceId()));
		MutationResult mr = mutator.execute();
		System.out.println("time: " + mr.getExecutionTimeMicro());
	}

	private GamePlayer mappingHashMapIntoGamePlayerObject(String rowkey,
			HashMap<String, String> h) {
		GamePlayer gp = new GamePlayer();
		gp.setUUIDString(rowkey);
		gp.setUserName(h.get("username"));
		gp.setUserPassword(h.get("userpassword"));
		gp.setRegistDate(h.get("registdate"));
		gp.setLastActiceDate(h.get("lastactivedate"));
		gp.setLastActiveIp(h.get("lastactiveip"));
		gp.setTrueName(h.get("truename"));
		gp.setEmail(h.get("email"));
		gp.setBirth(h.get("birthday"));
		gp.setMapId(Integer.valueOf(h.get("mapid")));
		gp.setHeroClass(h.get("heroclass"));
		gp.setHeroRace(h.get("herorace"));
		gp.setCurrHp(Integer.valueOf(h.get("currhp")));
		gp.setMaxHp(Integer.valueOf(h.get("maxhp")));
		gp.setCurrExp(Integer.valueOf(h.get("currexp")));
		gp.setMaxExp(Integer.valueOf(h.get("maxexp")));
		gp.setStrength(Integer.valueOf(h.get("strength")));
		gp.setAttack(Integer.valueOf(h.get("attack")));
		gp.setDefense(Integer.valueOf(h.get("defense")));
		gp.setClassId(Integer.valueOf(h.get("classid")));
		gp.setRaceId(Integer.valueOf(h.get("raceid")));
		return gp;

	}

	public static void createGamePlayerSchema() {
		logger.info("======>creating cassandra schema gameplayer ");

		// the user name of a account username column has a index, so we can use
		// Rangeslicesquery to retrieve it by setting addequalsexpression

		// in the sql database, this colums name is id!!!!!!!!
		// this column is the secondary index, we can retrive it use
		// "where username = xx"
		BasicColumnDefinition colUserNameDef = new BasicColumnDefinition();
		colUserNameDef.setName(stringSerializer.toByteBuffer("username"));
		colUserNameDef.setIndexName("loginname_idx");
		colUserNameDef.setIndexType(ColumnIndexType.KEYS);
		colUserNameDef.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		// the user password
		BasicColumnDefinition colUserPassword = new BasicColumnDefinition();
		colUserPassword.setName(stringSerializer.toByteBuffer("userpassword"));
		colUserPassword.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		// the register date of the account
		BasicColumnDefinition colDateRegister = new BasicColumnDefinition();
		colDateRegister.setName(stringSerializer.toByteBuffer("registdate"));
		colDateRegister.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// the last accssed ip
		BasicColumnDefinition colLastIp = new BasicColumnDefinition();
		colLastIp.setName(stringSerializer.toByteBuffer("lastactiveip"));
		colLastIp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colLastActive = new BasicColumnDefinition();
		colLastActive.setName(stringSerializer.toByteBuffer("lastactivedate"));
		colLastActive.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colTrueName = new BasicColumnDefinition();
		colTrueName.setName(stringSerializer.toByteBuffer("truename"));
		colTrueName.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colEmail = new BasicColumnDefinition();
		colEmail.setName(stringSerializer.toByteBuffer("email"));
		colEmail.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colBirth = new BasicColumnDefinition();
		colBirth.setName(stringSerializer.toByteBuffer("birthday"));
		colBirth.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colMap = new BasicColumnDefinition();
		colMap.setName(stringSerializer.toByteBuffer("mapid"));
		colMap.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colheroClass = new BasicColumnDefinition();
		colheroClass.setName(stringSerializer.toByteBuffer("heroclass"));
		colheroClass
				.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colheroRace = new BasicColumnDefinition();
		colheroRace.setName(stringSerializer.toByteBuffer("herorace"));
		colheroRace.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colCurrHp = new BasicColumnDefinition();
		colCurrHp.setName(stringSerializer.toByteBuffer("currhp"));
		colCurrHp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colMaxHp = new BasicColumnDefinition();
		colMaxHp.setName(stringSerializer.toByteBuffer("maxhp"));
		colMaxHp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colCurrExp = new BasicColumnDefinition();
		colCurrExp.setName(stringSerializer.toByteBuffer("currexp"));
		colCurrExp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colMaxExp = new BasicColumnDefinition();
		colMaxExp.setName(stringSerializer.toByteBuffer("maxexp"));
		colMaxExp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colStrength = new BasicColumnDefinition();
		colStrength.setName(stringSerializer.toByteBuffer("strength"));
		colStrength.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colAttack = new BasicColumnDefinition();
		colAttack.setName(stringSerializer.toByteBuffer("attack"));
		colAttack.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colDefense = new BasicColumnDefinition();
		colDefense.setName(stringSerializer.toByteBuffer("defense"));
		colDefense.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colClassId = new BasicColumnDefinition();
		colClassId.setName(stringSerializer.toByteBuffer("classid"));
		colClassId.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colRaceId = new BasicColumnDefinition();
		colRaceId.setName(stringSerializer.toByteBuffer("raceid"));
		colRaceId.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		// definite a basic cf, add all columns in it.
		BasicColumnFamilyDefinition basicCFGamePlayerDef = new BasicColumnFamilyDefinition();
		basicCFGamePlayerDef.setKeyspaceName(KeySpaceName); // keyspace name
		basicCFGamePlayerDef.setName(ColumnFamilyName);// column family name
		// use utf8 type, use command list login, human readable column name
		basicCFGamePlayerDef.setComparatorType(ComparatorType.UTF8TYPE);
		basicCFGamePlayerDef.addColumnDefinition(colUserNameDef);
		basicCFGamePlayerDef.addColumnDefinition(colUserPassword);
		basicCFGamePlayerDef.addColumnDefinition(colDateRegister);
		basicCFGamePlayerDef.addColumnDefinition(colLastIp);
		basicCFGamePlayerDef.addColumnDefinition(colLastActive);
		basicCFGamePlayerDef.addColumnDefinition(colTrueName);
		basicCFGamePlayerDef.addColumnDefinition(colEmail);
		basicCFGamePlayerDef.addColumnDefinition(colBirth);

		basicCFGamePlayerDef.addColumnDefinition(colMap);
		basicCFGamePlayerDef.addColumnDefinition(colheroClass);
		basicCFGamePlayerDef.addColumnDefinition(colheroRace);
		basicCFGamePlayerDef.addColumnDefinition(colCurrHp);
		basicCFGamePlayerDef.addColumnDefinition(colMaxHp);
		basicCFGamePlayerDef.addColumnDefinition(colCurrExp);
		basicCFGamePlayerDef.addColumnDefinition(colMaxExp);
		basicCFGamePlayerDef.addColumnDefinition(colStrength);
		basicCFGamePlayerDef.addColumnDefinition(colAttack);
		basicCFGamePlayerDef.addColumnDefinition(colDefense);

		basicCFGamePlayerDef.addColumnDefinition(colClassId);
		basicCFGamePlayerDef.addColumnDefinition(colRaceId);

		ColumnFamilyDefinition cfLoginUserDef = new ThriftCfDef(
				basicCFGamePlayerDef);
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(cfLoginUserDef));

		try {

			if (gameCluster.describeKeyspace(KeySpaceName) != null) {
				gameCluster.dropColumnFamily(KeySpaceName, ColumnFamilyName,
						true);
			} else {
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());
		}
		gameCluster.addColumnFamily(cfLoginUserDef);

		logger.info(" cassandra gameplayer schema sucessfuly <======");

	}

	public static void GamePlayerPrePopulate() {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		// add a default user
		// addInsseration(mutator, "1");
		String dateString = CassandraConnection.CurrentDateString;
		addInsseration(mutator, "1", "player1", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1985-10-25", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "1", "1");
		addInsseration(mutator, "2", "player2", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1987-03-21", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "2", "2");
		addInsseration(mutator, "3", "player3", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1975-07-12", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "3", "1");
		addInsseration(mutator, "4", "player4", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1986-01-29", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "4", "2");
		addInsseration(mutator, "5", "player5", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1999-11-05", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "5", "1");
		addInsseration(mutator, "6", "player6", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1979-10-25", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "6", "2");
		addInsseration(mutator, "7", "player7", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1988-03-04", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "7", "1");
		addInsseration(mutator, "8", "player8", "player", dateString,
				"127.0.0.1", dateString, "wangshuo", "testmail@gmail.com",
				"1989-06-12", "1", "Assisin", "Human", "100", "100", "0",
				"1000", "10", "10", "10", "8", "2");

		mutator.execute();
	}

	private static void addInsseration(Mutator<String> mut, String rowkey,
			String username, String userpassword, String registdate,
			String lastactiveip, String lastactivedate, String truename,
			String email, String birthday, String mapid, String heroclass,
			String herorace, String currhp, String maxhp, String currexp,
			String maxexp, String strength, String attack, String defense,
			String classid, String raceid) {

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("username", username));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("userpassword", userpassword));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("registdate", registdate));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastactiveip", lastactiveip));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastactivedate", lastactivedate));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("truename", truename));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("email", email));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("birthday", birthday));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("mapid", mapid));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("heroclass", heroclass));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("herorace", herorace));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("currhp", currhp));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("maxhp", maxhp));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("currexp", currexp));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("maxexp", maxexp));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("strength", strength));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("attack", attack));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("defense", defense));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("classid", classid));

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("raceid", raceid));
	}

	public void testVectorClans() {
		GamePlayer vo;
		Iterator<GamePlayer> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println("   " + vo.toString() + "   ");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CassandraDAOGamePlayer.createGamePlayerSchema();
		// CassandraDAOGamePlayer.GamePlayerPrePopulate();
		CassandraDAOGamePlayer cdp = new CassandraDAOGamePlayer();
		cdp.selectAll();
		// cdp.selectByPk(5);
		// cdp.selectByLoginName("player1");
		// cdp.testVectorClans();
		// List<GamePlayer> ll = new ArrayList<GamePlayer>();
		// for (int i = 0; i < 1000; i++) {
		// ll.add(GamePlayerFactory.createPlayer());

		// }
		// cdp.addNewGamePlayer(ll);
	}
}
