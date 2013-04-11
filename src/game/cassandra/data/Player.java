//package game.cassandra.data;
//
//import java.util.UUID;
//
//import game.core.CoreManagedObjects;
//
///**
// * 
// * @author Michel Montenegro
// * 
// */
//public class Player extends CoreManagedObjects {
//
//	private static final long serialVersionUID = -1818861658962193220L;
//
//	private int id;
//	private String name;
//
//	private UUID loginId;
//	private int mapId;
//	private int classeId;
//
//	private int hpMax;
//	private int hpCurr;
//	private int manaMax;
//	private int manaCurr;
//	private int expMax;
//	private int expCurr;
//	private int sp;
//	private int str;
//	private int dex;
//	private int inte;
//	private int con;
//	private int cha;
//	private int wis;
//	private int stamina;
//	private String sex;
//	private int resMagic;
//	private int resPhysical;
//	private int evasion;
//	private String dateCreate;
//	private String onLine;
//	private String lastAcess;
//	private int sector;
//
//	public Player() {
//		super("", "");
//	}
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//		setObjectName(name);
//		setObjectDescription("Objeto que representa o Player");
//	}
//
//	public int getHpMax() {
//		return hpMax;
//	}
//
//	public void setHpMax(int hpMax) {
//		this.hpMax = hpMax;
//	}
//
//	public int getManaMax() {
//		return manaMax;
//	}
//
//	public void setManaMax(int manaMax) {
//		this.manaMax = manaMax;
//	}
//
//	public int getStr() {
//		return str;
//	}
//
//	public void setStr(int str) {
//		this.str = str;
//	}
//
//	public int getDex() {
//		return dex;
//	}
//
//	public void setDex(int dex) {
//		this.dex = dex;
//	}
//
//	public int getInte() {
//		return inte;
//	}
//
//	public void setInte(int inte) {
//		this.inte = inte;
//	}
//
//	public int getCon() {
//		return con;
//	}
//
//	public void setCon(int con) {
//		this.con = con;
//	}
//
//	public int getCha() {
//		return cha;
//	}
//
//	public void setCha(int cha) {
//		this.cha = cha;
//	}
//
//	public int getWis() {
//		return wis;
//	}
//
//	public void setWis(int wis) {
//		this.wis = wis;
//	}
//
//	public int getStamina() {
//		return stamina;
//	}
//
//	public void setStamina(int stamina) {
//		this.stamina = stamina;
//	}
//
//	public String getSex() {
//		return sex;
//	}
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}
//
//	public int getResMagic() {
//		return resMagic;
//	}
//
//	public void setResMagic(int resMagic) {
//		this.resMagic = resMagic;
//	}
//
//	public int getResPhysical() {
//		return resPhysical;
//	}
//
//	public void setResPhysical(int resPhysical) {
//		this.resPhysical = resPhysical;
//	}
//
//	public int getEvasion() {
//		return evasion;
//	}
//
//	public void setEvasion(int evasion) {
//		this.evasion = evasion;
//	}
//
//	public UUID getLoginId() {
//		return loginId;
//	}
//
//	public void setLoginId(UUID loginId) {
//		this.loginId = loginId;
//	}
//
//	public int getMapId() {
//		return mapId;
//	}
//
//	public void setMapId(int mapId) {
//		this.mapId = mapId;
//	}
//
//	public int getClasseId() {
//		return classeId;
//	}
//
//	public void setClasseId(int classeId) {
//		this.classeId = classeId;
//	}
//
//	public int getHpCurr() {
//		return hpCurr;
//	}
//
//	public void setHpCurr(int hpCurr) {
//		this.hpCurr = hpCurr;
//	}
//
//	public int getManaCurr() {
//		return manaCurr;
//	}
//
//	public void setManaCurr(int manaCurr) {
//		this.manaCurr = manaCurr;
//	}
//
//	public int getSp() {
//		return sp;
//	}
//
//	public void setSp(int sp) {
//		this.sp = sp;
//	}
//
//	public int getExpMax() {
//		return expMax;
//	}
//
//	public void setExpMax(int expMax) {
//		this.expMax = expMax;
//	}
//
//	public int getExpCurr() {
//		return expCurr;
//	}
//
//	public void setExpCurr(int expCurr) {
//		this.expCurr = expCurr;
//	}
//
//	public String getDateCreate() {
//		return dateCreate;
//	}
//
//	public void setDateCreate(String dateCreate) {
//		this.dateCreate = dateCreate;
//	}
//
//	public String getOnLine() {
//		return onLine;
//	}
//
//	public void setOnLine(String onLine) {
//		this.onLine = onLine;
//	}
//
//	public String getLastAcess() {
//		return lastAcess;
//	}
//
//	public void setLastAcess(String lastAcess) {
//		this.lastAcess = lastAcess;
//	}
//
//	public int getSector() {
//		return sector;
//	}
//
//	public void setSector(int sector) {
//		this.sector = sector;
//	}
//
//}
