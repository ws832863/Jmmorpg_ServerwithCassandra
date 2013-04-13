package game.cassandra.dao;

import game.cassandra.conn.CassandraConnection;
import game.cassandra.data.Classe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.log4j.Logger;

public class CassandraDAOClasse {

	private final static Logger logger = Logger
			.getLogger(CassandraDAOClasse.class.getName());

	private final static Cluster gameCluster = CassandraConnection.getCluster();

	private final static String KeySpaceName = CassandraConnection
			.getKeySpaceName();

	private final static StringSerializer stringSerializer = StringSerializer
			.get();

	private final static Keyspace keyspaceOperator = HFactory.createKeyspace(
			KeySpaceName, gameCluster);

	private final static String ColumnFamilyName = "classe";

	private Vector<Classe> vos;

	public Vector<Classe> getVos() {
		return vos;
	}

	public CassandraDAOClasse() {
		vos = new Vector<Classe>();
	}

	public void selectByPk(int rowKey) {
		vos.removeAllElements();

		SliceQuery<String, String, String> query = HFactory.createSliceQuery(
				keyspaceOperator, stringSerializer, stringSerializer,
				stringSerializer);
		String key = String.valueOf(rowKey);

		query.setKey(key).setColumnFamily(ColumnFamilyName);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(
				query, null, "\uFFFF", false);

		HashMap<String, String> tempResult = new HashMap<String, String>();

		while (iterator.hasNext()) {
			HColumn<String, String> c = iterator.next();
			tempResult.put(c.getName(), c.getValue());
		}

		vos.add(this.mappingHashMapIntoClasseObject(key, tempResult));

	}

	private Classe mappingHashMapIntoClasseObject(String key,
			HashMap<String, String> h) {

		Classe c = new Classe();
		c.setId(Integer.valueOf(key));
		c.setChaBase(Integer.valueOf(h.get("chabase")));
		c.setConBase(Integer.valueOf(h.get("conbase")));
		c.setDexBase(Integer.valueOf(h.get("dexbase")));
		c.setEvasionBase(Integer.valueOf(h.get("evasionbase")));
		c.setHpMaxBase(Integer.valueOf(h.get("hpmaxbase")));
		c.setInteBase(Integer.valueOf(h.get("intebase")));
		c.setManaMaxBase(Integer.valueOf(h.get("manamaxbase")));
		c.setNameClasse(h.get("nameclasse"));
		c.setRaceId(Integer.valueOf(h.get("raceid")));
		c.setResMagicBase(Integer.valueOf(h.get("resmagicbase")));
		c.setResPhysicalBase(Integer.valueOf(h.get("resphysicalbase")));
		c.setSexBase(h.get("sexbase"));
		c.setSpBase(Integer.valueOf(h.get("spbase")));
		c.setStaminaBase(Integer.valueOf(h.get("staminabase")));
		c.setStrBase(Integer.valueOf(h.get("strbase")));
		c.setWisBase(Integer.valueOf(h.get("wisbase")));
		return c;
	}

	public static void prepopulateClasseData() {
		ColumnFamilyTemplate<String, String> columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspaceOperator, ColumnFamilyName, stringSerializer,
				stringSerializer);

