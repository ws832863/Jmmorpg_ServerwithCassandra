package game.database.classe.vo;


/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Classe {

	private int id;
	private String nameClasse;
	private int hpMaxBase;
	private int manaMaxBase;
	private int strBase;
	private int dexBase;
	private int inteBase;
	private int conBase;
	private int chaBase;
	private int wisBase;
	private int staminaBase;
	private String sexBase;
	private int resMagicBase;
	private int resPhysicalBase;
	private int evasionBase;
	private int spBase;
	private int raceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameClasse() {
		return nameClasse;
	}

	public void setNameClasse(String nameClasse) {
		this.nameClasse = nameClasse;
	}

	public int getHpMaxBase() {
		return hpMaxBase;
	}

	public void setHpMaxBase(int hpMaxBase) {
		this.hpMaxBase = hpMaxBase;
	}

	public int getManaMaxBase() {
		return manaMaxBase;
	}

	public void setManaMaxBase(int manaMaxBase) {
		this.manaMaxBase = manaMaxBase;
	}

	public int getStrBase() {
		return strBase;
	}

	public void setStrBase(int strBase) {
		this.strBase = strBase;
	}

	public int getDexBase() {
		return dexBase;
	}

	public void setDexBase(int dexBase) {
		this.dexBase = dexBase;
	}

	public int getInteBase() {
		return inteBase;
	}

	public void setInteBase(int inteBase) {
		this.inteBase = inteBase;
	}

	public int getConBase() {
		return conBase;
	}

	public void setConBase(int conBase) {
		this.conBase = conBase;
	}

	public int getChaBase() {
		return chaBase;
	}

	public void setChaBase(int chaBase) {
		this.chaBase = chaBase;
	}

	public int getWisBase() {
		return wisBase;
	}

	public void setWisBase(int wisBase) {
		this.wisBase = wisBase;
	}

	public int getStaminaBase() {
		return staminaBase;
	}

	public void setStaminaBase(int staminaBase) {
		this.staminaBase = staminaBase;
	}

	public String getSexBase() {
		return sexBase;
	}

	public void setSexBase(String sexBase) {
		this.sexBase = sexBase;
	}

	public int getResMagicBase() {
		return resMagicBase;
	}

	public void setResMagicBase(int resMagicBase) {
		this.resMagicBase = resMagicBase;
	}

	public int getResPhysicalBase() {
		return resPhysicalBase;
	}

	public void setResPhysicalBase(int resPhysicalBase) {
		this.resPhysicalBase = resPhysicalBase;
	}

	public int getEvasionBase() {
		return evasionBase;
	}

	public void setEvasionBase(int evasionBase) {
		this.evasionBase = evasionBase;
	}

	public int getSpBase() {
		return spBase;
	}

	public void setSpBase(int spBase) {
		this.spBase = spBase;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " - Language: " + getNameClasse();
	}

}
