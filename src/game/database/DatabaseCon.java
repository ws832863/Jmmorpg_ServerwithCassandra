//package game.database;
//
//import game.utils.Configures;
//
//import java.util.Properties;
//import java.util.logging.Logger;
//
///**
// * Essa classe � responsavel por carregar as propriedades do arquivo de
// * configura��o referente ao seu nome sem o "con"
// * 
// * @author antonio junior
// * @modified Ares
// * @modified Michel Montenegro
// */
//public class DatabaseCon {
//
//	/**
//	 * Pega o arquivo "database" para carregar suas configura��es
//	 */
//	private static Properties propertie = Configures.properties("database");
//
//	// DATABASE ACESSO
//	// Futuro
//	/** de bug */
//	public static boolean DEBUG = true;
//	/** Driver para acesso ao banco de dados */
//	public static String DATABASE_DRIVER;
//	/** Path to access to database */
//	public static String DATABASE_URL;
//	/** Database login */
//	public static String DATABASE_LOGIN;
//	/** Database password */
//	public static String DATABASE_PASSWORD;
//	/** Maximum number of Statements to the players */
//	public static int DATABASE_MAXSTATEMENTS;
//	/** Maximum number of connections to the database */
//	public static int DATABASE_MAX_CONNECTIONS;
//	/** Maximum PoolSize */
//	public static int DATABASE_MIN_POOLSIZE;
//	/** Minimum PoolSize */
//	public static int DATABASE_MAX_POOLSIZE;
//	/** If Pool Is Exhausted, Get 1 More Connections At A Time */
//	public static int DATABASE_ACQUIREINCREMENT;
//	/** Test Idle Connection Time */
//	public static int DATABASE_IDLECONNECTIONTEST;
//	/** Max Idle Time */
//	public static int DATABASE_MAXIDLETIME;
//	/** Maximum number of players allowed to play simultaneously on server */
//	public static int MAXIMUM_ONLINE_USERS;
//	
//	public static int DATABASE_InitialPoolSize;
//    private static final Logger logger =
//        Logger.getLogger(DatabaseCon.class.getName());		
//
//	/** Character name template */
//
//	/*
//	 * Retorna um valor logico se o de bug esta ativado ou nao
//	 */
//	public static boolean isdeBugAtived() {
//		return DEBUG;
//	}
//
//	/*
//	 * Get data base driver
//	 */
//	public static String GetDataBaseDriver() {
//		return DATABASE_DRIVER;
//	}
//
//	/*
//	 * Get data base URL
//	 */
//	public static String GetDataBaseURL() {
//		return DATABASE_URL;
//	}
//
//	/*
//	 * Get database Login
//	 */
//	public static String GetDataBaseLogin() {
//		return DATABASE_LOGIN;
//	}
//
//	/*
//	 * Get database Pass
//	 */
//	public static String GetDatabasePass() {
//		return DATABASE_PASSWORD;
//	}
//
//	/*
//	 * Get max de DB connections
//	 */
//	public static int GetMaxDbConnections() {
//		return DATABASE_MAX_CONNECTIONS;
//	}
//
//	/*
//	 * Get max statements
//	 */
//	public static int GetMaxStatements() {
//		return DATABASE_MAXSTATEMENTS;
//	}
//
//	/*
//	 * Get min Pool Size
//	 */
//	public static int GetMinPoolSize() {
//		return DATABASE_MIN_POOLSIZE;
//	}
//
//	/*
//	 * Get max Pool Size
//	 */
//	public static int GetMaxPoolSize() {
//		return DATABASE_MAX_POOLSIZE;
//	}
//
//	/*
//	 * Get Acquire Increment
//	 */
//	public static int GetAcquireIncrement() {
//		return DATABASE_ACQUIREINCREMENT;
//	}
//
//	/*
//	 * Set Idle Connection Test
//	 */
//	public static int GetIdleConnectioTest() {
//		return DATABASE_IDLECONNECTIONTEST;
//	}
//
//	/*
//	 * Set Max Id Le Time
//	 */
//	public static int GetMaxIdLeTime() {
//		return DATABASE_MAXIDLETIME;
//	}
//
//	
//	public static int GetInitialPollSize() {
//		return DATABASE_InitialPoolSize;
//	}
//	/**
//	 * --------------------------------------------------------- Atribuindo
//	 * valores as variaveis de configuracao do Driver
//	 * 
//	 * @author Ares ---------------------------------------------------------
//	 */
//	/*
//	 * Set data base driver
//	 */
//	private static void SetDataBaseDriver() {
//		DATABASE_DRIVER = propertie.getProperty("Driver",
//				"com.mysql.jdbc.Driver");
//	}
//
//	/*
//	 * Set data base URL
//	 */
//	private static void SetDataBaseURL() {
//		DATABASE_URL = propertie.getProperty("URL",
//				"jdbc:mysql://instance39399.db.xeround.com:7189/dbjmmorpg");
//	}
//
//	/*
//	 * Set database Login
//	 */
//	private static void SetDataBaseLogin() {
//		DATABASE_LOGIN = propertie.getProperty("Login", "shuwang");
//	}
//
//	/*
//	 * Set database Pass
//	 */
//	private static void SetDatabasePass() {
//		DATABASE_PASSWORD = propertie.getProperty("Password", "ws832863");
//	}
//
//	/*
//	 * Set max de DB connections
//	 */
//	private static void SetMaxDbConnections() {
//		DATABASE_MAX_CONNECTIONS = Integer.parseInt(propertie.getProperty(
//				"MaximumDbConnections", "3"));// free database max 3 connections shuowang
//	}
//
//	/*
//	 * Set max statements
//	 */
//	private static void SetMaxStatements() {
//		DATABASE_MAXSTATEMENTS = Integer.parseInt(propertie.getProperty(
//				"MaximumStateMents", "2"));
//	}
//
//	/*
//	 * Set min Pool Size
//	 */
//	private static void SetMinPoolSize() {
//		DATABASE_MIN_POOLSIZE = Integer.parseInt(propertie.getProperty(
//				"MinPoolSize", "2"));
//	}
//
//	/*
//	 * Set max Pool Size
//	 */
//	private static void SetMaxPoolSize() {
//		DATABASE_MAX_POOLSIZE = Integer.parseInt(propertie.getProperty(
//				"MaxPoolSize", "2"));
//	}
//
//	/*
//	 * Set Acquire Increment
//	 */
//	private static void SetAcquireIncrement() {
//		DATABASE_ACQUIREINCREMENT = Integer.parseInt(propertie.getProperty(
//				"AquireIncrement", "1"));
//	}
//
//	/*
//	 * Set Idle Connection Test
//	 */
//	private static void SetIdleConnectioTest() {
//		DATABASE_IDLECONNECTIONTEST = Integer.parseInt(propertie.getProperty(
//				"IdleConnectionTest", "10800"));
//	}
//
//	/*
//	 * Set Max Id Le Time
//	 */
//	private static void SetMaxIdLeTime() {
//		DATABASE_MAXIDLETIME = Integer.parseInt(propertie.getProperty(
//				"MaxIdleTime", "0"));
//	}
//	/*
//	 * Set initinal pool size
//	 */
//	private static void SetInitialPoolSize() {
//		DATABASE_InitialPoolSize = Integer.parseInt(propertie.getProperty(
//				"InitialPoolSize", "3"));
//	}
//
//	/**
//	 * Classe construtura que carrega as subfun��esp ara pegar valores
//	 */
//	public DatabaseCon() {
//		// Load properties
//		SetDataBaseDriver();
//		SetDataBaseURL();
//		SetDataBaseLogin();
//		SetDatabasePass();
//		SetMaxDbConnections();
//		SetMaxStatements();
//		SetMinPoolSize();
//		SetMaxPoolSize();
//		SetAcquireIncrement();
//		SetIdleConnectioTest();
//		SetMaxIdLeTime();
//		
//		SetInitialPoolSize(); 
//		logger.info("database.properties loaded.");
//	}
//}
