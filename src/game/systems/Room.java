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

package game.systems;

import game.cassandra.data.GamePlayer;
import game.core.CoreManagedObjects;
import game.darkstar.network.GamePlayerClientSessionListener;
import game.drakstar.task.TaskCheckUserInformation;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.TaskManager;

/**
 * Cada regi�o do mapa sera representada por uma Room, os players s� v�o
 * interagir com os players da mesma room, futuramente havera intera��o global
 * 
 * @author Michel Montenegro Represents a room in the {@link JMMORPG} example
 *         MUD.
 */
public class Room extends CoreManagedObjects {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 3886351480109103254L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(Room.class.getName());

	/** The set of players in this room. */
	private final Set<ManagedReference<GamePlayerClientSessionListener>> players = new HashSet<ManagedReference<GamePlayerClientSessionListener>>();

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

		return players.add(dataManager.createReference(player));
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

		return players.remove(dataManager.createReference(player));
	}

	public String showPlayersPositions(GamePlayerClientSessionListener player) {
		logger.log(Level.INFO, "{0} sypl {1}", new Object[] { player, this });

		String output = "";

		List<GamePlayerClientSessionListener> otherPlayers = getPlayersExcluding(player);
		Iterator<GamePlayerClientSessionListener> it = otherPlayers.iterator();
		while (it.hasNext()) {
			GamePlayerClientSessionListener p = it.next();
			output += "2/" + p.getTempPosX() + "/" + p.getTempPosY() + "-";
		}

		return output.toString();
	}

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
			output.append("nobody here but you");
		}

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
			List<? extends CoreManagedObjects> list) {
		if (list.isEmpty()) {
			return;
		}

		int lastIndex = list.size() - 1;
		CoreManagedObjects last = list.get(lastIndex);

		Iterator<? extends CoreManagedObjects> it = list.subList(0, lastIndex)
				.iterator();
		if (it.hasNext()) {
			CoreManagedObjects other = it.next();
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
		if (players.isEmpty()) {
			return Collections.emptyList();
		}

		ArrayList<GamePlayerClientSessionListener> otherPlayers = new ArrayList<GamePlayerClientSessionListener>(
				players.size());

		for (ManagedReference<GamePlayerClientSessionListener> playerRef : players) {
			GamePlayerClientSessionListener other = playerRef.get();
			if (!player.equals(other)) {
				otherPlayers.add(other);
			}
		}

		return Collections.unmodifiableList(otherPlayers);
	}
}
