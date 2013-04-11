package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.data.Login;
import game.cassandra.data.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.log4j.Logger;

public class DAOPlayer {
	private final Cluster gameCluster;
	private final Keyspace keyspace;

	private StringSerializer stringSerializer = StringSerializer.get();
	private final static Logger logger = Logger.getLogger(DAOLogin.class
			.getName());
	/** specify the keyspace name **/
	private final static String keySpaceName = "JMMORPG";

	/** the column family name **/
	private final static String CFLoginUser = "UserLogin";

	private Vector<Player> vos;
	private HashMap<UUID, Map<String, String>> resultList;

	public DAOPlayer() {
		vos = new Vector<Player>();
		gameCluster = CassandraConnection.getCluster();
		keyspace = HFactory.createKeyspace(keySpaceName, gameCluster);
		resultList = new HashMap<UUID, Map<String, String>>();
	}

	public Vector<Player> getVos() {
		return vos;
	}

	public Player getUserHero(String username) {
		UUID uuid = this.getUUIDbySpecifyHeroName(username);

		return this.getHeroByUserNameAndHeroName(uuid, username, username);
	}

	public UUID getUUIDbySpecifyHeroName(String Heroname) {
		UUID uuid = null;
		int row_count = 10;
		RangeSlicesQuery<UUID, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspace, UUIDSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		rangeSliceQuery.setColumnFamily("UserLogin");
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);// get
																		// 1000
																		// columns
		rangeSliceQuery.setRowCount(row_count);
		rangeSliceQuery.setKeys(null, null);
		rangeSliceQuery.addEqualsExpression("username", Heroname);
		//rangeSliceQuery.setReturnKeysOnly();
		QueryResult<OrderedRows<UUID, String, String>> result = rangeSliceQuery
				.execute();
		OrderedRows<UUID, String, String> rows = result.get();
		if (rows.getCount() != 0) {// at least one rows returned
			Iterator<Row<UUID, String, String>> rowsIterator = rows.iterator();
			while (rowsIterator.hasNext()) {
				Row<UUID, String, String> row = rowsIterator.next();
				uuid = row.getKey();
			}
		}
		return uuid;
	}

	/**
	 * 
	 * create a player roller by specifying a username and uuid
	 * 
	 * @param uuid
	 *            the user's unique uuid
	 * @param username
	 *            the user's login name
	 * @param Heroname
	 *            the user's heroname
	 */
	public void insertPlayerHeroIntoCassandra(UUID uuid, String username,
			String Heroname) {
		ColumnFamilyTemplate<UUID, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, CFLoginUser, UUIDSerializer.get(), stringSerializer);
		String RowKeyPrifix = username + "_" + Heroname + "_";
		String randomClassid = String.valueOf((new Random(32).nextInt(8) + 1));

		Mutator<UUID> mut = columnFamilyTemplate.createMutator();
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "mapid", "1", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "heroname", Heroname, stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "classid", randomClassid, stringSerializer, stringSerializer));

		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "hpmax", "100", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "hpcurr", "100", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "manamax", "100", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "manacurr", "100", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "expmax", "100", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "expcurr", "0", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "sp", "0", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "str", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "dex", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "inte", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "con", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "cha", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "wis", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "stamina", "10", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "sex", "M", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "resMagic", "5", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "resPhysical", "5", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "evasion", "0", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "datecreate", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "online", "f", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "lastaccess", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "logoutpoxmap", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "logoutpoymap", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "sector", "", stringSerializer, stringSerializer));

		mut.execute();
	}

	/**
	 * get a user's Hero and return a player instance
	 * 
	 * @param keyuuid
	 * @param uname
	 * @param Heroname
	 * @return
	 */
	public Player getHeroByUserNameAndHeroName(UUID keyuuid, String uname,
			String Heroname) {
		Player player = new Player();
		SliceQuery<UUID, String, String> query = HFactory.createSliceQuery(
				keyspace, UUIDSerializer.get(), stringSerializer,
				StringSerializer.get());
		String start = uname + "_" + Heroname + "_";
		String end = start + "zzzzzzzz";
		System.out.println(uname + "_" + Heroname + "_");
		query.setKey(keyuuid).setColumnFamily("UserLogin");
		query.setRange(start, end, false, Integer.MAX_VALUE);
		ColumnSliceIterator<UUID, String, String> iterator = new ColumnSliceIterator<UUID, String, String>(
				query, start, end, false);

		HashMap<String, String> colMap = new HashMap<String, String>();
		while (iterator.hasNext()) {

			HColumn<String, String> c = iterator.next();
			colMap.put(c.getName(), c.getValue());

		}

		// System.out.println(colMap.keySet());
		player.setLoginId(keyuuid);
		player.setCha(Integer.valueOf(colMap.get(start + "cha")));
		player.setClasseId(Integer.valueOf(colMap.get(start + "classid")));
		player.setCon(Integer.valueOf(colMap.get(start + "con")));
		player.setDateCreate(colMap.get(start + "datecreate"));
		player.setDex(Integer.valueOf(colMap.get(start + "dex")));
		player.setEvasion(Integer.valueOf(colMap.get(start + "evasion")));
		player.setExpCurr(Integer.valueOf(colMap.get(start + "expcurr")));
		player.setExpMax(Integer.valueOf(colMap.get(start + "expmax")));
		player.setHpCurr(Integer.valueOf(colMap.get(start + "hpcurr")));
		player.setHpMax(Integer.valueOf(colMap.get(start + "hpmax")));
		player.setInte(Integer.valueOf(colMap.get(start + "inte")));
		player.setLastAcess(colMap.get(start + "lastaccess"));
		player.setManaCurr(Integer.valueOf(colMap.get(start + "manacurr")));
		player.setManaMax(Integer.valueOf(colMap.get(start + "manamax")));
		player.setMapId(Integer.valueOf(colMap.get(start + "mapid")));
		// player.setName(Heroname);
		player.setOnLine(colMap.get(start + "online"));
		player.setResMagic(Integer.valueOf(colMap.get(start + "resMagic")));
		player.setResPhysical(Integer.valueOf(colMap.get(start + "resPhysical")));
		player.setSector(Integer.valueOf(colMap.get(start + "sector")));
		player.setSex(colMap.get(start + "sex"));
		player.setSp(Integer.valueOf(colMap.get(start + "sp")));
		player.setStamina(Integer.valueOf(colMap.get(start + "stamina")));
		player.setStr(Integer.valueOf(colMap.get(start + "str")));
		player.setWis(Integer.valueOf(colMap.get(start + "wis")));

		return player;

	}

}