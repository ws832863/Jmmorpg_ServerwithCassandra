package game.drakstar.task;

import game.cassandra.Factorys.EquipmentFactory;
import game.cassandra.gamestates.Room;

import java.io.Serializable;

import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class ProduceItemInTheRoom implements Task, Serializable {

	/**
	 * a periodic task to produce random Item in a given room
	 */
	private static final long serialVersionUID = 860597950174550283L;
	ManagedReference<Room> roomRef;

	public ProduceItemInTheRoom(ManagedReference<Room> room) {
		roomRef = room;
	}

	@Override
	public void run() throws Exception {

		roomRef.get().addItem(EquipmentFactory.createRandomGameItem());
		
	}

}
