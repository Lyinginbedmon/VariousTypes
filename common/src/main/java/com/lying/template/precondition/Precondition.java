package com.lying.template.precondition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.reference.Reference.ModInfo;
import com.lying.type.TypeSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
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
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner) { return !sheet.<TypeSet>element(VTSheetElements.TYPES).containsAny(VTTypes.CONSTRUCT.get(), VTTypes.UNDEAD.get()); }
	});
	
	public static final Supplier<Precondition> PHYSICAL		= register(prefix("is_physical"), () -> new SimpleCondition(prefix("is_physical"))
	{
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner) { return !sheet.<AbilitySet>element(VTSheetElements.ABILITES).hasAbility(VTAbilities.GHOSTLY.get().registryName()); }
	});
	
	public static final Supplier<Precondition> HAS_ALL_TYPE	= register(prefix("has_all_of_types"), () -> new TypeCondition.All(prefix("has_all_of_types")));
	public static final Supplier<Precondition> HAS_ANY_TYPE	= register(prefix("has_any_of_types"), () -> new TypeCondition.Any(prefix("has_any_of_types")));
	public static final Supplier<Precondition> HAS_NO_TYPE	= register(prefix("has_none_of_types"), () -> new TypeCondition.None(prefix("has_none_of_types")));
	
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
	
	public Text describe(DynamicRegistryManager manager) { return Reference.ModInfo.translate("precondition", registryName.getPath()); }
	
	public abstract boolean isValidFor(CharacterSheet sheet, LivingEntity owner);
	
	public abstract JsonElement writeToJson(RegistryWrapper.WrapperLookup manager);
	
	public static Precondition readFromJson(JsonElement value)
	{
		if(value.isJsonObject())
		{
			JsonObject data = value.getAsJsonObject();
			Precondition condition = get(new Identifier(data.get("Name").getAsString()));
			if(condition == null)
				return null;
			data.remove("Name");
			condition.read(data);
			return condition;
		}
		
		return get(new Identifier(value.getAsString()));
	}
	
	protected void read(JsonObject data) { }
}