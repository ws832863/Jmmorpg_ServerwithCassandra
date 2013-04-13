package game.login;

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

import game.cassandra.dao.CassandraDAOLogin;
import game.database.login.vo.Login;
//import game.systems.WorldMap;

import java.util.Properties;
import java.util.logging.Logger;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;

import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.auth.IdentityAuthenticator;
import com.sun.sgs.auth.IdentityCredentials;
import com.sun.sgs.impl.auth.IdentityImpl;
import com.sun.sgs.impl.auth.NamePasswordCredentials;

/**
 * This simple implementation provides authentication based on a name and a
 * password. It is intended only for development use.
 * <p>
 * The names and cooresponding passwords are provided through a file, which is
 * read on startup and then never re-read. The file is named using the property
 * <code>PASSWORD_FILE_PROPERTY</code>. The file is of a simple form that
 * consists of one entry per line, where each entry has a name, some whitespace,
 * a SHA-256 hashed password that is encoded via <code>encodeBytes</code>, and
 * finally a newline.
 */
public class NamePasswordAuthenticator implements IdentityAuthenticator {

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger
			.getLogger(NamePasswordAuthenticator.class.getName());

	// add logined user to managedreference
	private ManagedReference<Login> loginRef = null;

	public NamePasswordAuthenticator(Properties properties) {

		logger.info("****** Initializing JMMORPG ******");
		logger.info("++++++ Initializing Authenticator ++++++ ");
		// is this also can be done in Listener?
		//new WorldMap();

	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSupportedCredentialTypes() {
		return new String[] { NamePasswordCredentials.TYPE_IDENTIFIER };
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The provided <code>IdentityCredentials</code> must be an instance of
	 * <code>NamePasswordCredentials</code>.
	 * 
	 * @throws AccountNotFoundException
	 *             if the identity is unknown
	 * @throws CredentialException
	 *             if the credentials are invalid
	 */
	public Identity authenticateIdentity(IdentityCredentials credentials)
			throws AccountNotFoundException, CredentialException {
		NamePasswordCredentials npc = (NamePasswordCredentials) credentials;
		/*
		 * searching a user in cassandra , if the user not exists, create a user
		 * and give the user a default hero with random hero class
		 */

		// DAOLogin dao = new DAOLogin();

		CassandraDAOLogin dao = new CassandraDAOLogin();

		Login login = null;
		try {

			dao.selectByLoginPassword(npc.getName(),
					new String(npc.getPassword()));

			logger.info("the user information received form the client client session.getname "
					+ npc.getName() + "pwd " + npc.getPassword().toString());

			// dao.selectAll();
			// dao.SearchingUserUseSecondaryIndexByNameAndPassword(npc.getName(),
			// new String(npc.getPassword());
			if (dao.getVos() != null && !dao.getVos().isEmpty()) {
				login = dao.getVos().firstElement();

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
				// dao.insertUserIntoCassandra(npc.getName(),
				// new String(npc.getPassword()), true);
				// the get the created user information from the database
				// dao.SearchingUserUseSecondaryIndexByNameAndPassword(
				// npc.getName(), new String(npc.getPassword()));
				// login = dao.getVos().firstElement();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (login == null) {
			return null;
		}

		return new IdentityImpl(npc.getName());
	}
}
