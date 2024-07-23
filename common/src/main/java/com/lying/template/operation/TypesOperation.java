package com.lying.template.operation;

import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TypesOperation extends ConfigurableOperation
{
	protected final TypeSet types = new TypeSet();
	
	protected TypesOperation(Identifier nameIn, Type... typesIn)
	{
		super(nameIn);
		for(Type type : typesIn)
			types.add(type);
	}
	
	public abstract void applyToTypes(TypeSet typeSet);
	
	protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
	{
		data.add("Types", types.writeToJson(manager));
		return data;
	}
	
	protected void read(JsonObject data)
	{
		types.clear();
		if(data.has("Types"))
			types.addAll(TypeSet.readFromJson(data.getAsJsonArray("Types")));
		
		if(types.isEmpty())
			VariousTypes.LOGGER.warn("Loaded empty types operation in a template, is this correct?");
	}
	
	public static class Add extends TypesOperation
	{
		public Add(Identifier nameIn, Type... typesIn)
		{
			super(nameIn, typesIn);
		}
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.add_types", types.asNameList(manager));
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
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.lose_types", types.asNameList(manager));
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
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.set_types", types.display(manager));
		}
		
		public static Set of(Type... typesIn) { return new Set(Operation.SET_TYPES.get().registryName(), typesIn); }
		
		public void applyToTypes(TypeSet typeSet)
		{
			typeSet.clear();
			typeSet.addAll(this.types);
		}
	}
	
	public static class SetSupertypes extends TypesOperation
	{
		public SetSupertypes(Identifier nameIn, Type... typesIn)
		{
			super(nameIn, typesIn);
		}
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.set_supertypes", types.asNameList(manager));
		}
		
		public static SetSupertypes of(Type... typesIn) { return new SetSupertypes(Operation.SET_SUPERTYPES.get().registryName(), typesIn); }
		
		public void applyToTypes(TypeSet typeSet)
		{
			typeSet.removeIf(type -> type.tier() == Tier.SUPERTYPE);
			this.types.forEach(type -> typeSet.add(type));
		}
	}
}
