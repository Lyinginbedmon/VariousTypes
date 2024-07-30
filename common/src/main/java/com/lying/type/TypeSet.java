package com.lying.type;

import static com.lying.utility.VTUtils.listToString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.type.Type.Tier;
import com.lying.utility.VTUtils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
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
		if(types.isEmpty())
			return Text.empty();
		
		MutableText[] vars = new MutableText[Tier.values().length];
		for(Tier tier : Tier.values())
		{
			List<Type> types = ofTier(tier);
			switch(types.size())
			{
				case 0:
					vars[tier.ordinal()] = Text.literal("N/A");
					break;
				case 1:
					vars[tier.ordinal()] = VTUtils.describeType(types.get(0)).copy();
					break;
				default:
					Collections.sort(types, Type.SORT_FUNC);
					vars[tier.ordinal()] = listToString(types, type -> VTUtils.describeType(type), tier.ordinal() == 0 ? " " : ", ");
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
	
	public NbtList writeToNbt()
	{
		NbtList list = new NbtList();
		types.forEach(type -> 
		{
			NbtCompound data = new NbtCompound();
			if(type instanceof DummyType)
				data = (NbtCompound)((DummyType)type).toNbt();
			else
				type.writeToNbt(data);
			list.add(data);
		});
		return list;
	}
	
	public static TypeSet readFromNbt(NbtList list)
	{
		TypeSet set = new TypeSet();
		list.forEach(element -> 
		{
			Type inst = VTTypes.fromNbt(element);
			if(inst != null)
			{
				if(element.getType() == NbtElement.COMPOUND_TYPE)
					inst.read((NbtCompound)element);
				set.add(inst);
			}
			else
				VariousTypes.LOGGER.error("Failed to load type from NBT: "+element.toString());
		});
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
	
	public Optional<Type> get(Identifier listId)
	{
		return types.stream().filter(type -> type.listID().equals(listId)).findFirst();
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
		int originalSize = types.size();
		types.removeIf(type -> type.listID().equals(typeIn.listID()));
		return types.size() != originalSize;
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