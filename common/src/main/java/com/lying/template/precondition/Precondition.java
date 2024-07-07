package com.lying.template.precondition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference.ModInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/** A requirement that must be met prior to applying this template during character creation */
public abstract class Precondition
{
	private static final Map<Identifier, Supplier<Precondition>> CONDITIONS = new HashMap<>();
	
	/*
	 * Has certain ability
	 * From certain dimension
	 */
	public static final Supplier<Precondition> IS_LIVING	= register(prefix("is_living"), () -> new SimpleCondition(prefix("is_living")) 
	{
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner) { return !sheet.getTypes().contains(VTTypes.UNDEAD.get()); }
	});
	
	public static final Supplier<Precondition> PHYSICAL		= register(prefix("physical"), () -> new SimpleCondition(prefix("physical"))
	{
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner) { return !sheet.getAbilities().hasAbility(VTAbilities.GHOSTLY.get().registryName()); }
	});
	
	public static final Supplier<Precondition> HAS_ALL_TYPE	= register(prefix("has_all_types"), () -> new TypeCondition.All(prefix("has_all_types")));
	public static final Supplier<Precondition> HAS_ANY_TYPE	= register(prefix("has_any_types"), () -> new TypeCondition.All(prefix("has_any_types")));
	public static final Supplier<Precondition> HAS_NO_TYPE	= register(prefix("has_no_types"), () -> new TypeCondition.All(prefix("has_no_types")));
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	private static Supplier<Precondition> register(Identifier nameIn, Supplier<Precondition> supplierIn)
	{
		CONDITIONS.put(nameIn, supplierIn);
		return supplierIn;
	}
	
	@Nullable
	public static Precondition get(Identifier name)
	{
		return CONDITIONS.getOrDefault(name, () -> null).get();
	}
	
	protected final Identifier registryName;
	
	protected Precondition(Identifier idIn)
	{
		this.registryName = idIn;
	}
	
	public abstract boolean isValidFor(CharacterSheet sheet, LivingEntity owner);
	
	public abstract JsonElement writeToJson(RegistryWrapper.WrapperLookup manager);
	
	public static Precondition readFromJson(JsonObject data)
	{
		Precondition condition = get(new Identifier(data.get("Name").getAsString()));
		if(condition == null)
			return null;
		data.remove("Name");
		condition.read(data);
		return condition;
	}
	
	protected void read(JsonObject data) { }
}