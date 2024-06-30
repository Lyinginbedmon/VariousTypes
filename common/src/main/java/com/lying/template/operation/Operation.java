package com.lying.template.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.reference.Reference;
import com.lying.type.DummyType;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.util.Identifier;

/** Something this template does to the creature when it is applied*/
public class Operation
{
	private static final Map<Identifier, Supplier<Operation>> OPERATIONS = new HashMap<>();
	
	public static final Supplier<Operation> LOSE_ALL_TYPES			= register(prefix("lose_all_types"), () -> new Operation(prefix("lose_all_types")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.clear(); }
	});
	
	public static final Supplier<Operation> LOSE_SUPERTYPES			= register(prefix("lose_supertypes"), () -> new Operation(prefix("lose_supertypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type.tier() == Tier.SUPERTYPE); }
	});
	
	public static final Supplier<Operation> LOSE_SUBTYPES			= register(prefix("lose_subtypes"), () -> new Operation(prefix("lose_subtypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type.tier() == Tier.SUBTYPE); }
	});
	
	/** Remove flavour subtypes */
	public static final Supplier<Operation> LOSE_DUMMY_SUBTYPES		= register(prefix("lose_dummy_subtypes"), () -> new Operation(prefix("lose_dummy_subtypes")) 
	{
		public void applyToTypes(TypeSet typeSet) { typeSet.removeIf(type -> type instanceof DummyType); }
	});
	
	/*
	 * Add types
	 * Remove types
	 * Replace types
	 * 
	 * Set home dimension
	 * 
	 * Add ability
	 * Remove ability(s)
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
	
	public JsonObject writeToJson()
	{
		JsonObject data = new JsonObject();
		data.addProperty("Name", registryName().toString());
		write(data);
		return data;
	}
	
	protected JsonObject write(JsonObject data) { return data; }
	
	@Nullable
	public static Operation readFromJson(JsonObject data)
	{
		Operation op = get(new Identifier(data.get("Name").getAsString()));
		if(op == null)
			return null;
		data.remove("Name");
		op.read(data);
		return op;
	}
	
	protected void read(JsonObject data) { }
	
	public void applyToTypes(TypeSet typeSet) { }
	
	public void applyToAbilities(AbilitySet abilitySet) { }
}