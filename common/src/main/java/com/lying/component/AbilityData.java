 package com.lying.component;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;

public class AbilityData
 {
	 private Map<Identifier, CooldownEntry> cooldownManager = new HashMap<>();
	 
	 public boolean isOnCooldown(Identifier mapName) { return cooldownManager.containsKey(mapName); }
	 
	 public float cooldownProgress(Identifier mapName, long gameTime)
	 {
		 return cooldownManager.containsKey(mapName) ? cooldownManager.get(mapName).getProgress(gameTime) : 0F; 
	 }
	 
	 public void update(long gameTime)
	 {
		 cooldownManager.entrySet().stream().filter(entry -> entry.getValue().isExpired(gameTime)).forEach(entry -> cooldownManager.remove(entry.getKey()));
	 }
	 
	 public void set(Identifier mapName, int duration, long gameTime)
	 {
		 // Only put an ability on cooldown if it already isn't on cooldown OR its remaining cooldown is less than this cooldown
		 if(!cooldownManager.containsKey(mapName) || cooldownManager.get(mapName).isLongerThan(duration, gameTime))
			 cooldownManager.put(mapName, new CooldownEntry(gameTime, gameTime + duration));
	 }
	 
	 public void clear(Identifier mapName)
	 {
		 if(cooldownManager.containsKey(mapName))
			 cooldownManager.remove(mapName);
	 }
	 
	 private class CooldownEntry
	 {
		 private final long startTick, endTick;
		 
		 public CooldownEntry(long startTick, long endTick)
		 {
			 this.startTick = startTick;
			 this.endTick = endTick;
		 }
		 
		 public boolean isExpired(long gameTime)
		 {
			 return gameTime >= endTick;
		 }
		 
		 public float getProgress(long gameTime) { return (gameTime - startTick) / (endTick - startTick); }
		 
		 public boolean isLongerThan(int duration, long gameTime) { return (endTick - gameTime) > duration; }
	 }
 }