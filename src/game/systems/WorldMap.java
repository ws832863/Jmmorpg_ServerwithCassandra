package game.systems;

import game.cassandra.dao.CassandraDAOMap;
import game.database.map.vo.Map;
import game.utils.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class WorldMap {
	/**
	 * in this class defined how the world will be build. the tile type and so on
	 */
	// Name Of The Map (Even used for Channels (Map <n>)), MatrizDoMapa 
	// X <NomeDoMapa, the X position <loginID, X,Y>>
	
	private static HashMap<String, MapWorld[][]> world;

	public WorldMap() {
		initAllMaps();//static function, use a static world that the constructor will be invoked once never channged
	}
	
	public void initAllMaps() {
		world = new HashMap<String, MapWorld[][]>();
		MapWorld[][] mapWorlds;
		MapWorld mapWorld;
		CassandraDAOMap daoMap = new CassandraDAOMap();
		
		try {
			daoMap.selectAll();// load map in mapvector the use getVos get the 
			Iterator<Map> it = daoMap.getVos().iterator();//
	        while (it.hasNext()) {
				Map mapVo = it.next();
				mapWorlds = new MapWorld[mapVo.getWidthInTiles()][mapVo.getHeightInTiles()];//30x30 Tiles
				for (int x = 0; x < mapWorlds.length; x++) {
					for (int y = 0; y < mapWorlds[0].length; y++) {
						mapWorld = new MapWorld();
						mapWorld.setTerrainType(Util.TERRAIN_GROUND);
						mapWorld.setNpcId(-1);
						mapWorld.setCanSpawnMob(false);
						mapWorld.setLoginIDs(new TreeSet<Integer>());  //maybe the userlist of this map?
						
						mapWorlds[x][y] = mapWorld;
					}
				}
				world.put("map_"+mapVo.getId(), mapWorlds); //map_1 channel name?
				mapWorlds=null;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, MapWorld[][]> getWorld() {
		return	world;
	}
	
}
