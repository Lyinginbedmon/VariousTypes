package com.lying.template.precondition;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.component.CharacterSheet;
import com.lying.type.Type;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class TypeCondition extends ConfigurableCondition
{
	protected final List<Type> types = Lists.newArrayList();
	
	protected TypeCondition(Identifier idIn, Type... typesIn)
	{
		super(idIn);
		for(Type type : typesIn)
			types.add(type);
	}
	
	public boolean isValidFor(CharacterSheet sheet, LivingEntity owner)
	{
		return types.isEmpty() || types.stream().allMatch(type -> sheet.types().contains(type));
	}
	
	protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
	{
		JsonArray list = new JsonArray();
		types.forEach(type -> list.add(type.writeToJson(manager)));
		data.add("Types", list);
		return data;
	}
	
	protected void read(JsonObject data)
	{
		types.clear();
		JsonArray list = data.getAsJsonArray("Types");
		for(JsonElement entry : list.asList())
		{
			Type type = Type.readFromJson(entry);
			if(type != null)
				types.add(type);
		}
	}
	
	public static class All extends TypeCondition
	{
		public All(Identifier idIn, Type... typesIn)
		{
			super(idIn, typesIn);
		}
		
		public static All of(Type... typesIn) { return new All(Precondition.HAS_ALL_TYPE.get().registryName, typesIn); }
		
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner)
		{
			return types.isEmpty() || types.stream().allMatch(type -> sheet.types().contains(type));
		}
	}
	
	public static class Any extends TypeCondition
	{
		public Any(Identifier idIn, Type... typesIn)
		{
			super(idIn, typesIn);
		}
		
		public static Any of(Type... typesIn) { return new Any(Precondition.HAS_ANY_TYPE.get().registryName, typesIn); }
		
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner)
		{
			return types.isEmpty() || types.stream().anyMatch(type -> sheet.types().contains(type));
		}
	}
	
	public static class None extends TypeCondition
	{
		public None(Identifier idIn, Type... typesIn)
		{
			super(idIn, typesIn);
		}
		
		public static None of(Type... typesIn) { return new None(Precondition.HAS_NO_TYPE.get().registryName, typesIn); }
		
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner)
		{
			return types.isEmpty() || types.stream().noneMatch(type -> sheet.types().contains(type));
		}
	}
}
