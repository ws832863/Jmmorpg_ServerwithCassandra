package game.cassandra.dao;

import game.cassandra.data.PlayerInventory;

public class InventoryHelper {
	InventoryDAO iDao;

	public InventoryHelper() {
		iDao = new InventoryDAO();
	}

	/*
	 * create a instance of playerinventory , give it to player
	 */

	public PlayerInventory getUsersInventoryFromDb(String playerUUID) {

		PlayerInventory pi = new PlayerInventory();

		int money = iDao.getMoney(playerUUID);
		pi.setMoney(money);
		pi.addAll(iDao.getAllItemOfPlayer(playerUUID));
		pi.setPlayer(playerUUID);
		return pi;

	}

	public void InventoryToDataBase(PlayerInventory pi) {

		String rowkey = pi.getPlayerUUIDString();
		iDao.addMultiItem(pi.getAllItems());
		iDao.updateMoney(rowkey, pi.getMoney());

	}

}
