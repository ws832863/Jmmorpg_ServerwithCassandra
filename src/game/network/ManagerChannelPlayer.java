package game.network;

//import game.systems.MapWorld;
//import game.systems.WorldMap;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import game.systems.MessageHandler;
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

	@Override
	public void receivedMessage(Channel channel, ClientSession session,
			ByteBuffer message) {

		MessageHandler msgHandler = new MessageHandler();
		msgHandler.handleMessage(channel, session, message);

	}

}
