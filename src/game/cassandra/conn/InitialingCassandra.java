package game.cassandra.conn;

import game.cassandra.dao.DAOLogin;
import game.cassandra.dao.DAOPlayer;

import java.util.UUID;

public class InitialingCassandra {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * here we can create the schema and prepopulate the database
		 */
		
		
		GameSchemaCreation gsc = new GameSchemaCreation();
		 DataPrepopulate dp = new DataPrepopulate();
		dp.preInsertLoginUserData();

		DAOLogin dao = new DAOLogin();
		DAOPlayer player = new DAOPlayer();
		UUID uuid=player.getUUIDbySpecifyHeroName("player1");
		
		// // System.out.println( player.getUUIDbySpecifyHeroName("player1"));
		// Player pp ;//= player.getUserHero("player1");
		// pp=player.getHeroByUserNameAndHeroName(uuid, "player1", "player1");
		// System.out.println("" + pp.getClasseId() + pp.getLoginId()
		// + pp.getCon());
		// // dao.selectAll();

		// dao.selectByLoginPassword("player2", "player");
		// dao.SearchingUserUseSecondaryIndexByNameAndPassword("player1",
		// "player");
		dao.SearchingUserUseSecondaryIndexByNameAndPassword("abcd", "abcd");
		// dao.SearchingUserUseSecondaryIndexByNameAndPassword("player2",
		// "player");

		dao.testVectorLogin();

	}
}
