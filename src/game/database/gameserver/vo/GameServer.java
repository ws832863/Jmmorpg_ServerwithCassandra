package game.database.gameserver.vo;


/**
 * 
 * @author Michel Montenegro
 * 
 */
public class GameServer {

	private int id;
	private String name;
	private String ip;
	private int active;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " - Name: " + getName();
	}
}
