package com.lying.client.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.utility.Cosmetic;
import com.lying.utility.Cosmetic.Type;

public class CosmeticSet
{
	private final Map<Type, List<Cosmetic>> set = new HashMap<>();
	
	public boolean isEmpty() { return set.isEmpty(); }
	
	public void clear() { set.clear(); }
	
	public void clone(CosmeticSet setIn)
	{
		clear();
		if(setIn.isEmpty())
			return;
		
		set.putAll(setIn.set);
	}
	
	public void add(@Nullable Cosmetic cosmetic)
	{
		if(cosmetic == null)
			return;
		
		Type type = cosmetic.type();
		List<Cosmetic> list = ofType(type);
		
		// Ensure unique cosmetics
		list.removeIf(cos -> cos.registryName().equals(cosmetic.registryName()));
		list.add(cosmetic);
		
		// Restrict to capacity of type, dropping the earliest
		if(!type.uncapped() && !list.isEmpty())
			while(list.size() > type.capacity())
				list.remove(0);
		
		set.put(type, list);
	}
	
	public List<Cosmetic> ofType(Type type) { return set.getOrDefault(type, Lists.newArrayList()); }
}
