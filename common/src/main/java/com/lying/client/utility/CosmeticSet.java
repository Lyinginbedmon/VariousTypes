package com.lying.client.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.init.VTCosmetics;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;

public class CosmeticSet
{
	public static final Codec<CosmeticSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Cosmetic.CODEC.listOf().fieldOf("Values").forGetter(CosmeticSet::values))
				.apply(instance, CosmeticSet::of));
	
	private final Map<Identifier, List<Cosmetic>> set = new HashMap<>();
	
	protected CosmeticSet() { }
	
	public static CosmeticSet of(List<Cosmetic> values)
	{
		CosmeticSet set = new CosmeticSet();
		values.forEach(c -> set.add(c));
		return set;
	}
	
	public List<Cosmetic> values()
	{
		List<Cosmetic> values = Lists.newArrayList();
		set.values().forEach(list -> list.forEach(c -> values.add(c)));
		return values;
	}
	
	public boolean isEmpty() { return set.isEmpty(); }
	
	public void clear() { set.clear(); }
	
	public int size()
	{
		int tally = 0;
		for(List<Cosmetic> list : set.values())
			tally += list.size();
		return tally;
	}
	
	public void clone(CosmeticSet setIn)
	{
		clear();
		if(setIn.isEmpty())
			return;
		
		addAll(setIn);
	}
	
	public boolean has(Identifier registryId)
	{
		return values().stream().anyMatch(c -> c.registryName().equals(registryId));
	}
	
	public boolean has(Identifier registryId, int colour)
	{
		return values().stream().anyMatch(c -> c.registryName().equals(registryId) && c.tinted() && c.color().get() == colour);
	}
	
	public void add(@Nullable Cosmetic cosmetic)
	{
		if(cosmetic == null)
			return;
		
		CosmeticType type = cosmetic.type();
		List<Cosmetic> list = ofType(type);
		
		// Ensure unique cosmetics
		list.removeIf(cos -> cos.registryName().equals(cosmetic.registryName()));
		list.add(cosmetic);
		
		// Restrict to capacity of type, dropping the earliest
		if(!type.uncapped() && !list.isEmpty())
			while(list.size() > type.capacity())
				list.remove(0);
		
		set.put(type.registryName(), list);
	}
	
	public boolean remove(Identifier registryId)
	{
		return remove(registryId, -1);
	}
	
	public boolean remove(Identifier registryId, int colour)
	{
		Optional<Cosmetic> cos = VTCosmetics.get(registryId);
		if(cos.isEmpty())
			return false;
		
		Cosmetic cosmetic = cos.get();
		cosmetic.tint(colour);
		
		return remove(cosmetic, colour >= 0);
	}
	
	public boolean remove(Cosmetic cosmetic, boolean matchColour)
	{
		CosmeticType type = cosmetic.type();
		List<Cosmetic> list = ofType(type);
		if(list.isEmpty())
			return false;
		
		boolean result = list.removeIf(c -> c.matches(cosmetic, matchColour));
		if(result)
			set.put(type.registryName(), list);
		return result;
	}
	
	public boolean removeAll(CosmeticType category)
	{
		return set.remove(category.registryName()) != null;
	}
	
	public void addAll(@NotNull CosmeticSet set)
	{
		set.set.values().forEach(list -> list.forEach(cosmetic -> add(cosmetic)));
	}
	
	public List<Cosmetic> ofType(CosmeticType type) { return ofType(type.registryName()); }
	
	public List<Cosmetic> ofType(Identifier registryName) { return set.getOrDefault(registryName, Lists.newArrayList()); }
}
