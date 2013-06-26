package game.cassandra.conn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sun.sgs.app.AppContext;

import game.cassandra.gamestates.GameManagedObjects;

public class ManagedConfigurationHelper extends GameManagedObjects {

	private String clusterName;
	private String host;
	private String keyspaceName;
	private String clusterIp;
	int modifyDelay;
	int modifyPeriod;
	int persistDelay;
	int persistPeriod;

	public int getModifyDelay() {
		return modifyDelay;
	}

	public void setModifyDelay(int modifyDelay) {
		this.modifyDelay = modifyDelay;
	}

	public int getModifyPeriod() {
		return modifyPeriod;
	}

	public void setModifyPeriod(int modifyPeriod) {
		this.modifyPeriod = modifyPeriod;
	}

	public int getPersistDelay() {
		return persistDelay;
	}

	public void setPersistDelay(int persistDelay) {
		this.persistDelay = persistDelay;
	}

	public int getPersistPeriod() {
		return persistPeriod;
	}

	public void setPersistPeriod(int persistPeriod) {
		this.persistPeriod = persistPeriod;
	}

	public String getClusterIp() {
		return clusterIp;
	}

	public void setClusterIp(String clusterIp) {
		this.clusterIp = clusterIp;
	}

	public ManagedConfigurationHelper(String objetcName,
			String objectDescription) {
		super(objetcName, objectDescription);
		System.out.println("gameserver running path:" + this.getPath()
				+ "cassandra.properties");

		this.loadProperties();

		System.out.println("Read the db information from config file:");
		System.out.println("ClusterName:" + this.getClusterName());
		System.out.println("HOST:" + this.getHost());
		System.out.println("KeySpaceName:" + this.getKeyspaceName());
		System.out.println("ClusterIp:" + this.getClusterIp());

	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		AppContext.getDataManager().markForUpdate(this);
		this.clusterName = clusterName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		AppContext.getDataManager().markForUpdate(this);

		this.host = host;
	}

	public String getKeyspaceName() {
		return keyspaceName;
	}

	public void setKeyspaceName(String keyspaceName) {
		AppContext.getDataManager().markForUpdate(this);

		this.keyspaceName = keyspaceName;
	}

	private void loadProperties() {
		// InputStream inputStream = this.getClass().getClassLoader()
		// .getResourceAsStream(this.getPath() + "cassandra.properties");

		// String filePath = System.getProperty("user.dir")
		// + "/config/init.properties";
		Properties p = new Properties();
		try {
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(getPath() + "cassandra.properties"));
			p.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setClusterName(p.getProperty("ClusterName"));
		this.setHost(p.getProperty("Host"));
		this.setKeyspaceName(p.getProperty("KeySpaceName"));
		this.setClusterIp(p.getProperty("ClusterIp"));

		this.setModifyDelay(Integer.parseInt(p.getProperty("CheckModifyDelay")));
		this.setModifyPeriod(Integer.parseInt(p
				.getProperty("CheckModifyPeriod")));
		this.setPersistDelay(Integer.parseInt(p.getProperty("PersistTaskDelay")));
		this.setPersistPeriod(Integer.parseInt(p
				.getProperty("PersistTaskPeriod")));

		// this.setKeyspaceName(p.getProperty(""));

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8754867567838876743L;

	public String getPath() {
		// path="/home/parallels/sgs-server-dist-0.10.2.1/jmmorpg/JMMORPG_SERVER.jar"
		String path = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();

		return path.substring(0, path.lastIndexOf("/") + 1);
		// return "/home/parallels/sgs-server-dist-0.10.2.1/jmmorpg/
		// JMMORPG_SERVER.jar"
	}
}
