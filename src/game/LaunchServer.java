package game;

import game.cassandra.dao.CassandraDAOGamePlayer;
import game.cassandra.dao.CassandraDAOMap;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.Map;
import game.drakstar.task.TestATask;
import game.network.ManagerChannelPlayer;
import game.network.ManagerSessionPlayer;
import game.systems.Room;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.TaskManager;

/**
 * 
 * @author shuo wang
 * 
 */
public class LaunchServer implements AppListener, Serializable {

	private static final long serialVersionUID = -6610709588215806740L;

	private ManagedReference<Room> sectionRef = null;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(LaunchServer.class
			.getName());

	/** A reference to the one-and-only {@linkplain SwordWorldRoom room}. */
	private Set<ManagedReference<Room>> rooms = null;

	public static final String MESSAGE_CHARSET = "UTF-8";

	/**
	 * The first {@link Channel}. The second channel is looked up by name.
	 */
	private Set<ManagedReference<Channel>> channels = null;

	/**
	 * Gets the SwordWorld's One True Room.
	 * <p>
	 * 
	 * @return the room for this {@code SwordWorld}
	 */
	public Room getSection() {
		if (sectionRef == null) {
			return null;
		}

		return sectionRef.get();
	}

	/**
	 * Gets the SwordWorld's One True Room.
	 * <p>
	 * 
	 * @return the room for this {@code SwordWorld}
	 */
	public Room getRoom(String objectName) {
		Iterator<ManagedReference<Room>> it = rooms.iterator();
		while (it.hasNext()) {
			Room room = it.next().get();
			System.out.println(">>>>" + room.getObjectName());
			System.out.println("=>=>" + objectName);
			if (room.getObjectName().equals(objectName)) {
				System.out.println(">>>> Existe");
				return room;
			}
		}
		return null;
	}

