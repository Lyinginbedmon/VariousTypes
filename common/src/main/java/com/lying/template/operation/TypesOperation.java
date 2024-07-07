package com.lying.template.operation;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lying.init.VTTypes;
import com.lying.type.Type;
import com.lying.type.TypeSet;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class TypesOperation extends ConfigurableOperation
{
	protected final List<Type> types = Lists.newArrayList();
	
	protected TypesOperation(Identifier nameIn, Type... typesIn)
	{
		super(nameIn);
		for(Type type : typesIn)
			if(!types.contains(type))
				types.add(type);
	}
	
	public abstract void applyToTypes(TypeSet typeSet);
	
	protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
	{
		JsonArray list = new JsonArray();
		for(Type type : types)
			list.add(type.writeToJson(manager));
		data.add("Types", list);
		return data;
	}

	protected void read(JsonObject data, DynamicRegistryManager manager)
	{
		types.clear();
		if(data.has("Types"))
		{
			JsonArray list = data.getAsJsonArray("Types");
			for(int i=0; i<list.size(); i++)
			{
				Type type = VTTypes.get(new Identifier(list.get(i).getAsString()));
				if(type != null)
					types.add(type);
			}
		}
	}
	
	public static class Add extends TypesOperation
	{
		public Add(Identifier nameIn, Type... typesIn)
		{
			super(nameIn, typesIn);
		}
		
		public static Add of(Type... typesIn) { return new Add(Operation.ADD_TYPES.get().registryName(), typesIn); }
		
		public void applyToTypes(TypeSet typeSet)
		{
			this.types.forEach(type -> typeSet.add(type));
		}
	}
	
	public static class Remove extends TypesOperation
	{
		public Remove(Identifier nameIn, Type... typesIn)
		{
			super(nameIn, typesIn);
		}
		
		public static Remove of(Type... typesIn) { return new Remove(Operation.REMOVE_TYPES.get().registryName(), typesIn); }
		
		public void applyToTypes(TypeSet typeSet)
		{
			this.types.forEach(type -> typeSet.remove(type));
		}
	}
	
	public static class Set extends TypesOperation
	{
		public Set(Identifier nameIn, Type... typesIn)
		{
			super(nameIn, typesIn);
		}
		
		public static Set of(Type... typesIn) { return new Set(Operation.SET_TYPES.get().registryName(), typesIn); }
		
		public void applyToTypes(TypeSet typeSet)
		{
			typeSet.clear();
			this.types.forEach(type -> typeSet.add(type));
		}
	}
}
