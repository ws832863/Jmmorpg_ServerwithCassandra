package game.darkstar.network;

import game.cassandra.dao.GamePlayerHelper;
import game.cassandra.dao.InventoryHelper;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.GameManagedObjects;
import game.cassandra.gamestates.Room;
import game.systems.MessageHandler;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.NameNotBoundException;

/**
 * 
 * @author Shuo Wang
 * 
 */
public class GamePlayerClientSessionListener extends GameManagedObjects
		implements Serializable, ClientSessionListener {

	private static final long serialVersionUID = -6143746028573880418L;

	private static final Logger logger = Logger
			.getLogger(GamePlayerClientSessionListener.class.getName());

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/** The prefix for player bindings in the {@code DataManager}. */
	protected static final String PLAYER_BIND_PREFIX = "Player.";

	/** The {@code ClientSession} for this player, or null if logged out. */
	private ManagedReference<ClientSession> currentSessionRef = null;

	/** The {@link Room} this player is in, or null if none. */
	private ManagedReference<Room> currentRoomRef = null;

	private ManagedReference<GamePlayer> playerRef = null;

	private MessageHandler handler;

	// private ManagedReference<PlayerInventory> inventoryRef = null;

	// private ManagedReference<GlobalPlayersControl> global = null;

	// private Set<ManagedReference<PlayerInventory>> inventorySet = null;

	/**
	 * Creates a new {@code Player} with the given name.
	 * 
	 * @param name
	 *            the name of this player
	 */
	public GamePlayerClientSessionListener(String objetcName,
			String objectDescription) {
		super(objetcName, objectDescription);
	}

	/**
	 * Find or create the player object for the given session, and mark the
	 * player as logged in on that session.
	 * 
	 * @param session
	 *            which session to find or create a player for
	 * @return a player for the given session
	 */
	public static GamePlayerClientSessionListener loggedIn(
			ClientSession session, GamePlayer gp) {

		String playerBinding = PLAYER_BIND_PREFIX + session.getName();
		// try to find player object, if non existent then create
		DataManager dataMgr = AppContext.getDataManager();
		GamePlayerClientSessionListener player;

		try {
			player = (GamePlayerClientSessionListener) dataMgr
					.getBinding(playerBinding);
		} catch (NameNotBoundException ex) {
			// this is a new player
			player = new GamePlayerClientSessionListener(playerBinding,
					"a game user");
			logger.log(Level.INFO, "New player created: {0}", player);
			dataMgr.setBinding(playerBinding, player);

		}
		// this class has a instance of Gameplayer
		player.setPlayer(gp);
		player.setSession(session);
		player.handler = new MessageHandler(player);
		return player;
	}

	/**
	 * Returns the session for this listener.
	 * 
	 * @return the session for this listener
	 */
	public ClientSession getSession() {
		if (currentSessionRef == null) {
			return null;
		}

		return currentSessionRef.get();
	}

	/**
	 * Mark this player as logged in on the given session.
	 * 
	 * @param session
	 *            the session this player is logged in on
	 */
	protected void setSession(ClientSession session) {
		if (session != null) {
			DataManager dataMgr = AppContext.getDataManager();
			dataMgr.markForUpdate(this);

			currentSessionRef = dataMgr.createReference(session);

			logger.log(Level.INFO, "Set session for {0} to {1}", new Object[] {
					this, session });
		}
	}

	/**
	 * Handles a player entering a room.
	 * 
	 * @param room
	 *            the room for this player to enter
	 */
	public void enter(Room room) {
		logger.log(Level.INFO, "{0} enters {1} and {2}", new Object[] { this,
				room, getPlayer() });
		room.addPlayerSession(this);
		setRoom(room);

	}

	/** {@inheritDoc} */
	public void receivedMessage(ByteBuffer message) {

		String command = decodeString(message);
		// logger.log(Level.INFO, "{0} received command: {1} ", new Object[] {
		// this, command });

		if (command.equalsIgnoreCase("look")) {

			String reply = getRoom().look(this);
			sendClientMessage(getSession(), reply);
		} else if (command.equalsIgnoreCase("inventory")) {

			StringBuilder sb = new StringBuilder();
			sb.append("return maximal 100 items in your Inventory");
			sb.append(getPlayer().getInventory());
			sendClientMessage(getSession(), sb.toString());

		} else {
			handler.handleMessage(this, command);
		}

	}

	public void sendClientMessage(ClientSession cs, String message) {
		getSession().send(encodeString(message));

	}

	/** {@inheritDoc} */
	public void disconnected(boolean graceful) {

		Channel channel = AppContext.getChannelManager().getChannel(
				"map_" + playerRef.get().getMapId());
		channel.send(null, encodeString("m/loggout/"
				+ playerRef.get().getUUIDString() + "/"));

		logger.log(Level.INFO, "Disconnected: {0}", this);

		cleanUp();
		// this.inventoryRef = null;

	}

	/**
	 * clean up the object store delete name binding remove the managedobject
	 */

	public void cleanUp() {
		// don't forget clean up inventory
		// setSession(null);
		persistToDB();
		logger.log(Level.INFO, "User disconnect, cleanup");
		getRoom().removePlayer(this);
		setRoom(null);

		// remove the player from data manager
		logger.log(Level.INFO, "remove binding {0}", playerRef.get()
				.getUserName());
		AppContext.getDataManager().removeBinding(
				PLAYER_BIND_PREFIX + playerRef.get().getUserName());
		// remove reference of the user's inventory
		AppContext.getDataManager().removeObject(getPlayer().getInventory());
		// remove reference of the player
		AppContext.getDataManager().removeObject(playerRef.get());
		playerRef = null;
		logger.log(Level.INFO, "clean up finished");
	}

	/**
	 * persist all player and inventory to DB
	 */
	public void persistToDB() {
		DataManager dm = AppContext.getDataManager();
		try {
			AppContext.getDataManager().removeObject(
					dm.getBinding(getPlayer().getUUIDString()));
			AppContext.getDataManager().removeBinding(
					getPlayer().getUUIDString());
			GamePlayerHelper gp = new GamePlayerHelper();
			InventoryHelper ih = new InventoryHelper();
			gp.logOutInformationPersistence(this.getPlayer());
			ih.InventoryToDataBase(getPlayer().getInventory());
		} catch (NameNotBoundException ex) {

		}

	}

	/**
	 * Returns the room this player is currently in, or {@code null} if this
	 * player is not in a room.
	 * <p>
	 * 
	 * @return the room this player is currently in, or {@code null}
	 */
	protected Room getRoom() {
		if (currentRoomRef == null) {
			return null;
		}

		return currentRoomRef.get();
	}

	/**
	 * Sets the room this player is currently in. If the room given is null,
	 * marks the player as not in any room.
	 * <p>
	 * 
	 * @param room
	 *            the room this player should be in, or {@code null}
	 */
	protected void setRoom(Room room) {
		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		if (room == null) {
			currentRoomRef = null;
			return;
		}

		currentRoomRef = dataManager.createReference(room);
	}

	/**
	 * get the Gameplayer
	 * 
	 * @return GamePlayer
	 */
	public GamePlayer getPlayer() {
		if (playerRef != null) {
			return playerRef.get();
		} else {
			return null;
		}
	}

	/**
	 * set Gameplayer reference
	 * 
	 * @param player
	 */
	protected void setPlayer(GamePlayer player) {
		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		if (player == null) {
			currentRoomRef = null;
			return;
		}

		playerRef = dataManager.createReference(player);
		/*
		 * this should happens outside, search the inventory from cassandra when
		 * the use trying to login in , use dbhelper not here. but for using the
		 * bind, we put in here
		 */
		// setInventory(playerRef.get().getUserName());
	}

	/**
	 * try to find the users inventory in the object store if not found, then
	 * create a new inventory store.
	 * 
	 * use database in the future
	 * 
	 * @param bind
	 */
	protected void setInventory(String bind) {
		PlayerInventory inventory;
		DataManager dataMgr = AppContext.getDataManager();
		try {
			inventory = (PlayerInventory) dataMgr.getBinding(bind);
		} catch (NameNotBoundException ex) {
			inventory = new PlayerInventory();
			dataMgr.setBinding(bind, inventory);
		}

		this.getPlayer().setInventory(inventory);
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

	/**
	 * Decodes a message into a {@code String}.
	 * 
	 * @param message
	 *            the message to decode
	 * @return the decoded string
	 */
	protected static String decodeString(ByteBuffer message) {
		try {
			byte[] bytes = new byte[message.remaining()];
			message.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(getObjectName());
		buf.append('@');
		if (getSession() == null) {
			buf.append("null");
		} else {
			buf.append(currentSessionRef.getId());
		}
		return buf.toString();
	}

}