	@Override
	public void initialize(Properties arg0) {
		// Create the Room

		this.channels = new HashSet<ManagedReference<Channel>>();
		this.rooms = new HashSet<ManagedReference<Room>>();

		logger.info("initialing rooms-------LaunchServer");
		CassandraDAOMap dao = new CassandraDAOMap();
		try {
			dao.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("read daomap sucessfully!!!!");
		Iterator<Map> it = dao.getVos().iterator();
		ChannelManager channelMgr = AppContext.getChannelManager();
		while (it.hasNext()) {
			System.out.println("it has next" + it.toString());
			Map map = it.next();
			Channel c1 = channelMgr.createChannel(("map_" + map.getId()),
					new ManagerChannelPlayer(), Delivery.RELIABLE);
			ManagedReference<Channel> channel1 = AppContext.getDataManager()
					.createReference(c1);
			this.channels.add(channel1);

			Room room = new Room("Room" + map.getId(), "Room" + map.getId());
			DataManager dataManager = AppContext.getDataManager();
			ManagedReference<Room> r = dataManager.createReference(room);
			rooms.add(r);
			System.out.println("map_" + map.getId());
			System.out.println("Room" + map.getId());
		}

		logger.info("-- JMMORPG Initialized ---");

	}

	@Override
	public ClientSessionListener loggedIn(ClientSession session) {
		System.out.println("Login get invoked!!!!!!!!!!!!!!!!!!!!");

		logger.info("schedule task run========login");
		TaskManager simpleTask = AppContext.getTaskManager();
		TestATask t = new TestATask(session.getName());
		// simpleTask.schedulePeriodicTask(t, 500, 500);
		simpleTask.scheduleTask(t);
		if (session == null) {
			System.out.println("null sesseion");
			// throw new NullPointerException("null session");
			return null;
		}
		String name = session.getName();

		logger.log(Level.INFO, "JMMORPG Client login: {0}", name);

		// CassandraDAOPlayer dao = new CassandraDAOPlayer();
		// CassandraDAOClasse daoClasse = new CassandraDAOClasse();
		// CassandraDAORace daoRace = new CassandraDAORace();
		CassandraDAOGamePlayer cdgp = new CassandraDAOGamePlayer();
		CassandraDAOMap daomap = new CassandraDAOMap();
		// Player player = null;
		GamePlayer gPlayer = new GamePlayer();

		Map map = null;

		ManagerSessionPlayer SessionPlayer = null;
		try {
			

			cdgp.selectByLoginName(name);

			if (cdgp.getVos() != null && !cdgp.getVos().isEmpty()) {
				gPlayer=cdgp.getVos().firstElement();
			
				System.out.println("username >>>>" + gPlayer.getUserName());
				System.out.println("classe >>>>" + gPlayer.getHeroClass());

				System.out.println("Race >>>>" + gPlayer.getHeroRace());

				daomap.selectByPk(gPlayer.getMapId());
				map = daomap.getVos().firstElement();

				// server send command, client use this command to load player
				 StringBuilder sbMsg=new StringBuilder();
				 sbMsg.append("loadPlayer").append("/");
				 sbMsg.append(gPlayer.getLoginId()).append("/");//player.getId() 
				 sbMsg.append(gPlayer.getUserName()).append("/");//player.getName() 
				 sbMsg.append(gPlayer.getLoginId()).append("/");//player.getLoginId()
				 sbMsg.append(gPlayer.getMapId()).append("/");//player.getMapId()
				 sbMsg.append(gPlayer.getClassId()).append("/");//player.getClasseId()
				 sbMsg.append(gPlayer.getMaxHp()).append("/");//player.getHpMax()
				 sbMsg.append(gPlayer.getCurrHp()).append("/");//player.getHpCurr()
				 sbMsg.append("10").append("/");//player.getManaMax()
				 sbMsg.append("10").append("/");//player.getManaCurr()
				 sbMsg.append(gPlayer.getMaxExp()).append("/");//player.getExpMax() 
				 sbMsg.append(gPlayer.getCurrExp()).append("/");//player.getExpCurr()
				 sbMsg.append(gPlayer.getAttack()).append("/");//player.getSp()
				 sbMsg.append(gPlayer.getStrength()).append("/");//player.getStr() 
				 sbMsg.append(gPlayer.getDefense()).append("/");//player.getDex()
				 sbMsg.append("10").append("/");//player.getCon()
				 sbMsg.append("10").append("/");//player.getInte() 
				 sbMsg.append("10").append("/");//player.getCha()
				 sbMsg.append("10").append("/");//player.getWis()
				 sbMsg.append("10").append("/");//player.getStamina()
				 sbMsg.append("10").append("/");//player.getSex()
				 sbMsg.append("10").append("/");//player.getResMagic()
				 sbMsg.append("10").append("/");//player.getResPhysical()
				 sbMsg.append("10").append("/");//player.getEvasion()
				 sbMsg.append(gPlayer.getRegistDate()).append("/");//player.getDateCreate()
				 sbMsg.append("F").append("/");//player.getOnLine()
				 sbMsg.append(gPlayer.getLastActiceDate()).append("/");//player.getLastAcess()
				 sbMsg.append("0").append("/");//player.getSector()
				 sbMsg.append(gPlayer.getHeroClass()).append("/");//classe.getNameClasse()
				 sbMsg.append(gPlayer.getHeroRace()).append("/");//race.getRace()
				 sbMsg.append(map.getStartTileHeroPosX()).append("/");// map.getStartTileHeroPosX()
				 sbMsg.append(map.getStartTileHeroPosY()).append("/");//map.getStartTileHeroPosY()
				 sbMsg.append(map.getPosition()).append("/");//map.getPosition()
				 
					/*	// Alguns dados da Classe do Personagem
						+ "/"
						+ 
						// Alguns dados da Ra�a do Personagem
						+ "/"
						+ 
						// Posi��o do mapa que deve iniciar
						+ "/" + map.getStartTileHeroPosX() + "/"
						+ map.getStartTileHeroPosY() + "/" + map.getPosition()
						*/
				System.out.println(sbMsg.toString());
				session.send(encodeString(sbMsg.toString()));
				// Envio um aviso a todos os players deste canal que este player
				// deslogou
				Channel channel = AppContext.getChannelManager().getChannel(
						"map_" + gPlayer.getMapId());
				channel.send(
						null,
						encodeString("m/" + gPlayer.getLoginId() + "/"
								+ gPlayer.getClassId() + "/" + gPlayer.getUserName()
								+ "/" + map.getStartTileHeroPosX() + "/"
								+ map.getStartTileHeroPosY() + "/"
								+ map.getPosition() + "/"));

				// Cria um canal e adiciona no vetor
				SessionPlayer = ManagerSessionPlayer.loggedIn(session);

				// Put player in room
				SessionPlayer
						.enter(getRoom("Room" + gPlayer.getMapId()), gPlayer);

				// add this player to our channel
				AppContext.getChannelManager()
						.getChannel("map_" + gPlayer.getMapId()).join(session);

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			// e.printStackTrace();
			return null;
		}

		// return player object as listener to this client session
		return SessionPlayer;
	}

	/**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s
	 *            the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	protected static ByteBuffer encodeString(String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

}
