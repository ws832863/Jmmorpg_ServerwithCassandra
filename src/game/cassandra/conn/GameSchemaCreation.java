package game.cassandra.conn;

import java.util.Arrays;
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
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.log4j.Logger;

/**
 * A Class , create all the schema which the game needed
 * 
 * @author shuo wang
 * 
 */
public class GameSchemaCreation {

	private final static Logger logger = Logger
			.getLogger(GameSchemaCreation.class.getName());
	private final static String CFLoginUser = "UserLogin";
	private final static String CFMap = "CFMap";

	private Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private Keyspace keyspace = HFactory.createKeyspace(KeySpaceName,
			gameCluster);
	private StringSerializer stringSerializer = StringSerializer.get();

	public GameSchemaCreation() {

		logger.debug("creating all gameSchema");
		createAllNeededSchema();
		logger.debug("Keyspaces was sucessfuly created");

		// logger.debug("inserting data to the schema");
		// new DataPrepopulate().prePrepopulate();
		// LOG.debug("insert data completed");

		// cluster.getConnectionManager().shutdown();
	}

	private void createAllNeededSchema() {
		logger.info("Creating ColumnFamily CFLoginUser");
		CreateCFLoginUser();// the userlogin columnfamily
		createUserNameMappingUUID();// the columnfamily which stores the
									// username(rowkey) and uuid password
	}

	/**
	 * create the main CF USER AND he's Roller
	 */
	public void CreateCFLoginUser() {

		// String KeySpaceName = KeySpaceName;
		String ColumnFamilyName = "UserLogin";

		/** Login id ,the unique UUID **/
		BasicColumnDefinition colLoginIdDef = new BasicColumnDefinition();
		colLoginIdDef.setName(stringSerializer.toByteBuffer("loginid"));
		colLoginIdDef.setIndexName("LoginId_idx");
		colLoginIdDef.setIndexType(ColumnIndexType.KEYS);
		colLoginIdDef.setValidationClass(ComparatorType.ASCIITYPE
				.getClassName());

		BasicColumnDefinition clanIdDef = new BasicColumnDefinition();
		clanIdDef.setName(stringSerializer.toByteBuffer("clainid"));
		clanIdDef.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
		// the user name of a account username column has a index, so we can use
		// Rangeslicequery to retrieve it by setting addequalsexpression
		BasicColumnDefinition colUserNameDef = new BasicColumnDefinition();
		colUserNameDef.setName(stringSerializer.toByteBuffer("username"));
		colLoginIdDef.setIndexName("username_idx");
		colLoginIdDef.setIndexType(ColumnIndexType.KEYS);
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
		basicCFUserLoginDef.addColumnDefinition(colLoginIdDef);
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
				gameCluster.dropKeyspace(KeySpaceName, true);
				System.out.println("keyspace already exists, drop it");
			}
			gameCluster.addKeyspace(keyspaceDefinition);// send the definition
														// to
			// cluster
			// HFactory.createKeyspace(ks, cluster); where is different?
			System.out.println("creating keyspeace :" + KeySpaceName);

		} catch (HectorException he) {
			he.printStackTrace();
		}
	}

	/**
	 * this function not be used a secondary-index instead
	 */
	public void createUserNameMappingUUID() {
		String ColumnFamilyName = "UserNameForUUID";

		BasicColumnDefinition colUsername = new BasicColumnDefinition();
		colUsername.setName(stringSerializer.toByteBuffer("username"));
		colUsername.setIndexName("username_Index");
		colUsername.setIndexType(ColumnIndexType.KEYS);
		colUsername.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colUUID = new BasicColumnDefinition();
		colUUID.setName(stringSerializer.toByteBuffer("uuid"));
		colUUID.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colUserPassword = new BasicColumnDefinition();
		colUserPassword.setName(stringSerializer.toByteBuffer("userpassword"));
		colUserPassword.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnFamilyDefinition basicUserNameForUUID = new BasicColumnFamilyDefinition();
		basicUserNameForUUID.setName(ColumnFamilyName);
		basicUserNameForUUID.setKeyspaceName(KeySpaceName);
		basicUserNameForUUID.addColumnDefinition(colUsername);
		basicUserNameForUUID.addColumnDefinition(colUUID);
		basicUserNameForUUID.addColumnDefinition(colUserPassword);

		ColumnFamilyDefinition cfUserNameForUUID = new ThriftCfDef(
				basicUserNameForUUID);
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(cfUserNameForUUID));

		gameCluster.addColumnFamily(cfUserNameForUUID);

		// try {
		// if (gameCluster.describeKeyspace(KeySpaceName) != null) {
		// gameCluster.dropKeyspace(KeySpaceName, true);
		// System.out.println("keyspace already exists, drop it");
		// }
		// gameCluster.addKeyspace(keyspaceDefinition);// send the definition
		// // to
		//
		// System.out.println("creating keyspeace :" + KeySpaceName);
		//
		// } catch (HectorException he) {
		// he.printStackTrace();
		// }

	}

	public void createPlayer() {
		String ColumnFamilyName = "PlayerRoller";

		BasicColumnDefinition colUsername = new BasicColumnDefinition();
		colUsername.setName(stringSerializer.toByteBuffer("username"));
		colUsername.setIndexName("username_Index");
		colUsername.setIndexType(ColumnIndexType.KEYS);
		colUsername.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colUUID = new BasicColumnDefinition();
		colUUID.setName(stringSerializer.toByteBuffer("uuid"));
		colUUID.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colUserPassword = new BasicColumnDefinition();
		colUserPassword.setName(stringSerializer.toByteBuffer("userpassword"));
		colUserPassword.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnFamilyDefinition basicUserNameForUUID = new BasicColumnFamilyDefinition();
		basicUserNameForUUID.setName(ColumnFamilyName);
		basicUserNameForUUID.setKeyspaceName(KeySpaceName);
		basicUserNameForUUID.addColumnDefinition(colUsername);
		basicUserNameForUUID.addColumnDefinition(colUUID);
		basicUserNameForUUID.addColumnDefinition(colUserPassword);

		ColumnFamilyDefinition cfUserNameForUUID = new ThriftCfDef(
				basicUserNameForUUID);
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(cfUserNameForUUID));

		gameCluster.addColumnFamily(cfUserNameForUUID);
	}

	/*
	 * create a Hero for a player
	 */
	public void createNewPlayerRoller() {

	}

	/*
	 * for gametest, create a Random Hero for a automatic generated user
	 */
	public void createDefaultPlayerRoller(UUID uuid, String username) {

		ColumnFamilyTemplate<UUID, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, CFLoginUser, UUIDSerializer.get(), stringSerializer);
		String RowKeyPrifix = username + "_";
		Mutator<UUID> mut = columnFamilyTemplate.createMutator();
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "mapid", "1", stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "heroname", username, stringSerializer, stringSerializer));
		mut.addInsertion(uuid, CFLoginUser, HFactory.createColumn(RowKeyPrifix
				+ "classid", "1", stringSerializer, stringSerializer));
		mut.execute();
	}
}
