package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.utility.Cosmetic;
import com.lying.utility.Cosmetic.Type;

import net.minecraft.util.Identifier;

public class VTCosmetics
{
	private static final Map<Identifier, Supplier<Cosmetic>> COSMETICS_REGISTRY = new HashMap<>();
	
	public static final Supplier<Cosmetic> WINGS_ELYTRA		= wings("elytra");
	public static final Supplier<Cosmetic> WINGS_BUTTERFLY	= wings("butterfly_wings");
	public static final Supplier<Cosmetic> WINGS_DRAGONFLY	= wings("dragonfly_wings");
	public static final Supplier<Cosmetic> WINGS_BEETLE		= wings("beetle_wings");
	public static final Supplier<Cosmetic> WINGS_BIRD		= wings("bird_wings");
	public static final Supplier<Cosmetic> WINGS_ANGEL		= wings("angel_wings");
	public static final Supplier<Cosmetic> WINGS_WITCH		= wings("witch_wings");
	public static final Supplier<Cosmetic> WINGS_BAT		= wings("bat_wings");
	public static final Supplier<Cosmetic> WINGS_DRAGON		= wings("dragon_wings");
	public static final Supplier<Cosmetic> WINGS_SKELETON	= wings("skeleton_wings");
	
	public static final Supplier<Cosmetic> NOSE_PIGLIN		= register(prefix("piglin_nose"), Type.NOSE);
	public static final Supplier<Cosmetic> EARS_PIGLIN		= register(prefix("piglin_ears"), Type.EARS);
	
	private static Supplier<Cosmetic> wings(String nameIn)
	{
		return register(prefix(nameIn), Type.WINGS);
	}
	
	public static Supplier<Cosmetic> register(Identifier regName, Type type)
	{
		return register(regName, () -> new Cosmetic(regName, type));
	}
	
	public static Supplier<Cosmetic> register(Identifier regName, Supplier<Cosmetic> supplierIn)
	{
		COSMETICS_REGISTRY.put(regName, supplierIn);
		return supplierIn;
	}
	
	public static Optional<Cosmetic> get(Identifier registryName)
	{
		Cosmetic cos = COSMETICS_REGISTRY.getOrDefault(registryName, () -> null).get();
		return cos == null ? Optional.empty() : Optional.of(cos);
	}
	
	public static void init()
	{
		VariousTypes.LOGGER.info(" # Initialised "+COSMETICS_REGISTRY.size()+" cosmetics");
	}
}
