package com.lying.template.precondition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class ConfigurableCondition extends Precondition
{
	public ConfigurableCondition(Identifier idIn)
	{
		super(idIn);
	}
	
	public final JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		JsonObject data = new JsonObject();
		data.addProperty("Name", registryName.toString());
		write(data, manager);
		return data;
	}
	
	protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager) { return data; }
}
