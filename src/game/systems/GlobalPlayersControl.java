package game.systems;

import game.cassandra.data.GamePlayer;
import game.cassandra.data.PlayerInventory;

import java.io.Serializable;
import java.util.Set;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

public class GlobalPlayersControl implements ManagedObject, Serializable {
	/**
	 * a class contains all the players created by the system
	 */
	private static final long serialVersionUID = -6267960132233912790L;
	public Set<ManagedReference<GamePlayer>> playerSet = null;
	public Set<ManagedReference<PlayerInventory>> inventorySet = null;

	public void containsUser(GamePlayer gp) {
		DataManager dm = AppContext.getDataManager();
		dm.markForUpdate(this);
		playerSet.add(dm.createReference(gp));
	}

	public void containsInventory(PlayerInventory pi) {
		DataManager dm = AppContext.getDataManager();
		dm.markForUpdate(this);
		inventorySet.add(dm.createReference(pi));
	}

	public void removeUser(GamePlayer gp) {
		DataManager dm = AppContext.getDataManager();
		dm.markForUpdate(this);
		playerSet.remove(dm.createReference(gp));
	}

	public void removeInventory(PlayerInventory pi) {
		DataManager dm = AppContext.getDataManager();
		dm.markForUpdate(this);
		inventorySet.remove(dm.createReference(pi));
	}
}
