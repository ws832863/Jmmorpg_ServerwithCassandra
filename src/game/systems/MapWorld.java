package game.systems;

import java.util.Set;


public class MapWorld {
	
	int terrainType;
	int npcId;
	Set<String> loginIDs;
	boolean canSpawnMob;
	
	public int getTerrainType() {
		return terrainType;
	}
	public void setTerrainType(int terrainType) {
		this.terrainType = terrainType;
	}
	public int getNpcId() {
		return npcId;
	}
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	public Set<String> getLoginIDs() {
		return loginIDs;
	}
	public void setLoginIDs(Set<String> loginIDs) {
		this.loginIDs = loginIDs;
	}
	public boolean isCanSpawnMob() {
		return canSpawnMob;
	}
	public void setCanSpawnMob(boolean canSpawnMob) {
		this.canSpawnMob = canSpawnMob;
	}

	
	
}
