package game.cassandra.perodictask;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class TaskSetListLoaded implements Task, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7753308161827716326L;
	ManagedReference<GlobalModifiedItemList> globalRef;

	public TaskSetListLoaded() {

		GlobalModifiedItemList gmil = GlobalModifiedItemList
				.getSingletonGlobalModifiedItemList();
		globalRef = AppContext.getDataManager().createReference(gmil);

	}

	@Override
	public void run() throws Exception {
		// every 5 seconds ,set the list as finished, and
		// the list will put the queue ready for insert to cassandra
		int addsize = globalRef.get().getCurrentListRef().getAddedSize();
		int delsize = globalRef.get().getCurrentListRef().getDeletedSize();

		if (addsize + delsize >= 1) {
			System.out.println("Task Trying to set a list as finished");
			// AppContext.getDataManager().markForUpdate(globalRef);
			globalRef.get().setFinished();

		} else {
			System.out.println("Nothing in the modified list,do nothing");
		}
		// FinishedListManager.getSingletonFinishedListManager().showAllList();

	}

}
