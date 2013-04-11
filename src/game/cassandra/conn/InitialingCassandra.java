package game.cassandra.conn;

import game.cassandra.dao.CassandraDAOClasse;
import game.cassandra.dao.CassandraDAOLogin;
import game.cassandra.dao.CassandraDAOMap;
import game.cassandra.dao.CassandraDAOPlayer;
import game.cassandra.dao.CassandraDAORace;

import java.util.UUID;

public class InitialingCassandra {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * here we can create the schema and prepopulate the database
		 */
		CassandraDAOLogin login = new CassandraDAOLogin();
		CassandraDAOPlayer player = new CassandraDAOPlayer();
		CassandraDAOMap map = new CassandraDAOMap();
		CassandraDAORace race = new CassandraDAORace();
		CassandraDAOClasse classe = new CassandraDAOClasse();
		
		CassandraDAOLogin.createLoginSchema();
		CassandraDAOLogin.prepopulateLoginData();
		CassandraDAOPlayer.createPlayerSchema();
		CassandraDAOPlayer.prepopulateLoginData();
		CassandraDAOMap.createMapSchema();
		CassandraDAOMap.prepopulateMapData();
		CassandraDAORace.createRaceSchema();
		CassandraDAORace.prepopulateMapData();
		CassandraDAOClasse.createClasseSchema();
		CassandraDAOClasse.prepopulateClasseData();
	}
}
