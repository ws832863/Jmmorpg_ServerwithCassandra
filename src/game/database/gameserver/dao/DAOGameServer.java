//package game.database.gameserver.dao;
//
//import game.database.DbConect;
//import game.database.gameserver.vo.GameServer;
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
//public class DAOGameServer {
//
//	private Vector<GameServer> vos;
//	public Vector<GameServer> getVos() {
//		return vos;
//	}
//
//	public DAOGameServer() {
//		vos = new Vector<GameServer>();
//	}
//
//	public void selectAll() throws Exception {
//		GameServer vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm.executeQuery("SELECT id, name, ip, active FROM GameServer ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new GameServer();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vo.setIp(rs.getString("ip"));
//			vo.setActive(rs.getInt("active"));
//			vos.add(vo);
//		}
//		vo = null;
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		GameServer vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, name, ip, active ");
//		sql.append(" from GameServer where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new GameServer();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vo.setIp(rs.getString("ip"));
//			vo.setActive(rs.getInt("active"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name, String ip, int active) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" GameServer ");
//		sql.append(" (name, ip, active) ");
//		sql.append(" VALUES ");
//		sql.append("(?, ?, ?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setString(2, ip);
//		stmt.setInt(3, active);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String name, String ip, int active) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" GameServer set ");
//		sql.append(" name = ?, ip = ?, active = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setString(2, ip);
//		stmt.setInt(3, active);
//		stmt.setInt(4, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//	
//	public void testVectorClans() {
//		GameServer vo;
//		Iterator<GameServer> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
