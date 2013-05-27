package game.cassandra.Factorys;

import java.security.SecureRandom;

import game.cassandra.gamestates.Armor;
import game.cassandra.gamestates.Item;
import game.cassandra.gamestates.Sword;
import game.cassandra.utils.Utils;

public class EquipmentFactory {

	public static Item createRandomGameItem() {
		int i = Utils.getRandomInt(2);
		switch (i) {
		case 0:
			return EquipmentFactory.createArmor();
		case 1:
			return EquipmentFactory.createArmor();

		case 2:
			return EquipmentFactory.createASword();

		default:
			return EquipmentFactory.createASword();

		}

	}

	public static Sword createASword() {
		int price = randomInt(1000);
		String desc = description(price);
		return new Sword(price, "System", "A " + desc + " Sword",
				randomInt(100), "Sword of " + desc);
	}

	public static Armor createArmor() {
		int price = randomInt(1000);
		String desc = description(price);
		return new Armor(price, "System", "A " + desc + " Armor",
				randomInt(100), "Armor of " + desc);
	}

	private static String description(int price) {
		String word;
		if (price >= 0 && price < 200) {
			word = "basic";
		} else if (price >= 200 && price < 400) {
			word = "good";
		} else if (price > 400 && price < 600) {
			word = "wonderful";
		} else if (price >= 600 && price < 800) {
			word = "incredible";
		} else {
			word = "fatastic";
		}
		return word;

	}

	private static int randomInt(int num) {
		SecureRandom random = new SecureRandom();
		int price = random.nextInt(num) + 1;
		return price;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			int s = EquipmentFactory.randomInt(99);
			if (s <= 0 || s == 100) {
				System.out.println(s);
			}
			System.out.println(EquipmentFactory.createASword().toString());
			System.out.println(EquipmentFactory.createArmor().toString());

		}
	}

}
