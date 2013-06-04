package game.cassandra.dao;

import game.cassandra.conn.CassandraHelper;
import game.cassandra.gamestates.Armor;
import game.cassandra.gamestates.Item;
import game.cassandra.gamestates.Sword;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.log4j.Logger;

public class InventoryDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6885602600343068038L;

	/**
	 * @param args
	 */
	private static CassandraHelper dbHelper = CassandraHelper
			.getCassandraHelperInstance();

	private final static Logger logger = Logger.getLogger(InventoryDAO.class
			.getName());

	private Cluster gameCluster = dbHelper.getGameCluster();

	private String KeySpaceName = dbHelper.getKeySpaceName();

	private static StringSerializer stringSerializer = StringSerializer.get();

	private Keyspace keyspaceOperator = HFactory.createKeyspace(KeySpaceName,
			gameCluster);

	// only create login, player not include
	private final String ColumnFamilyName = "Inventory";

	private Vector<Item> vos;

	public Vector<Item> getVos() {
		return vos;
	}

	public InventoryDAO() {
		vos = new Vector<Item>();
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
	}

	public int getMoney(String rowkey) {
		ColumnQuery<String, String, String> columnQuery = HFactory
				.createStringColumnQuery(keyspaceOperator);
		columnQuery.setColumnFamily(ColumnFamilyName).setKey(rowkey)
				.setName("money");
		QueryResult<HColumn<String, String>> result = columnQuery.execute();

		// System.out.println(result.get().getName());
		// System.out.println(result.get().getValue());
		int money = 0;

		if (result != null) {
			try {
				money = Integer.valueOf(result.get().getValue());
			} catch (Exception ex) {
				return 0;
			}
		}
		return money;

	}

	/*
	 * update the Inventorys money
	 */
	public MutationResult updateMoney(String rowkey, long money) {
		if (rowkey == null) {
			return null;
		}
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		return mutator.insert(rowkey, ColumnFamilyName,
				HFactory.createColumn("money", String.valueOf(money)));
	}

	/*
	 * remove the whole row by specifying the rowkey(uuidString)
	 */
	public MutationResult removeRow(String rowkey) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		// column name is null, delete the whole row
		return mutator.delete(rowkey, ColumnFamilyName, null, stringSerializer);
	}

	/*
	 * remove on item in the inventory
	 */
	public MutationResult removeItem(Item item) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		return mutator.delete(item.getOwnerUUIDString(), ColumnFamilyName,
				item.getUUIDString(), stringSerializer);
	}

	public MutationResult removeMultiItem(HashMap<String, ArrayList<String>> hm) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		Set<Entry<String, ArrayList<String>>> entrys = hm.entrySet();
		Set<HColumn<String, String>> columns = new HashSet<HColumn<String, String>>();
		for (Entry<String, ArrayList<String>> e : entrys) {
			List<String> itemUids = e.getValue();
			for (String s : itemUids) {
				mutator.addDeletion(e.getKey(), ColumnFamilyName, s,
						stringSerializer);
			}
		}

		return mutator.execute();
	}

	/*
	 * remove multiitem for multi row in a inventory
	 */
	public MutationResult removeMultiItem(List<Item> itemList) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		for (Item item : itemList) {
			this.addDeletionToMut(mutator, item);
		}
		return mutator.execute();
	}

	/*
	 * add on item in the inventory
	 */
	public MutationResult addOneItem(Item item) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		// use the playeruuid as inventory's rowkey
		String name = item.getUUIDString();
		// make other properties of item as a string
		String value = item.getDescription();
		MutationResult mr = mutator.insert(item.getOwnerUUIDString(),
				ColumnFamilyName, HFactory.createColumn(name, value));
		return mr;
		// ItemToHColumn(mutator, item);
		// mutator.execute();

	}

	public MutationResult addMultiItem(List<Item> itemSet) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		for (Item item : itemSet) {
			addInsertionToMut(mutator, item);
		}
		MutationResult mr = mutator.execute();
		return mr;
	}

	private void addDeletionToMut(Mutator<String> mut, Item item) {
		mut.addDeletion(item.getOwnerUUIDString(), ColumnFamilyName,
				item.getUUIDString(), stringSerializer);
	}

	private void addInsertionToMut(Mutator<String> mut, Item item) {
		// use the playeruuid as inventory's rowkey
		String rowKey = item.getOwnerUUIDString();
		// make other properties of item as a string
		String name = item.getUUIDString();
		String value = item.getDescription();

		mut.addInsertion(rowKey, ColumnFamilyName,
				HFactory.createColumn(name, value));
	}

	private HColumn<String, String> itemToHColumn(Item item) {
		return HFactory.createColumn(item.getUUIDString(),
				item.getDescription());
	}

	/*
	 * get all items for a player
	 */
	public Vector<Item> getAllItemOfPlayer(String PlayerUUID) {

		String key = PlayerUUID;
		vos.removeAllElements();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspaceOperator, stringSerializer, stringSerializer,
				stringSerializer);

		query.setKey(key).setColumnFamily(ColumnFamilyName);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, null, "\uFFFF", false);

		Item item = null;
		while (iterator.hasNext()) {
			HColumn<String, String> c = iterator.next();
			if (!c.getName().equals("money")) {
				String[] value = c.getValue().split("/");

				// "0/" + name + "/" + attack + "/" + price;
				if (value[0].equals("2")) {
					item = new Sword(Integer.valueOf(value[3]), key,
							c.getValue(), Integer.valueOf(value[2]), value[1]);
					// "1/" + name + "/" + defense + "/" + price;
				} else if (value[0].equals("1")) {
					item = new Armor(Integer.valueOf(value[3]), key,
							c.getValue(), Integer.valueOf(value[2]), value[1]);

				} else {
					item = new Item();
					item.setDescription(c.getValue());
					item.setOwnerUUIDString(key);
				}
				// set the uuid for this item
				item.setUUIDString(c.getName());
		
				vos.add(item);
			} // tempResult.put(c.getName(), c.getValue());
		}
		return vos;

	}

	public void createInventorySchema() {
		logger.info("======>creating cassandra schema Inventory ");

		// the item uuidString
		BasicColumnDefinition colItemUUIDString = new BasicColumnDefinition();
		colItemUUIDString.setName(StringSerializer.get().toByteBuffer(
				"itemuuid"));
		colItemUUIDString.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		// the money of the users
		BasicColumnDefinition colMoney = new BasicColumnDefinition();
		colMoney.setName(StringSerializer.get().toByteBuffer("money"));
		colMoney.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
		// the last accssed ip

		// definite a basic cf, add all columns in it.
		BasicColumnFamilyDefinition basicItemDefinition = new BasicColumnFamilyDefinition();
		basicItemDefinition.setKeyspaceName(KeySpaceName); // keyspace name
		basicItemDefinition.setName(ColumnFamilyName);// column family name
		// use utf8 type, use command list login, human readable column name
		basicItemDefinition.setComparatorType(ComparatorType.UTF8TYPE);
		basicItemDefinition.addColumnDefinition(colItemUUIDString);
		basicItemDefinition.addColumnDefinition(colMoney);

		ColumnFamilyDefinition cfItemDef = new ThriftCfDef(basicItemDefinition);
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(cfItemDef));

		try {

			if (gameCluster.describeKeyspace(KeySpaceName) != null) {
				try {
					// if the keyspace exists, drop the columnfamily, with the
					// same name
					gameCluster.dropColumnFamily(KeySpaceName,
							ColumnFamilyName, true);
				} catch (HectorException he) {

				} finally {
					// add columnfamily to the exists keyspace
					gameCluster.addColumnFamily(cfItemDef);
				}
			} else {
				logger.debug("Keyspace " + KeySpaceName
						+ " not exists, create it");
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());

		} finally {

		}

		logger.info(" cassandra Item schema sucessfuly <======");

	}

	public MutationResult prepopulateForUsers(List<String> playerUUIDs) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);

		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());

		Mutator<String> mutator = columnFamilyTemplate.createMutator();

		for (String s : playerUUIDs) {
			mutator.addInsertion(s, ColumnFamilyName,
					HFactory.createColumn("money", "999999"));
		}
		MutationResult mr = mutator.execute();
		return mr;
	}

	public static void main(String[] args) {
		new InventoryDAO().createInventorySchema();
	}

}
