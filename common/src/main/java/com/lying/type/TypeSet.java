package com.lying.type;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.type.Type.Tier;

import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

/** Manager object for a set of unique types */
public class TypeSet
{
	private final List<Type> types = Lists.newArrayList();
	
	public TypeSet(Type... typesIn)
	{
		for(Type type : typesIn)
			types.add(type);
	}
	
	public boolean isEmpty() { return types.isEmpty(); }
	
	public TypeSet copy() { return new TypeSet(types.toArray(new Type[0])); }
	
	public NbtList writeToNbt()
	{
		NbtList list = new NbtList();
		types.forEach(type -> list.add(NbtString.of(type.registryName().toString())));
		return list;
	}
	
	public static TypeSet readFromNbt(NbtList list)
	{
		TypeSet set = new TypeSet();
		for(int i=0; i<list.size(); i++)
		{
			Type inst = VTTypes.get(new Identifier(list.getString(i)));
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
	
	public JsonArray writeToJson()
	{
		JsonArray list = new JsonArray();
		types.forEach(inst -> list.add(inst.registryName().toString()));
		return list;
	}
	
	public static TypeSet readFromJson(JsonArray list)
	{
		TypeSet set = new TypeSet();
		for(JsonElement entry : list.asList())
		{
			Type inst = VTTypes.get(new Identifier(entry.getAsString()));
			if(inst != null)
				set.add(inst);
		}
		return set;
	}
	
	public void forEach(Consumer<Type> actionIn)
	{
		types.forEach(actionIn);
	}
	
	public boolean contains(Type typeIn)
	{
		return types.contains(typeIn);
	}
	
	public boolean containsAny(Type... typesIn)
	{
		for(Type type : typesIn)
			if(contains(type))
				return true;
		return false;
	}
	
	public boolean add(Type typeIn)
	{
		return types.stream().allMatch(type -> type.compatibleWith(typeIn)) ? types.add(typeIn) : false;
	}
	
	public boolean remove(Type typeIn)
	{
		return types.remove(typeIn);
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