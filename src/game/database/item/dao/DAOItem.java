//package game.database.item.dao;
//
//import game.database.DbConect;
//import game.database.item.vo.Item;
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
//public class DAOItem {
//
//	private Vector<Item> vos;
//	public Vector<Item> getVos() {
//		return vos;
//	}
//
//	public DAOItem() {
//		vos = new Vector<Item>();
//	}
//	
//	public void selectAll() throws Exception {
//		Item vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery(" SELECT id, name, stackable, bonusDamagePhysical, bonusDamageMagic, bonusArmorPhysical, bonusArmorMagic, bonusStr,"  
//				+ " bonusDex, bonusInte, bonusCon, manaLeft, bonusWis, bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha FROM Item ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Item();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vo.setStackable(rs.getInt("stackable"));
//			vo.setBonusDamagePhysical(rs.getInt("bonusDamagePhysical"));
//			vo.setBonusDamageMagic(rs.getInt("bonusDamageMagic"));
//			vo.setBonusArmorPhysical(rs.getInt("bonusArmorPhysical"));
//			vo.setBonusArmorMagic(rs.getInt("bonusArmorMagic"));
//			vo.setBonusStr(rs.getInt("bonusStr"));
//			vo.setStackable(rs.getInt("bonusDex"));
//			vo.setStackable(rs.getInt("bonusInte"));
//			vo.setStackable(rs.getInt("bonusCon"));
//			vo.setStackable(rs.getInt("manaLeft"));
//			vo.setStackable(rs.getInt("bonusWis"));
//			vo.setStackable(rs.getInt("bonusStamina"));
//			vo.setStackable(rs.getInt("bonusEvasion"));
//			vo.setStackable(rs.getInt("bonusSp"));
//			vo.setStackable(rs.getInt("bonusHp"));
//			vo.setStackable(rs.getInt("bonusMana"));
//			vo.setStackable(rs.getInt("bonusCha"));
//			
//			vos.add(vo);
//		}
//		vo = null;
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Item vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, name, stackable, bonusDamagePhysical, bonusDamageMagic, bonusArmorPhysical, bonusArmorMagic, bonusStr, " 
//				+ " bonusDex, bonusInte, bonusCon, manaLeft, bonusWis, bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha ");
//		sql.append(" from Item where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Item();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vo.setStackable(rs.getInt("stackable"));
//			vo.setBonusDamagePhysical(rs.getInt("bonusDamagePhysical"));
//			vo.setBonusDamageMagic(rs.getInt("bonusDamageMagic"));
//			vo.setBonusArmorPhysical(rs.getInt("bonusArmorPhysical"));
//			vo.setBonusArmorMagic(rs.getInt("bonusArmorMagic"));
//			vo.setBonusStr(rs.getInt("bonusStr"));
//			vo.setStackable(rs.getInt("bonusDex"));
//			vo.setStackable(rs.getInt("bonusInte"));
//			vo.setStackable(rs.getInt("bonusCon"));
//			vo.setStackable(rs.getInt("manaLeft"));
//			vo.setStackable(rs.getInt("bonusWis"));
//			vo.setStackable(rs.getInt("bonusStamina"));
//			vo.setStackable(rs.getInt("bonusEvasion"));
//			vo.setStackable(rs.getInt("bonusSp"));
//			vo.setStackable(rs.getInt("bonusHp"));
//			vo.setStackable(rs.getInt("bonusMana"));
//			vo.setStackable(rs.getInt("bonusCha"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name, int stackable, int bonusDamagePhysical, int bonusDamageMagic, int bonusArmorPhysical, 
//			int bonusArmorMagic, int bonusStr, int bonusDex, int bonusInte, int bonusCon, int manaLeft, int bonusWis, 
//			int bonusStamina, int bonusEvasion, int bonusSp, int bonusHp, int bonusMana, int bonusCha) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Item ");
//		sql.append(" (name, stackable, bonusDamagePhysical, bonusDamageMagic, bonusArmorPhysical, bonusArmorMagic, bonusStr, " 
//				+ " bonusDex, bonusInte, bonusCon, manaLeft, bonusWis, bonusStamina, bonusEvasion, bonusSp, bonusHp, bonusMana, bonusCha) ");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(1, stackable);
//		stmt.setInt(2, bonusDamagePhysical);
//		stmt.setInt(3, bonusDamageMagic);
//		stmt.setInt(4, bonusArmorPhysical);
//		stmt.setInt(5, bonusArmorMagic);
//		stmt.setInt(6, bonusStr);
//		stmt.setInt(7, bonusDex);
//		stmt.setInt(8, bonusInte);
//		stmt.setInt(9, bonusCon);
//		stmt.setInt(10, manaLeft);
//		stmt.setInt(11, bonusWis);
//		stmt.setInt(12, bonusStamina);
//		stmt.setInt(13, bonusEvasion);
//		stmt.setInt(14, bonusSp);
//		stmt.setInt(15, bonusHp);
//		stmt.setInt(16, bonusMana);
//		stmt.setInt(17, bonusCha);		
//		
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String name, int stackable, int bonusDamagePhysical, int bonusDamageMagic, int bonusArmorPhysical, 
//			int bonusArmorMagic, int bonusStr, int bonusDex, int bonusInte, int bonusCon, int manaLeft, int bonusWis, 
//			int bonusStamina, int bonusEvasion, int bonusSp, int bonusHp, int bonusMana, int bonusCha) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Item set ");
//		sql.append(" name = ?, stackable = ?, bonusDamagePhysical = ?, bonusDamageMagic = ?, bonusArmorPhysical = ?, bonusArmorMagic = ?, bonusStr = ?, " 
//				+ " bonusDex = ?, bonusInte = ?, bonusCon = ?, manaLeft = ?, bonusWis = ?, bonusStamina = ?, bonusEvasion = ?, bonusSp = ?, bonusHp = ?, bonusMana = ?, bonusCha = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(1, stackable);
//		stmt.setInt(2, bonusDamagePhysical);
//		stmt.setInt(3, bonusDamageMagic);
//		stmt.setInt(4, bonusArmorPhysical);
//		stmt.setInt(5, bonusArmorMagic);
//		stmt.setInt(6, bonusStr);
//		stmt.setInt(7, bonusDex);
//		stmt.setInt(8, bonusInte);
//		stmt.setInt(9, bonusCon);
//		stmt.setInt(10, manaLeft);
//		stmt.setInt(11, bonusWis);
//		stmt.setInt(12, bonusStamina);
//		stmt.setInt(13, bonusEvasion);
//		stmt.setInt(14, bonusSp);
//		stmt.setInt(15, bonusHp);
//		stmt.setInt(16, bonusMana);
//		stmt.setInt(17, bonusCha);
//		stmt.setInt(18, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//	
//	public void testVectorClans() {
//		Item vo;
//		Iterator<Item> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
