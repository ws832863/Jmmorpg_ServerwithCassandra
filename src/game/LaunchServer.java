package game;

import game.cassandra.dao.CassandraDAOGamePlayer;
import game.cassandra.dao.CassandraDAOMap;
import game.cassandra.dao.UserLoginHelper;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.Map;
import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Room;
import game.darkstar.network.GameChannelsListener;
import game.darkstar.network.GamePlayerClientSessionListener;
import game.drakstar.task.ProduceItemInTheRoom;
import game.drakstar.task.TestATask;
import game.systems.GlobalPlayersControl;

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

	// private Set<ManagedReference<GamePlayer>> playerList = null;
	// private Set<ManagedReference<PlayerInventory>> inventoryList = null;

	// private ManagedReference<GlobalPlayersControl> globalRef = null;

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


		logger.info("Initialing all gamestate in the gameword ,only once-------LaunchServer");

		// read the map information in the database, and create all room for
		// users
		CassandraDAOMap dao = new CassandraDAOMap();
		try {
			dao.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("read daomap(environment) sucessfully!!!!");
		Iterator<Map> it = dao.getVos().iterator();
		ChannelManager channelMgr = AppContext.getChannelManager();
		while (it.hasNext()) {
			System.out.println("it has next" + it.toString());
			Map map = it.next();

			// create a channel for the current map
			// we can create different channel, read the name of map from
			// database
			Channel c1 = channelMgr.createChannel(("map_" + map.getId()),
					new GameChannelsListener(), Delivery.RELIABLE);
			// add a managedreference to channel1
			ManagedReference<Channel> channel1 = AppContext.getDataManager()
					.createReference(c1);
			// create a channel for everyroom and add it to the channelsSet
			channels.add(channel1);

			Room room = new Room("Room" + map.getId(), "Room" + map.getId());
			DataManager dataManager = AppContext.getDataManager();
			ManagedReference<Room> r = dataManager.createReference(room);

			rooms.add(r);

			System.out.println("map_" + map.getId());
			System.out.println("Room" + map.getId());

			logger.info("runnning periodic task");
			
			
			// schedule the task
			TaskManager tm = AppContext.getTaskManager();
			tm.schedulePeriodicTask(new ProduceItemInTheRoom(r), 0, 10000);
		}

		logger.info("-- JMMORPG Initialized ---");

	}

	@Override
	public ClientSessionListener loggedIn(ClientSession session) {

		String name = session.getName();
		logger.log(Level.INFO, "JMMORPG Client trying to login: {0}", name);
		UserLoginHelper check = new UserLoginHelper();
		GamePlayer gamePlayer = check.checkUser(name, "player");
		Map map = null;

		// the users exists and get the gameplayer information from database
		if (gamePlayer != null) {
			session.send(encodeString(check.verfiedString()));
			map = check.getMap();
		} else {

			return null;
		}
		

		// connect this session with map_x channel, all players in this
		// channel
		// will see the status changes of other users also in the same
		// channel
		Channel channel = AppContext.getChannelManager().getChannel(
				"map_" + gamePlayer.getMapId());

		channel.send(
				null,
				encodeString("m/" + gamePlayer.getLoginId() + "/"
						+ gamePlayer.getClassId() + "/"
						+ gamePlayer.getUserName() + "/"
						+ map.getStartTileHeroPosX() + "/"
						+ map.getStartTileHeroPosY() + "/" + map.getPosition()
						+ "/"));
		GamePlayerClientSessionListener SessionPlayer = null;

		SessionPlayer = GamePlayerClientSessionListener.loggedIn(session);

		// in the mean time the players inventory will be set
		SessionPlayer.setPlayerRef(AppContext.getDataManager().createReference(
				gamePlayer));

		// Put player in room.
		SessionPlayer
				.enter(getRoom("Room" + gamePlayer.getMapId()), gamePlayer);

		// add this player to one channel, it depends which room is
		// player in
	
		
		
		AppContext.getChannelManager()
				.getChannel("map_" + gamePlayer.getMapId()).join(session);

		/*
		 * when user logined in ,send the lasted postions of this user via
		 * channel.
		 */
		channel.send(
				null,
				encodeString("m/" + gamePlayer.getLoginId() + "/"
						+ gamePlayer.getClassId() + "/"
						+ gamePlayer.getUserName() + "/"
						+ map.getStartTileHeroPosX() + "/"
						+ map.getStartTileHeroPosY() + "/" + map.getPosition()
						+ "/"));
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
