package game.cassandra.dao;

import java.util.Arrays;
import java.util.UUID;

import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;

public class CassandraDataTest {

	/**
	 * @param args
	 */
	private StringSerializer stringSerializer = StringSerializer.get();
	private Cluster gameCluster = HFactory.getOrCreateCluster("testCluster",
			"127.0.0.1:9160");
	private String keySpaceName = "testSpace";
	private Keyspace keyspaceOperator = HFactory.createKeyspace(keySpaceName,
			gameCluster);
	private String ColumnFamilyName = "testCF";

	public CassandraDataTest() {
		// BasicColumnDefinition colUserNameDef = new BasicColumnDefinition();
		// definite a basic cf, add all columns in it.
		BasicColumnFamilyDefinition basicCFGamePlayerDef = new BasicColumnFamilyDefinition();
		basicCFGamePlayerDef.setKeyspaceName(keySpaceName); // keyspace name
		basicCFGamePlayerDef.setName(ColumnFamilyName);// column family name

		ColumnFamilyDefinition cfLoginUserDef = new ThriftCfDef(
				basicCFGamePlayerDef);
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(keySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(cfLoginUserDef));
		try {

			if (gameCluster.describeKeyspace(keySpaceName) != null) {
				try {
					gameCluster.dropColumnFamily(keySpaceName,
							ColumnFamilyName, true);
				} catch (HectorException he) {

				} finally {
					gameCluster.addColumnFamily(cfLoginUserDef);

				}
			} else {
				System.out.println("Keyspace " + keySpaceName
						+ " not exists, create it");
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			he.printStackTrace();
		}

	}

	public void insertSingleValue(int count) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		System.out.println("开始压力测试，我们插入50W条数据到2节点集群中");
		System.out.println("...");

		// 标记开始时间
		long startTime = System.currentTimeMillis();
		System.out.println("开始压力测试，我们插入50W条数据到2节点集群中");
		System.out.println("...");

		for (int i = 0; i < count; i++) {

			mutator.insert("rowkey" + i, ColumnFamilyName,
					HFactory.createColumn("name", "student" + 1));
			mutator.insert("rowkey" + i, ColumnFamilyName,
					HFactory.createColumn("id", i));
		}

		// 标记结束时间
		long endTime = System.currentTimeMillis();
		// 标记一共用时
		long elapsedTime = endTime - startTime;

		System.out.println("压力测试完毕,用时: " + elapsedTime + " 毫秒");

	}

	public MutationResult insertValue(int count) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < count; i++) {
			mutator.addInsertion(uuid, ColumnFamilyName,
					HFactory.createColumn("username", "wang"));
			mutator.addInsertion(uuid, ColumnFamilyName,
					HFactory.createColumn("password", "password"));
		}
		return mutator.execute();
	}

	public long insertBatchValue(int count, int batch) {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);
		keyspaceOperator
				.setConsistencyLevelPolicy(new AllOneConsistencyLevelPolicy());
		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		long executeTime = 0;
		int c = 0;
		System.out.println("prepering to insert " + count + " values");
		for (int i = 0; i < count; i++) {
			c++;
			String uuid = UUID.randomUUID().toString();
			mutator.addInsertion(uuid, ColumnFamilyName,
					HFactory.createColumn("username", "wang"));
			mutator.addInsertion(uuid, ColumnFamilyName,
					HFactory.createColumn("password", "password"));
			if (c == batch) {
				System.out.println("c == batch " + c + " == " + batch);

				executeTime += mutator.execute().getExecutionTimeMicro();
				System.out.println(executeTime);
				c = 0;
			}
		}
		if (c != 0) {
			System.out.println("c= " + c);

			executeTime += mutator.execute().getExecutionTimeMicro();
		}
		return executeTime;
	}

	public static void main(String[] args) {
		CassandraDataTest cdt = new CassandraDataTest();
		int count = 500000;
		cdt.insertSingleValue(count);
		// System.out.println("insert 10000 value used time: "
		// + cdt.insertValue(10000).getExecutionTimeMicro());

		// System.out.println("batch insert :"
		// + cdt.insertBatchValue(count, 10000));

	}

}
