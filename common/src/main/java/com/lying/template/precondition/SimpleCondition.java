package com.lying.template.precondition;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class SimpleCondition extends Precondition
{
	public SimpleCondition(Identifier idIn)
	{
		super(idIn);
	}
	
	public final JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		return new JsonPrimitive(registryName.toString());
	}
}
