package com.lying.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilitySource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/** Manager object for a set of ability instances */
public class AbilitySet
{
	public static final Codec<AbilitySet> CODEC_VITALS = AbilityInstance.LIST_CODEC_VITALS.comapFlatMap(set -> { return DataResult.success(AbilitySet.listToSet(set)); }, AbilitySet::abilitiesList);
	public static final Codec<AbilitySet> CODEC = AbilityInstance.LIST_CODEC.comapFlatMap(set -> { return DataResult.success(AbilitySet.listToSet(set)); }, AbilitySet::abilitiesList);
	
	protected final Map<Identifier, AbilityInstance> abilities = new HashMap<>();
	
	public static AbilitySet listToSet(List<AbilityInstance> list)
	{
		AbilitySet abilities = new AbilitySet();
		abilities.addAll(list);
		return abilities;
	}
	
	public AbilitySet copy()
	{
		AbilitySet copy = new AbilitySet();
		abilities.values().forEach(inst -> copy.add(inst.copy()));
		return copy;
	}
	
	public AbilitySet replicateFor(AbilitySource source)
	{
		AbilitySet copy = new AbilitySet();
		abilities.values().forEach(inst -> 
		{
			AbilityInstance dupe = inst.ability().instance(source);
			dupe.copyDetails(inst);
			copy.add(dupe);
		});
		return copy;
	}
	
	public void clear() { abilities.clear(); }
	
	public boolean isEmpty() { return abilities.isEmpty(); }
	
	public int size() { return abilities.size(); }
	
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
	
	public void addAll(Collection<AbilityInstance> abilities)
	{
		abilities.forEach(inst -> add(inst));
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
	
	/** Adds the ability, overriding any pre-existing ability if necessary */
	public void set(AbilityInstance ability)
	{
		if(abilities.containsKey(ability.mapName()))
			abilities.remove(ability.mapName());
		abilities.put(ability.mapName(), ability);
	}
	
	/**
	 * Ensures the given set has all abilities of this set and none outside of it, without modifying the actual abilities.<br>
	 * Used to create an AbilitySet that can be acted upon by user input.
	 */
	public void mergeActivated(AbilitySet activeSet)
	{
		// Add every ability I have that they don't
		for(Entry<Identifier, AbilityInstance> entry : activatedAbilities().entrySet())
			if(!activeSet.hasAbilityInstance(entry.getKey()))
				activeSet.add(entry.getValue().copy());
		
		// Remove every ability they have that I don't
		for(Entry<Identifier, AbilityInstance> entry : activeSet.activatedAbilities().entrySet())
			if(!hasAbilityInstance(entry.getKey()))
				activeSet.remove(entry.getKey());
	}
	
	/** Returns a map of all non-passive ability instances in this set */
	protected Map<Identifier, AbilityInstance> activatedAbilities()
	{
		Map<Identifier, AbilityInstance> activatedAbilities = new HashMap<>();
		abilities.values().stream()
			.filter(inst -> Ability.isActivatedAbility(inst.ability())).forEach(inst -> activatedAbilities.put(inst.mapName(), inst));
		return activatedAbilities;
	}
	
	public boolean hasAbility(Identifier registryName)
	{
		return abilities.values().stream().anyMatch(instance -> instance.ability().registryName().equals(registryName));
	}
	
	@Nullable
	public AbilityInstance get(Identifier mapName) { return abilities.getOrDefault(mapName, null); }
	
	public List<AbilityInstance> getAbilitiesOfType(Identifier registryName)
	{
		return abilities.values().stream().filter(inst -> inst.ability().registryName().equals(registryName)).toList();
	}
	
	public List<AbilityInstance> getAbilitiesOfClass(Class<?> classIn)
	{
		return abilities.values().stream().filter(inst -> classIn.isAssignableFrom(inst.ability().getClass())).toList();
	}
	
	public boolean hasAbilityInstance(Identifier mapName)
	{
		return abilities.values().stream().anyMatch(instance -> instance.mapName().equals(mapName));
	}
	
	public Collection<AbilityInstance> abilities() { return abilities.values(); }
	
	protected List<AbilityInstance> abilitiesList() { return Lists.newArrayList(abilities()); }
	
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
	
	public NbtElement writeToNbt()
	{
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static AbilitySet readFromNbt(NbtList list)
	{
		return CODEC.parse(NbtOps.INSTANCE, list).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager, boolean vitalOnly)
	{
		JsonArray list = new JsonArray();
		abilities.values().forEach(inst -> list.add(inst.writeToJson(manager, vitalOnly)));
		return list;
	}
	
	public static AbilitySet readFromJson(JsonArray list, AbilitySource forceSource)
	{
		AbilitySet set = new AbilitySet();
		for(JsonElement entry : list.asList())
		{
			AbilityInstance inst = AbilityInstance.readFromJson(entry, forceSource);
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
}