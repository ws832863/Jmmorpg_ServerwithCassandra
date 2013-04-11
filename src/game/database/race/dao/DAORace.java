//package game.database.race.dao;
//
//import game.database.DbConect;
//import game.database.race.vo.Race;
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
//public class DAORace {
//
//	private Vector<Race> vos;
//	public Vector<Race> getVos() {
//		return vos;
//	}
//
//	public DAORace() {
//		vos = new Vector<Race>();
//	}
//
//	public void selectAll() throws Exception {
//		Race vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery("SELECT id, race FROM race");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Race();
//			vo.setId(rs.getInt("id"));
//			vo.setRace(rs.getString("race"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Race vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, race ");
//		sql.append(" from race where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Race();
//			vo.setId(rs.getInt("id"));
//			vo.setRace(rs.getString("race"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String race) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" race ");
//		sql.append(" (race) ");
//		sql.append(" VALUES ");
//		sql.append("(?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, race);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String race) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" race set ");
//		sql.append(" race = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, race);
//		stmt.setInt(2, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorRaces() {
//		Race vo;
//		Iterator<Race> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
