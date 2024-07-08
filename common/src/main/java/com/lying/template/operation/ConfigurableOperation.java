package com.lying.template.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class ConfigurableOperation extends Operation
{
	public ConfigurableOperation(Identifier nameIn)
	{
		super(nameIn);
	}
	
	public final JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		JsonObject data = new JsonObject();
		data.addProperty("Name", registryName().toString());
		write(data, manager);
		return data;
	}
	
	protected abstract JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager);
}
