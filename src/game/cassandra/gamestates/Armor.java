package game.cassandra.gamestates;

import game.cassandra.Factorys.EquipmentFactory;

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
		super.OwnerUUIDString = ownerId;
		super.price = price;
		// 1 means armor
		super.description = "1/" + name + "/" + defense + "/" + price;
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
		sbArmor.append(OwnerUUIDString);
		sbArmor.append("UUID").append(this.getUUIDString());
		// sbArmor.append("|| expired :");
		// sbArmor.append(expired());
		return sbArmor.toString();
	}

	public static void main(String[] args) {
		Armor a1 = EquipmentFactory.createArmor();
		Armor a2 = EquipmentFactory.createArmor();
		Armor a3 = EquipmentFactory.createArmor();
		a3.setUUIDString(a1.getUUIDString());

		System.out.println(a1.equals(a2));
		System.out.println(a1.equals(a3));

	}
}
