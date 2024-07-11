package com.lying.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Identifier;

/** Manager object for a set of ability instances */
public class AbilitySet
{
	private final Map<Identifier, AbilityInstance> abilities = new HashMap<>();
	
	public AbilitySet copy()
	{
		AbilitySet copy = new AbilitySet();
		abilities.values().forEach(inst -> copy.add(inst.copy()));
		return copy;
	}
	
	public void clear() { abilities.clear(); }
	
	public boolean isEmpty() { return abilities.isEmpty(); }
	
	/** Adds the given ability if it does not already exist in the map or if the given instance should overrule it */
	public boolean add(AbilityInstance ability)
	{
		Identifier name = ability.mapName();
		if(!abilities.containsKey(name) || ability.source().overrules(abilities.get(name).source()))
		{
			abilities.put(name, ability);
			return true;
		}
		return false;
	}
	
	/** Remove any ability with the given map name */
	public boolean remove(Identifier mapName)
	{
		return abilities.remove(mapName) != null;
	}
	
	/** Remove all abilities of the given registry name */
	public void removeAll(Identifier registryName)
	{
		if(!remove(registryName))
		{
			Set<Entry<Identifier,AbilityInstance>> remainder = Set.of();
			remainder.addAll(abilities.entrySet());
			remainder.forEach(entry -> 
			{
				if(entry.getValue().ability().registryName().equals(registryName))
					abilities.remove(entry.getKey());
			});
		}
	}
	
	public boolean hasAbility(Identifier registryName)
	{
		return abilities.values().stream().anyMatch(instance -> instance.ability().registryName().equals(registryName));
	}
	
	public List<AbilityInstance> getAbilitiesOfType(Identifier registryName)
	{
		return abilities.values().stream().filter(inst -> inst.ability().registryName().equals(registryName)).toList();
	}
	
	public boolean hasAbilityInstance(Identifier mapName)
	{
		return abilities.values().stream().anyMatch(instance -> instance.mapName().equals(mapName));
	}
	
	public Collection<AbilityInstance> abilities() { return abilities.values(); }
	
	/** Returns a subset of abilities that are not hidden from displays */
	public List<AbilityInstance> allNonHidden()
	{
		List<AbilityInstance> abilities = Lists.newArrayList();
		abilities().forEach(inst -> 
		{
			if(!inst.ability().isHidden(inst))
				abilities.add(inst);
		});
		return abilities;
	}
	
	public NbtList writeToNbt(WrapperLookup manager)
	{
		NbtList list = new NbtList();
		abilities().forEach(inst -> list.add(inst.writeToNbt(new NbtCompound(), manager)));
		return list;
	}
	
	public static AbilitySet readFromNbt(NbtList list)
	{
		AbilitySet set = new AbilitySet();
		for(int i=0; i<list.size(); i++)
		{
			AbilityInstance inst = AbilityInstance.readFromNbt(list.getCompound(i));
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
	
	public JsonArray writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		JsonArray list = new JsonArray();
		abilities().forEach(inst -> list.add(inst.writeToJson(manager)));
		return list;
	}
	
	public static AbilitySet readFromJson(JsonArray list)
	{
		AbilitySet set = new AbilitySet();
		for(JsonElement entry : list.asList())
		{
			AbilityInstance inst = AbilityInstance.readFromJson(entry.getAsJsonObject());
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
}