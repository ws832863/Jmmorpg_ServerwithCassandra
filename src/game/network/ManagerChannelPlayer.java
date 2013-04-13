package game.network;

//import game.systems.MapWorld;
//import game.systems.WorldMap;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * this channel use to send broadcast to all user move command
 * 
 * @author parallels
 * 
 */
public class ManagerChannelPlayer implements Serializable, ChannelListener {

	private static final long serialVersionUID = 8421775775445048325L;

	private static final Logger logger = Logger
			.getLogger(ManagerChannelPlayer.class.getName());

	/** The message encoding. */
	public static final String MESSAGE_CHARSET = "UTF-8";

	@Override
	public void receivedMessage(Channel channel, ClientSession session,
			ByteBuffer message) {
		// check the message sent by player (session) in one channel
		// this message sent to the channel.
		//so far ,found two type of messages
		// chat/player7: something here
		// m/7/7/player7/7/21/0/
		channel.send(session, message);
		String messageFromClient = decodeString(message);
		System.out.println("a message from client  " + messageFromClient);
		
/**i think the following code doing nothing**/
//		if (logger.isLoggable(Level.INFO)) {
//			logger.log(Level.INFO, "Channel message from {0} on channel {1}",
//					new Object[] { session.getName(), channel.getName() });
//		}
//		String messageFromClient = decodeString(message);
//
//		// message form: m(move)/loginid/classeid/heroname/tilex/tiley/0/
//
//		System.out.println("a message from client  " + messageFromClient);
//
//		String[] msg = messageFromClient.split("/");
//
//		// the position of the player in the currentmap
//		if (msg[0].equals("m")) {
//			// m denotes the command , a user moves
//
//			int loginId = Integer.valueOf(msg[1]);
//			int tileX = Integer.valueOf(msg[4]);
//			int tileY = Integer.valueOf(msg[5]);
//
//			// Check if this player j occupied another position earlier releases
//			// posioe
//
//			// Check if the reported position different from the previous
//			// position
//
//			MapWorld[][] mapWorlds = WorldMap.getWorld().get(channel.getName());// get
//																				// the
//																				// pre
//																				// loaded
//																				// world,
//																				// every
//																				// channelname
//																				// is
//			// i am wondering if this mapWorld.getWorld() has a instance..
//
//			// also a map name, every map use a specified channel
//			for (int x = 0; x < mapWorlds.length; x++) {
//				for (int y = 0; y < mapWorlds[0].length; y++) {
//					MapWorld mapWorld = mapWorlds[x][y];
//					if (!mapWorld.getLoginIDs().isEmpty()) {// there are players
//															// in the map ,not
//															// empty
//						// loginId is the loginID of current users ,
//						// we will remove it from the list
//						if (mapWorld.getLoginIDs().contains(loginId)) {
//							mapWorld.getLoginIDs().remove(loginId);
//						}
//					}
//				}
//			}
//
//			// "m/loginId/ClasseId/PlayerName/x/y/position"
//			// this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/1",
//			// Util.CHANNEL_MAP);
//			MapWorld mapWorld = mapWorlds[tileX][tileY];
//			mapWorld.getLoginIDs().add(loginId);
//			System.out
//					.println("Current/destination? Position Map(tilex/tiley):["
//							+ tileX + "/" + tileY + "] - LoginId: " + loginId);
//
//			channel.send(session,
//					encodeString("ManagerChannelPlayer: channel send message "
//							+ loginId));
//		}
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
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 * 
	 * @param buf
	 *            the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
	protected static String decodeString(ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

}
