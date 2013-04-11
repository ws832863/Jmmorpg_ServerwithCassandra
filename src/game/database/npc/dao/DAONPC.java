//package game.database.npc.dao;
//
//import game.database.DbConect;
//import game.database.npc.vo.NPC;
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
//public class DAONPC {
//
//
//	private Vector<NPC> vos;
//	public Vector<NPC> getVos() {
//		return vos;
//	}
//
//	public DAONPC() {
//		vos = new Vector<NPC>();
//	}
//
//	public void selectAll() throws Exception {
//		NPC vo;
//		Connection conn = DbConect.getInstance().getConnection();
//		Statement stm = conn.createStatement();
//		ResultSet rs = stm
//				.executeQuery("SELECT id, Map_id, Race_id, Clan_id, Classe_id, name, manaMax, " +
//						"str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, " +
//						"evasion, hpMax, hpCurr, manaCurr, level, typeNPC, aggro, posXMap, posYMap FROM NPC ");
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new NPC();
//			vo.setId(rs.getInt("id"));
//			vo.setMapId(rs.getInt("Map_id"));
//			vo.setRaceId(rs.getInt("Race_id"));
//			vo.setClanId(rs.getInt("Clan_id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setName(rs.getString("name"));
//			vo.setManaMax(rs.getInt("manaMax"));
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
//			vo.setHpMax(rs.getInt("hpMax"));
//			vo.setHpCurr(rs.getInt("hpCurr"));
//			vo.setManaCurr(rs.getInt("manaCurr"));
//			vo.setLevel(rs.getInt("level"));
//			vo.setTypeNPC(rs.getString("typeNPC"));
//			vo.setAggro(rs.getString("aggro"));
//			vo.setPosXMap(rs.getInt("posXMap"));
//			vo.setPosYMap(rs.getInt("posYMap"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void selectByPK(int pkID) throws Exception {
//		NPC vo;
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select ");
//		sql.append(" id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, ");
//		sql
//				.append(" wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, ");
//		sql
//				.append(" fileSpriteName, startColSprite, endColSprite, startRowSprite, endRowSprite ");
//		sql.append(" from Classe where ");
//		sql.append(" id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		PreparedStatement p = conn.prepareStatement(sql.toString());
//		p.setInt(1, pkID);
//		ResultSet rs = p.executeQuery();
//		vos.removeAllElements();
//		while (rs.next()) {
//			vo = new NPC();
//			vo.setId(rs.getInt("id"));
//			vo.setMapId(rs.getInt("Map_id"));
//			vo.setRaceId(rs.getInt("Race_id"));
//			vo.setClanId(rs.getInt("Clan_id"));
//			vo.setClasseId(rs.getInt("Classe_id"));
//			vo.setName(rs.getString("name"));
//			vo.setManaMax(rs.getInt("manaMax"));
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
//			vo.setHpMax(rs.getInt("hpMax"));
//			vo.setHpCurr(rs.getInt("hpCurr"));
//			vo.setManaCurr(rs.getInt("manaCurr"));
//			vo.setLevel(rs.getInt("level"));
//			vo.setTypeNPC(rs.getString("typeNPC"));
//			vo.setAggro(rs.getString("aggro"));
//			vo.setPosXMap(rs.getInt("posXMap"));
//			vo.setPosYMap(rs.getInt("posYMap"));
//
//			vos.add(vo);
//		}
//		conn.close();
//	}
//
//	public void insertVo(int mapId, int raceId, int clanId, int classeId, String name, int hpMax, int hpCurr, int manaMax, 
//			int manaCurr, int str, int dex, int inte, int con, int cha, int wis, int stamina, String sex, int resMagic, 
//			int resPhysical, int evasion, int level, String typeNPC, String aggro, int posXMap, int posYMap) throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" INSERT INTO ");
//		sql.append(" NPC ");
//		sql.append(" ( Map_id, Race_id, Clan_id, Classe_id, name, manaMax, " +
//						"str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, " +
//						"evasion, hpMax, hpCurr, manaCurr, level, typeNPC, aggro, posXMap, posYMap ) ");
//		sql.append(" VALUES ");
//		sql.append("(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setInt(1, mapId);		
//		stmt.setInt(2, raceId);
//		stmt.setInt(3, clanId);
//		stmt.setInt(4, classeId);
//		stmt.setString(5, name);
//		stmt.setInt(8, manaMax);
//		stmt.setInt(10, str);
//		stmt.setInt(11, dex);
//		stmt.setInt(12, inte);
//		stmt.setInt(13, con);
//		stmt.setInt(14, cha);
//		stmt.setInt(15, wis);
//		stmt.setInt(16, stamina);
//		stmt.setString(17, sex);
//		stmt.setInt(18, resMagic);
//		stmt.setInt(19, resPhysical);		
//		stmt.setInt(18, evasion);
//		stmt.setInt(6, hpMax);
//		stmt.setInt(7, hpCurr);
//		stmt.setInt(9, manaCurr);
//		stmt.setInt(19, level);
//		stmt.setString(17, typeNPC);
//		stmt.setString(17, aggro);
//		stmt.setInt(18, posXMap);
//		stmt.setInt(19, posYMap);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void updateVo(int id, int mapId, int raceId, int clanId, int classeId, String name, int hpMax, int hpCurr, int manaMax, 
//			int manaCurr, int str, int dex, int inte, int con, int cha, int wis, int stamina, String sex, int resMagic, 
//			int resPhysical, int evasion, int level, String typeNPC, String aggro, int posXMap, int posYMap)
//			throws SQLException {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE ");
//		sql.append(" NPC set ");
//		sql.append(" Map_id = ?, Race_id = ?, Clan_id = ?, Classe_id = ?, name = ?, manaMax = ?, " +
//				   "str = ?, dex = ?, inte = ?, con = ?, cha = ?, wis = ?, stamina = ?, sex = ?, resMagic = ?, resPhysical = ?, " +
//				   "evasion = ?, hpMax = ?, hpCurr = ?, manaCurr = ?, level = ?, typeNPC = ?, aggro = ?, posXMap = ?, posYMap = ?  ");		
//		sql.append(" where id = ? ");
//		Connection conn = DbConect.getInstance().getConnection();
//		conn.setAutoCommit(false);
//		PreparedStatement stmt = conn.prepareStatement(sql.toString());
//		stmt.setInt(1, mapId);		
//		stmt.setInt(2, raceId);
//		stmt.setInt(3, clanId);
//		stmt.setInt(4, classeId);
//		stmt.setString(5, name);
//		stmt.setInt(8, manaMax);
//		stmt.setInt(10, str);
//		stmt.setInt(11, dex);
//		stmt.setInt(12, inte);
//		stmt.setInt(13, con);
//		stmt.setInt(14, cha);
//		stmt.setInt(15, wis);
//		stmt.setInt(16, stamina);
//		stmt.setString(17, sex);
//		stmt.setInt(18, resMagic);
//		stmt.setInt(19, resPhysical);		
//		stmt.setInt(18, evasion);
//		stmt.setInt(6, hpMax);
//		stmt.setInt(7, hpCurr);
//		stmt.setInt(9, manaCurr);
//		stmt.setInt(19, level);
//		stmt.setString(17, typeNPC);
//		stmt.setString(17, aggro);
//		stmt.setInt(18, posXMap);
//		stmt.setInt(19, posYMap);
//		stmt.setInt(20, id);
//		stmt.executeUpdate();
//		conn.commit();
//
//		stmt.close();
//		conn.close();
//	}
//
//	public void testVectorClasses() {
//		NPC vo;
//		Iterator<NPC> it = vos.iterator();
//		while (it.hasNext()) {
//			vo = it.next();
//			System.out.println(vo.toString());
//		}
//	}
//
//}
