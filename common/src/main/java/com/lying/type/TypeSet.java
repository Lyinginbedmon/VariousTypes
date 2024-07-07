package com.lying.type;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.type.Type.Tier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/** Manager object for a set of unique types */
public class TypeSet
{
	private final List<Type> types = Lists.newArrayList();
	
	public TypeSet(Type... typesIn)
	{
		for(Type type : typesIn)
			add(type);
	}
	
	public boolean isEmpty() { return types.isEmpty(); }
	
	public void clear() { types.clear(); }
	
	public TypeSet copy() { return new TypeSet(types.toArray(new Type[0])); }
	
	public NbtList writeToNbt(DynamicRegistryManager manager)
	{
		NbtList list = new NbtList();
		types.forEach(type -> list.add(type.writeToNbt(new NbtCompound(), manager)));
		return list;
	}
	
	public static TypeSet readFromNbt(NbtList list)
	{
		TypeSet set = new TypeSet();
		for(int i=0; i<list.size(); i++)
		{
			NbtCompound data = list.getCompound(i);
			Type inst = VTTypes.get(new Identifier(data.getString("Type")));
			if(inst != null)
			{
				data.remove("Type");
				inst.read(data);
				set.add(inst);
			}
		}
		return set;
	}
	
	public JsonArray writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		JsonArray list = new JsonArray();
		types.forEach(inst -> list.add(inst.writeToJson(manager)));
		return list;
	}
	
	public static TypeSet readFromJson(JsonArray list, RegistryWrapper.WrapperLookup manager)
	{
		TypeSet set = new TypeSet();
		for(JsonElement entry : list.asList())
		{
			Type inst = Type.readFromJson(entry, manager);
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
	
	public void forEach(Consumer<Type> actionIn)
	{
		contents().forEach(actionIn);
	}
	
	public boolean contains(Type typeIn)
	{
		return types.stream().anyMatch(type -> type.listID().equals(typeIn.listID()));
	}
	
	public boolean containsAny(Type... typesIn)
	{
		for(Type type : typesIn)
			if(contains(type))
				return true;
		return false;
	}
	
	/** Returns a list of all types in this set, in order of supertype -> subtype */
	public List<Type> contents()
	{
		List<Type> types = Lists.newArrayList();
		for(Tier tier : Tier.values())
			types.addAll(ofTier(tier));
		return types;
	}
	
	public List<Type> ofTier(Tier tier)
	{
		List<Type> types = Lists.newArrayList();
		this.types.stream().filter(type -> type.tier() == tier).forEach(type -> types.add(type));
		return types;
	}
	
	/** Adds the given type if it does not already exist in the set and is mutually compatible with all others */
	public boolean add(Type typeIn)
	{
		return typeIn != null && !contains(typeIn) && types.stream().allMatch(type -> type.compatibleWith(typeIn) && typeIn.compatibleWith(type)) ? types.add(typeIn) : false;
	}
	
	public void addAll(Type... typesIn)
	{
		for(Type type : typesIn)
			add(type);
	}
	
	public boolean remove(Type typeIn)
	{
		return types.remove(typeIn);
	}
	
	public void removeAll(Collection<Type> typesIn)
	{
		types.removeAll(typesIn);
	}
	
	public void removeIf(Predicate<Type> condition)
	{
		types.removeIf(condition);
	}
	
	/**
	 * Returns a collection of abilities applied by this set.<br>
	 * This is constructed by applying abilities from Supertypes first, then Subtypes.<br>
	 * @return
	 */
	public Collection<AbilityInstance> abilities()
	{
		AbilitySet abilities = new AbilitySet();
		types.stream().filter(type -> type.tier() == Tier.SUPERTYPE).forEach(type -> type.abilities().forEach(ability -> abilities.add(ability)));
		types.stream().filter(type -> type.tier() == Tier.SUBTYPE).forEach(type -> type.abilities().forEach(ability -> abilities.add(ability)));
		return abilities.abilities();
	}
}