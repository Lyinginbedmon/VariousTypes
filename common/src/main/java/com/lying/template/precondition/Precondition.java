package com.lying.template.precondition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.lying.component.CharacterSheet;
import com.lying.init.VTTypes;
import com.lying.reference.Reference.ModInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/** A requirement that must be met prior to applying this template during character creation */
public abstract class Precondition
{
	private static final Map<Identifier, Supplier<Precondition>> CONDITIONS = new HashMap<>();
	
	/*
	 * Has certain ability
	 * From certain dimension
	 */
	public static final Supplier<Precondition> IS_LIVING	= register(prefix("is_living"), () -> new Precondition(prefix("is_living")) 
	{
		public boolean isValidFor(CharacterSheet sheet, LivingEntity owner) { return !sheet.getTypes().contains(VTTypes.UNDEAD.get()); }
	});
	
	public static final Supplier<Precondition> HAS_TYPE	= register(prefix("has_type"), () -> new IsTypeCondition(prefix("has_type")));
	
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
	
	public JsonObject writeToJson()
	{
		JsonObject data = new JsonObject();
		data.addProperty("Name", registryName.toString());
		write(data);
		return data;
	}
	
	protected JsonObject write(JsonObject data) { return data; }
	
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