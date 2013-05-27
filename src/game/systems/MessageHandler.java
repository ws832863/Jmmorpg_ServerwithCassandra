package game.systems;

import game.cassandra.utils.Utils;
import game.darkstar.network.GamePlayerClientSessionListener;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;

public class MessageHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7909343275679216917L;
	
	//private Logger logger = Logger.getLogger(MessageHandler.class.getName());
	
	private ManagedReference<ClientSession> sessionRef = null;
	private ManagedReference<GamePlayerClientSessionListener> playerSessionRef = null;

	/**
	 * @param args
	 */

	public void handleMessage(GamePlayerClientSessionListener playerSession,
			ByteBuffer message) {
		String decodeMsg = Utils.decodeByteBufferToString(message);
		System.out.println("a message from client  " + decodeMsg);
		String msg[] = decodeMsg.split("/");

		if (msg[0].toLowerCase().equals("/look")) {
			this.handleChatMessage(msg);
		} else if (msg[0].toLowerCase().equals("m")) {
			this.handleMovementMessage(msg);
		} else if (msg[0].toLowerCase().equals("trade")) {
			this.handleTradeMessage(msg);
		} else {
	//		logger.info("Unsupport Command");
			this.handleUnSupportMessage(msg);
		}
	}

	public void handleChatMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
	//	logger.info("Handle a <Chat> Message  " + sb.toString());
		getSession().send(Utils.encodeStringToByteBuffer(sb.toString()));
	}

	public void handleMovementMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
	//	logger.info("Handle a <move> Message   " + sb.toString());
		getSession().send(Utils.encodeStringToByteBuffer(sb.toString()));

	}

	public void handleTradeMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
	//	logger.info("Handle a <Trade> Message   " + sb.toString());
		getSession().send(Utils.encodeStringToByteBuffer(sb.toString()));

	}

	public void handleUnSupportMessage(String message[]) {
		StringBuilder sb = new StringBuilder("");
		for (String s : message) {
			sb.append(s);
			sb.append("/");
		}
	//	logger.info("Handle a <UnSupport> Message   " + sb.toString());
		getSession().send(Utils.encodeStringToByteBuffer(sb.toString()));

	}

	private ClientSession getSession() {
		if (sessionRef != null) {
			return sessionRef.get();
		}
		return null;
	}
}
