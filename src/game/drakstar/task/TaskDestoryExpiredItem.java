package game.drakstar.task;

import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Room;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class TaskDestoryExpiredItem implements Task, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4833715481389183813L;
	ManagedReference<Room> roomRef;

	public TaskDestoryExpiredItem(ManagedReference<Room> roomRef) {
		this.roomRef = roomRef;

	}

	@Override
	public void run() throws Exception {
		System.out.println("====================clean==================");
		PlayerInventory p = (PlayerInventory) AppContext.getDataManager()
				.getBinding("iii");
		p.clear();
		// roomRef.get().destoryExpiretItem();
	}
}
