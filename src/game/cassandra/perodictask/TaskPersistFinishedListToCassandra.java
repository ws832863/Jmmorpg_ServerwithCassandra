package game.cassandra.perodictask;

import game.cassandra.dao.InventoryHelper;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class TaskPersistFinishedListToCassandra implements Task, Serializable {

	/**
	 * prodically insert the global modified items into cassandra
	 */
	private static final long serialVersionUID = -1823874592194196088L;
	private static final Logger logger = Logger
			.getLogger(TaskPersistFinishedListToCassandra.class.getName());
	private ManagedReference<FinishedListManager> finishRef;

	public TaskPersistFinishedListToCassandra() {
		finishRef = AppContext.getDataManager().createReference(
				FinishedListManager.getSingletonFinishedListManager());
	}

	@Override
	public void run() throws Exception {
		InventoryHelper ih = new InventoryHelper();
		if (finishRef.get().getSize() > 0) {

			logger.info("ready to persist modified item to cassandra");

			// if the loaded modified list exists, the get the reference
			// ManagedReference<ModifiedItemList> mfiRef = AppContext
			// .getDataManager().createReference(
			// finishRef.get().getFirstList());
			ManagedReference<ModifiedItemList> ml;
			if (finishRef.get().getSize() >= 3) {
				// if more than 3 list in modified lists
				ml = AppContext.getDataManager().createReference(
						finishRef.get().combineAll());
			} else {
				ml = AppContext.getDataManager().createReference(
						finishRef.get().getFirstList());
			}

			long executeTime = 0;
			if (ml.get().getAddedList().size() > 0) {
				executeTime = ih.insertModifiedItemTask(ml.get().getItemList())
						.getExecutionTimeMicro();
				// // .getExecutionTimeMicro();
				logger.info("insert sucessful, use " + executeTime
						+ " Microseconds");
			}
			if (ml.get().getDeletedSize() > 0) {
				executeTime = ih
						.deleteModifiedItemTask(ml.get().getDeleteMap())
						.getExecutionTimeMicro();
				logger.info("delete sucessful, use " + executeTime
						+ " Microseconds");
			}
			AppContext.getDataManager().removeObject(ml.get());

		}

	}
}
