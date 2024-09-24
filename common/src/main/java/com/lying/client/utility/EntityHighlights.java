package com.lying.client.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.lying.utility.EntityHighlight;

import net.minecraft.client.MinecraftClient;

public class EntityHighlights
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final Map<UUID, Long> ENTITIES = new HashMap<>();
	
	public static void add(EntityHighlight highlight)
	{
		add(highlight.uuid(), highlight.expiry());
	}
	
	public static void add(UUID entityID, long expiry)
	{
		if(mc.player == null || !entityID.equals(mc.player.getUuid()))
			ENTITIES.put(entityID, expiry);
	}
	
	public static void clear() { ENTITIES.clear(); }
	
	public static boolean isHighlighted(UUID entityID)
	{
		return ENTITIES.containsKey(entityID);
	}
	
	public static void tick(long currentTime)
	{
		List<UUID> expired = Lists.newArrayList();
		for(Entry<UUID, Long> entry : ENTITIES.entrySet())
			if(entry.getValue() < currentTime)
				expired.add(entry.getKey());
		
		expired.forEach(id -> ENTITIES.remove(id));
	}
}
