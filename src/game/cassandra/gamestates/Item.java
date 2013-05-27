package game.cassandra.gamestates;

import java.io.Serializable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;

/**
 * a abstract class represents the items in game world. all the items in game
 * world inherited from this class sword, armor and so on
 * 
 * @author parallels
 * 
 */
public abstract class Item implements Serializable, ManagedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4360673646698717143L;

	protected int price;
	protected String owner_id = null;
	protected String description;

	public Item(int price, String ownerId, String description) {
		this.price = price;
		this.owner_id = ownerId;
		this.description = description;
	}

	public Item() {
		price = 100;
		description = "A undescripted Item";
	}

	/*
	 * each Item has a owner, if owner is null, it belongs to system
	 */
	public void setOwnerId(String ownerId) {
		AppContext.getDataManager().markForUpdate(this);

		this.owner_id = ownerId;
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

	/*
	 * get the ownerIds, if this item has no owner yet, set the owner as system
	 */
	public String getOwnerId() {
		return owner_id == null ? "SystemFactory" : owner_id;
	}

	public String toString() {
		return "A Item: " + description + ", price : " + price;
	}

}
