package game.drakstar.task;

import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Room;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class ProduceItemInTheRoom implements Task, Serializable {

	/**
	 * a periodic task to produce random Item in a given room
	 */
	private static final long serialVersionUID = 860597950174550283L;
	ManagedReference<Room> roomRef;
	ManagedReference<PlayerInventory> ref = null;

	public ProduceItemInTheRoom(ManagedReference<Room> room) {
		roomRef = room;
		AppContext.getDataManager().setBinding("task1", new PlayerInventory());

	}

	@Override
	public void run() throws Exception {
		((PlayerInventory) AppContext.getDataManager().getBinding("task1"))
				.testBuy();
		// roomRef.get().addItem(EquipmentFactory.createRandomGameItem());
		// roomRef.get().destoryExpiretItem();
	}
}