		Mutator<String> mutator = columnFamilyTemplate.createMutator();
		addInsseration(mutator, "1", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "F", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Mage");
		addInsseration(mutator, "2", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "F", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Monge");
		addInsseration(mutator, "3", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "M", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Assassin");
		addInsseration(mutator, "4", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "M", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Warrior");
		addInsseration(mutator, "5", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "F", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Warrior");
		addInsseration(mutator, "6", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "M", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Monge");
		addInsseration(mutator, "7", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "F", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Assassin");
		addInsseration(mutator, "8", "1", "0", "0", "0", "0", "no", "0", "0",
				"0", "5", "5", "M", "10", "10", "10", "10", "10", "10", "10",
				"100", "100", "Mage");
		mutator.execute();
	}

	private static void addInsseration(Mutator<String> mut, String rowkey,
			String Raceid, String FaceHeight, String FaceWidth,
			String FacePosY, String FacePosX, String FaceFileName,
			String SpBase, String StartRowSprite, String EvasionBase,
			String ResPhysicalBase, String ResMagicBase, String SexBase,
			String StaminaBase, String WisBase, String ChaBase, String ConBase,
			String InteBase, String DexBase, String StrBase,
			String ManaMaxBase, String HpMaxBase, String NameClasse

	) {

		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("raceid", Raceid));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("nameclasse", NameClasse));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("hpmaxbase", HpMaxBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("manamaxbase", ManaMaxBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("strbase", StrBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("dexbase", DexBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("intebase", InteBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("conbase", ConBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("chabase", ChaBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("wisbase", WisBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("staminabase", StaminaBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("sexBase", SexBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("resmagicbase", ResMagicBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("resphysicalbase", ResPhysicalBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("evasionbase", EvasionBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("startrowsprite", StartRowSprite));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("spbase", SpBase));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("facefilename", FaceFileName));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("faceposx", FacePosX));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("faceposy", FacePosY));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("facewidth", FaceWidth));
		mut.addInsertion(rowkey, ColumnFamilyName,
				HFactory.createColumn("faceheight", FaceHeight));
	}

	public static void createClasseSchema() {
		logger.info("====>Creating ClasseSchema in Cassandra");

		BasicColumnDefinition columnRace = new BasicColumnDefinition();
		columnRace.setName(stringSerializer.toByteBuffer("raceid"));
		columnRace.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colNameClasse = new BasicColumnDefinition();
		colNameClasse.setName(stringSerializer.toByteBuffer("nameclasse"));
		colNameClasse.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colHpMaxBase = new BasicColumnDefinition();
		colHpMaxBase.setName(stringSerializer.toByteBuffer("hpmaxbase"));
		colHpMaxBase
				.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colManaMaxBase = new BasicColumnDefinition();
		colManaMaxBase.setName(stringSerializer.toByteBuffer("manamaxbase"));
		colManaMaxBase.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colStrBase = new BasicColumnDefinition();
		colStrBase.setName(stringSerializer.toByteBuffer("strbase"));
		colStrBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colDexBase = new BasicColumnDefinition();
		colDexBase.setName(stringSerializer.toByteBuffer("dexbase"));
		colDexBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colInteBase = new BasicColumnDefinition();
		colInteBase.setName(stringSerializer.toByteBuffer("intebase"));
		colInteBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colConBase = new BasicColumnDefinition();
		colConBase.setName(stringSerializer.toByteBuffer("conbase"));
		colConBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colChaBase = new BasicColumnDefinition();
		colChaBase.setName(stringSerializer.toByteBuffer("chabase"));
		colChaBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colWisBase = new BasicColumnDefinition();
		colWisBase.setName(stringSerializer.toByteBuffer("wisbase"));
		colWisBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colStaminaBase = new BasicColumnDefinition();
		colStaminaBase.setName(stringSerializer.toByteBuffer("staminabase"));
		colStaminaBase.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colSexBase = new BasicColumnDefinition();
		colSexBase.setName(stringSerializer.toByteBuffer("sexBase"));
		colSexBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colResMagicBase = new BasicColumnDefinition();
		colResMagicBase.setName(stringSerializer.toByteBuffer("resmagicbase"));
		colResMagicBase.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colResPhysicalBase = new BasicColumnDefinition();
		colResPhysicalBase.setName(stringSerializer
				.toByteBuffer("resphysicalbase"));
		colResPhysicalBase.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colEvasionBase = new BasicColumnDefinition();
		colEvasionBase.setName(stringSerializer.toByteBuffer("evasionbase"));
		colEvasionBase.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colStartRowSprite = new BasicColumnDefinition();
		colStartRowSprite.setName(stringSerializer
				.toByteBuffer("startrowsprite"));
		colStartRowSprite.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colSpBase = new BasicColumnDefinition();
		colSpBase.setName(stringSerializer.toByteBuffer("spbase"));
		colSpBase.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colFaceFileName = new BasicColumnDefinition();
		colFaceFileName.setName(stringSerializer.toByteBuffer("facefilename"));
		colFaceFileName.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		BasicColumnDefinition colFacePosX = new BasicColumnDefinition();
		colFacePosX.setName(stringSerializer.toByteBuffer("faceposx"));
		colFacePosX.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colFacePosY = new BasicColumnDefinition();
		colFacePosY.setName(stringSerializer.toByteBuffer("faceposy"));
		colFacePosY.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colFaceWidth = new BasicColumnDefinition();
		colFaceWidth.setName(stringSerializer.toByteBuffer("facewidth"));
		colFaceWidth
				.setValidationClass(ComparatorType.BYTESTYPE.getClassName());

		BasicColumnDefinition colFaceHeight = new BasicColumnDefinition();
		colFaceHeight.setName(stringSerializer.toByteBuffer("faceheight"));
		colFaceHeight.setValidationClass(ComparatorType.BYTESTYPE
				.getClassName());

		// add all columnDefinition to columnfamily definition
		BasicColumnFamilyDefinition basicClasseDef = new BasicColumnFamilyDefinition();
		basicClasseDef.addColumnDefinition(columnRace);
		basicClasseDef.addColumnDefinition(colFaceHeight);
		basicClasseDef.addColumnDefinition(colFaceWidth);
		basicClasseDef.addColumnDefinition(colFacePosY);
		basicClasseDef.addColumnDefinition(colFacePosX);
		basicClasseDef.addColumnDefinition(colFaceFileName);
		basicClasseDef.addColumnDefinition(colSpBase);
		basicClasseDef.addColumnDefinition(colStartRowSprite);
		basicClasseDef.addColumnDefinition(colEvasionBase);
		basicClasseDef.addColumnDefinition(colResPhysicalBase);
		basicClasseDef.addColumnDefinition(colResMagicBase);
		basicClasseDef.addColumnDefinition(colSexBase);
		basicClasseDef.addColumnDefinition(colStaminaBase);
		basicClasseDef.addColumnDefinition(colWisBase);
		basicClasseDef.addColumnDefinition(colChaBase);
		basicClasseDef.addColumnDefinition(colConBase);
		basicClasseDef.addColumnDefinition(colInteBase);
		basicClasseDef.addColumnDefinition(colDexBase);
		basicClasseDef.addColumnDefinition(colStrBase);
		basicClasseDef.addColumnDefinition(colManaMaxBase);
		basicClasseDef.addColumnDefinition(colHpMaxBase);
		basicClasseDef.addColumnDefinition(colNameClasse);

		basicClasseDef.setName(ColumnFamilyName); // set a name for this
													// columnfamily
		basicClasseDef.setKeyspaceName(KeySpaceName);
		// set comparatortype of the columnfamily
		basicClasseDef.setComparatorType(ComparatorType.UTF8TYPE);

		ColumnFamilyDefinition CFClasseDef = new ThriftCfDef(basicClasseDef);
		// if the keyspace not exists, then create a keyspace
		KeyspaceDefinition keyspaceDefinition = HFactory
				.createKeyspaceDefinition(KeySpaceName,
						"org.apache.cassandra.locator.SimpleStrategy", 1,
						Arrays.asList(CFClasseDef));

		try {
			// if the keyspace exists
			if (gameCluster.describeKeyspace(KeySpaceName) != null) {
				// the columnfamily probaly exist, drop it
				gameCluster.dropColumnFamily(KeySpaceName, ColumnFamilyName,
						true);
			} else {
				gameCluster.addKeyspace(keyspaceDefinition);
			}

		} catch (HectorException he) {
			logger.warn("a error occured :" + he.toString());
			// he.printStackTrace();
		}
		gameCluster.addColumnFamily(CFClasseDef);// the new columnfamily into
													// cassandra
		logger.info(" ClasseSchema created<====");
	}

	public void testVectorClasses() {
		Classe vo;
		Iterator<Classe> it = vos.iterator();
		while (it.hasNext()) {
			vo = it.next();
			System.out.println(vo.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//CassandraDAOClasse.createClasseSchema();
		//CassandraDAOClasse.prepopulateClasseData();
		CassandraDAOClasse dao=new CassandraDAOClasse();
		dao.selectByPk(2);
		dao.testVectorClasses();
		
		
	}

}
