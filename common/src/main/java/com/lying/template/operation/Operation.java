package com.lying.template.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.reference.Reference;
import com.lying.type.DummyType;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/** Something this template does to the creature when it is applied*/
public abstract class Operation
{
	private static final Map<Identifier, Supplier<Operation>> OPERATIONS = new HashMap<>();
	
	public static final Supplier<Operation> LOSE_ALL_TYPES			= register(prefix("lose_all_types"), () -> new SimpleOperation(prefix("lose_all_types")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.clear(); }
	});
	
	public static final Supplier<Operation> LOSE_SUPERTYPES			= register(prefix("lose_supertypes"), () -> new SimpleOperation(prefix("lose_supertypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type.tier() == Tier.SUPERTYPE); }
	});
	
	public static final Supplier<Operation> LOSE_SUBTYPES			= register(prefix("lose_subtypes"), () -> new SimpleOperation(prefix("lose_subtypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type.tier() == Tier.SUBTYPE); }
	});
	
	/** Remove flavour subtypes */
	public static final Supplier<Operation> LOSE_DUMMY_SUBTYPES		= register(prefix("lose_dummy_subtypes"), () -> new SimpleOperation(prefix("lose_dummy_subtypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type instanceof DummyType); }
	});
	
	public static final Supplier<Operation> ADD_TYPES				= register(prefix("add_types"), () -> new TypesOperation.Add(prefix("add_types")));
	
	public static final Supplier<Operation> REMOVE_TYPES			= register(prefix("remove_types"), () -> new TypesOperation.Remove(prefix("remove_types")));
	
	public static final Supplier<Operation> SET_TYPES				= register(prefix("set_types"), () -> new TypesOperation.Set(prefix("set_types")));
	
	/** If target types are present, remove them and add the given types */
	public static final Supplier<Operation> REPLACE_TYPES			= register(prefix("replace_types"), () -> new OperationReplaceTypes(prefix("set_types"), new TypeSet()));
	
	public static final Supplier<Operation> ADD_ABILITY				= register(prefix("add_ability"), () -> new AbilityOperation.Add(prefix("add_ability"), VTAbilities.DUMMY.get().instance(AbilitySource.MISC)));
	
	/** Remove all abilities of the given map name */
	public static final Supplier<Operation> REMOVE_ABILITY			= register(prefix("remove_ability"), () -> new AbilityOperation.Remove(prefix("remove_ability")));
	
	/** Remove all abilities of the same registry name */
	public static final Supplier<Operation> REMOVE_ALL_ABILITY		= register(prefix("remove_all_ability"), () -> new AbilityOperation.RemoveAll(prefix("remove_all_ability")));
	
	/*
	 * Set home dimension
	 */
	
	private static Supplier<Operation> register(Identifier nameIn, Supplier<Operation> supplierIn)
	{
		OPERATIONS.put(nameIn, supplierIn);
		return supplierIn;
	}
	
	@Nullable
	public static Operation get(Identifier nameIn)
	{
		return OPERATIONS.getOrDefault(nameIn, () -> null).get();
	}
	
	private static Identifier prefix(String name) { return Reference.ModInfo.prefix(name); }
	
	private final Identifier registryName;
	
	protected Operation(Identifier nameIn)
	{
		registryName = nameIn;
	}
	
	public Identifier registryName() { return registryName; }
	
	public abstract JsonElement writeToJson(RegistryWrapper.WrapperLookup manager);
	
	@Nullable
	public static Operation readFromJson(JsonElement value)
	{
		if(value.isJsonObject())
		{
			JsonObject data = value.getAsJsonObject();
			Operation op = get(new Identifier(data.get("Name").getAsString()));
			if(op == null)
				return null;
			
			data.remove("Name");
			op.read(data);
			return op;
		}
		return get(new Identifier(value.getAsString()));
	}
	
	protected void read(JsonObject data) { }
	
	public void applyToTypes(TypeSet typeSet) { }
	
	public void applyToAbilities(AbilitySet abilitySet) { }
}