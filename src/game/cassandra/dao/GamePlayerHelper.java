package game.cassandra.dao;

import game.cassandra.Factorys.GamePlayerFactory;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.Map;
import game.cassandra.data.PlayerInventory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePlayerHelper {

	// ManagedReference<ClientSession> sessionRef = null;
	// a
	private String verifiedString;
	private static final Logger logger = Logger
			.getLogger(GamePlayerHelper.class.getName());
	private CassandraDAOGamePlayer cdgp = null;
	private CassandraDAOMap daomap = null;
	private Map map = null;
	private PlayerInventory pi;
	private InventoryHelper inventoryHelper = new InventoryHelper();

	public GamePlayerHelper() {// ClientSession session) {
		// sessionRef = AppContext.getDataManager().createReference(session);
		cdgp = new CassandraDAOGamePlayer();
		daomap = new CassandraDAOMap();
	}

	public GamePlayer createUser(String name) {
		GamePlayer g = new GamePlayer();
		if (name == null || name.trim().length() == 0) {

			g = GamePlayerFactory.createPlayer();
		} else {
			g = GamePlayerFactory.createPlayer(name, "player");
		}
		// add user to database
		cdgp.addNewGamePlayer(g);
		// add inventory
		pi = new PlayerInventory();
		pi.setPlayer(g.getUUIDString());
		pi.setMoney(999999);
		g.setInventory(pi);
		return g;
	}

	public GamePlayer checkUser(String username, String password) {

		GamePlayer gPlayer = null;

		try {
			// get a user by username from cassandra database

			cdgp.selectByLoginName(username);

			if (cdgp.getVos() != null && !cdgp.getVos().isEmpty()) {
				gPlayer = cdgp.getVos().firstElement();

				logger.log(Level.INFO,
						"Get the userinformation from database:\n");
				// System.out.println("username >>>>" + gPlayer.getUserName());
				// System.out.println("classe >>>>" + gPlayer.getHeroClass());
				//
				// System.out.println("Race >>>>" + gPlayer.getHeroRace());

				// get the users inventory
				pi = inventoryHelper.getUsersInventoryFromDb(gPlayer
						.getUUIDString());
				gPlayer.setInventory(pi);
				// which map are the users in

				verifiedString = formatVerifyString(gPlayer);

				// here we can set users inventory

			} else {
				verifiedString = null;
				gPlayer = null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// get the useer's inventory from db

		return gPlayer;
	}

	public String formatVerifyString(GamePlayer gPlayer) {
		// server send command, client use this command to load player
		StringBuilder sbMsg = new StringBuilder();

		daomap.selectByPk(gPlayer.getMapId());
		map = daomap.getVos().firstElement();
		sbMsg.append("loadPlayer").append("/");
		sbMsg.append(gPlayer.getUUIDString()).append("/");// player.getId()
															// nouse
		sbMsg.append(gPlayer.getUserName()).append("/");// player.getName()
		sbMsg.append(gPlayer.getUUIDString()).append("/");// player.getLoginId()
		sbMsg.append(gPlayer.getMapId()).append("/");// player.getMapId()
		sbMsg.append(gPlayer.getClassId()).append("/");// player.getClasseId()
		sbMsg.append(gPlayer.getMaxHp()).append("/");// player.getHpMax()
		sbMsg.append(gPlayer.getCurrHp()).append("/");// player.getHpCurr()
		sbMsg.append(gPlayer.getMaxExp()).append("/");// player.getExpMax()
		sbMsg.append(gPlayer.getCurrExp()).append("/");// player.getExpCurr()
		sbMsg.append(gPlayer.getAttack()).append("/");// player.getSp()
		sbMsg.append(gPlayer.getStrength()).append("/");// player.getStr()
		sbMsg.append(gPlayer.getDefense()).append("/");// player.getDex()

		sbMsg.append(gPlayer.getRegistDate()).append("/");// player.getDateCreate()
		sbMsg.append("F").append("/");// player.getOnLine()
		sbMsg.append(gPlayer.getLastActiceDate()).append("/");// player.getLastAcess()
		sbMsg.append(gPlayer.getHeroClass()).append("/");// classe.getNameClasse()
		sbMsg.append(gPlayer.getHeroRace()).append("/");// race.getRace()
		sbMsg.append(map.getStartTileHeroPosX()).append("/");// map.getStartTileHeroPosX()
		sbMsg.append(map.getStartTileHeroPosY()).append("/");// map.getStartTileHeroPosY()
		sbMsg.append(map.getPosition()).append("/");// map.getPosition()

		return sbMsg.toString();
	}

	public Map getMap() {
		return map;
	}

	public String verfiedString() {
		return verifiedString;
	}

	// store the last access, position information and ip adresse to cassandra

	public void logOutInformationPersistence(GamePlayer gp) {

	}
}
