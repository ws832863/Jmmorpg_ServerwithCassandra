package game.cassandra.perodictask;

import game.cassandra.gamestates.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

public class ModifiedItemList implements Serializable, ManagedObject {

	/**
	 * this class represent a List, every 5 second will create a instance to
	 * store the user's modify, and then the finished will be set false the a
	 * thread will check, if the finished is false will store items in this list
	 * to cassandra
	 */
	private static final long serialVersionUID = 8250175971614210556L;

	private String Playeruuid;
	private ManagedReference<Item> itemRef;
	private String uuidString;
	private static final Logger logger = Logger
			.getLogger(ModifiedItemList.class.getName());
	private boolean finished = false;
	private boolean loaded = false;
	private ArrayList<ManagedReference<Item>> addItemList;
	private HashMap<String, ArrayList<String>> delItemMap;

	public ModifiedItemList() {
		logger.log(Level.INFO, "A ModifiedItemList created==============");
		uuidString = UUID.randomUUID().toString();
		addItemList = new ArrayList<ManagedReference<Item>>();
		delItemMap = new HashMap<String, ArrayList<String>>();
	}

	public String getUUIDString() {
		return uuidString;
	}

	public void addInsertedItem(Item item) {
		AppContext.getDataManager().markForUpdate(this);

		addItemList.add(AppContext.getDataManager().createReference(item));

	}

	public void addDeletedItem(String player, String ItemUUID) {
		AppContext.getDataManager().markForUpdate(this);
		ArrayList<String> al = delItemMap.get(player);
		if (al == null)
			al = new ArrayList<String>();
		al.add(ItemUUID);
		delItemMap.put(player, al);

	}

	public ModifiedItemList combine(ModifiedItemList mi) {
		this.getAddedList().addAll(mi.getAddedList());
		this.getDeleteMap().putAll(mi.getDeleteMap());
		return this;

	}

	public String getPlayeruuid() {
		return Playeruuid;
	}

	public void setPlayeruuid(String playeruuid) {
		Playeruuid = playeruuid;
	}

	public ManagedReference<Item> getItemRef() {
		return itemRef;
	}

	public void setItemRef(ManagedReference<Item> itemRef) {
		this.itemRef = itemRef;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getAddedSize() {
		return this.addItemList.size();
	}

	public int getDeletedSize() {
		return this.delItemMap.size();
	}

	public List<Item> getItemList() {
		ArrayList<Item> al = new ArrayList<Item>();
		for (ManagedReference<Item> i : addItemList) {
			al.add(i.get());
		}
		return al;

	}

	public ArrayList<ManagedReference<Item>> getAddedList() {
		return this.addItemList;
	}

	public HashMap<String, ArrayList<String>> getDeleteMap() {
		return this.delItemMap;
	}

	// public Vector<Item> getAddedItemVector(){
	//
	// }

}
