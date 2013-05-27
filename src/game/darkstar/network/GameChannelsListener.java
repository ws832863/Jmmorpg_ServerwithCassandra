package game.darkstar.network;

import game.systems.ChannelMessageHandler;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * this channel use to send broadcast to all user move command
 * 
 * @author parallels
 * 
 */
public class GameChannelsListener implements Serializable, ChannelListener {

	private static final long serialVersionUID = 8421775775445048325L;

	@Override
	public void receivedMessage(Channel channel, ClientSession session,
			ByteBuffer message) {

		ChannelMessageHandler msgHandler = new ChannelMessageHandler();
		msgHandler.handleMessage(channel, session, message);

	}

}
