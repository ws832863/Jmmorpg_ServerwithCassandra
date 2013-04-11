package game;

import game.cassandra.dao.CassandraDAOClasse;
import game.cassandra.dao.CassandraDAOMap;
import game.cassandra.dao.CassandraDAOPlayer;
import game.cassandra.dao.CassandraDAORace;

import game.database.classe.vo.Classe;
import game.database.login.vo.Login;
import game.database.map.vo.Map;

import game.database.player.vo.Player;
import game.database.race.vo.Race;
import game.network.ManagerChannelPlayer;
import game.network.ManagerSessionPlayer;
import game.systems.Room;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
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

/**
 * @author Michel Montenegro
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

	private Login login;

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

		if (session == null) {
			System.out.println("null sesseion");
			// throw new NullPointerException("null session");
			return null;
		}
		String name = session.getName();

		logger.log(Level.INFO, "JMMORPG Client login: {0}", name);

		CassandraDAOPlayer dao = new CassandraDAOPlayer();
		CassandraDAOClasse daoClasse = new CassandraDAOClasse();
		CassandraDAORace daoRace = new CassandraDAORace();
		CassandraDAOMap daomap = new CassandraDAOMap();
		Player player = null;
		Map map = null;

		ManagerSessionPlayer SessionPlayer = null;
		try {
			// System.out.println("select " + name + " from database");
			//
			dao.selectByLoginName(name);

			// // if (dao.getVos() != null && !dao.getVos().isEmpty()) {
			// if (dao.getVos() == null || dao.getVos().isEmpty()) {
			// System.out.println("not find  " + name
			// + "from database,create it");
			//
			// // dao.createDefaultPlayer(session);
			// // dao.selectByLogin(name);
			// System.out.println("create " + name + " sucessful");

			// } else {
			if (dao.getVos() != null && !dao.getVos().isEmpty()) {
				player = dao.getVos().firstElement();
				System.out.println(">>>>" + player.getName());

				daoClasse.selectByPk(player.getClasseId());
				Classe classe = daoClasse.getVos().firstElement();
				System.out.println(">>>>" + classe.getNameClasse());

				daoRace.selectByPk(classe.getRaceId());
				Race race = daoRace.getVos().firstElement();
				System.out.println(">>>>" + race.getRace());

				daomap.selectByPk(player.getMapId());
				map = daomap.getVos().firstElement();

				// server send command, client use this command to loada player
				String msg = "loadPlayer" + "/" + player.getId() + "/"
						+ player.getName() + "/" + player.getLoginId() + "/"
						+ player.getMapId() + "/" + player.getClasseId() + "/"
						+ player.getHpMax() + "/" + player.getHpCurr() + "/"
						+ player.getManaMax() + "/" + player.getManaCurr()
						+ "/" + player.getExpMax() + "/" + player.getExpCurr()
						+ "/" + player.getSp() + "/" + player.getStr() + "/"
						+ player.getDex() + "/" + player.getInte() + "/"
						+ player.getCon() + "/" + player.getCha() + "/"
						+ player.getWis() + "/" + player.getStamina() + "/"
						+ player.getSex() + "/" + player.getResMagic() + "/"
						+ player.getResPhysical() + "/" + player.getEvasion()
						+ "/" + player.getDateCreate() + "/"
						+ player.getOnLine() + "/" + player.getLastAcess()
						+ "/"
						+ player.getSector()
						// Alguns dados da Classe do Personagem
						+ "/"
						+ classe.getNameClasse()
						// Alguns dados da Ra�a do Personagem
						+ "/"
						+ race.getRace()
						// Posi��o do mapa que deve iniciar
						+ "/" + map.getStartTileHeroPosX() + "/"
						+ map.getStartTileHeroPosY() + "/" + map.getPosition()
						+ "/";
				System.out.println(msg);
				session.send(encodeString(msg));
				// Envio um aviso a todos os players deste canal que este player
				// deslogou
				Channel channel = AppContext.getChannelManager().getChannel(
						"map_" + player.getMapId());
				channel.send(
						null,
						encodeString("m/" + player.getLoginId() + "/"
								+ player.getClasseId() + "/" + player.getName()
								+ "/" + map.getStartTileHeroPosX() + "/"
								+ map.getStartTileHeroPosY() + "/"
								+ map.getPosition() + "/"));

				// Cria um canal e adiciona no vetor
				SessionPlayer = ManagerSessionPlayer.loggedIn(session);

				// Put player in room
				SessionPlayer
						.enter(getRoom("Room" + player.getMapId()), player);

				// add this player to our channel
				AppContext.getChannelManager()
						.getChannel("map_" + player.getMapId()).join(session);

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
