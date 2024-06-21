package com.lying.init;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.reference.Reference;
import com.lying.type.Type;

import net.minecraft.util.Identifier;

public class VTTypes
{
	private static final Map<Identifier, Supplier<Type>> TYPES = new HashMap<>();
	
	private static Identifier withName(String name) { return new Identifier(Reference.ModInfo.MOD_ID, name); }
	
	public static Supplier<Type> register(String name, Supplier<Type> ability)
	{
		TYPES.put(withName(name), ability);
		return ability;
	}
	
	public static void init() { }
	
	@Nullable
	public static Type get(Identifier registryName)
	{
		return exists(registryName) ? TYPES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return TYPES.containsKey(registryName); }
}