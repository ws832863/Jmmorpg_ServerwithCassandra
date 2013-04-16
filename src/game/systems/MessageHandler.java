package game.systems;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;

public class MessageHandler {
	/** The message encoding. */
	private static final String MESSAGE_CHARSET = "UTF-8";
	private Logger logger = Logger.getLogger(MessageHandler.class.getName());
	private Channel channel;
	private ClientSession clientSession;

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
		String decodeMsg = decodeString(message);
		System.out.println("a message from client  " + decodeMsg
				+ "   on channel " + cnl.getName());
		String msg[] = decodeMsg.split("/");

		if (msg[0].toLowerCase().equals("chat")) {
			this.handleChatMessage(msg);
		} else if (msg[0].toLowerCase().equals("m")) {
			this.handleMovementMessage(msg);
		} else if (msg[0].toLowerCase().equals("trade")) {
			this.handleTradeMessage(msg);
		} else {
			logger.info("Unsupport Command");
			this.handleUnSupportMessage(msg);
		}

	}

	public void handleChatMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
		logger.info("Handle a <Chat> Message  " + sb.toString());
		channel.send(clientSession, encodeString(sb.toString()));
	}

	public void handleMovementMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
		logger.info("Handle a <move> Message   " + sb.toString());
		channel.send(clientSession, encodeString(sb.toString()));

	}

	public void handleTradeMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
		logger.info("Handle a <Trade> Message   " + sb.toString());
		channel.send(clientSession, encodeString(sb.toString()));

	}

	public void handleUnSupportMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
		logger.info("Handle a <UnSupport> Message   " + sb.toString());
		channel.send(clientSession, encodeString(sb.toString()));

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
