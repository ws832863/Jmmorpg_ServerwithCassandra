//package game.database.login.dao;
//
//import game.database.DbConect;
//import game.database.login.vo.Login;
//import game.database.player.dao.DAOPlayer;
//
//import java.sql.Connection;
//import java.sql.Date;
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
//public class DAOLogin {
//
//	private Vector<Login> vos;
//
//	public Vector<Login> getVos() {
//		return vos;
//	}
//
//	public DAOLogin() {
//		vos = new Vector<Login>();
//	}
//
//	public void selectAll() throws Exception {
//		Login vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm
//				.executeQuery("SELECT id, Clan_id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth FROM Login ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Login();
//			vo.setId(rs.getInt("id"));
//			vo.setClanId(rs.getInt("Clan_id"));
//			vo.setLogin(rs.getString("login"));
//			vo.setUser_password(rs.getString("user_password"));
//			vo.setAccessLevel(rs.getString("accessLevel"));
//			vo.setDateRegister(rs.getDate("dateRegister"));
//			vo.setLastIP(rs.getString("lastIP"));
//			vo.setLastactive(rs.getDate("lastactive"));
//			vo.setUserCurrIP(rs.getString("userCurrIP"));
//			vo.setLastServer(rs.getString("lastServer"));
//			vo.setName(rs.getString("name"));
//			vo.setEmail(rs.getString("email"));
//			vo.setBirth(rs.getDate("birth"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Login vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, Clan_id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth ");
//		sql.append(" from Login where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Login();
//			vo.setId(rs.getInt("id"));
//			vo.setClanId(rs.getInt("Clan_id"));
//			vo.setLogin(rs.getString("login"));
//			vo.setUser_password(rs.getString("user_password"));
//			vo.setAccessLevel(rs.getString("accessLevel"));
//			vo.setDateRegister(rs.getDate("dateRegister"));
//			vo.setLastIP(rs.getString("lastIP"));
//			vo.setLastactive(rs.getDate("lastactive"));
//			vo.setUserCurrIP(rs.getString("userCurrIP"));
//			vo.setLastServer(rs.getString("lastServer"));
//			vo.setName(rs.getString("name"));
//			vo.setEmail(rs.getString("email"));
//			vo.setBirth(rs.getDate("birth"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByLoginPassword(String login, String password)
//			throws Exception {
//		Login vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, Clan_id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth ");
//		sql.append(" from Login where ");
//		sql.append(" login = ? and user_password = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setString(1, login);
//		p.setString(2, password);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Login();
//			vo.setId(rs.getInt("id"));
//			vo.setClanId(rs.getInt("Clan_id"));
//			vo.setLogin(rs.getString("login"));
//			vo.setUser_password(rs.getString("user_password"));
//			vo.setAccessLevel(rs.getString("accessLevel"));
//			vo.setDateRegister(rs.getDate("dateRegister"));
//			vo.setLastIP(rs.getString("lastIP"));
//			vo.setLastactive(rs.getDate("lastactive"));
//			vo.setUserCurrIP(rs.getString("userCurrIP"));
//			vo.setLastServer(rs.getString("lastServer"));
//			vo.setName(rs.getString("name"));
//			vo.setEmail(rs.getString("email"));
//			vo.setBirth(rs.getDate("birth"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(String login, String user_password,
//			String accessLevel, Date dateRegister, String lastIP,
//			Date lastactive, String userCurrIP, String lastServer, String name,
//			String email, Date birth, int clanId) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Login ");
//		sql.append(" (login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) ");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, login);
//		stmt.setString(2, user_password);
//		stmt.setString(3, accessLevel);
//		stmt.setDate(4, dateRegister);
//		stmt.setString(5, lastIP);
//		stmt.setDate(6, lastactive);
//		stmt.setString(7, userCurrIP);
//		stmt.setString(8, lastServer);
//		stmt.setString(9, name);
//		stmt.setString(10, email);
//		stmt.setDate(11, birth);
//		stmt.setInt(12, clanId);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	/**
//	 * if a user not exists in database ,then create it
//	 * 
//	 * @param username
//	 * @param password
//	 */
//	public void createDefaultAccount(String username, String password) {
//		String login = username;
//		String user_password = password;
//		String accessLevel = "0";
//		Date dateRegister = new Date(System.currentTimeMillis());
//		Date lastactive = new Date(System.currentTimeMillis());
//		Date birth = new Date(System.currentTimeMillis());
//
//		String lastIP = "127.0.0.1";
//		String userCurrIP = "127.0.0.1";
//		String lastServer = "addedserver";
//		String name = "testname";
//		String email = "test@email.com";
//		int clanId = 2;
//
//		try {
//			this.insertVo(login, user_password, accessLevel, dateRegister,
//					lastIP, lastactive, userCurrIP, lastServer, name, email,
//					birth, clanId);
//			selectByLoginPassword(login, user_password);
//			int id = vos.get(0).getId();// get the login just inserted
//			System.out.println("insert new user " + login + " login id :" + id
//					+ "  password " + user_password);
//			DAOPlayer dp = new DAOPlayer();
//			dp.createDefaultPlayer(login, id);
//		} catch (SQLException e) {
//			System.out.println("Insert New User Erro" + e.toString());
//			// e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public void updateVo(int id, String login, String user_password,
//			String accessLevel, Date dateRegister, String lastIP,
//			Date lastactive, String userCurrIP, String lastServer, String name,
//			String email, Date birth, int clanId) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Login set ");
//		sql.append(" login = ?, user_password = ?, accessLevel = ?, dateRegister = ?, lastIP = ?, lastactive = ?, userCurrIP = ?, lastServer = ?, name = ?, email = ?, birth = ?, Clan_id = ? ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setString(1, login);
//		stmt.setString(2, user_password);
//		stmt.setString(3, accessLevel);
//		stmt.setDate(4, dateRegister);
//		stmt.setString(5, lastIP);
//		stmt.setDate(6, lastactive);
//		stmt.setString(7, userCurrIP);
//		stmt.setString(8, lastServer);
//		stmt.setString(9, name);
//		stmt.setString(10, email);
//		stmt.setDate(11, birth);
//		stmt.setInt(12, clanId);
//		stmt.setInt(13, id);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorClans() {
//		Login vo;
//		Iterator<Login> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
