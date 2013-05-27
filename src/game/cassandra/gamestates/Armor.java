package game.cassandra.gamestates;

import com.sun.sgs.app.AppContext;

public class Armor extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7233101806921809011L;

	protected int defense;
	protected String name;

	public Armor(int price, String ownerId, String description) {
		super(price, ownerId, description);

		defense = 10;
		name = "Armor of Basic";
	}

	public Armor(int price, String ownerId, String description, int defense,
			String name) {

		this.defense = defense;
		this.name = name;
		super.owner_id = ownerId;
		super.description = description;
		super.price = price;
	}

	public String toString() {
		StringBuilder sbArmor = new StringBuilder("A Armor ");
		sbArmor.append("|| name :");
		sbArmor.append(name);
		sbArmor.append("|| price :");
		sbArmor.append(price);
		sbArmor.append("|| defense :");
		sbArmor.append(defense);
		sbArmor.append("|| description :");
		sbArmor.append(description);
		sbArmor.append("|| owner Id :");
		sbArmor.append(owner_id);
		return sbArmor.toString();
	}
}
