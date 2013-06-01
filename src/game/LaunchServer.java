package game;

import game.cassandra.Factorys.GamePlayerFactory;
import game.cassandra.conn.ManagedConfigurationHelper;
import game.cassandra.dao.CassandraDAOGamePlayer;
import game.cassandra.dao.CassandraDAOMap;
import game.cassandra.dao.InventoryDAO;
import game.cassandra.dao.UserLoginHelper;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.Map;
import game.cassandra.gamestates.Room;
import game.darkstar.network.GameChannelsListener;
import game.darkstar.network.GamePlayerClientSessionListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.sun.sgs.tutorial.server.swordworld.SwordWorldRoom;

/**
 * 
 * @author shuo wang
 * 
 */
public class LaunchServer implements AppListener, Serializable {

	private static final long serialVersionUID = -6610709588215806740L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(LaunchServer.class
			.getName());

	/**
	 * A collections for rooms , the data retrived from cassandra, every room
	 * hold a channel, all users enter the room can talk with this channel, and
	 * room also holes a Playerlist, contains all player currently in this room.
	 */
	private Set<ManagedReference<Room>> rooms = null;
	/**
	 * A reference to the all created channel every channel responsed for one
	 * gamespace(room), if user enters a room, join the user to the specific
	 * channel, all users in the room should join on same channel, all users
	 * communications in one room are limited to the channel in the room
	 * 
	 */
	private Set<ManagedReference<Channel>> channels = null;

	public static final String MESSAGE_CHARSET = "UTF-8";

