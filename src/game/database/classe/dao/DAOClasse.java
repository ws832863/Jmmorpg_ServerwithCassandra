//package game.database.classe.dao;
//
//import game.database.DbConect;
//import game.database.classe.vo.Classe;
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
//public class DAOClasse {
//
//
//	private Vector<Classe> vos;
//	public Vector<Classe> getVos() {
//		return vos;
//	}
//
//	public DAOClasse() {
//		vos = new Vector<Classe>();
//	}
//
//	public void selectAll() throws Exception {
//		Classe vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm
//				.executeQuery("SELECT id, Race_id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, "
//						+ "conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase "
//						+ " FROM Classe ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Classe();
//			vo.setId(rs.getInt("id"));
//			vo.setRaceId(rs.getInt("Race_id"));
//			vo.setNameClasse(rs.getString("nameClasse"));
//			vo.setHpMaxBase(rs.getInt("hpMaxBase"));
//			vo.setManaMaxBase(rs.getInt("manaMaxBase"));
//			vo.setStrBase(rs.getInt("strBase"));
//			vo.setDexBase(rs.getInt("dexBase"));
//			vo.setInteBase(rs.getInt("inteBase"));
//			vo.setConBase(rs.getInt("conBase"));
//			vo.setChaBase(rs.getInt("chaBase"));
//			vo.setWisBase(rs.getInt("wisBase"));
//			vo.setStaminaBase(rs.getInt("staminaBase"));
//			vo.setSexBase(rs.getString("sexBase"));
//			vo.setResMagicBase(rs.getInt("resMagicBase"));
//			vo.setResPhysicalBase(rs.getInt("resPhysicalBase"));
//			vo.setEvasionBase(rs.getInt("evasionBase"));
//		
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Classe vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql
//				.append(" id, Race_id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, ");
//		sql
//				.append(" wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase ");
//		sql.append(" from Classe where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Classe();
//			vo.setId(rs.getInt("id"));
//			vo.setRaceId(rs.getInt("Race_id"));
//			vo.setNameClasse(rs.getString("nameClasse"));
//			vo.setHpMaxBase(rs.getInt("hpMaxBase"));
//			vo.setManaMaxBase(rs.getInt("manaMaxBase"));
//			vo.setStrBase(rs.getInt("strBase"));
//			vo.setDexBase(rs.getInt("dexBase"));
//			vo.setInteBase(rs.getInt("inteBase"));
//			vo.setConBase(rs.getInt("conBase"));
//			vo.setChaBase(rs.getInt("chaBase"));
//			vo.setWisBase(rs.getInt("wisBase"));
//			vo.setStaminaBase(rs.getInt("staminaBase"));
//			vo.setSexBase(rs.getString("sexBase"));
//			vo.setResMagicBase(rs.getInt("resMagicBase"));
//			vo.setResPhysicalBase(rs.getInt("resPhysicalBase"));
//			vo.setEvasionBase(rs.getInt("evasionBase"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String nameClasse, int hpMaxBase, int manaMaxBase,
//			int strBase, int dexBase, int inteBase, int conBase, int chaBase,
//			int wisBase, int staminaBase, String sexBase, int resMagicBase,
//			int resPhysicalBase, int evasionBase, int raceId ) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Classe ");
//		sql
//				.append(" ( nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, ");
//		sql
//				.append("  wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, ");
//		sql
//				.append(" Race_id ) ");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//
//		stmt.setString(1, nameClasse);
//		stmt.setInt(2, hpMaxBase);
//		stmt.setInt(3, manaMaxBase);
//		stmt.setInt(4, strBase);
//		stmt.setInt(5, dexBase);
//		stmt.setInt(6, inteBase);
//		stmt.setInt(7, conBase);
//		stmt.setInt(8, chaBase);
//		stmt.setInt(9, wisBase);
//		stmt.setInt(10, staminaBase);
//		stmt.setString(11, sexBase);
//		stmt.setInt(12, resMagicBase);
//		stmt.setInt(13, resPhysicalBase);
//		stmt.setInt(14, evasionBase);
//		stmt.setInt(20, raceId);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String nameClasse, int hpMaxBase,
//			int manaMaxBase, int strBase, int dexBase, int inteBase,
//			int conBase, int chaBase, int wisBase, int staminaBase,
//			String sexBase, int resMagicBase, int resPhysicalBase,
//			int evasionBase, int raceId)
//			throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Classe set ");
//		sql
//				.append(" nameClasse = ?, hpMaxBase = ?, manaMaxBase = ?, strBase = ?, dexBase = ?, inteBase = ?, ");
//		sql
//				.append(" conBase = ?, chaBase = ?, wisBase = ?, staminaBase = ?, sexBase = ?, resMagicBase = ?, ");
//		sql
//				.append(" resPhysicalBase = ?, evasionBase = ? ");
//		sql.append(" Race_id = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, nameClasse);
//		stmt.setInt(2, hpMaxBase);
//		stmt.setInt(3, manaMaxBase);
//		stmt.setInt(4, strBase);
//		stmt.setInt(5, dexBase);
//		stmt.setInt(6, inteBase);
//		stmt.setInt(7, conBase);
//		stmt.setInt(8, chaBase);
//		stmt.setInt(9, wisBase);
//		stmt.setInt(10, staminaBase);
//		stmt.setString(11, sexBase);
//		stmt.setInt(12, resMagicBase);
//		stmt.setInt(13, resPhysicalBase);
//		stmt.setInt(14, evasionBase);
//		stmt.setInt(20, raceId);
//
//		stmt.setInt(26, id);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorClasses() {
//		Classe vo;
//		Iterator<Classe> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
