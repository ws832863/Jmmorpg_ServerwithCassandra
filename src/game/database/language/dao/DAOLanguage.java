//package game.database.language.dao;
//
//import game.database.DbConect;
//import game.database.language.vo.Language;
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
//public class DAOLanguage {
//
//	private Vector<Language> vos;
//	public Vector<Language> getVos() {
//		return vos;
//	}
//
//	public DAOLanguage() {
//		vos = new Vector<Language>();
//	}
//
//	public void selectAll() throws Exception {
//		Language vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery("SELECT id, language FROM Language");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Language();
//			vo.setId(rs.getInt("id"));
//			vo.setLanguage(rs.getString("language"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Language vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, language ");
//		sql.append(" from Language where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Language();
//			vo.setId(rs.getInt("id"));
//			vo.setLanguage(rs.getString("language"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Language ");
//		sql.append(" (language) ");
//		sql.append(" VALUES ");
//		sql.append("(?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String language) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Language set ");
//		sql.append(" language = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, language);
//		stmt.setInt(2, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorLanguages() {
//		Language vo;
//		Iterator<Language> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
