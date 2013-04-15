package game.login;

import game.cassandra.dao.CassandraDAOLogin;
import game.cassandra.data.Login;

import java.io.Serializable;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

public class TaskIdentifyLoginUser implements Task, Serializable {
	IdentityCredentials identity;
	NamePasswordCredentials npc;
	private static final Logger logger = Logger
			.getLogger(TaskIdentifyLoginUser.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 3482016011256768543L;

	ManagedReference<Login> loginRef;

	public TaskIdentifyLoginUser(IdentityCredentials credential) {
		logger.info("Task constructor");
		identity = credential;
	}

	@Override
	public void run() throws Exception {
		logger.info("Task run");

		npc = (NamePasswordCredentials) identity;

		CassandraDAOLogin dao = new CassandraDAOLogin();

		Login login = null;

		String playPrifix = npc.getName();
		try {

			dao.selectByLoginPassword(npc.getName(),
					new String(npc.getPassword()));

			logger.info("the user information received form the client client session.getname "
					+ npc.getName() + "pwd " + npc.getPassword().toString());

			if (dao.getVos() != null && !dao.getVos().isEmpty()) {
				login = dao.getVos().firstElement();

				// bind login to a Managedreference/
				AppContext.getDataManager().setBinding(playPrifix, login);

				System.out
						.println("a user attempting to login, find user in the database "
								+ login.getName());
			} else {// the provide name and password not exists,create a new
					// account added by shuowang 2013 4.8
				System.out
						.println("User provide a name and password not in database, create it ");
				/*
				 * wangshuo wrote 2013 4 10
				 * 
				 * create a user game account and meanwhile create a default
				 * hero with random race for the user true say that we will
				 * create a hero for the user, hero name is username in
				 * cassandra, the hero will store as long user account in
				 * following form username_heroname_xx xx ist the hero 's
				 * charactor , e.c attack ,we can get certain users play heros
				 * character by doing a slicequery specifying the
				 * range(username_heroname_0 username_heroname_zzzzzzzzz)
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (login == null) {
			// return null;
		}
		logger.info("Task over");
	}

}
