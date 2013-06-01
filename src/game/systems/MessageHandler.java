package game.systems;

import game.cassandra.Factorys.EquipmentFactory;
import game.cassandra.data.GamePlayer;
import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Item;
import game.cassandra.utils.Utils;
import game.darkstar.network.GamePlayerClientSessionListener;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;

public class MessageHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7909343275679216917L;

	// private Logger logger = Logger.getLogger(MessageHandler.class.getName());

	private ManagedReference<ClientSession> sessionRef = null;
	private ManagedReference<GamePlayerClientSessionListener> playerSessionRef = null;
	private ManagedReference<PlayerInventory> inventoryRef = null;
	private ManagedReference<GamePlayer> playerRef = null;

	/**
	 * @param args
	 */
	public MessageHandler(GamePlayerClientSessionListener gcl) {
		setGamePlayer(gcl.getPlayer());
		setClientSession(gcl.getSession());
		setInventory(getGamePlayer().getInventory());
		playerSessionRef = AppContext.getDataManager().createReference(gcl);

	}

	protected void setInventory(PlayerInventory pi) {
		inventoryRef = AppContext.getDataManager().createReference(pi);
	}

	protected PlayerInventory getPlayerInventory() {
		if (inventoryRef != null) {
			return inventoryRef.get();
		} else {
			System.out.println("ERROR NULL INVENTORY");
		}

		return null;
	}

	protected void setGamePlayer(GamePlayer gp) {
		if (gp == null)
			return;

		playerRef = AppContext.getDataManager().createReference(gp);

	}

	protected GamePlayer getGamePlayer() {
		if (playerRef == null) {
			return null;
		}
		return playerRef.get();
	}

	protected void setClientSession(ClientSession cs) {
		if (cs == null)
			return;
		sessionRef = AppContext.getDataManager().createReference(cs);
	}

	protected ClientSession getClientSession() {
		if (sessionRef == null) {
			return null;
		}
		return sessionRef.get();
	}

	public MessageHandler() {

	}

	public void handleMessage(GamePlayerClientSessionListener gcl,
			String message) {

		String decodeMsg = message;// Utils.decodeByteBufferToString(message);

		if (decodeMsg.toLowerCase().equals("look")) {
			this.handleLookaroundMessage(decodeMsg);
		} else if (decodeMsg.toLowerCase().equals("buy")) {
			this.handleBuyMessage(decodeMsg);
		} else if (decodeMsg.toLowerCase().equals("sell")) {
			this.handleSellMessage(decodeMsg);
		} else {
			// logger.info("Unsupport Command");
			this.handleUnSupportMessage(decodeMsg);
		}
	}

	public void handleLookaroundMessage(String message) {
		getClientSession().send(
				Utils.encodeStringToByteBuffer("you send a command :"
						+ message.toString()));

	}

	public void handleBuyMessage(String message) {
		DataManager dm = AppContext.getDataManager();
		// inventoryRef = dm.createReference(playerSessionRef.get().getPlayer()
		// .getInventory());
		// dm.markForUpdate(inventoryRef.get());
		ManagedReference<Item> itemRef = dm.createReference(EquipmentFactory
				.createRandomGameItem());
		getPlayerInventory().add(itemRef.get());
		getClientSession().send(
				Utils.encodeStringToByteBuffer("you have bought a:"
						+ itemRef.get().toString()));

	}

	public void handleSellMessage(String message) {
		DataManager dm = AppContext.getDataManager();
		// inventoryRef = dm.createReference(playerSessionRef.get().getPlayer()
		// .getInventory());
		// dm.markForUpdate(inventoryRef.get());

		if (getPlayerInventory().getFirstItem() != null) {
			ManagedReference<Item> item = dm
					.createReference(getPlayerInventory().getFirstItem());
			System.out.println("delete this" + item.get().toString());
			getPlayerInventory().delete(item.get());
			getClientSession().send(
					Utils.encodeStringToByteBuffer("deleted sucessful"));
		} else {
			getClientSession().send(
					Utils.encodeStringToByteBuffer("you have nothing to sell"));
		}
	}

	public void handleUnSupportMessage(String message) {

		getClientSession().send(
				Utils.encodeStringToByteBuffer("you send a unsupport command :"
						+ message.toString()
						+ "supported command(#look #buy #sell #inventory)"));
		// getClientSession()
		// .send(encodeString("you send a unsupport command("
		// + command
		// +
		// "), currently only <look> support, you can type look to check other players around here"));

	}

}
