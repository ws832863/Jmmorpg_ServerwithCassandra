package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.conn.DataPrepopulate;
import game.cassandra.conn.GameSchemaCreation;
import game.cassandra.data.Login;
import game.cassandra.data.Player;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import me.prettyprint.cassandra.serializers.ByteBufferSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
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

public class DAOLogin {

	private final Cluster gameCluster;
	private final Keyspace keyspace;

	private StringSerializer stringSerializer = StringSerializer.get();
	private final static Logger logger = Logger.getLogger(DAOLogin.class
			.getName());
	/** specify the keyspace name **/
	private final static String keySpaceName = "JMMORPG";

	/** the column family name **/
	private final static String CFLoginUser = "UserLogin";

	public DAOLogin() {
		vos = new Vector<Login>();
		gameCluster = CassandraConnection.getCluster();
		keyspace = HFactory.createKeyspace(keySpaceName, gameCluster);
		resultList = new HashMap<UUID, Map<String, String>>();
	}

	private Vector<Login> vos;
	private HashMap<UUID, Map<String, String>> resultList;

	public Vector<Login> getVos() {
		return vos;
	}

	public void selectAll() {
		int row_count = 100;
		RangeSlicesQuery<UUID, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspace, UUIDSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		rangeSliceQuery.setColumnFamily("UserLogin");
		rangeSliceQuery.setRange(null, null, false, 1000);// get 1000 columns
		rangeSliceQuery.setRowCount(row_count);
		UUID last_key = null;

		while (true) {
			rangeSliceQuery.setKeys(last_key, null);
			System.out.println(">" + last_key);
			QueryResult<OrderedRows<UUID, String, String>> result = rangeSliceQuery
					.execute();
			OrderedRows<UUID, String, String> rows = result.get();
			Iterator<Row<UUID, String, String>> rowsIterator = rows.iterator();

			while (rowsIterator.hasNext()) {
				// skip the first one ,since it is the same as the last one from
				// the
				// previous time we execute

				Row<UUID, String, String> row = rowsIterator.next();

				last_key = row.getKey();
				if (row.getColumnSlice().getColumns().isEmpty()) {
					continue;
				}

				ColumnSlice<String, String> cs = row.getColumnSlice();

				Map<String, String> resultMap = new HashMap<String, String>();

				for (HColumn<String, String> c : cs.getColumns()) {
					resultMap.put(c.getName(), c.getValue());

				}

				resultList.put(row.getKey(), resultMap);
			}
			if (rows.getCount() < row_count)
				break;
		}
		// System.out.println(resultList.toString());
		resultListToVector(resultList);
	}

	public static void main(String[] str) {
		DAOLogin dao = new DAOLogin();
		dao.SearchingUserUseSecondaryIndexByNameAndPassword("player1", "player");
		Login lo = dao.getVos().firstElement();
		DAOPlayer dp = new DAOPlayer();
		System.out.println(dp.getUUIDbySpecifyHeroName("player1"));
		dp.getHeroByUserNameAndHeroName(lo.getLoginId(), lo.getName(),
				lo.getName());
		System.out.println(lo.getLoginId());
	}

	/**
	 * 
	 * @param username
	 *            loginid of the user
	 * @param password
	 *            password of the user
	 * @param createHera
	 *            if the createHero is ture, then we will create a default hero
	 *            with random hero race for the user
	 */
	public void insertUserIntoCassandra(String username, String password,
			boolean createHero) {
		ColumnFamilyTemplate<UUID, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, CFLoginUser, UUIDSerializer.get(), stringSerializer);

