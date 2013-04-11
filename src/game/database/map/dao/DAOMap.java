//package game.database.map.dao;
//
//import game.database.DbConect;
//import game.database.map.vo.Map;
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
//public class DAOMap {
//	/** The vector used to store Map information **/
//	private Vector<Map> vos;
//
//	public Vector<Map> getVos() {
//		return vos;
//	}
//
//	/**
//	 * construct a vector, all the Map stored in the database will be retrived
//	 * and stored in this vector
//	 */
//	public DAOMap() {
//		vos = new Vector<Map>();
//	}
//
//	public void selectAll() throws Exception {
//		// there is only one map in the database
//
//		Map vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm
//				.executeQuery("SELECT id, name, widthInTiles, heightInTiles, sizeTile, startTileHeroPosX, startTileHeroPosY, position FROM map");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Map();
//			vo.setId(rs.getInt("id"));// 1
//			vo.setName(rs.getString("name"));// map_1
//			vo.setWidthInTiles(rs.getInt("widthInTiles"));// 30
//			vo.setHeightInTiles(rs.getInt("heightInTiles"));// 30
//			vo.setSizeTile(rs.getInt("sizeTile"));// 32
//			vo.setStartTileHeroPosX(rs.getInt("startTileHeroPosX"));// 12
//			vo.setStartTileHeroPosY(rs.getInt("startTileHeroPosY"));// 9
//			vo.setPosition(rs.getInt("position"));//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Map vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, name, widthInTiles, heightInTiles, sizeTile, startTileHeroPosX, startTileHeroPosY, position ");
//		sql.append(" from map where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Map();
//			vo.setId(rs.getInt("id"));
//			vo.setName(rs.getString("name"));
//			vo.setWidthInTiles(rs.getInt("widthInTiles"));
//			vo.setHeightInTiles(rs.getInt("heightInTiles"));
//			vo.setSizeTile(rs.getInt("sizeTile"));
//			vo.setStartTileHeroPosX(rs.getInt("startTileHeroPosX"));
//			vo.setStartTileHeroPosY(rs.getInt("startTileHeroPosY"));
//			vo.setPosition(rs.getInt("position"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String name, int widthInTiles, int heightInTiles,
//			int sizeTile, int startTileHeroPosX, int startTileHeroPosY,
//			int position) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" map ");
//		sql.append(" (name, widthInTiles, heightInTiles, sizeTile, startTileHeroPosX, startTileHeroPosY, position) ");
//		sql.append(" VALUES ");
//		sql.append("(?, ?, ?, ?, ?, ?, ?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(2, widthInTiles);
//		stmt.setInt(3, heightInTiles);
//		stmt.setInt(4, sizeTile);
//		stmt.setInt(5, startTileHeroPosX);
//		stmt.setInt(6, startTileHeroPosY);
//		stmt.setInt(7, position);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, String name, int widthInTiles,
//			int heightInTiles, int sizeTile, int startTileHeroPosX,
//			int startTileHeroPosY, int position) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" map set ");
//		sql.append(" name = ?, widthInTiles = ?, heightInTiles = ?, sizeTile = ?, startTileHeroPosX = ? , startTileHeroPosY =?, position = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, name);
//		stmt.setInt(2, widthInTiles);
//		stmt.setInt(3, heightInTiles);
//		stmt.setInt(4, sizeTile);
//		stmt.setInt(5, startTileHeroPosX);
//		stmt.setInt(6, startTileHeroPosY);
//		stmt.setInt(7, position);
//
//		stmt.setInt(8, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorMaps() {
//		Map vo;
//		Iterator<Map> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
