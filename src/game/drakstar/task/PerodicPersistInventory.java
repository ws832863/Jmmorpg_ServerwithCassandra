package game.drakstar.task;

import game.cassandra.dao.InventoryHelper;
import game.cassandra.data.GamePlayer;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.ObjectNotFoundException;
import com.sun.sgs.app.Task;

public class PerodicPersistInventory implements Task, ManagedObject,
		Serializable {

	/**
	 * 
	 */
	private ManagedReference<GamePlayer> playerRef;
	private static final long serialVersionUID = 1L;

	public PerodicPersistInventory(GamePlayer g) {
		playerRef = AppContext.getDataManager().createReference(g);
	}

	@Override
	public void run() throws Exception {
		try {
			InventoryHelper ih = new InventoryHelper();
			ih.InventoryToDataBase(playerRef.get().getInventory());
			System.out.println("Persist inventory for "
					+ playerRef.get().getUserName());
		} catch (ObjectNotFoundException ex) {

		}
	}
}