		Mutator<UUID> mut = columnFamilyTemplate.createMutator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String dateString = sdf.format(new Date()).toString();
		UUID uuid = UUID.randomUUID();
		createAdefaultUser(mut, uuid, "2", "player1", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player1", "player1@st.ovgu.de", "1985-10-11");
		if (createHero) {
			/**
			 * specify if we will create a hero for the user in to database
			 * uuid, userloginname,and the heroname, by default the hero name is
			 * identisch with the username
			 */
			DAOPlayer daoplayer = new DAOPlayer();
			daoplayer.insertPlayerHeroIntoCassandra(uuid, username, username);
		}

		mut.execute();
	}

	/**
	 * create a default user in the cassandra
	 * 
	 * @param mut
	 * @param key
	 * @param clanid
	 * @param username
	 * @param usrpwd
	 * @param accesslevel
	 * @param dateregister
	 * @param lastip
	 * @param lastactive
	 * @param usrcurrip
	 * @param lastserver
	 * @param truename
	 * @param email
	 * @param birthday
	 */
	private void createAdefaultUser(Mutator<UUID> mut, UUID key, String clanid,
			String username, String usrpwd, String accesslevel,
			String dateregister, String lastip, String lastactive,
			String usrcurrip, String lastserver, String truename, String email,
			String birthday) {
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("clainid", clanid));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("username", username));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("userpassword", usrpwd));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("accesslevel", accesslevel));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("dateregister", dateregister));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("lastip", lastip));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("lastactive", lastactive));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("usrcurrip", usrcurrip));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("lastserver", lastserver));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("truename", truename));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("email", email));
		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn("birthday", birthday));

		/*
		 * 2013 4 9 add, Index Helper deprecated, because i applied a secondary
		 * index
		 * 
		 * creating a user in the meanwhile create a roll for the user in CF
		 * Loginuser
		 * 
		 * columname username_classid username_xxxx
		 */
		// Index_Helper.insertUserNameForUUID(username, key, usrpwd);
		// createDefaultPlayerRoller(key, username);

	}

	/**
	 * get a user's information by specify a uuid
	 * 
	 * @param uuid
	 * @return
	 */
	public HashMap<UUID, Map<String, String>> getAUserByUUID(UUID uuid) {
		SliceQuery<UUID, String, String> query = HFactory.createSliceQuery(
				keyspace, UUIDSerializer.get(), stringSerializer,
				stringSerializer);

		query.setKey(uuid).setColumnFamily("UserLogin");

		HashMap<UUID, Map<String, String>> res = new HashMap<UUID, Map<String, String>>();
		HashMap<String, String> colMap = new HashMap<String, String>();
		ColumnSliceIterator<UUID, String, String> iterator = new ColumnSliceIterator<UUID, String, String>(
				query, null, "\uFFFF", false);

		while (iterator.hasNext()) {

			HColumn<String, String> c = iterator.next();
			colMap.put(c.getName(), c.getValue());

		}

		res.put(uuid, colMap);
		System.out.println("Searching a user by specifying uuid "
				+ uuid.toString());
		System.out.println(res);

		return res;

	}

	public void selectByLoginPassword(String username, String password) {
		vos.removeAllElements();

		ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(
				keyspace, "UserNameForUUID", stringSerializer, stringSerializer);
		ColumnFamilyResult<String, String> res = template
				.queryColumns(username);

		UUID uuid = res.getUUID("uuid");
		String pwd = res.getString("userpassword");

		if (uuid != null && pwd.equals(password)) {
			System.out
					.println("======================user verified=========================");
			this.getAUserByUUID(uuid);
			this.resultListToVector(this.getAUserByUUID(uuid));
		} else {
			vos.removeAllElements();
		}
	}

	/**
	 * load the resultmaplist into the Vector<Login>
	 * 
	 * @param resultList
	 */
	private void resultListToVector(
			HashMap<UUID, Map<String, String>> resultList) {
		vos.removeAllElements();
		System.out.println("size of result =========================="
				+ resultList.size() + resultList.toString());
		for (UUID id : resultList.keySet()) {

			Login login = new Login();

			login.setLoginId(id);
			// login.setId(id);
			login.setBirthday(resultList.get(id).get("birthday"));
			// login.setBirth(resultList.get(id).get("birthday"));

			login.setUser_password(resultList.get(id).get("userpassword"));
			login.setLogin(resultList.get(id).get("username"));
			login.setLastServer(resultList.get(id).get("lastserver"));
			login.setAccessLevel(resultList.get(id).get("accesslevel"));
			login.setName(resultList.get(id).get("truename"));
			login.setEmail(resultList.get(id).get("email"));

			login.setLastactivetime(resultList.get(id).get("lastactive"));
			// login.setLastactive(resultList.get(id).get("lastactive"));

			login.setRegisterDate(resultList.get(id).get("dateregister"));
			// login.setDateRegister(resultList.get(id).get("dateregister"));
			login.setUserCurrIP(resultList.get(id).get("usrcurrip"));

			login.setClanId(Integer.valueOf(resultList.get(id).get("clainid")));
			login.setLastIP(resultList.get(id).get("lastip"));
			// System.out.print(id + ": ");
			vos.add(login);
			System.out.println(resultList.get(id));
		}
	}

	public void insertVo(String login, String user_password,
			String accessLevel, Date dateRegister, String lastIP,
			Date lastactive, String userCurrIP, String lastServer, String name,
			String email, Date birth, int clanId) {
		StringBuilder sql = new StringBuilder();

	}

	// public static Date StringToJavaSqlDate(String str) {
	// SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
	// try {
	// sdf.parse(str);
	// } catch (ParseException e) {
	// // TODO Automatisch erstellter Catch-Block
	// e.printStackTrace();
	// }
	// return new Date(sdf.YEAR_FIELD, sdf.MONTH_FIELD, sdf.DATE_FIELD);
	// }

	public void testVectorLogin() {
		Login vo;
		Iterator<Login> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	public void SearchingUserUseSecondaryIndexByNameAndPassword(
			String username, String password) {
		vos.removeAllElements();
		int row_count = 10;
		boolean userverified = false;
		RangeSlicesQuery<UUID, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspace, UUIDSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		rangeSliceQuery.setColumnFamily("UserLogin");
		rangeSliceQuery.setRange(null, null, false, Integer.MAX_VALUE);// get
																		// 1000
																		// columns
		rangeSliceQuery.setRowCount(row_count);
		rangeSliceQuery.setKeys(null, null);
		rangeSliceQuery.addEqualsExpression("username", username);

		QueryResult<OrderedRows<UUID, String, String>> result = rangeSliceQuery
				.execute();
		OrderedRows<UUID, String, String> rows = result.get();

		if (rows.getCount() != 0) {// at least one rows returned

			Iterator<Row<UUID, String, String>> rowsIterator = rows.iterator();

			System.out.println("has next " + rowsIterator.hasNext());
			while (rowsIterator.hasNext()) {
				Row<UUID, String, String> row = rowsIterator.next();
				ColumnSlice<String, String> cs = row.getColumnSlice();

				Map<String, String> resultMap = new HashMap<String, String>();
				for (HColumn<String, String> c : cs.getColumns()) {// add all
																	// column in
					// the map
					resultMap.put(c.getName(), c.getValue());

				}
				logger.info(row.getKey() + "  " + resultMap.toString());
				// logger.info(password);
				// logger.info(resultMap.get("birthday"));
				// logger.info(resultMap.get("lastserver"));
				// logger.info(resultMap.get("userpassword"));

				if (resultMap.get("userpassword").equals(password)) {
					userverified = true;
					resultList.put(row.getKey(), resultMap);
				}
			}
			if (!userverified) {

				System.out.println("Wrong Password");
				resultList.clear();
			} else {

				System.out
						.println("=============Searching using secondary Index=================");
				System.out.println(resultList.toString());
				resultListToVector(resultList);
			}

		} else {// no result returnd
			System.out.println("No User was found in Cassandra");
		}
	}
}
