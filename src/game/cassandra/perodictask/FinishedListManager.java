package game.cassandra.perodictask;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.NameNotBoundException;

public class FinishedListManager implements ManagedObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4129824290835780224L;

	private static final Logger logger = Logger
			.getLogger(FinishedListManager.class.getName());

	private Queue<ManagedReference<ModifiedItemList>> loadedListRef;

	private FinishedListManager() {
		loadedListRef = new LinkedList<ManagedReference<ModifiedItemList>>();
	}

	public static FinishedListManager getSingletonFinishedListManager() {
		try {
			return (FinishedListManager) AppContext.getDataManager()
					.getBinding("loadedlist");
		} catch (NameNotBoundException ex) {
			FinishedListManager flm = new FinishedListManager();
			AppContext.getDataManager().setBinding("loadedlist", flm);
			return flm;
		}
	}

	public void insertInQueue(ModifiedItemList mil) {
		logger.log(Level.INFO, "A list inserted into Queue,now {0} lists",
				this.getSize());
		AppContext.getDataManager().markForUpdate(this);
		loadedListRef.add(AppContext.getDataManager().createReference(mil));
	}

	public void insertInQueue(ManagedReference<ModifiedItemList> ref) {

		AppContext.getDataManager().markForUpdate(this);
		loadedListRef.add(ref);
	}

	// get the first item and remove from the qneue

	public ModifiedItemList getFirstList() {
		logger.log(Level.INFO, "A list removed from Queue,now {0} lists",
				this.getSize());
		AppContext.getDataManager().markForUpdate(this);
		return loadedListRef.poll().get();
	}

	/*
	 * if there are more than 3 Finishedlist, trying to combine them and return
	 */

	public ModifiedItemList combineAll() {
		AppContext.getDataManager().markForUpdate(this);
		logger.info("Tring to combine " + getSize() + "lists into One");
		ModifiedItemList mfi = loadedListRef.poll().get();
		for (int i = 0; i < this.getSize()-1; i++) {
			mfi = mfi.combine(loadedListRef.poll().get());
		}
		return mfi;
	}

	public int getSize() {
		return loadedListRef.size();
	}

	public boolean isEmpty() {
		return loadedListRef.isEmpty();
	}

	public void showAllList() {
		if (loadedListRef != null) {
			System.out.println("There are " + getSize()
					+ " list in the finishedlist");

			for (int i = 0; i < getSize(); i++) {

				ModifiedItemList mfl = loadedListRef.poll().get();
				System.out
						.println("added items===============================");
				for (int j = 0; j < mfl.getAddedSize(); j++) {
					System.out.println(mfl.getAddedList());
				}

				System.out
						.println("deleted items==============================");
				for (int k = 0; k < mfl.getDeletedSize(); k++) {
					System.out.println(mfl.getDeleteMap());
				}
				AppContext.getDataManager().removeObject(mfl);
				System.out
						.println("===========================================");

			}
		}
	}
}
