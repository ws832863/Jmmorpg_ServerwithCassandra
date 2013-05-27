package game.cassandra.gamestates;

public class Sword extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3726513039404792944L;

	protected int attack;
	protected String name;

	public Sword(int price, String ownerId, String description) {
		super(price, ownerId, description);
		attack = 10;
		name = "Sword of Basic";
		// TODO Auto-generated constructor stub
	}

	public Sword(int price, String ownerId, String description, int attack,
			String name) {

		this.attack = attack;
		this.name = name;
		super.owner_id = ownerId;
		super.description = description;
		super.price = price;
	}

	public String toString() {
		StringBuilder sbSword = new StringBuilder("A Sword ");
		sbSword.append("|| name :");
		sbSword.append(name);
		sbSword.append("|| price :");
		sbSword.append(price);
		sbSword.append("|| attack :");
		sbSword.append(attack);
		sbSword.append("|| description :");
		sbSword.append(description);
		sbSword.append("|| owner Id :");
		sbSword.append(owner_id);
		return sbSword.toString();
	}
}
