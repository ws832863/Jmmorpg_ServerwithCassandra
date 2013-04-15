package game.cassandra.data;

import java.io.Serializable;
import java.sql.Date;

import com.sun.sgs.app.ManagedObject;

/**
 * 
 * @author shuo wang a login is a managedobject. for a player session
 * 
 */
public class Login implements ManagedObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1391403479074600422L;
	private int id;
	private String login;
	private String user_password;
	private String accessLevel;
	private Date dateRegister;
	private String lastIP;
	private Date lastactive;
	private String userCurrIP;
	private String lastServer;
	private String name;
	private String email;
	private Date birth;
	private int clanId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Date getDateRegister() {
		return dateRegister;
	}

	public void setDateRegister(Date dateRegister) {
		this.dateRegister = dateRegister;
	}

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public Date getLastactive() {
		return lastactive;
	}

	public void setLastactive(Date lastactive) {
		this.lastactive = lastactive;
	}

	public String getUserCurrIP() {
		return userCurrIP;
	}

	public void setUserCurrIP(String userCurrIP) {
		this.userCurrIP = userCurrIP;
	}

	public String getLastServer() {
		return lastServer;
	}

	public void setLastServer(String lastServer) {
		this.lastServer = lastServer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getClanId() {
		return clanId;
	}

	public void setClanId(int clanId) {
		this.clanId = clanId;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " - Name: " + getName();
	}
}
