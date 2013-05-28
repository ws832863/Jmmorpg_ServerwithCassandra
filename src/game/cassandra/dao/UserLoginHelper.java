package game.cassandra.dao;

import game.cassandra.Factorys.GamePlayerFactory;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;

public class UserLoginHelper {

	// ManagedReference<ClientSession> sessionRef = null;
	// a
	private String verifiedString;
	private static final Logger logger = Logger.getLogger(UserLoginHelper.class
			.getName());
	private CassandraDAOGamePlayer cdgp = null;
	private CassandraDAOMap daomap = null;
	private Map map = null;

	public UserLoginHelper() {// ClientSession session) {
		// sessionRef = AppContext.getDataManager().createReference(session);
		cdgp = new CassandraDAOGamePlayer();
		daomap = new CassandraDAOMap();
	}

	public GamePlayer checkUser(String username, String password) {

		GamePlayer gPlayer = null;

		try {
			cdgp.selectByLoginName(username);

			if (cdgp.getVos() != null && !cdgp.getVos().isEmpty()) {
				gPlayer = cdgp.getVos().firstElement();

				logger.log(Level.INFO,
						"Get the userinformation from database:\n");
				System.out.println("username >>>>" + gPlayer.getUserName());
				System.out.println("classe >>>>" + gPlayer.getHeroClass());

				System.out.println("Race >>>>" + gPlayer.getHeroRace());

				// which map are the users in
				daomap.selectByPk(gPlayer.getMapId());
				map = daomap.getVos().firstElement();

				// here we can set users inventory

				// server send command, client use this command to load player
				StringBuilder sbMsg = new StringBuilder();
				sbMsg.append("loadPlayer").append("/");
				sbMsg.append(gPlayer.getUUIDString()).append("/");// player.getId() nouse
				sbMsg.append(gPlayer.getUserName()).append("/");// player.getName()
				sbMsg.append(gPlayer.getUUIDString()).append("/");// player.getLoginId()
				sbMsg.append(gPlayer.getMapId()).append("/");// player.getMapId()
				sbMsg.append(gPlayer.getClassId()).append("/");// player.getClasseId()
				sbMsg.append(gPlayer.getMaxHp()).append("/");// player.getHpMax()
				sbMsg.append(gPlayer.getCurrHp()).append("/");// player.getHpCurr()
				//sbMsg.append("10").append("/");// player.getManaMax()
				//sbMsg.append("10").append("/");// player.getManaCurr()
				sbMsg.append(gPlayer.getMaxExp()).append("/");// player.getExpMax()
				sbMsg.append(gPlayer.getCurrExp()).append("/");// player.getExpCurr()
				sbMsg.append(gPlayer.getAttack()).append("/");// player.getSp()
				sbMsg.append(gPlayer.getStrength()).append("/");// player.getStr()
				sbMsg.append(gPlayer.getDefense()).append("/");// player.getDex()
				//sbMsg.append("10").append("/");// player.getCon()
				//sbMsg.append("10").append("/");// player.getInte()
				//sbMsg.append("10").append("/");// player.getCha()
				//sbMsg.append("10").append("/");// player.getWis()
				//sbMsg.append("10").append("/");// player.getStamina()
				//sbMsg.append("10").append("/");// player.getSex()
				//sbMsg.append("10").append("/");// player.getResMagic()
				//sbMsg.append("10").append("/");// player.getResPhysical()
				//sbMsg.append("10").append("/");// player.getEvasion()
				sbMsg.append(gPlayer.getRegistDate()).append("/");// player.getDateCreate()
				sbMsg.append("F").append("/");// player.getOnLine()
				sbMsg.append(gPlayer.getLastActiceDate()).append("/");// player.getLastAcess()
				//sbMsg.append("0").append("/");// player.getSector()
				sbMsg.append(gPlayer.getHeroClass()).append("/");// classe.getNameClasse()
				sbMsg.append(gPlayer.getHeroRace()).append("/");// race.getRace()
				sbMsg.append(map.getStartTileHeroPosX()).append("/");// map.getStartTileHeroPosX()
				sbMsg.append(map.getStartTileHeroPosY()).append("/");// map.getStartTileHeroPosY()
				sbMsg.append(map.getPosition()).append("/");// map.getPosition()

				System.out.println(sbMsg.toString());

				verifiedString = sbMsg.toString();

			} else {
				verifiedString = null;
				gPlayer = null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gPlayer;
	}

	public Map getMap() {
		return map;
	}

	public String verfiedString() {
		return verifiedString;
	}

	public void createNewUser(String username) {
		cdgp.addNewGamePlayer(GamePlayerFactory.createPlayer());

	}
}
