package com.lying.template.operation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class SimpleOperation extends Operation
{
	public SimpleOperation(Identifier nameIn)
	{
		super(nameIn);
	}
	
	public final JsonElement writeToJson(RegistryWrapper.WrapperLookup manager) { return new JsonPrimitive(registryName().toString()); }
}
