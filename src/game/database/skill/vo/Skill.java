package game.database.skill.vo;


/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Skill {

	private static final long serialVersionUID = 1L;

	  private int id;
	  private String name;
	  private int classeId;
	  private String typeAction;
	  private int damagePhysical;
	  private int damageMagic;
	  private int bonusArmorPhysical;
	  private int bonusArmorMagic ;
	  private int bonusDamagePhysical ;
	  private int bonusDamageMagic ;
	  private int bonusStr;
	  private int bonusDex;
	  private int bonusInte ;
	  private int bonusCon ;
	  private int manaLeft ;
	  private int bonusWis ;
	  private int bonusStamina ;
	  private int bonusEvasion ;
	  private int bonusSp;
	  private int bonusHp ;
	  private int bonusMana ;
	  private int bonusCha ;
	  private String fileSpriteName;
	  private int colSprite;
	  private int rowSprite;

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

	public int getClasseId() {
		return classeId;
	}

	public void setClasseId(int classeId) {
		this.classeId = classeId;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public int getDamagePhysical() {
		return damagePhysical;
	}

	public void setDamagePhysical(int damagePhysical) {
		this.damagePhysical = damagePhysical;
	}

	public int getDamageMagic() {
		return damageMagic;
	}

	public void setDamageMagic(int damageMagic) {
		this.damageMagic = damageMagic;
	}

	public int getBonusArmorPhysical() {
		return bonusArmorPhysical;
	}

	public void setBonusArmorPhysical(int bonusArmorPhysical) {
		this.bonusArmorPhysical = bonusArmorPhysical;
	}

	public int getBonusArmorMagic() {
		return bonusArmorMagic;
	}

	public void setBonusArmorMagic(int bonusArmorMagic) {
		this.bonusArmorMagic = bonusArmorMagic;
	}

	public int getBonusDamagePhysical() {
		return bonusDamagePhysical;
	}

	public void setBonusDamagePhysical(int bonusDamagePhysical) {
		this.bonusDamagePhysical = bonusDamagePhysical;
	}

	public int getBonusDamageMagic() {
		return bonusDamageMagic;
	}

	public void setBonusDamageMagic(int bonusDamageMagic) {
		this.bonusDamageMagic = bonusDamageMagic;
	}

	public int getBonusStr() {
		return bonusStr;
	}

	public void setBonusStr(int bonusStr) {
		this.bonusStr = bonusStr;
	}

	public int getBonusDex() {
		return bonusDex;
	}

	public void setBonusDex(int bonusDex) {
		this.bonusDex = bonusDex;
	}

	public int getBonusInte() {
		return bonusInte;
	}

	public void setBonusInte(int bonusInte) {
		this.bonusInte = bonusInte;
	}

	public int getBonusCon() {
		return bonusCon;
	}

	public void setBonusCon(int bonusCon) {
		this.bonusCon = bonusCon;
	}

	public int getManaLeft() {
		return manaLeft;
	}

	public void setManaLeft(int manaLeft) {
		this.manaLeft = manaLeft;
	}

	public int getBonusWis() {
		return bonusWis;
	}

	public void setBonusWis(int bonusWis) {
		this.bonusWis = bonusWis;
	}

	public int getBonusStamina() {
		return bonusStamina;
	}

	public void setBonusStamina(int bonusStamina) {
		this.bonusStamina = bonusStamina;
	}

	public int getBonusEvasion() {
		return bonusEvasion;
	}

	public void setBonusEvasion(int bonusEvasion) {
		this.bonusEvasion = bonusEvasion;
	}

	public int getBonusSp() {
		return bonusSp;
	}

	public void setBonusSp(int bonusSp) {
		this.bonusSp = bonusSp;
	}

	public int getBonusHp() {
		return bonusHp;
	}

	public void setBonusHp(int bonusHp) {
		this.bonusHp = bonusHp;
	}

	public int getBonusMana() {
		return bonusMana;
	}

	public void setBonusMana(int bonusMana) {
		this.bonusMana = bonusMana;
	}

	public int getBonusCha() {
		return bonusCha;
	}

	public void setBonusCha(int bonusCha) {
		this.bonusCha = bonusCha;
	}

	public String getFileSpriteName() {
		return fileSpriteName;
	}

	public void setFileSpriteName(String fileSpriteName) {
		this.fileSpriteName = fileSpriteName;
	}

	public int getColSprite() {
		return colSprite;
	}

	public void setColSprite(int colSprite) {
		this.colSprite = colSprite;
	}

	public int getRowSprite() {
		return rowSprite;
	}

	public void setRowSprite(int rowSprite) {
		this.rowSprite = rowSprite;
	}

	@Override
	public String toString() {
		return "Id: " + getId() + " - Name: " + getName();
	}
}
