//package game.database.player.dao;
//
//import game.database.DbConect;
//import game.database.player.vo.Player;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Iterator;
//import java.util.Random;
//import java.util.Vector;
//
//import com.sun.sgs.app.ClientSession;
//
///**
// * 
// * @author Michel Montenegro
// * 
// */
//public class DAOPlayer {
//
//	private Vector<Player> vos;
//
//	public Vector<Player> getVos() {
//		return vos;
//	}
//
//	public DAOPlayer() {
//		vos = new Vector<Player>();
//	}
//
//	public void selectAll() throws Exception {
//		Player vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm
//				.executeQuery(" SELECT id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr,"
//						+ "sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess,"
//						+ " sector FROM Player ");
//
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Player();
//			vo.setId(rs.getInt("id"));
//			vo.setLoginId(rs.getInt("Login_id"));
//			vo.setName(rs.getString("name"));
//			vo.setMapId(rs.getInt("Map_id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setHpMax(rs.getInt("hpMax"));
//			vo.setHpCurr(rs.getInt("hpCurr"));
//			vo.setManaMax(rs.getInt("manaMax"));
//			vo.setExpMax(rs.getInt("expMax"));
//			vo.setExpCurr(rs.getInt("expCurr"));
//			vo.setSp(rs.getInt("sp"));
//			vo.setStr(rs.getInt("str"));
//			vo.setDex(rs.getInt("dex"));
//			vo.setInte(rs.getInt("inte"));
//			vo.setCon(rs.getInt("con"));
//			vo.setCha(rs.getInt("cha"));
//			vo.setWis(rs.getInt("wis"));
//			vo.setStamina(rs.getInt("stamina"));
//			vo.setSex(rs.getString("sex"));
//			vo.setResMagic(rs.getInt("resMagic"));
//			vo.setResPhysical(rs.getInt("resPhysical"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setDateCreate(rs.getDate("dateCreate"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setOnLine(rs.getString("onLine"));
//			vo.setLastAcess(rs.getDate("lastAcess"));
//			vo.setSector(rs.getInt("sector"));
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		Player vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr,"
//				+ "sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess,"
//				+ " sector ");
//		sql.append(" from Player where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Player();
//			vo.setId(rs.getInt("id"));
//			vo.setLoginId(rs.getInt("Login_id"));
//			vo.setName(rs.getString("name"));
//			vo.setMapId(rs.getInt("Map_id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setHpMax(rs.getInt("hpMax"));
//			vo.setHpCurr(rs.getInt("hpCurr"));
//			vo.setManaMax(rs.getInt("manaMax"));
//			vo.setExpMax(rs.getInt("expMax"));
//			vo.setExpCurr(rs.getInt("expCurr"));
//			vo.setSp(rs.getInt("sp"));
//			vo.setStr(rs.getInt("str"));
//			vo.setDex(rs.getInt("dex"));
//			vo.setInte(rs.getInt("inte"));
//			vo.setCon(rs.getInt("con"));
//			vo.setCha(rs.getInt("cha"));
//			vo.setWis(rs.getInt("wis"));
//			vo.setStamina(rs.getInt("stamina"));
//			vo.setSex(rs.getString("sex"));
//			vo.setResMagic(rs.getInt("resMagic"));
//			vo.setResPhysical(rs.getInt("resPhysical"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setDateCreate(rs.getDate("dateCreate"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setOnLine(rs.getString("onLine"));
//			vo.setLastAcess(rs.getDate("lastAcess"));
//			vo.setSector(rs.getInt("sector"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByLogin(String nameLogin) throws Exception {
//		Player vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" p.id, p.Login_id, p.name, p.Map_id, p.Classe_id, p.hpMax, p.hpCurr, p.manaMax, p.manaCurr, p.expMax, p.expCurr,"
//				+ "p.sp, p.str, p.dex, p.inte, p.con, p.cha, p.wis, p.stamina, p.sex, p.resMagic, p.resPhysical, p.evasion, p.dateCreate, p.onLine, p.lastAcess,"
//				+ " p.sector ");
//		sql.append(" from Player p, Login l where ");
//		sql.append(" p.name = '");
//		sql.append(nameLogin);
//		sql.append("' and p.Login_id=l.id ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		// p.setString(1, nameLogin);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new Player();
//			vo.setId(rs.getInt("id"));
//			vo.setLoginId(rs.getInt("Login_id"));
//			vo.setName(rs.getString("name"));
//			vo.setMapId(rs.getInt("Map_id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setHpMax(rs.getInt("hpMax"));
//			vo.setHpCurr(rs.getInt("hpCurr"));
//			vo.setManaMax(rs.getInt("manaMax"));
//			vo.setExpMax(rs.getInt("expMax"));
//			vo.setExpCurr(rs.getInt("expCurr"));
//			vo.setSp(rs.getInt("sp"));
//			vo.setStr(rs.getInt("str"));
//			vo.setDex(rs.getInt("dex"));
//			vo.setInte(rs.getInt("inte"));
//			vo.setCon(rs.getInt("con"));
//			vo.setCha(rs.getInt("cha"));
//			vo.setWis(rs.getInt("wis"));
//			vo.setStamina(rs.getInt("stamina"));
//			vo.setSex(rs.getString("sex"));
//			vo.setResMagic(rs.getInt("resMagic"));
//			vo.setResPhysical(rs.getInt("resPhysical"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setDateCreate(rs.getDate("dateCreate"));
//			vo.setEvasion(rs.getInt("evasion"));
//			vo.setOnLine(rs.getString("onLine"));
//			vo.setLastAcess(rs.getDate("lastAcess"));
//			vo.setSector(rs.getInt("sector"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(int loginId, String name, int mapId, int classeId,
//			int hpMax, int hpCurr, int manaMax, int manaCurr, int expMax,
//			int expCurr, int sp, int str, int dex, int inte, int con, int cha,
//			int wis, int stamina, String sex, int resMagic, int resPhysical,
//			int evasion, Date dateCreate, String onLine, Date lastAcess,
//			int sector) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" Player ");
//		sql.append(" ( Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr,"
//				+ "sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess,"
//				+ " sector )  ");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//
//		stmt.setInt(1, loginId);
//		stmt.setString(2, name);
//		stmt.setInt(3, mapId);
//		stmt.setInt(4, classeId);
//		stmt.setInt(5, hpMax);
//		stmt.setInt(6, hpCurr);
//		stmt.setInt(7, manaMax);
//		stmt.setInt(8, manaCurr);
//		stmt.setInt(9, expMax);
//		stmt.setInt(10, expCurr);
//		stmt.setInt(11, sp);
//		stmt.setInt(12, str);
//		stmt.setInt(13, dex);
//		stmt.setInt(14, inte);
//		stmt.setInt(15, con);
//		stmt.setInt(16, cha);
//		stmt.setInt(17, wis);
//		stmt.setInt(18, stamina);
//		stmt.setString(19, sex);
//		stmt.setInt(20, resMagic);
//		stmt.setInt(21, resPhysical);
//		stmt.setInt(22, evasion);
//		stmt.setDate(23, dateCreate);
//		stmt.setString(24, onLine);
//		stmt.setDate(25, lastAcess);
//		stmt.setInt(26, sector);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, int loginId, String name, int mapId,
//			int classeId, int hpMax, int hpCurr, int manaMax, int manaCurr,
//			int expMax, int expCurr, int sp, int str, int dex, int inte,
//			int con, int cha, int wis, int stamina, String sex, int resMagic,
//			int resPhysical, int evasion, Date dateCreate, String onLine,
//			Date lastAcess, int sector) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" Player set ");
//		sql.append(" Login_id = ? , name = ? , Map_id = ?, Classe_id = ? , hpMax = ? , hpCurr = ? , manaMax = ? , manaCurr = ? , expMax = ? , expCurr = ? ,"
//				+ "sp = ? , str = ? , dex = ? , inte = ? , con = ? , cha = ? , wis = ? , stamina = ? , sex = ? , resMagic = ? , resPhysical = ? , evasion = ? , dateCreate = ? , onLine = ? , lastAcess = ? ,"
//				+ " sector = ?  ");
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setInt(1, loginId);
//		stmt.setString(2, name);
//		stmt.setInt(3, mapId);
//		stmt.setInt(4, classeId);
//		stmt.setInt(5, hpMax);
//		stmt.setInt(6, hpCurr);
//		stmt.setInt(7, manaMax);
//		stmt.setInt(8, manaCurr);
//		stmt.setInt(9, expMax);
//		stmt.setInt(10, expCurr);
//		stmt.setInt(11, sp);
//		stmt.setInt(12, str);
//		stmt.setInt(13, dex);
//		stmt.setInt(14, inte);
//		stmt.setInt(15, con);
//		stmt.setInt(16, cha);
//		stmt.setInt(17, wis);
//		stmt.setInt(18, stamina);
//		stmt.setString(19, sex);
//		stmt.setInt(20, resMagic);
//		stmt.setInt(21, resPhysical);
//		stmt.setInt(22, evasion);
//		stmt.setDate(23, dateCreate);
//		stmt.setString(24, onLine);
//		stmt.setDate(25, lastAcess);
//		stmt.setInt(28, sector);
//		stmt.setInt(29, id);
//
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorClasses() {
//		Player vo;
//		Iterator<Player> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//	public void createDefaultPlayer(ClientSession session) {
//		Random rand = new Random(32);
//		int loginId = rand.nextInt(8) + 1;
//		String Sessionname = session.getName();
//		int mapId = 1;
//		int classeId = rand.nextInt(8) + 1;
//		;
//		int hpMax = 100;
//		int hpCurr = 100;
//		int manaMax = 100;
//		int manaCurr = 100;
//		int expMax = 100;
//		int expCurr = 0;
//		int sp = 0;
//		int str = 10;
//		int dex = 10;
//		int inte = 10;
//		int con = 10;
//		int cha = 10;
//		int wis = 10;
//		int stamina = 10;
//		String sex="M";
//		int resMagic = 5;
//		int resPhysical = 5;
//		int evasion = 0;
//		Date dateCreate = null;
//		String onLine = "f";
//		Date lastAcess = null;
//		int sector = 1;
//		try {
//			this.insertVo(loginId, Sessionname, mapId, classeId, hpMax, hpCurr,
//					manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte,
//					con, cha, wis, stamina, sex, resMagic, resPhysical,
//					evasion, dateCreate, onLine, lastAcess, sector);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}public void createDefaultPlayer(String playername,int lid) {
//		Random rand = new Random(32);
//		int loginId = lid;
//		String Sessionname = playername;
//		int mapId = 1;
//		int classeId = rand.nextInt(8) + 1;
//		;
//		int hpMax = 100;
//		int hpCurr = 100;
//		int manaMax = 100;
//		int manaCurr = 100;
//		int expMax = 100;
//		int expCurr = 0;
//		int sp = 0;
//		int str = 10;
//		int dex = 10;
//		int inte = 10;
//		int con = 10;
//		int cha = 10;
//		int wis = 10;
//		int stamina = 10;
//		String sex="M";
//		int resMagic = 5;
//		int resPhysical = 5;
//		int evasion = 0;
//		Date dateCreate = new Date(System.currentTimeMillis());
//		String onLine = "f";
//		Date lastAcess = new Date(System.currentTimeMillis());
//		int sector = 1;
//		try {
//			this.insertVo(loginId, Sessionname, mapId, classeId, hpMax, hpCurr,
//					manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte,
//					con, cha, wis, stamina, sex, resMagic, resPhysical,
//					evasion, dateCreate, onLine, lastAcess, sector);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
