package game.systems;

import game.cassandra.Factorys.EquipmentFactory;
import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Item;
import game.cassandra.utils.Utils;
import game.darkstar.network.GamePlayerClientSessionListener;

import java.io.Serializable;
import java.nio.ByteBuffer;

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

	/**
	 * @param args
	 */
	public MessageHandler(GamePlayerClientSessionListener gcl) {
		playerSessionRef = AppContext.getDataManager().createReference(gcl);
		sessionRef = AppContext.getDataManager().createReference(
				playerSessionRef.get().getSession());
	}

	public MessageHandler() {

	}

	public void handleMessage(GamePlayerClientSessionListener gcl,
			String message) {

		playerSessionRef = AppContext.getDataManager().createReference(gcl);
		sessionRef = AppContext.getDataManager().createReference(
				playerSessionRef.get().getSession());

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
		getSession().send(
				Utils.encodeStringToByteBuffer("you send a command :"
						+ message.toString()));

	}

	public void handleBuyMessage(String message) {
		DataManager dm = AppContext.getDataManager();
		inventoryRef = dm.createReference(playerSessionRef.get().getPlayer()
				.getInventory());
		dm.markForUpdate(inventoryRef.get());
		ManagedReference<Item> itemRef = dm.createReference(EquipmentFactory
				.createRandomGameItem());
		inventoryRef.get().add(itemRef.get());
		getSession().send(
				Utils.encodeStringToByteBuffer("you have bought a:"
						+ itemRef.get().toString()));

	}

	public void handleSellMessage(String message) {
		DataManager dm = AppContext.getDataManager();
		inventoryRef = dm.createReference(playerSessionRef.get().getPlayer()
				.getInventory());
		dm.markForUpdate(inventoryRef.get());

		if (inventoryRef.get().getFirstItem() != null) {
			ManagedReference<Item> item = dm.createReference(inventoryRef.get()
					.getFirstItem());
			System.out.println("delete this" + item.get().toString());
			inventoryRef.get().delete(item.get());
			getSession().send(
					Utils.encodeStringToByteBuffer("deleted sucessful"));
		} else {
			getSession().send(
					Utils.encodeStringToByteBuffer("you have nothing to sell"));
		}
	}

	public void handleUnSupportMessage(String message) {

		getSession().send(
				Utils.encodeStringToByteBuffer("you send a unsupport command :"
						+ message.toString()
						+ "supported command(#look #buy #sell #inventory)"));
		// getSession()
		// .send(encodeString("you send a unsupport command("
		// + command
		// +
		// "), currently only <look> support, you can type look to check other players around here"));

	}

	private ClientSession getSession() {
		if (sessionRef != null) {
			return sessionRef.get();
		}
		return null;
	}
}
