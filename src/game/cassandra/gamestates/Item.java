package game.cassandra.gamestates;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;

/**
 * a abstract class represents the items in game world. all the items in game
 * world inherited from this class sword, armor and so on
 * 
 * @author parallels
 * 
 */
public class Item implements Serializable, ManagedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4360673646698717143L;

	protected int price;
	// protected String owner_id = null;
	protected String description;
	protected String uuidString = null;
	protected long produceTime;
	protected String OwnerUUIDString;

	public Item(int price, String ownerId, String description) {
		this.price = price;
		this.OwnerUUIDString = ownerId;
		this.description = description;
		this.uuidString = UUID.randomUUID().toString();
		produceTime = System.currentTimeMillis();

	}

	public Item() {
		price = 100;
		description = "A undescripted Item";
		this.uuidString = UUID.randomUUID().toString();
		produceTime = System.currentTimeMillis();
	}

	/*
	 * each Item has a owner, if owner is null, it belongs to system
	 */
	public void setOwnerUUIDString(String ownerId) {
		AppContext.getDataManager().markForUpdate(this);

		this.OwnerUUIDString = ownerId;
	}

	/*
	 * get the ownerIds, if this item has no owner yet, set the owner as system
	 */
	public String getOwnerUUIDString() {
		return OwnerUUIDString == null ? "System" : OwnerUUIDString;
	}

	/*
	 * set a price for this item
	 */
	public void setPrice(int price) {

		AppContext.getDataManager().markForUpdate(this);

		this.price = price;
	}

	public void setDescription(String desc) {
		AppContext.getDataManager().markForUpdate(this);

		this.description = desc;
	}

	public String getDescription() {
		return this.description;

	}

	/*
	 * get the price
	 */
	public int getPrice() {

		return price;
	}

	public String toString() {
		return "A Item(" + getUUIDString() + "): " + description + ", price : "
				+ price + ")";
	}

	public String getUUIDString() {
		return this.uuidString;
	}

	public void setUUIDString(String str) {
		this.uuidString = str;
	}

	public void destoryMe() {
		
		AppContext.getDataManager().removeObject(this);
		
	}


	public long getProduceTime() {
		return this.produceTime;
	}

	public boolean equals(Item item) {
		boolean flag = false;
		if (item.getUUIDString().equals(this.getUUIDString()))
			flag = true;
		return flag;

	}

	public static void main(String[] args) throws InterruptedException {

		Item i1 = new Item();
		Item i2 = new Item();
		Item i3 = new Item();
		long produceTime;
		long existsTime = 10000;
		long now = System.currentTimeMillis();

		for (int i = 0; i < 20; i++) {

			TimeUnit.MILLISECONDS.sleep(1000);
		}
		System.out.println(i1);
		System.out.println(i2);
		System.out.println(i3);

	}

}
