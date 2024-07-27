package com.lying.type;

import static com.lying.utility.VTUtils.listToString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.type.Type.Tier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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
	
	/** Returns a formatted view of the contents of this TypeSet */
	public Text display()
	{
		MutableText[] vars = new MutableText[Tier.values().length];
		for(Tier tier : Tier.values())
		{
			List<Type> types = ofTier(tier);
			switch(types.size())
			{
				case 0:
					break;
				case 1:
					vars[tier.ordinal()] = types.get(0).displayName().copy();
					break;
				default:
					Collections.sort(types, Type.SORT_FUNC);
					vars[tier.ordinal()] = listToString(types, type -> type.displayName(), tier.ordinal() == 0 ? " " : ", ");
					break;
			}
		}
		
		return vars[Tier.SUBTYPE.ordinal()] == null ? vars[Tier.SUPERTYPE.ordinal()] : Reference.ModInfo.translate("gui", "typeset", vars);
	}
	
	public Text asNameList()
	{
		List<Type> typeList = contents();
		typeList.sort(Type.SORT_FUNC);
		
		MutableText name = null;
		if(typeList.isEmpty())
			name = Text.empty();
		else
		{
			name = typeList.get(0).displayName().copy();
			if(typeList.size() > 1)
				for(int i=1; i<typeList.size(); i++)
					name = name.append(", ").append(typeList.get(i).displayName());
		}
		
		return name;
	}
	
	public boolean isEmpty() { return types.isEmpty(); }
	
	public void clear() { types.clear(); }
	
	public TypeSet copy() { return (new TypeSet()).addAll(this); }
	
	public NbtList writeToNbt(WrapperLookup manager)
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
	
	public static TypeSet readFromJson(JsonArray list)
	{
		TypeSet set = new TypeSet();
		for(JsonElement entry : list.asList())
		{
			Type inst = Type.readFromJson(entry);
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
	
	public TypeSet addAll(TypeSet typesIn)
	{
		typesIn.contents().forEach(type -> add(type));
		return this;
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