package com.lying.client.utility.highlights;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.lying.utility.Highlight;

public class EntityHighlights extends HighlightManager<Highlight.Entity>
{
	private final Map<UUID, Long> ENTITIES = new HashMap<>();
	
	public EntityHighlights() { super(Highlight.Type.ENTITY); }
	
	public boolean isEmpty() { return ENTITIES.isEmpty(); }
	
	public void add(Highlight.Entity highlight)
	{
		add(highlight.uuid(), highlight.expiry());
	}
	
	public void add(UUID entityID, long expiry)
	{
		if(mc.player == null || !entityID.equals(mc.player.getUuid()))
			ENTITIES.put(entityID, expiry);
	}
	
	public void clear() { ENTITIES.clear(); }
	
	public boolean isHighlighted(UUID entityID)
	{
		return ENTITIES.containsKey(entityID);
	}
	
	public void tick(long currentTime)
	{
		List<UUID> expired = Lists.newArrayList();
		for(Entry<UUID, Long> entry : ENTITIES.entrySet())
			if(entry.getValue() < currentTime)
				expired.add(entry.getKey());
		
		expired.forEach(id -> ENTITIES.remove(id));
	}
}
