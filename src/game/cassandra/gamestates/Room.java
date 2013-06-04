/*
 * Copyright 2007-2010 Sun Microsystems, Inc.
 *
 * This file is part of Project Darkstar Server.
 *
 * Project Darkstar Server is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation and
 * distributed hereunder to you.
 *
 * Project Darkstar Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * --
 */

package game.cassandra.gamestates;

import game.darkstar.network.GamePlayerClientSessionListener;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;

/**
 * 
 * 
 * This class represents a room in the game world, inherits from
 * gamemanagedobject and managed by Datamanager
 * 
 * see also the original example by Darkstar
 */
public class Room extends GameManagedObjects {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 3886351480109103254L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(Room.class.getName());

	/** The set of items in this room */
	private final Vector<ManagedReference<Item>> items = new Vector<ManagedReference<Item>>();

	/** The set of players in this room. */
	private final Vector<ManagedReference<GamePlayerClientSessionListener>> playersSet = new Vector<ManagedReference<GamePlayerClientSessionListener>>();

	// a managedlist to manage items
	// private ManagedReference<ManagedReferenceList<Item>> itemsList;;

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	/**
	 * Creates a new room with the given name and description, initially empty
	 * of items and players.
	 * 
	 * @param name
	 *            the name of this room
	 * @param description
	 *            a description of this room
	 */
	public Room(String name, String description) {
		super(name, description);
		// itemsList = AppContext.getDataManager().createReference(
		// new ManagedReferenceList<Item>());
	}

	public boolean addItem(Item item) {
		logger.log(Level.INFO, "{0} placed in {1}", new Object[] { item, this });

		// itemsList.get().add(item);

		// we can not directly save the item in the list
		// instead we must save a managedreference to the item

		DataManager dataMgr = AppContext.getDataManager();
		dataMgr.markForUpdate(this);
		ManagedReference<Item> itemRef = dataMgr.createReference(item);

		// this.showAllItemsInTheRoom();

		return items.add(itemRef);

	}

	public void showAllItemsInTheRoom() {
		// StringBuilder itemsOnTheGround = new StringBuilder(
		// "Items produced by system, in the room :");
		// itemsOnTheGround.append(items.size());
		//
		// itemsOnTheGround.append("\n");
		// itemsOnTheGround.append(getObjectName());
		// itemsOnTheGround.append("\n ------------------------------\n");
		//
		// for (ManagedReference<Item> itemRef : items) {
		// itemsOnTheGround.append(itemRef.get());
		// itemsOnTheGround.append("\n");
		// }
		// itemsOnTheGround.append("------------------------------\n");

		// logger.log(Level.INFO, itemsList.get().toString());
	}

	public String toString() {
		StringBuilder itemsOnTheGround = new StringBuilder(
				"Items produced by system, in the room :");
		itemsOnTheGround.append(items.size());

		itemsOnTheGround.append("\n");
		itemsOnTheGround.append(getObjectName());
		itemsOnTheGround.append("\n");

		itemsOnTheGround.append(" ------------------------------\n");

		for (ManagedReference<Item> itemRef : items) {
			itemsOnTheGround.append(itemRef.get());
			itemsOnTheGround.append("\n");
		}
		itemsOnTheGround.append("------------------------------\n");

		return itemsOnTheGround.toString();
	}

	/**
	 * Adds a player to this room.
	 * 
	 * @param managerPlayers
	 *            the player to add
	 * @return {@code true} if the player was added to the room
	 */
	public boolean addPlayerSession(GamePlayerClientSessionListener player) {
		logger.log(Level.INFO, "{0} enters {1}", new Object[] { player, this });

		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		return playersSet.add(dataManager.createReference(player));
	}

	/**
	 * Removes a player from this room.
	 * 
	 * @param player
	 *            the player to remove
	 * @return {@code true} if the player was in the room
	 */
	public boolean removePlayer(GamePlayerClientSessionListener player) {
		logger.log(Level.INFO, "{0} leaves {1}", new Object[] { player, this });

		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		return playersSet.remove(dataManager.createReference(player));
	}

