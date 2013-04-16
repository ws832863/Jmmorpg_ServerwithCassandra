package game.drakstar.task;

import game.cassandra.dao.CassandraDAOGamePlayer;
import game.cassandra.data.GamePlayer;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class TaskCheckUserInformation implements Serializable, Task {

	/**
	 * a task, check the user information by username
	 */
	String uname;
	ManagedReference<GamePlayer> gpRef;
	private static final long serialVersionUID = 8174880422660629281L;

	public TaskCheckUserInformation(String username) {
		uname = username;
	}

	@Override
	public void run() throws Exception {

		CassandraDAOGamePlayer cdgp = new CassandraDAOGamePlayer();
		cdgp.selectByLoginName(uname);
		GamePlayer g = cdgp.getVos().firstElement();
		AppContext.getDataManager().setBinding("return." + g.getUserName(), g);
	}
}
