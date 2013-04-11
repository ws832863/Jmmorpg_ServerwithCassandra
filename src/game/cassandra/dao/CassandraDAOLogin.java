package game.cassandra.dao;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.utils.Utils;
import game.database.login.vo.Login;
import game.database.map.vo.Map;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
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

import org.apache.log4j.Logger;

public class CassandraDAOLogin {

	private final static Logger logger = Logger
			.getLogger(CassandraDAOLogin.class.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	// only create login, player not include
	private final static String ColumnFamilyName = "Login";

	private Vector<Login> vos;

	public Vector<Login> getVos() {
		return vos;
	}

	public CassandraDAOLogin() {
		vos = new Vector<Login>();
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

		vos.add(this.mappingHashMapIntoLoginObject(key, tempResult));

	}

	public void selectByLoginPassword(String username, String password) {
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

				logger.info("searching database with " + row.getKey()
						+ " resultmap= " + tempMap.toString());

				if (tempMap.get("userpassword").equals(password)) {
					logger.info("user verified");
					userverified = true;
					vos.add(this.mappingHashMapIntoLoginObject(row.getKey(),
							tempMap));

				}
			}
			if (!userverified) {

				System.out.println("User gives Wrong Password");
			}

		} else {// no result returnd
			System.out.println("No User was found in Cassandra");
		}
	}

	public void selectAll() {
		// once get 100 rows
		int row_count = 100;
		RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory
				.createRangeSlicesQuery(keyspaceOperator, stringSerializer,
						stringSerializer, stringSerializer);
		rangeSliceQuery.setColumnFamily("Login");
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
				vos.add(this.mappingHashMapIntoLoginObject(row.getKey(),
						tempMap));

				// resultMap.put(row.getKey(), tempMap);
			}
			if (rows.getCount() < row_count)
				break;
		}

	}

	private Login mappingHashMapIntoLoginObject(String rowkey,
			HashMap<String, String> h) {
		Login login = new Login();

		// pass a row key to a login object
		login.setId(Integer.valueOf(rowkey));

		login.setBirth(Utils.dateFromString(h.get("birthday")));
		login.setUser_password(h.get("userpassword"));
		login.setLogin(h.get("username"));
		login.setLastServer(h.get("lastserver"));
		login.setAccessLevel(h.get("accesslevel"));
		login.setName(h.get("truename"));
		login.setEmail(h.get("email"));
		login.setLastactive(Utils.dateFromString(h.get("lastactive")));
		login.setDateRegister(Utils.dateFromString(h.get("dateregister")));
		login.setUserCurrIP(h.get("usrcurrip"));
		login.setClanId(Integer.valueOf(h.get("clainid")));
		login.setLastIP(h.get("lastip"));
		return login;
	}

	// add a default user, the userid is integer
	//
	public void insertOneDefaultUser(String username, String pwd) {

	}

	public static void createLoginSchema() {
		logger.info("======>creating cassandra schema Login");

		BasicColumnDefinition clanIdDef = new BasicColumnDefinition();
		clanIdDef.setName(stringSerializer.toByteBuffer("clainid"));
		clanIdDef.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
		// the user name of a account username column has a index, so we can use
		// Rangeslicequery to retrieve it by setting addequalsexpression

		// in the sql database, this colums name is id!!!!!!!!
		// this column is the secondary index, we can retrive it use
		// "where username = xx"
		BasicColumnDefinition colUserNameDef = new BasicColumnDefinition();
		colUserNameDef.setName(stringSerializer.toByteBuffer("username"));
		colUserNameDef.setIndexName("usrname_idx");
		colUserNameDef.setIndexType(ColumnIndexType.KEYS);
		colUserNameDef.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		// the user password
		BasicColumnDefinition colUserPassword = new BasicColumnDefinition();
		colUserPassword.setName(stringSerializer.toByteBuffer("userpassword"));
		colUserPassword.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colAccessLevel = new BasicColumnDefinition();
		colAccessLevel.setName(stringSerializer.toByteBuffer("accesslevel"));
		colAccessLevel.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// the register date of the account
		BasicColumnDefinition colDateRegister = new BasicColumnDefinition();
		colDateRegister.setName(stringSerializer.toByteBuffer("dateregister"));
		colDateRegister.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// the last accssed ip
		BasicColumnDefinition colLastIp = new BasicColumnDefinition();
		colLastIp.setName(stringSerializer.toByteBuffer("lastip"));
		colLastIp.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colLastActive = new BasicColumnDefinition();
		colLastActive.setName(stringSerializer.toByteBuffer("lastactive"));
		colLastActive.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());
		// the current id of the users
		BasicColumnDefinition colUserCurrentIp = new BasicColumnDefinition();
		colUserCurrentIp.setName(stringSerializer.toByteBuffer("usrcurrip"));
		colUserCurrentIp.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colLastServer = new BasicColumnDefinition();
		colLastServer.setName(stringSerializer.toByteBuffer("lastserver"));
		colLastServer.setValidationClass(ComparatorType.BYTESTYPE
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

		// definite a basic cf, add all columns in it.
		BasicColumnFamilyDefinition basicCFUserLoginDef = new BasicColumnFamilyDefinition();
		basicCFUserLoginDef.setKeyspaceName(KeySpaceName); // keyspace name
		basicCFUserLoginDef.setName(ColumnFamilyName);// column family name
		// use utf8 type, use command list login, human readable column name
		basicCFUserLoginDef.setComparatorType(ComparatorType.UTF8TYPE);
		basicCFUserLoginDef.addColumnDefinition(clanIdDef);
		basicCFUserLoginDef.addColumnDefinition(colUserNameDef);
		basicCFUserLoginDef.addColumnDefinition(colUserPassword);
		basicCFUserLoginDef.addColumnDefinition(colAccessLevel);
		basicCFUserLoginDef.addColumnDefinition(colDateRegister);
		basicCFUserLoginDef.addColumnDefinition(colLastIp);
		basicCFUserLoginDef.addColumnDefinition(colLastActive);
		basicCFUserLoginDef.addColumnDefinition(colUserCurrentIp);
		basicCFUserLoginDef.addColumnDefinition(colLastServer);
		basicCFUserLoginDef.addColumnDefinition(colTrueName);
		basicCFUserLoginDef.addColumnDefinition(colEmail);
		basicCFUserLoginDef.addColumnDefinition(colBirth);
		basicCFUserLoginDef.addColumnDefinition(colLastIp);

		ColumnFamilyDefinition cfLoginUserDef = new ThriftCfDef(
				basicCFUserLoginDef);
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
			// he.printStackTrace();
		}
		gameCluster.addColumnFamily(cfLoginUserDef);
		// add new columnfamily into cassandra

		logger.info(" cassandra schema Login sucessfuly <======");

	}

	public static void prepopulateLoginData() {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		// add a default user
		// addInsseration(mutator, "1");
		String dateString = CassandraConnection.CurrentDateString;
		addInsseration(mutator, "1", "2", "player1", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player1",
				"player1@st.ovgu.de", "1985-10-11");
		addInsseration(mutator, "2", "2", "player2", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player2",
				"player2@st.ovgu.de", "1986-11-10");
		addInsseration(mutator, "3", "2", "player3", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player3",
				"player3@st.ovgu.de", "1987-10-11");
		addInsseration(mutator, "4", "2", "player4", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player4",
				"player4@st.ovgu.de", "1988-02-28");
		addInsseration(mutator, "5", "2", "player5", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player5",
				"player5@st.ovgu.de", "1956-04-30");
		addInsseration(mutator, "6", "2", "player6", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player6",
				"player6@st.ovgu.de", "1985-06-15");
		addInsseration(mutator, "7", "2", "player7", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player7",
				"player7@st.ovgu.de", "1986-08-23");
		addInsseration(mutator, "8", "2", "player8", "player", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "player8",
				"player8@st.ovgu.de", "1956-03-03");
		addInsseration(mutator, "9", "1", "admin", "admin", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1",
				"administrator", "admin@st.ovgu.de", "1956-12-11");
		addInsseration(mutator, "10", "2", "abcd", "abcd", "0", dateString,
				"127.0.0.1", dateString, "127.0.0.1", "server1", "wangshuo",
				"admin@st.ovgu.de", "1985-12-01");

		mutator.execute();
	}

	private static void addInsseration(Mutator<String> mut, String rowkey,
			String clanid, String username, String usrpwd, String accesslevel,
			String dateregister, String lastip, String lastactive,
			String usrcurrip, String lastserver, String truename, String email,
			String birthday) {
		// mut.addInsertion(rowkey, ColumnFamilyName,
		// HFactory.createColumn("", ""));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("clainid", clanid));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("username", username));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("userpassword", usrpwd));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("accesslevel", accesslevel));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("dateregister", dateregister));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastip", lastip));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastactive", lastactive));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("usrcurrip", usrcurrip));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("lastserver", lastserver));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("truename", truename));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("email", email));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("birthday", birthday));
	}

	public void testVectorClans() {
		Login vo;
		Iterator<Login> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CassandraDAOLogin.createLoginSchema();
		// CassandraDAOLogin.prepopulateLoginData();
		CassandraDAOLogin dao = new CassandraDAOLogin();
		// dao.selectAll();
		dao.selectByPk(1);
		dao.testVectorClans();
		dao.selectByPk(2);
		dao.testVectorClans();

	}

}