	public void destoryExpiretItem() {
		Iterator<ManagedReference<Item>> it = items.iterator();
		ManagedReference<Item> iRef = null;
		// AppContext.getDataManager().markForUpdate(this);
		// while (it.hasNext()) {
		// iRef = it.next();
		// if (iRef.get().expired()) {
		// items.remove(iRef);
		// AppContext.getDataManager().removeObject(iRef.get());
		// }
		// }

		logger.log(Level.INFO, "trying to find expired items");
		// Iterator<ManagedReference<Item>> it = itemsList.get().getIterator();
		// DataManager dataManager = AppContext.getDataManager();
		// dataManager.markForUpdate(this);
		// dataManager.markForUpdate(itemsList.get());
		//
		// ManagedReference<Item> itRef = null;
		// while (it.hasNext()) {
		// itRef = it.next();
		// if (itRef.get().expired()) {
		// itemsList.get().remove(itRef.get());
		// dataManager.removeObject(itRef.get());
		//
		// }
		//
		// }
		//
		// for (int i = 0; i < itemsList.size(); i++) {
		// }
		// for (Item item : itemsList) {
		// if ((System.currentTimeMillis() - itemRef.get().getProduceTime()) >
		// 10000) {
		//
		// logger.log(Level.INFO, "Item {0} expired,will be destoryed ",
		// itemRef.get());
		// items.remove(itemRef);
		// dataManager.removeObject(itemRef.get());
		// }
		// }
	}

	// public String showPlayersPositions(GamePlayerClientSessionListener
	// player) {
	// logger.log(Level.INFO, "{0} sypl {1}", new Object[] { player, this });
	//
	// String output = "";
	//
	// List<GamePlayerClientSessionListener> otherPlayers =
	// getPlayersExcluding(player);
	// Iterator<GamePlayerClientSessionListener> it = otherPlayers.iterator();
	// while (it.hasNext()) {
	// GamePlayerClientSessionListener p = it.next();
	// output += "2/" + p.getTempPosX() + "/" + p.getTempPosY() + "-";
	// }
	//
	// return output.toString();
	// }

	/**
	 * Returns a description of what the given player sees in this room.
	 * 
	 * @param looker
	 *            the player looking in this room
	 * @return a description of what the given player sees in this room
	 */
	public String look(GamePlayerClientSessionListener lookerPlayer) {
		logger.log(Level.INFO, "{0} looks at {1}", new Object[] { lookerPlayer,
				this });

		StringBuilder output = new StringBuilder();
		output.append("You are in ").append(getObjectDescription())
				.append(".\n");

		List<GamePlayerClientSessionListener> otherPlayers = getPlayersExcluding(lookerPlayer);

		if (!otherPlayers.isEmpty()) {
			output.append("Also in here are :");

			appendPrettyList(output, otherPlayers);
			output.append(".\n");
		} else {
			output.append("nobody here but you/n");
		}
		output.append("items in this room, on the ground /n");
		output.append(this.toString());
		this.showAllItemsInTheRoom();
		// show all items on the ground

		return output.toString();
	}

	protected static ByteBuffer encodeString(String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

	/**
	 * Appends the names of the {@code SwordWorldObject}s in the list to the
	 * builder, separated by commas, with an "and" before the final item.
	 * 
	 * @param builder
	 *            the {@code StringBuilder} to append to
	 * @param list
	 *            the list of items to format
	 */
	private void appendPrettyList(StringBuilder builder,
			List<? extends GameManagedObjects> list) {
		if (list.isEmpty()) {
			return;
		}

		int lastIndex = list.size() - 1;
		GameManagedObjects last = list.get(lastIndex);

		Iterator<? extends GameManagedObjects> it = list.subList(0, lastIndex)
				.iterator();
		if (it.hasNext()) {
			GameManagedObjects other = it.next();
			builder.append(other.getObjectName());
			while (it.hasNext()) {
				other = it.next();
				builder.append(" ,");
				builder.append(other.getObjectName());
			}
			builder.append(" and ");
		}
		builder.append(last.getObjectName());
	}

	/**
	 * Returns a list of players in this room excluding the given player.
	 * 
	 * @param player
	 *            the player to exclude
	 * @return the list of players
	 */
	private List<GamePlayerClientSessionListener> getPlayersExcluding(
			GamePlayerClientSessionListener player) {
		if (playersSet.isEmpty()) {
			return Collections.emptyList();
		}

		ArrayList<GamePlayerClientSessionListener> otherPlayers = new ArrayList<GamePlayerClientSessionListener>(
				playersSet.size());

		for (ManagedReference<GamePlayerClientSessionListener> playerRef : playersSet) {
			GamePlayerClientSessionListener other = playerRef.get();
			// if the exists plater not equals youself
			if (!player.equals(other)) {
				otherPlayers.add(other);
			}
		}

		return Collections.unmodifiableList(otherPlayers);
	}
}
