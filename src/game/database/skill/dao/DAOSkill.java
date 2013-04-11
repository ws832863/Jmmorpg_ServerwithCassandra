//package game.database.skill.dao;
//
//import game.database.DbConect;
//import game.database.skill.vo.Skill;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Iterator;
//import java.util.Vector;
//
///**
// * 
// * @author Michel Montenegro
// * 
// */
//public class DAOSkill {
//
//	private Vector<Skill> vos;
//	public Vector<Skill> getVos() {
//		return vos;
//	}
//
//	public DAOSkill() {
//		vos = new Vector<Skill>();
//	}
//
//	public void selectAll() throws Exception {
//		Skill vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery("SELECT id, Classe_id, name, typeAction, damagePhysical, damageMagic, bonusArmorPhysical, bonusArmorMagic," 
//	  + " bonusDamagePhysical, bonusDamageMagic, bonusStr, bonusDex, bonusInte, bonusCon, manaLeft, bonusWis," 
//	  + " bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha, fileSpriteName, colSprite," 
//	  + " rowSprite FROM Skill ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Skill();
//			vo.setId(rs.getInt("id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setName(rs.getString("name"));
//			vo.setTypeAction(rs.getString("typeAction"));
//			vo.setDamagePhysical(rs.getInt("damagePhysical"));
//			vo.setDamageMagic(rs.getInt("damageMagic"));
//			vo.setBonusArmorPhysical(rs.getInt("bonusArmorPhysical"));
//			vo.setBonusArmorMagic(rs.getInt("bonusArmorMagic"));
//			vo.setBonusDamagePhysical(rs.getInt("bonusDamagePhysical"));
//			vo.setBonusDamageMagic(rs.getInt("bonusDamageMagic"));
//			vo.setBonusStr(rs.getInt("bonusStr"));
//			vo.setBonusDex(rs.getInt("bonusDex"));
//			vo.setBonusInte(rs.getInt("bonusInte"));
//			vo.setBonusCon(rs.getInt("bonusCon"));
//			vo.setManaLeft(rs.getInt("manaLeft"));
//			vo.setBonusWis(rs.getInt("bonusWis"));
//			vo.setBonusStamina(rs.getInt("bonusStamina"));
//			vo.setBonusEvasion(rs.getInt("bonusEvasion"));
//			vo.setBonusSp(rs.getInt("bonusSp"));
//			vo.setBonusHp(rs.getInt("bonusHp"));
//			vo.setBonusMana(rs.getInt("bonusMana"));
//			vo.setBonusCha(rs.getInt("bonusCha"));
//			vo.setFileSpriteName(rs.getString("fileSpriteName"));
//			vo.setColSprite(rs.getInt("colSprite"));
//			vo.setRowSprite(rs.getInt("rowSprite"));
//			vos.add(vo);
//		}
//		vo = null;
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Skill vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, Classe_id, name, typeAction, damagePhysical, damageMagic, bonusArmorPhysical, bonusArmorMagic," 
//	  + " bonusDamagePhysical, bonusDamageMagic, bonusStr, bonusDex, bonusInte, bonusCon, manaLeft, bonusWis," 
//	  + " bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha, fileSpriteName, colSprite," 
//	  + " rowSprite ");
//		sql.append(" from Skill where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Skill();
//			vo.setId(rs.getInt("id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setName(rs.getString("name"));
//			vo.setTypeAction(rs.getString("typeAction"));
//			vo.setDamagePhysical(rs.getInt("damagePhysical"));
//			vo.setDamageMagic(rs.getInt("damageMagic"));
//			vo.setBonusArmorPhysical(rs.getInt("bonusArmorPhysical"));
//			vo.setBonusArmorMagic(rs.getInt("bonusArmorMagic"));
//			vo.setBonusDamagePhysical(rs.getInt("bonusDamagePhysical"));
//			vo.setBonusDamageMagic(rs.getInt("bonusDamageMagic"));
//			vo.setBonusStr(rs.getInt("bonusStr"));
//			vo.setBonusDex(rs.getInt("bonusDex"));
//			vo.setBonusInte(rs.getInt("bonusInte"));
//			vo.setBonusCon(rs.getInt("bonusCon"));
//			vo.setManaLeft(rs.getInt("manaLeft"));
//			vo.setBonusWis(rs.getInt("bonusWis"));
//			vo.setBonusStamina(rs.getInt("bonusStamina"));
//			vo.setBonusEvasion(rs.getInt("bonusEvasion"));
//			vo.setBonusSp(rs.getInt("bonusSp"));
//			vo.setBonusHp(rs.getInt("bonusHp"));
//			vo.setBonusMana(rs.getInt("bonusMana"));
//			vo.setBonusCha(rs.getInt("bonusCha"));
//			vo.setFileSpriteName(rs.getString("fileSpriteName"));
//			vo.setColSprite(rs.getInt("colSprite"));
//			vo.setRowSprite(rs.getInt("rowSprite"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name, int classeId, String typeAction, int damagePhysical, int damageMagic, 
//			  int bonusArmorPhysical, int bonusArmorMagic, int bonusDamagePhysical, int bonusDamageMagic, 
//			  int bonusStr, int bonusDex, int bonusInte, int bonusCon, int manaLeft, int bonusWis, int bonusStamina, 
//			  int bonusEvasion, int bonusSp, int bonusHp, int bonusMana, int bonusCha, String fileSpriteName, 
//			  int colSprite, int rowSprite) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Skill ");
//		sql.append(" (Classe_id, name, typeAction, damagePhysical, damageMagic, bonusArmorPhysical, bonusArmorMagic," 
//	  + " bonusDamagePhysical, bonusDamageMagic, bonusStr, bonusDex, bonusInte, bonusCon, manaLeft, bonusWis," 
//	  + " bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha, fileSpriteName, colSprite," 
//	  + " rowSprite)");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setInt(1, classeId);
//		stmt.setString(2, name);
//		stmt.setString(3, typeAction);
//		stmt.setInt(4, damagePhysical);
//		stmt.setInt(5, damageMagic);
//		stmt.setInt(6, bonusArmorPhysical);
//		stmt.setInt(7, bonusArmorMagic);
//		stmt.setInt(8, bonusDamagePhysical);
//		stmt.setInt(9, bonusDamageMagic);
//		stmt.setInt(10, bonusStr);
//		stmt.setInt(11, bonusDex);
//		stmt.setInt(12, bonusInte);
//		stmt.setInt(13, bonusCon);
//		stmt.setInt(14, manaLeft);
//		stmt.setInt(15, bonusWis);
//		stmt.setInt(16, bonusStamina);
//		stmt.setInt(17, bonusEvasion);
//		stmt.setInt(18, bonusSp);
//		stmt.setInt(14, bonusHp);
//		stmt.setInt(15, bonusMana);
//		stmt.setInt(16, bonusCha);
//		stmt.setString(17, fileSpriteName);
//		stmt.setInt(18, colSprite);
//		stmt.setInt(19, rowSprite);
//		
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String name, int classeId, String typeAction, int damagePhysical, int damageMagic, 
//			  int bonusArmorPhysical, int bonusArmorMagic, int bonusDamagePhysical, int bonusDamageMagic, 
//			  int bonusStr, int bonusDex, int bonusInte, int bonusCon, int manaLeft, int bonusWis, int bonusStamina, 
//			  int bonusEvasion, int bonusSp, int bonusHp, int bonusMana, int bonusCha, String fileSpriteName, 
//			  int colSprite, int rowSprite) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Skill set ");
//		sql.append(" Classe_id = ?, name = ?, typeAction = ?, damagePhysical = ?, damageMagic = ?, bonusArmorPhysical = ?, bonusArmorMagic = ?," 
//	  + " bonusDamagePhysical = ?, bonusDamageMagic = ?, bonusStr = ?, bonusDex = ?, bonusInte = ?, bonusCon = ?, manaLeft = ?, bonusWis = ?," 
//	  + " bonusStamina = ?, bonusEvasion = ?, bonusSp = ?, bonusHp = ?, bonusMana = ?, bonusCha = ?, fileSpriteName = ?, colSprite = ?," 
//	  + " rowSprite = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(2, classeId);
//		stmt.setString(3, typeAction);
//		stmt.setInt(4, damagePhysical);
//		stmt.setInt(5, damageMagic);
//		stmt.setInt(6, bonusArmorPhysical);
//		stmt.setInt(7, bonusArmorMagic);
//		stmt.setInt(8, bonusDamagePhysical);
//		stmt.setInt(9, bonusDamageMagic);
//		stmt.setInt(10, bonusStr);
//		stmt.setInt(11, bonusDex);
//		stmt.setInt(12, bonusInte);
//		stmt.setInt(13, bonusCon);
//		stmt.setInt(14, manaLeft);
//		stmt.setInt(15, bonusWis);
//		stmt.setInt(16, bonusStamina);
//		stmt.setInt(17, bonusEvasion);
//		stmt.setInt(18, bonusSp);
//		stmt.setInt(14, bonusHp);
//		stmt.setInt(15, bonusMana);
//		stmt.setInt(16, bonusCha);
//		stmt.setString(17, fileSpriteName);
//		stmt.setInt(18, colSprite);
//		stmt.setInt(19, rowSprite);
//		stmt.setInt(20, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//	
//	public void testVectorClans() {
//		Skill vo;
//		Iterator<Skill> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
