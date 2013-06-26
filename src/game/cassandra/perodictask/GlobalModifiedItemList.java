package game.cassandra.perodictask;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.NameNotBoundException;

public class GlobalModifiedItemList implements Serializable, ManagedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2878956479420231941L;
	private static final Logger logger = Logger
			.getLogger(GlobalModifiedItemList.class.getName());
	private ManagedReference<ModifiedItemList> currentListRef;

	private boolean changingList = false;

	private GlobalModifiedItemList() {
		currentListRef = AppContext.getDataManager().createReference(
				new ModifiedItemList());
	}

	public static GlobalModifiedItemList getSingletonGlobalModifiedItemList() {

		try {
			return (GlobalModifiedItemList) AppContext.getDataManager()
					.getBinding("globallist");
		} catch (NameNotBoundException ex) {
			GlobalModifiedItemList global = new GlobalModifiedItemList();
			AppContext.getDataManager().setBinding("globallist", global);
			return global;
		}
	}

	public ModifiedItemList getCurrentListRef() {
		// not set finished by another check task
		// every five second
		// AppContext.getDataManager().markForUpdate(this);
		if (!currentListRef.get().isFinished()) {
			return currentListRef.get();

		} else {
			System.out
					.println("list be set to full, check if some changing list");
			if (changingList == false) {
				System.out.println("no one changing. i will change it");
				changingList = true;
				AppContext.getDataManager().markForUpdate(this);
				logger.log(Level.INFO, "===List is set to finished, added "
						+ currentListRef.get().getAddedSize()
						+ "items and deleted "
						+ currentListRef.get().getDeletedSize() + "===");
				currentListRef.get().setLoaded(true);
				FinishedListManager.getSingletonFinishedListManager()
						.insertInQueue(currentListRef);
				currentListRef = AppContext.getDataManager().createReference(
						new ModifiedItemList());
			} else {

				System.out
						.println("someone is changing the list!!!!!!!!!!!!!!!!!!!!!! read it");
				getCurrentListRef();
			}
			changingList = false;

			return currentListRef.get();

		}
	}

	public void setFinished() {
		currentListRef.get().setFinished(true);
	}

}
