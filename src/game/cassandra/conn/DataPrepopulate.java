package game.cassandra.conn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.log4j.Logger;

import game.cassandra.dao.Index_Helper;

;
public class DataPrepopulate {

	private final static Logger logger = Logger
			.getLogger(GameSchemaCreation.class.getName());
	private final static String CFLoginUser = "UserLogin";
	private final static String CFMap = "CFMap";

	private final static Cluster gameCluster = CassandraConnection.getCluster();
	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();
	private Keyspace keyspace = HFactory.createKeyspace(KeySpaceName,
			gameCluster);
	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private void addColumn(Mutator<UUID> mut, UUID key,
			String columnFamilyName, String columnName, String value) {

		mut.addInsertion(key, CFLoginUser,
				HFactory.createColumn(columnName, value));
	};

	private void createAdefaultUser(Mutator<UUID> mut, UUID key, String clanid,
			String username, String usrpwd, String accesslevel,
			String dateregister, String lastip, String lastactive,
			String usrcurrip, String lastserver, String truename, String email,
			String birthday) {
		addColumn(mut, key, CFLoginUser, "clainid", clanid);
		addColumn(mut, key, CFLoginUser, "username", username);
		addColumn(mut, key, CFLoginUser, "userpassword", usrpwd);
		addColumn(mut, key, CFLoginUser, "accesslevel", accesslevel);
		addColumn(mut, key, CFLoginUser, "dateregister", dateregister);
		addColumn(mut, key, CFLoginUser, "lastip", lastip);
		addColumn(mut, key, CFLoginUser, "lastactive", lastactive);
		addColumn(mut, key, CFLoginUser, "usrcurrip", usrcurrip);
		addColumn(mut, key, CFLoginUser, "lastserver", lastserver);
		addColumn(mut, key, CFLoginUser, "truename", truename);
		addColumn(mut, key, CFLoginUser, "email", email);
		addColumn(mut, key, CFLoginUser, "birthday", birthday);

		/*
		 * 2013 4 9 add, Index Helper deprecated, because i applied a secondary
		 * index
		 * 
		 * creating a user in the meanwhile create a roll for the user in CF
		 * Loginuser
		 * 
		 * columname username_classid username_xxxx
		 */
		Index_Helper.insertUserNameForUUID(username, key, usrpwd);
		createDefaultPlayerRoller(key, username, username);
		// addColumn(mut, key1, CFLoginUser, "clanid", "2");
		// addColumn(mut, key1, CFLoginUser, "username", "2");
		// addColumn(mut, key1, CFLoginUser, "usrpwd", "2");
		// addColumn(mut, key1, CFLoginUser, "accesslevel", "2");
		// addColumn(mut, key1, CFLoginUser, "dateregister", "2");
		// addColumn(mut, key1, CFLoginUser, "lastip", "2");
		// addColumn(mut, key1, CFLoginUser, "lastactive", "2");
		// addColumn(mut, key1, CFLoginUser, "usrcurrip", "2");
		// addColumn(mut, key1, CFLoginUser, "lastserver", "2");
		// addColumn(mut, key1, CFLoginUser, "truename", "2");
		// addColumn(mut, key1, CFLoginUser, "email", "2");
		// addColumn(mut, key1, CFLoginUser, "birthday", "2");

	}

	public void preInsertLoginUserData() {
		ColumnFamilyTemplate<UUID, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, CFLoginUser, UUIDSerializer.get(), stringSerializer);

