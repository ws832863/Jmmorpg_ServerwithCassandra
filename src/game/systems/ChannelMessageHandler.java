package game.systems;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;

public class ChannelMessageHandler {
	/** The message encoding. */
	private static final String MESSAGE_CHARSET = "UTF-8";
	private Logger logger = Logger.getLogger(ChannelMessageHandler.class
			.getName());
	private Channel channel;
	private ClientSession clientSession;
	private String msg;

	/**
	 * a class to handle the message from client and server
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void handleMessage(Channel cnl, ClientSession session,
			ByteBuffer message) {
		channel = cnl;
		clientSession = session;
		String decodeMsg = decodeByteBufferToString(message);
		System.out.println("A message from client  " + session.getName()
				+ "   on channel " + cnl.getName());

		if (decodeMsg.startsWith("m/")) {
			handleMovementMessage(decodeMsg);
			// cnl.send(session, encodeStringToByteBuffer(decodeMsg));
		} else if (decodeMsg.startsWith("chat/")) {
			handleChatMessage(decodeMsg);
		} else {
			// wrap a normal message to chat message
			decodeMsg = "chat/" + session.getName() + ":" + decodeMsg;
			handleUnSupportMessage(decodeMsg);
		}
	}

	public void handleChatMessage(String message) {
		// chat/username: messagebody

		logger.info("Handle a <Chat> Message  " + message.toString());

		channel.send(clientSession, encodeStringToByteBuffer(message));

	}

	public void handleMovementMessage(String message) {
		// the move message from gui channel, null mean the message will not
		// send back to the sender client

		logger.info("Handle a <move> Message   " + message);
		channel.send(clientSession, encodeStringToByteBuffer(message));

	}

	public void handleUnSupportMessage(String message) {
		logger.info("Handle a <UnSupport> Message   " + message);
		channel.send(clientSession, encodeStringToByteBuffer(message));

	}

	/**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s
	 *            the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	public static ByteBuffer encodeStringToByteBuffer(String s) {
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
	public static String decodeByteBufferToString(ByteBuffer buf) {
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
