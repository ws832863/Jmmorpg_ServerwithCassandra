package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.utils.Utils;
import game.database.player.vo.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
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
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.cassandra.thrift.IndexType;
import org.apache.log4j.Logger;

public class CassandraDAOPlayer {

	private final static Logger logger = Logger
			.getLogger(CassandraDAOPlayer.class.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	private final static String ColumnFamilyName = "Player";

	private Vector<Player> vos;

	public Vector<Player> getVos() {
		return vos;
	}

	public CassandraDAOPlayer() {
		vos = new Vector<Player>();
	}

	/**
	 * if a new user comes, create a hero for the user
	 * 
	 * @param loginId
	 * @param Username
	 */
	public static void createOneHeroForNewUser(String key, String loginId,
			String Username) {
		String currentTimeString = Utils.CurrentDateString;
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		// not solved, the key of the player
		String rowkey = key;
		// let key = loginId a user has noly one hero
		key = loginId;

		// user the loginname as Heroname and display it
		String Heroname = Username;
		// a random race of the hero
		String classeid = String.valueOf(new Random(32).nextInt(8) + 1);
		// the loginid must be identisch with the login columnfamily
		String loginid = loginId;

		addInsseration(mutator, key, "1", loginid, Heroname, classeid, "100",
				"100", "100", "100", "100", "0", "10", "10", "10", "10", "10",
				"10", "10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");

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
		logger.info(tempResult.toString());
		vos.add(this.mappingHashMapIntoPlayerObject(key, tempResult));

	}

	private Player mappingHashMapIntoPlayerObject(String rowkey,
			HashMap<String, String> h) {
		Player player = new Player();
		player.setId(Integer.valueOf(rowkey));
		// the heroname of the user, display online
		player.setName(h.get("heroname"));
		// forgien key ---- login-id
		player.setLoginId(Integer.valueOf(h.get("loginid")));
		// mapid associated with channel id
		player.setMapId(Integer.valueOf(h.get("mapid")));
		// which classe is this hero
		player.setClasseId(Integer.valueOf(h.get("classeid")));

		player.setCha(Integer.valueOf(h.get("cha")));
		player.setCon(Integer.valueOf(h.get("con")));
		player.setDateCreate(Utils.dateFromString(h.get("datecreate")
				.toString()));
		player.setDex(Integer.valueOf(h.get("dex")));
		player.setEvasion(Integer.valueOf(h.get("evasion")));
		player.setExpCurr(Integer.valueOf(h.get("expcurr")));
		player.setExpMax(Integer.valueOf(h.get("expmax")));
		player.setHpCurr(Integer.valueOf(h.get("hpcurr")));
		player.setHpMax(Integer.valueOf(h.get("hpmax")));
		player.setInte(Integer.valueOf(h.get("inte")));
		player.setLastAcess(Utils
				.dateFromString(h.get("lastaccess").toString()));
		player.setManaCurr(Integer.valueOf(h.get("manacurr")));
		player.setManaMax(Integer.valueOf(h.get("manamax")));
		player.setOnLine(h.get("online"));
		player.setResMagic(Integer.valueOf(h.get("resmagic")));
		player.setSector(Integer.valueOf(h.get("sector")));
		player.setSex(h.get("sex"));
		player.setSp(Integer.valueOf(h.get("sp")));
		player.setStamina(Integer.valueOf(h.get("stamina")));
		player.setStr(Integer.valueOf(h.get("str")));
		player.setWis(Integer.valueOf(h.get("wis")));

		return player;
	}

	public void selectByLoginName(String username) {
		// loginname is hero name, so we search the heroname in player column
		// family

		vos.removeAllElements();
		int row_count = 10;
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspaceOperator, stringSerializer,
						stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily(ColumnFamilyName);
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);// get
																		// 1000
																		// columns
		rangeSliceQuery.setRowCount(row_count);
		rangeSliceQuery.setKeys(null, null);
		rangeSliceQuery.addEqualsExpression("heroname", username);

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
						+ username);
				vos.add(this.mappingHashMapIntoPlayerObject(row.getKey(),
						tempMap));

			}
		}

	}

	public void selectAll() {

	}

	public static void createPlayerSchema() {

		logger.info("======>creating cassandra schema Player");
		BasicColumnDefinition columnMapid = new BasicColumnDefinition();
		columnMapid.setName(stringSerializer.toByteBuffer("mapid"));
		columnMapid.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition columnLoginid = new BasicColumnDefinition();
		columnLoginid.setName(stringSerializer.toByteBuffer("loginid"));
		columnLoginid.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// same with login name
		BasicColumnDefinition columnHeroName = new BasicColumnDefinition();
		columnHeroName.setName(stringSerializer.toByteBuffer("heroname"));
		columnHeroName.setIndexType(ColumnIndexType.KEYS);
		columnHeroName.setIndexName("heroname_Idx");
		columnHeroName.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colClassId = new BasicColumnDefinition();
		colClassId.setName(stringSerializer.toByteBuffer("classeid"));
		colClassId.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colHpMax = new BasicColumnDefinition();
		colHpMax.setName(stringSerializer.toByteBuffer("hpmax"));
		colHpMax.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colHpCurr = new BasicColumnDefinition();
		colHpCurr.setName(stringSerializer.toByteBuffer("hpcurr"));
		colHpCurr.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colManaMax = new BasicColumnDefinition();
		colManaMax.setName(stringSerializer.toByteBuffer("manamax"));
		colManaMax.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colManaCurr = new BasicColumnDefinition();
		colManaCurr.setName(stringSerializer.toByteBuffer("manacurr"));
		colManaCurr.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colExpMax = new BasicColumnDefinition();
		colExpMax.setName(stringSerializer.toByteBuffer("expmax"));
		colExpMax.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colExpCurr = new BasicColumnDefinition();
		colExpCurr.setName(stringSerializer.toByteBuffer("expcurr"));
		colExpCurr.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colStr = new BasicColumnDefinition();
		colStr.setName(stringSerializer.toByteBuffer("str"));
		colStr.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colDex = new BasicColumnDefinition();
		colDex.setName(stringSerializer.toByteBuffer("dex"));
		colDex.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colInte = new BasicColumnDefinition();
		colInte.setName(stringSerializer.toByteBuffer("inte"));
		colInte.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colCon = new BasicColumnDefinition();
		colCon.setName(stringSerializer.toByteBuffer("con"));
		colCon.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colCha = new BasicColumnDefinition();
		colCha.setName(stringSerializer.toByteBuffer("cha"));
		colCha.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colWis = new BasicColumnDefinition();
		colWis.setName(stringSerializer.toByteBuffer("wis"));
		colWis.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colStamina = new BasicColumnDefinition();
		colStamina.setName(stringSerializer.toByteBuffer("stamina"));
		colStamina.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colSex = new BasicColumnDefinition();
		colSex.setName(stringSerializer.toByteBuffer("sex"));
		colSex.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colResMagic = new BasicColumnDefinition();
		colResMagic.setName(stringSerializer.toByteBuffer("resmagic"));
		colResMagic.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colResPhysical = new BasicColumnDefinition();
		colResPhysical.setName(stringSerializer.toByteBuffer("resphysical"));
		colResPhysical.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colSp = new BasicColumnDefinition();
		colSp.setName(stringSerializer.toByteBuffer("sp"));
		colSp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colEvasion = new BasicColumnDefinition();
		colEvasion.setName(stringSerializer.toByteBuffer("evasion"));
		colEvasion.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colDateCreate = new BasicColumnDefinition();
		colDateCreate.setName(stringSerializer.toByteBuffer("datecreate"));
		colDateCreate.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colOnLine = new BasicColumnDefinition();
		colOnLine.setName(stringSerializer.toByteBuffer("online"));
		colOnLine.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
		BasicColumnDefinition colLastAccess = new BasicColumnDefinition();
		colLastAccess.setName(stringSerializer.toByteBuffer("lastaccess"));
		colLastAccess.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		BasicColumnDefinition colLogOutPosXMap = new BasicColumnDefinition();
		colLogOutPosXMap
				.setName(stringSerializer.toByteBuffer("logoutposxmap"));
		colLogOutPosXMap.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		BasicColumnDefinition colLogOutPosYMap = new BasicColumnDefinition();
		colLogOutPosYMap
				.setName(stringSerializer.toByteBuffer("logoutposymap"));
		colLogOutPosYMap.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colSector = new BasicColumnDefinition();
		colSector.setName(stringSerializer.toByteBuffer("sector"));
		colSector.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		// add all columnDefinition to columnfamily definition
		BasicColumnFamilyDefinition basicPlayerDef = new BasicColumnFamilyDefinition();

		basicPlayerDef.addColumnDefinition(columnMapid);
		basicPlayerDef.addColumnDefinition(columnLoginid);
		basicPlayerDef.addColumnDefinition(columnHeroName);
		basicPlayerDef.addColumnDefinition(colClassId);

		basicPlayerDef.addColumnDefinition(colManaMax);
		basicPlayerDef.addColumnDefinition(colManaCurr);
		basicPlayerDef.addColumnDefinition(colHpMax);
		basicPlayerDef.addColumnDefinition(colHpCurr);
		basicPlayerDef.addColumnDefinition(colExpMax);
		basicPlayerDef.addColumnDefinition(colExpCurr);

		basicPlayerDef.addColumnDefinition(colSp);
		basicPlayerDef.addColumnDefinition(colStr);
		basicPlayerDef.addColumnDefinition(colDex);
		basicPlayerDef.addColumnDefinition(colInte);
		basicPlayerDef.addColumnDefinition(colCon);
		basicPlayerDef.addColumnDefinition(colCha);
		basicPlayerDef.addColumnDefinition(colWis);
		basicPlayerDef.addColumnDefinition(colStamina);
		basicPlayerDef.addColumnDefinition(colSex);

		basicPlayerDef.addColumnDefinition(colResPhysical);
		basicPlayerDef.addColumnDefinition(colResMagic);
		basicPlayerDef.addColumnDefinition(colEvasion);

		basicPlayerDef.addColumnDefinition(colDateCreate);
		basicPlayerDef.addColumnDefinition(colOnLine);
		basicPlayerDef.addColumnDefinition(colLastAccess);
		basicPlayerDef.addColumnDefinition(colLogOutPosXMap);
		basicPlayerDef.addColumnDefinition(colLogOutPosYMap);
		basicPlayerDef.addColumnDefinition(colSector);

		basicPlayerDef.setName(ColumnFamilyName);
		basicPlayerDef.setKeyspaceName(KeySpaceName);
		// set comparator type of the column family
		basicPlayerDef.setComparatorType(ComparatorType.UTF8TYPE);

		ColumnFamilyDefinition CFPlayerDef = new ThriftCfDef(basicPlayerDef);
		// if the keyspace not exists, then create a keyspace
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(CFPlayerDef));

		try {
			// if the keyspace exists
			if (gameCluster.describeKeyspace(KeySpaceName) != null) {
				// the columnfamily probaly exist, drop it
				gameCluster.dropColumnFamily(KeySpaceName, ColumnFamilyName,
						true);
			} else {
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());
			// he.printStackTrace();
		}
		gameCluster.addColumnFamily(CFPlayerDef);// the new columnfamily into
													// cassandra
		logger.info("creating cassandra schema Player<=======");

	}

	public static void prepopulateLoginData() {
		String currentTimeString = Utils.CurrentDateString;
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		addInsseration(mutator, "1", "1", "1", "player1", "1", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "2", "1", "2", "player2", "2", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "3", "1", "3", "player3", "3", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "4", "1", "4", "player4", "4", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "5", "1", "5", "player5", "5", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "6", "1", "6", "player6", "6", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "7", "1", "7", "player7", "7", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");
		addInsseration(mutator, "8", "1", "8", "player8", "8", "100", "100",
				"100", "100", "100", "0", "10", "10", "10", "10", "10", "10",
				"10", "M", "5", "5", "0", "0", currentTimeString, "f",
				currentTimeString, "0", "0", "1");

		mutator.execute();

	}

	private static void addInsseration(Mutator<String> mut, String rowkey,
			String mapid, String loginid, String heroname, String classeid,
			String hpmax, String hpcurr, String manamax, String manacurr,
			String expmax, String expcurr, String str, String dex, String inte,
			String con, String cha, String wis, String stamina, String sex,
			String resmagic, String resphysical, String sp, String evasion,
			String datecreate, String online, String lastaccess,
			String logoutposxmap, String logoutposymap, String sector) {

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("mapid", mapid));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("loginid", loginid));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("heroname", heroname));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("classeid", classeid));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("hpmax", hpmax));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("hpcurr", hpcurr));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("manamax", manamax));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("manacurr", manacurr));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("expmax", expmax));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("expcurr", expcurr));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("str", str));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("dex", dex));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("inte", inte));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("con", con));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("cha", cha));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("wis", wis));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("stamina", stamina));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("sex", sex));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("resmagic", resmagic));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("resphysical", resphysical));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("sp", sp));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("evasion", evasion));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("datecreate", datecreate));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("online", online));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastaccess", lastaccess));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("logoutposxmap", logoutposxmap));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("logoutposymap", logoutposymap));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("sector", sector));
	}

	public void testVectorClasses() {
		Player vo;
		Iterator<Player> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	public static void main(String[] args) {
		CassandraDAOPlayer.createPlayerSchema();
		CassandraDAOPlayer.prepopulateLoginData();
		CassandraDAOPlayer dao = new CassandraDAOPlayer();
		dao.selectByLoginName("player1");
		dao.testVectorClasses();

	}

}
