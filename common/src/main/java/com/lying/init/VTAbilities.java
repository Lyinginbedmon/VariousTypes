package com.lying.init;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.ability.Ability;
import com.lying.ability.AbilityInstance;
import com.lying.reference.Reference;

import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class VTAbilities
{
	private static final Map<Identifier, Supplier<Ability>> ABILITIES = new HashMap<>();
	
	/** An ability that does nothing but which can be given a custom map name */
	public static final Supplier<Ability> DUMMY = register("dummy", () -> new Ability(withName("dummy"))
	{
		public Identifier mapName(AbilityInstance instance)
		{
			if(instance.memory().contains("MapName", NbtElement.STRING_TYPE))
				return new Identifier(instance.memory().getString("MapName"));
			return super.registryName();
		}
	});
	
	private static Identifier withName(String name) { return new Identifier(Reference.ModInfo.MOD_ID, name); }
	
	public static Supplier<Ability> register(String name, Supplier<Ability> ability)
	{
		ABILITIES.put(withName(name), ability);
		return ability;
	}
	
	public static void init() { }
	
	@Nullable
	public static Ability get(Identifier registryName)
	{
		return exists(registryName) ? ABILITIES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return ABILITIES.containsKey(registryName); }
}