	@Override
	public void initialize(Properties arg0) {
		DataManager dataManager = AppContext.getDataManager();
		ManagedConfigurationHelper dbInformation = new ManagedConfigurationHelper(
				"", "");
		dataManager.setBinding("CONFIG", dbInformation);
		cassandraDataInit();
		// unitTestFunction();

		this.channels = new HashSet<ManagedReference<Channel>>();
		this.rooms = new HashSet<ManagedReference<Room>>();

		// read the map information in the database, and create all room for
		// users
		CassandraDAOMap dao = new CassandraDAOMap();
		try {
			// load all maps in vector and invoke getVos() to get the
			// Vector<Map>
			dao.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out
				.println("read Map Information sucessfully! Starting create channels and gamespace for the map!");
		Iterator<Map> it = dao.getVos().iterator();
		ChannelManager channelMgr = AppContext.getChannelManager();

		while (it.hasNext()) {
			Map map = it.next();

			// create a channel for the current map
			// we can create different channel, read the name of map from
			// database
			logger.info("create channel for map " + map.getId()
					+ " bound the channel with name map_" + map.getId());
			Channel c1 = channelMgr.createChannel(("map_" + map.getId()),
					new GameChannelsListener(), Delivery.RELIABLE);
			// add a managedreference to channel1
			ManagedReference<Channel> channel1 = AppContext.getDataManager()
					.createReference(c1);
			// create a channel for everyroom and add it to the channelsSet
			channels.add(channel1);
			logger.info("create gamespace(room) for map " + map.getId()
					+ " bound the room with name room" + map.getId());

			Room room = new Room("Room" + map.getId(), "Room" + map.getId());
			ManagedReference<Room> r = dataManager.createReference(room);

			rooms.add(r);

		}

		logger.info("Starting init periodic tasks");

		// schedule the task
		// TaskManager tm = AppContext.getTaskManager();
		// tm.schedulePeriodicTask(new ProduceItemInTheRoom(r), 0,
		// 3000);
		// tm.schedulePeriodicTask(new TaskDestoryExpiredItem(r), 0,
		// 20000);

		logger.info("-- JMMORPG Server has Initialized ---");

	}

	@Override
	public ClientSessionListener loggedIn(ClientSession session) {

		/*
		 * first create a instance of Gameplayer by using the session name
		 * specified by user. if the specified name can be found in cassandra,
		 * then create a Gameplayer instance which represent a user, and the
		 * data of gameplayer will be read from database instead of darkstar
		 */
		String name = session.getName();
		logger.log(Level.INFO, "JMMORPG Client trying to login: {0}", name);
		UserLoginHelper check = new UserLoginHelper();
		// check if the user exists in cassandra,not check the password,only
		// searching the username
		GamePlayer gamePlayer = check.checkUser(name, "player");
		// a map instance, represent which map are the user currently in.
		Map map = null;

		// the users exists and get the gameplayer information from database
		if (gamePlayer != null) {
			session.send(encodeString(check.verfiedString()));
			map = check.getMap();
		} else {
			// if we can't find a user, return null, the the client will receive
			// a login failed information
			return null;
		}
		// end of create Gameplayer instance

		// we get the user from database, then send the user position
		// let the user join a channel, map_x x is the map id,which the user
		// last time in.
		AppContext.getChannelManager()
				.getChannel("map_" + gamePlayer.getMapId()).join(session);
		Channel channel = AppContext.getChannelManager().getChannel(
				"map_" + gamePlayer.getMapId());

		// a implementation of ClientSessionListener, it holds the Gameplayer
		// instance and playerinventory instance, a Clientsession reprentes a
		// complete user
		GamePlayerClientSessionListener SessionPlayer = null;
		// trying to find the user in darkstar memory,if not find,will create a
		// new instance but we will delete the user from darkstar memory every
		// time when user disconnect.
		// this login method are also used to set the client session. then we
		// can invoke
		// getSession() to get a current clientsession
		SessionPlayer = GamePlayerClientSessionListener.loggedIn(session,
				gamePlayer);

		// Put player in a specific room which the player last time in
		SessionPlayer.enter(getRoom("Room" + gamePlayer.getMapId()));

		/*
		 * when user logined in ,send the lasted postions of this user via
		 * channel to all users in this channel
		 */

		channel.send(
				null,
				encodeString("m/" + gamePlayer.getUUIDString() + "/"
						+ gamePlayer.getClassId() + "/"
						+ gamePlayer.getUserName() + "/"
						+ map.getStartTileHeroPosX() + "/"
						+ map.getStartTileHeroPosY() + "/" + map.getPosition()
						+ "/"));

		// return player object as listener to this client session
		return SessionPlayer;
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

	private void cassandraDataInit() {
		System.out
				.println("=================>Cassandra Data Schema init, this happens only once");
		CassandraDAOGamePlayer.createGamePlayerSchema();
		CassandraDAOGamePlayer cdp = new CassandraDAOGamePlayer();
		cdp.GamePlayerPrePopulate(100);
		CassandraDAOMap.createMapSchema();
		CassandraDAOMap cdm = new CassandraDAOMap();
		CassandraDAOMap.prepopulateMapData();
		InventoryDAO idao = new InventoryDAO();
		idao.createInventorySchema();
		System.out
				.println("Cassandra Data Schema init sucessfully=============================<");
	}

	private void unitTestFunction() {
		CassandraDAOGamePlayer.createGamePlayerSchema();
		CassandraDAOGamePlayer cdp = new CassandraDAOGamePlayer();
		cdp.GamePlayerPrePopulate(1000);
		// cdp.selectAll();
		cdp.selectByPk("5");
		cdp.selectByLoginName("player1");
		cdp.testVectorClans();
		List<GamePlayer> ll = new ArrayList<GamePlayer>();
		for (int i = 0; i < 1000; i++) {
			ll.add(GamePlayerFactory.createPlayer());

		}
		cdp.addNewGamePlayer(ll);
		// InventoryDAO iv = new InventoryDAO();
		// PlayerInventory pi = new PlayerInventory();
		// InventoryHelper ih = new InventoryHelper();
		// iv.createInventorySchema();
		// pi.setPlayer("System");
		// for (int i = 0; i < 5; i++) {
		// pi.add(EquipmentFactory.createRandomGameItem());
		//
		// }
		// pi.listAllItems();
		// pi.setMoney(20000);
		//
		//
		// ih.InventoryToDataBase(pi);
		// System.out.println("reterving data from cassandra ");
		// PlayerInventory pi1 = new PlayerInventory();
		// pi1 = ih.getUsersInventoryFromDb("System");
		//
		// pi1.listAllItems();
		// pi1.getMoney();
		// System.out.println("changing data from cassandra ");
		//
		// iv.updateMoney(pi1.getPlayerUUIDString(), 999);
		// iv.removeItem(pi1.getFirstItem());
		// iv.removeItem(pi1.getFirstItem());
		// iv.removeItem(pi1.getFirstItem());
		// pi1 = ih.getUsersInventoryFromDb("System");
		// pi1.listAllItems();

	}
}
