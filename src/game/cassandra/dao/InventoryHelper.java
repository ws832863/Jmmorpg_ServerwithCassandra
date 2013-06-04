package game.cassandra.dao;

import game.cassandra.data.PlayerInventory;
import game.cassandra.gamestates.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.prettyprint.hector.api.mutation.MutationResult;

public class InventoryHelper  {
	/**
	 * 
	 */
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

	public MutationResult insertModifiedItemTask(List<Item> list) {
		return iDao.addMultiItem(list);
	}

	public MutationResult deleteModifiedItemTask(
			HashMap<String, ArrayList<String>> hm) {
		return iDao.removeMultiItem(hm);
	}
}
