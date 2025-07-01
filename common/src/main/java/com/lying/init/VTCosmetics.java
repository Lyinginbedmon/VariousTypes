package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;

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
//	public static final Supplier<Cosmetic> WINGS_FAIRY		= wings("fairy_wings");
//	public static final Supplier<Cosmetic> WINGS_ENERGY		= wings("energy_wings");
	
	public static final Supplier<Cosmetic> NOSE_PIG			= nose("pig_nose");
	public static final Supplier<Cosmetic> NOSE_PIGLIN		= nose("piglin_nose");
	public static final Supplier<Cosmetic> NOSE_PIGZOMBIE	= nose("zombified_piglin_nose");
	public static final Supplier<Cosmetic> NOSE_VILLAGER	= nose("villager_nose");
	public static final Supplier<Cosmetic> NOSE_WITCH		= nose("witch_nose");
	
	public static final Supplier<Cosmetic> EARS_PIGLIN		= ears("piglin_ears");
	public static final Supplier<Cosmetic> EARS_PIGZOMBIE	= ears("zombified_piglin_ear");
	public static final Supplier<Cosmetic> EARS_AXOLOTL		= ears("axolotl_gills");
	public static final Supplier<Cosmetic> EARS_ELF			= ears("elf_ears");
	public static final Supplier<Cosmetic> EARS_FOX			= ears("fox_ears");
	public static final Supplier<Cosmetic> EARS_WOLF		= ears("wolf_ears");
	public static final Supplier<Cosmetic> EARS_CAT			= ears("cat_ears");
	public static final Supplier<Cosmetic> EARS_RABBIT		= ears("rabbit_ears");
	public static final Supplier<Cosmetic> EARS_GOBLIN		= ears("goblin_ears");
	
	public static final Supplier<Cosmetic> HORNS_SAIGA		= horns("saiga_horns");
	public static final Supplier<Cosmetic> HORNS_RAM		= horns("ram_horns");
	public static final Supplier<Cosmetic> HORNS_STAG		= horns("stag_antlers");
	public static final Supplier<Cosmetic> HORNS_KIRIN		= horns("kirin_horns");
	public static final Supplier<Cosmetic> HORNS_LIGHTNING	= horns("lightning_horns");
	public static final Supplier<Cosmetic> HORN_UNICORN		= horns("unicorn_horn");
	public static final Supplier<Cosmetic> HORNS_DEVIL		= horns("devil_horns");
	
	public static final Supplier<Cosmetic> TAIL_DRAGON		= tail("dragon_tail");
	public static final Supplier<Cosmetic> TAIL_KIRIN		= tail("kirin_tail");
	public static final Supplier<Cosmetic> TAIL_FOX			= tail("fox_tail");
	public static final Supplier<Cosmetic> TAIL_WOLF		= tail("wolf_tail");
//	public static final Supplier<Cosmetic> TAIL_CAT			= tail("cat_tail");
	public static final Supplier<Cosmetic> TAIL_RAT			= tail("rat_tail");
	public static final Supplier<Cosmetic> TAIL_RABBIT		= tail("rabbit_tail");
	public static final Supplier<Cosmetic> TAIL_AXOLOTL		= tail("axolotl_tail");
	public static final Supplier<Cosmetic> TAIL_SCORPION	= tail("scorpion_tail");
	public static final Supplier<Cosmetic> TAIL_WHALE		= tail("whale_tail");
	
	public static final Supplier<Cosmetic> MISC_GLOW_SPOTS	= misc("verdine_spots");
	public static final Supplier<Cosmetic> MISC_GELATINOUS	= misc("gelatinous");
	public static final Supplier<Cosmetic> MISC_GHOSTLY		= misc("ghostly");
	public static final Supplier<Cosmetic> MISC_HALO		= misc("halo");
//	public static final Supplier<Cosmetic> MISC_SHARK_FIN	= misc("shark_fin");
	
	public static final Supplier<Cosmetic> ICON_ASTRAL_EYE		= icon("astral_eye");
	public static final Supplier<Cosmetic> ICON_DIVINE_CROWN	= icon("divine_crown");
	public static final Supplier<Cosmetic> ICON_ETERNAL_FLAME	= icon("eternal_flame");
	public static final Supplier<Cosmetic> ICON_GEM				= icon("gem_icon");
	public static final Supplier<Cosmetic> ICON_CRYSTAL			= icon("crystal_icon");
	public static final Supplier<Cosmetic> ICON_CURRENCY		= icon("currency_icon");
	public static final Supplier<Cosmetic> ICON_EXCLAMATION		= icon("exclamation_icon");
	public static final Supplier<Cosmetic> ICON_QUESTION		= icon("question_icon");
	public static final Supplier<Cosmetic> ICON_SQUARE			= icon("square_icon");
	public static final Supplier<Cosmetic> ICON_CIRCLE			= icon("circle_icon");
	public static final Supplier<Cosmetic> ICON_TRIANGLE		= icon("triangle_icon");
	public static final Supplier<Cosmetic> ICON_HEXAGON			= icon("hexagon_icon");
	
	private static Supplier<Cosmetic> wings(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.WINGS); }
	private static Supplier<Cosmetic> nose(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.NOSE); }
	private static Supplier<Cosmetic> ears(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.EARS); }
	private static Supplier<Cosmetic> horns(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.HORNS); }
	private static Supplier<Cosmetic> tail(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.TAIL); }
	private static Supplier<Cosmetic> icon(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.ICON); }
	private static Supplier<Cosmetic> misc(String nameIn) { return register(prefix(nameIn), VTCosmeticTypes.MISC); }
	
	public static Supplier<Cosmetic> register(Identifier regName, Supplier<CosmeticType> type)
	{
		return register(() -> new Cosmetic(regName, type));
	}
	
	public static Supplier<Cosmetic> register(Supplier<Cosmetic> supplierIn)
	{
		COSMETICS_REGISTRY.put(supplierIn.get().registryName(), supplierIn);
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
		Map<CosmeticType, Integer> tallies = new HashMap<>();
		COSMETICS_REGISTRY.values().forEach(cos -> tallies.put(cos.get().type(), tallies.getOrDefault(cos.get().type(), 0) + 1));
		tallies.entrySet().stream().sorted((a,b) -> a.getValue() < b.getValue() ? -1 : a.getValue() > b.getValue() ? 1 : 0).forEach(entry -> VariousTypes.LOGGER.info(" # - {} {}", entry.getValue(), entry.getKey().registryName().getPath()));
	}
}
