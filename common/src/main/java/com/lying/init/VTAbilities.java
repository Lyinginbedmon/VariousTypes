package com.lying.init;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilityInstance;
import com.lying.reference.Reference.ModInfo;

import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class VTAbilities
{
	private static final Map<Identifier, Supplier<Ability>> ABILITIES = new HashMap<>();
	
	public static final Supplier<Ability> NIGHT_VISION	= register("night_vision", () -> new Ability(prefix("night_vision")));
	public static final Supplier<Ability> SWIM		= register("swim", () -> new Ability(prefix("swim")));
	public static final Supplier<Ability> FLY		= register("fly", () -> new Ability(prefix("fly")));
	public static final Supplier<Ability> BURROW	= register("burrow", () -> new Ability(prefix("burrow")));
	public static final Supplier<Ability> TELEPORT	= register("teleport", () -> new Ability(prefix("teleport")));
	public static final Supplier<Ability> GHOSTLY	= register("ghostly", () -> new Ability(prefix("ghostly")));
	
	/** An ability that does nothing but which can be given a custom map name */
	public static final Supplier<Ability> DUMMY = register("dummy", () -> new Ability(prefix("dummy"))
	{
		public Identifier mapName(AbilityInstance instance)
		{
			if(instance.memory().contains("MapName", NbtElement.STRING_TYPE))
				return new Identifier(instance.memory().getString("MapName"));
			return super.registryName();
		}
	});
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	public static Supplier<Ability> register(String name, Supplier<Ability> ability)
	{
		ABILITIES.put(prefix(name), ability);
		return ability;
	}
	
	public static void init() { VariousTypes.LOGGER.info(" # Initialised "+ABILITIES.size()+" abilities"); }
	
	@Nullable
	public static Ability get(Identifier registryName)
	{
		return exists(registryName) ? ABILITIES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return ABILITIES.containsKey(registryName); }
}