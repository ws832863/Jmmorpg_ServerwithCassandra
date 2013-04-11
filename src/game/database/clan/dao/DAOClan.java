//package game.database.clan.dao;
//
//import game.database.DbConect;
//import game.database.clan.vo.Clan;
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
//public class DAOClan {
//
//	private Vector<Clan> vos;
//	public Vector<Clan> getVos() {
//		return vos;
//	}
//
//	public DAOClan() {
//		vos = new Vector<Clan>();
//	}
//
//	public void selectAll() throws Exception {
//		Clan vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery("SELECT id, name FROM clan ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Clan();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vos.add(vo);
//		}
//		vo = null;
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Clan vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, name ");
//		sql.append(" from map where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Clan();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Clan ");
//		sql.append(" (name) ");
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
//	public void updateVo(int id, String name) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Clan set ");
//		sql.append(" name = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(2, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//	
//	public void testVectorClans() {
//		Clan vo;
//		Iterator<Clan> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
