package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
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
	
	public static final Supplier<Cosmetic> NOSE_PIG			= nose("pig_nose");
	public static final Supplier<Cosmetic> NOSE_PIGLIN		= nose("piglin_nose");
	public static final Supplier<Cosmetic> NOSE_PIGZOMBIE	= nose("zombified_piglin_nose");
	
	public static final Supplier<Cosmetic> EARS_PIGLIN		= ears("piglin_ears");
	public static final Supplier<Cosmetic> EARS_PIGZOMBIE	= ears("zombified_piglin_ear");
	public static final Supplier<Cosmetic> EARS_AXOLOTL		= ears("axolotl_gills");
	public static final Supplier<Cosmetic> EARS_ELF			= ears("elf_ears");
	
	public static final Supplier<Cosmetic> HORNS_HARTEBEEST	= horns("hartebeest_horns");
	
	public static final Supplier<Cosmetic> TAIL_DRAGON		= tail("dragon_tail");
	public static final Supplier<Cosmetic> TAIL_KIRIN		= tail("kirin_tail");
	
	public static final Supplier<Cosmetic> MISC_GLOW_SPOTS	= misc("verdine_spots");
	
	private static Supplier<Cosmetic> wings(String nameIn) { return register(prefix(nameIn), Type.WINGS); }
	private static Supplier<Cosmetic> nose(String nameIn) { return register(prefix(nameIn), Type.NOSE); }
	private static Supplier<Cosmetic> ears(String nameIn) { return register(prefix(nameIn), Type.EARS); }
	private static Supplier<Cosmetic> horns(String nameIn) { return register(prefix(nameIn), Type.HORNS); }
	private static Supplier<Cosmetic> tail(String nameIn) { return register(prefix(nameIn), Type.TAIL); }
	private static Supplier<Cosmetic> misc(String nameIn) { return register(prefix(nameIn), Type.MISC); }
	
	public static Supplier<Cosmetic> register(Identifier regName, Type type)
	{
		return register(regName, () -> new Cosmetic(regName, type));
	}
	
	public static Supplier<Cosmetic> register(Identifier regName, Supplier<Cosmetic> supplierIn)
	{
		COSMETICS_REGISTRY.put(regName, supplierIn);
		return supplierIn;
	}
	
	public static Collection<Identifier> cosmeticIds() { return COSMETICS_REGISTRY.keySet(); }
	
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
