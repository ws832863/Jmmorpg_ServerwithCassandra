//package game.database;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.logging.Logger;
//
//import com.mchange.v2.c3p0.ComboPooledDataSource;
//
///**
// * Classe para conexao com o banco de dados
// * 
// * @author L2JServer
// * @modified Ares, antonio junior
// * 
// */
//public class DbConect {
//	public static enum ProviderType {
//		MySql, MsSql
//	}
//
//	private static DbConect _instance;
//	private ProviderType _providerType;
//	private ComboPooledDataSource _source;
//	private static final Logger logger = Logger.getLogger(DbConect.class
//			.getName());
//
//	/**
//	 * Fun��o construtra da classe
//	 */
//	public DbConect() throws SQLException {
//		logger.info("Initializing Connections... ");
//
//		try {
//			if (DatabaseCon.GetMaxDbConnections() < 2) {
//				DatabaseCon.DATABASE_MAX_CONNECTIONS = 2;
//				logger.info("at least " + DatabaseCon.GetMaxDbConnections()
//						+ " db connections are required.");
//			}
//			_source = new ComboPooledDataSource();
//			
//			_source.setAutoCommitOnClose(true);
//			_source.setInitialPoolSize(DatabaseCon.GetInitialPollSize());
//			_source.setMinPoolSize(DatabaseCon.GetMinPoolSize());
//			_source.setMaxPoolSize(DatabaseCon.GetMaxPoolSize());
//			_source.setAcquireRetryAttempts(0); // try to obtain connections
//			// indefinitely (0 = never quit)
//			_source.setAcquireRetryDelay(500); // 500 miliseconds wait before
//			// try to acquire connection
//			// again
//			_source.setCheckoutTimeout(0); // 0 = wait indefinitely for new
//			// connection
//			// if pool is exhausted
//			_source.setAcquireIncrement(DatabaseCon.GetAcquireIncrement()); // if
//			// pool
//			// is
//			// exhausted,
//			// get
//			// 1
//			// more
//			// connections
//			// at a
//			// time
//			// cause there is a "long" delay on acquire connection
//			// so taking more than one connection at once will make
//			// connection
//			// pooling
//			// more effective.
//			// this "connection_test_table" is automatically created if not
//			// already there
//			_source.setAutomaticTestTable("connection_test_table");
//			_source.setTestConnectionOnCheckin(false);
//			// testing OnCheckin used with IdleConnectionTestPeriod is
//			// faster
//			// than testing on checkout
//			_source.setIdleConnectionTestPeriod(DatabaseCon
//					.GetIdleConnectioTest()); // test
//			// idle
//			// connection
//			// every
//			// 3minutes
//			_source.setMaxIdleTime(DatabaseCon.GetMaxIdLeTime()); // 0 =
//			// idle
//			// connections
//			// never
//			// expire
//			// *THANKS* to connection testing configured above
//			// but I prefer to disconnect all connections not used
//			// for more than 1 hour
//			// enables statement caching, there is a "semi-bug" in c3p0
//			// 0.9.0
//			// but in 0.9.0.2 and later it's fixed
//			_source.setMaxStatementsPerConnection(DatabaseCon
//					.GetMaxStatements());
//			_source.setBreakAfterAcquireFailure(false); // never fail if any way
//			// possible
//			// setting this to true will make
//			// c3p0 "crash" and refuse to work
//			// till restart thus making acquire
//			// errors "FATAL" ... we don't want that
//			// it should be possible to recover
//			_source.setDriverClass(DatabaseCon.GetDataBaseDriver());
//			_source.setJdbcUrl(DatabaseCon.GetDataBaseURL());
//			_source.setUser(DatabaseCon.GetDataBaseLogin());
//			_source.setPassword(DatabaseCon.GetDatabasePass());
//			System.out.println("%%%%%%%%%%%%%%%%%%%%%%DB CONNECTIONS USERNAME:"
//					+ DatabaseCon.GetDataBaseLogin() + "PASSWORD:"
//					+ DatabaseCon.GetDatabasePass()
//					+ "-------------------------/nmax connections "
//					+ DatabaseCon.GetMaxDbConnections() +"  " +_source.getDriverClass());
//			/* Test the connection */
//			_source.getConnection().close();
//			logger.info("Connection with database sucessfully");
//			if (DatabaseCon.GetDataBaseDriver().toLowerCase()
//					.contains("microsoft"))
//				_providerType = ProviderType.MsSql;
//			else
//				_providerType = ProviderType.MySql;
//		} catch (SQLException x) {
//			if (DatabaseCon.isdeBugAtived())
//				logger.info("Database Connection FAILED");
//			// rethrow the exception
//			throw x;
//		} catch (Exception e) {
//			if (DatabaseCon.isdeBugAtived())
//				logger.info("Database Connection FAILED");
//			throw new SQLException("could not init DB connection:" + e);
//		}
//	}
//
//	public final String prepQuerySelect(String[] fields, String tableName,
//			String whereClause, boolean returnOnlyTopRecord) {
//		String msSqlTop1 = "";
//		String mySqlTop1 = "";
//		if (returnOnlyTopRecord) {
//			if (getProviderType() == ProviderType.MsSql)
//				msSqlTop1 = " Top 1 ";
//			if (getProviderType() == ProviderType.MySql)
//				mySqlTop1 = " Limit 1 ";
//		}
//		String query = "SELECT " + msSqlTop1 + safetyString(fields) + " FROM "
//				+ tableName + " WHERE " + whereClause + mySqlTop1;
//		return query;
//	}
//
//	public void shutdown() {
//		try {
//			_source.close();
//		} catch (Exception e) {
//			// until.info(Level.INFO, "", e);
//		}
//		try {
//			_source = null;
//		} catch (Exception e) {
//			// until.info(Level.INFO, "", e);
//		}
//	}
//
//	public final String safetyString(String[] whatToCheck) {
//		// NOTE: Use brace as a safty percaution just incase name is a reserved
//		// word
//		String braceLeft = "`";
//		String braceRight = "`";
//		if (getProviderType() == ProviderType.MsSql) {
//			braceLeft = "[";
//			braceRight = "]";
//		}
//		String result = "";
//		for (String word : whatToCheck) {
//			if (result != "")
//				result += ", ";
//			result += braceLeft + word + braceRight;
//		}
//		return result;
//	}
//
//	// =========================================================
//	// Property - Public
//	public static DbConect getInstance() throws SQLException {
//		if (_instance == null)
//			_instance = new DbConect();
//		return _instance;
//	}
//
//	public Connection getConnection() // throws SQLException
//	{
//		Connection con = null;
//		while (con == null) {
//			try {
//				con = _source.getConnection();
//			} catch (SQLException e) {
//				logger.info("DatabaseFactory: getConnection() failed, trying again "
//						+ e);
//			}
//		}
//		return con;
//	}
//
//	public int getBusyConnectionCount() throws SQLException {
//		return _source.getNumBusyConnectionsDefaultUser();
//	}
//
//	public int getIdleConnectionCount() throws SQLException {
//		return _source.getNumIdleConnectionsDefaultUser();
//	}
//
//	public final ProviderType getProviderType() {
//		return _providerType;
//	}
//}
