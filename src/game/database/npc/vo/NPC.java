package game.database.npc.vo;


/**
 * 
 * @author Michel Montenegro
 * 
 */
public class NPC {

	private int id;
	private int mapId;
	private int raceId;
	private int clanId;
	private int classeId;
	private String name;
	private int hpMax;
	private int hpCurr;
	private int manaMax;
	private int manaCurr;
	private int str;
	private int dex;
	private int inte;
	private int con;
	private int cha;
	private int wis;
	private int stamina;
	private String sex;
	private int resMagic;
	private int resPhysical;
	private int evasion;
	private int level;
	private String typeNPC;
	private String aggro;
	private int posXMap;
	private int posYMap;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getManaMax() {
		return manaMax;
	}

	public void setManaMax(int manaMax) {
		this.manaMax = manaMax;
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public int getDex() {
		return dex;
	}

	public void setDex(int dex) {
		this.dex = dex;
	}

	public int getInte() {
		return inte;
	}

	public void setInte(int inte) {
		this.inte = inte;
	}

	public int getCon() {
		return con;
	}

	public void setCon(int con) {
		this.con = con;
	}

	public int getCha() {
		return cha;
	}

	public void setCha(int cha) {
		this.cha = cha;
	}

	public int getWis() {
		return wis;
	}

	public void setWis(int wis) {
		this.wis = wis;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getResMagic() {
		return resMagic;
	}

	public void setResMagic(int resMagic) {
		this.resMagic = resMagic;
	}

	public int getResPhysical() {
		return resPhysical;
	}

	public void setResPhysical(int resPhysical) {
		this.resPhysical = resPhysical;
	}

	public int getEvasion() {
		return evasion;
	}

	public void setEvasion(int evasion) {
		this.evasion = evasion;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public int getClanId() {
		return clanId;
	}

	public void setClanId(int clanId) {
		this.clanId = clanId;
	}

	public int getClasseId() {
		return classeId;
	}

	public void setClasseId(int classeId) {
		this.classeId = classeId;
	}

	public int getHpCurr() {
		return hpCurr;
	}

	public void setHpCurr(int hpCurr) {
		this.hpCurr = hpCurr;
	}

	public int getManaCurr() {
		return manaCurr;
	}

	public void setManaCurr(int manaCurr) {
		this.manaCurr = manaCurr;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTypeNPC() {
		return typeNPC;
	}

	public void setTypeNPC(String typeNPC) {
		this.typeNPC = typeNPC;
	}

	public String getAggro() {
		return aggro;
	}

	public void setAggro(String aggro) {
		this.aggro = aggro;
	}

	public int getPosXMap() {
		return posXMap;
	}

	public void setPosXMap(int posXMap) {
		this.posXMap = posXMap;
	}

	public int getPosYMap() {
		return posYMap;
	}

	public void setPosYMap(int posYMap) {
		this.posYMap = posYMap;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " - Language: " + getName();
	}
}