		Mutator<UUID> mut = columnFamilyTemplate.createMutator();
		// String keytest = getUUID();
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("clainid", "2"));
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("username", "player1"));
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("usrpwd", "player1"));
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("accesslevel", "value"));
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("dateregister", "value"));
		// mut.addInsertion(keytest, CFLoginUser,
		// HFactory.createColumn("test", "test"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String dateString = sdf.format(new Date()).toString();

		createAdefaultUser(mut, getUUID(), "2", "player1", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player1", "player1@st.ovgu.de", "1985-10-11");
		createAdefaultUser(mut, getUUID(), "2", "player2", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player2", "player2@st.ovgu.de", "1986-11-10");
		createAdefaultUser(mut, getUUID(), "2", "player3", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player3", "player3@st.ovgu.de", "1987-10-11");
		createAdefaultUser(mut, getUUID(), "2", "player4", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player4", "player4@st.ovgu.de", "1988-2-28");
		createAdefaultUser(mut, getUUID(), "2", "player5", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player5", "player5@st.ovgu.de", "1956-4-30");
		createAdefaultUser(mut, getUUID(), "2", "player6", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player6", "player6@st.ovgu.de", "1985-6-15");
		createAdefaultUser(mut, getUUID(), "2", "player7", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player7", "player7@st.ovgu.de", "1986-8-23");
		createAdefaultUser(mut, getUUID(), "2", "player8", "player", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"player8", "player8@st.ovgu.de", "1956-3-3");
		createAdefaultUser(mut, getUUID(), "1", "admin", "admin", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"administrator", "admin@st.ovgu.de", "1956-12-1");

		createAdefaultUser(mut, getUUID(), "2", "abcd", "abcd", "0",
				dateString, "127.0.0.1", dateString, "127.0.0.1", "server1",
				"wangshuo", "admin@st.ovgu.de", "1985-12-1");
		MutationResult mr = mut.execute();

		System.out.println(mr.toString());
	}

	private static void testEditCF() {
		// this example show a funtion to modify a exists column in a keyspace
		KeyspaceDefinition kd = gameCluster
				.describeKeyspace("wangshuowangshuo");
		ColumnFamilyDefinition cfDef = kd.getCfDefs().get(0);

		BasicColumnFamilyDefinition basicCfDef = new BasicColumnFamilyDefinition(
				cfDef);

		BasicColumnDefinition columnDefinition = new BasicColumnDefinition();

		columnDefinition.setName(stringSerializer.toByteBuffer("birthday"));
		columnDefinition.setIndexName("birthday_idx");
		columnDefinition.setIndexType(ColumnIndexType.KEYS);
		columnDefinition.setValidationClass(ComparatorType.LONGTYPE
				.getClassName());

		basicCfDef.addColumnDefinition(columnDefinition);

		gameCluster.updateColumnFamily(new ThriftCfDef(basicCfDef));

	}

	public static void testAddColumnFamily() {
		KeyspaceDefinition kd = gameCluster
				.describeKeyspace("wangshuowangshuo");

		// BasicColumnDefinition cd1 = new BasicColumnDefinition();
		// cd1.setName(StringSerializer.get().toByteBuffer("basicColumnname1"));
		// cd1.setIndexName("testIndex");
		// cd1.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
		// BasicColumnDefinition cd2 = new BasicColumnDefinition();
		// cd2.setName(StringSerializer.get().toByteBuffer("basicColumnname2"));
		// BasicColumnDefinition cd3 = new BasicColumnDefinition();
		// cd3.setName(StringSerializer.get().toByteBuffer("basicColumnname3"));

		BasicColumnFamilyDefinition bcf = new BasicColumnFamilyDefinition();

		// bcf.addColumnDefinition(cd1);
		// bcf.addColumnDefinition(cd2);
		// bcf.addColumnDefinition(cd3);

		bcf.setKeyspaceName("wangshuowangshuo");
		bcf.setName("testColumnFamily");
		bcf.setComparatorType(ComparatorType.UTF8TYPE);

		gameCluster.addColumnFamily(bcf);

	}

	public void createDefaultPlayerRoller(UUID uuid, String username,
			String Heroname) {

		ColumnFamilyTemplate<UUID, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, CFLoginUser, UUIDSerializer.get(), stringSerializer);
		String RowKeyPrifix = username + "_" + Heroname + "_";
		String randomClassid = String.valueOf((new Random(32).nextInt(8) + 1));
		Mutator<UUID> mut = columnFamilyTemplate.createMutator();
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "mapid", "1", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "heroname", username, stringSerializer, stringSerializer));
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
				+ "datecreate", CassandraConnection.CurrentDateString, stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "online", "f", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "lastaccess", CassandraConnection.CurrentDateString, stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "logoutpoxmap", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "logoutpoymap", "", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "sector", "1", stringSerializer, stringSerializer));

		mut.execute();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Initializing all data");
		DataPrepopulate dp = new DataPrepopulate();
		dp.preInsertLoginUserData();
		// DataPrepopulate.testEditCF();
		// DataPrepopulate.testAddColumnFamily();

	}

	private UUID getUUID() {
		return UUID.randomUUID();
	}

}
