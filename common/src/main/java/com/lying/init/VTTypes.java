package com.lying.init;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilityBurrow;
import com.lying.ability.SingleAttributeAbility;
import com.lying.data.VTTags;
import com.lying.reference.Reference.ModInfo;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.ActionHandler.Operation;
import com.lying.type.DummyType;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

public class VTTypes
{
	private static final Map<Identifier, Supplier<Type>> TYPES = new HashMap<>();
	
	private static final Predicate<TypeSet> IS_OTHALL = types -> types.contains(VTTypes.OTHALL.get());
	
	public static final Supplier<Type> ADUAIN			= register("aduain", () -> Type.Builder.of(prefix("aduain"), Tier.SUPERTYPE).colour(0x0029292)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> ANIMAL			= register("animal", () -> Type.Builder.of(prefix("animal"), Tier.SUPERTYPE).colour(0xFFFF6D).build());
	public static final Supplier<Type> ARTHROPOD		= register("arthropod", () -> Type.Builder.of(prefix("arthropod"), Tier.SUPERTYPE).colour(0x924900)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> CONSTRUCT		= register("construct", () -> Type.Builder.of(prefix("construct"), Tier.SUPERTYPE).colour(0xDB6D00)
			.setActions(ActionHandler.NONE)
			.addAbility(VTAbilities.BONUS_HEALTH.get(), nbt -> nbt.putInt(SingleAttributeAbility.AMOUNT, 5))
			.addAbility(VTAbilities.NIGHT_VISION.get(), VTAbilities.MITHRIDATIC.get()).build());
	public static final Supplier<Type> DRAGON			= register("dragon", () -> Type.Builder.of(prefix("dragon"), Tier.SUPERTYPE).colour(0x920000)
			.addAbility(VTAbilities.BONUS_HEALTH.get(), nbt -> nbt.putInt(SingleAttributeAbility.AMOUNT, 10))
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> ELEMENT			= register("element", () -> Type.Builder.of(prefix("element"), Tier.SUPERTYPE).colour(0xFF6DB6)
			.setActions(ActionHandler.REGEN_ONLY)
			.addAbility(VTAbilities.MITHRIDATIC.get()).build());
	public static final Supplier<Type> FAE				= register("fae", () -> Type.Builder.of(prefix("fae"), Tier.SUPERTYPE).colour(0xDDB6DB)
			.addAbility(VTAbilities.BONUS_HEALTH.get(), nbt -> nbt.putInt(SingleAttributeAbility.AMOUNT, -5))
			.addAbility(VTAbilities.FAESKIN.get()).build());
	public static final Supplier<Type> HUMAN			= register("human", () -> Type.Builder.of(prefix("human"), Tier.SUPERTYPE).colour(0x006DDB).build());
	public static final Supplier<Type> OOZE				= register("ooze", () -> Type.Builder.of(prefix("ooze"), Tier.SUPERTYPE).colour(0xB6DBFF)
			.setActions(ActionHandler.of(Action.EAT.get(), Action.BREATHE.get(), Action.REGEN.get()).allowBreathe(VTTags.AIR))
			.addAbility(VTAbilities.BONUS_HEALTH.get(), nbt -> nbt.putInt(SingleAttributeAbility.AMOUNT, 5))
			.addAbility(VTAbilities.MITHRIDATIC.get(), VTAbilities.GELATINOUS.get(), VTAbilities.SCULK_SIGHT.get()).build());
	public static final Supplier<Type> OTHALL			= register("othall", () -> Type.Builder.of(prefix("othall"), Tier.SUPERTYPE).colour(0xB66DFF)
			.setActions(ActionHandler.of(Action.BREATHE.get(), Action.REGEN.get()).allowBreathe(VTTags.AIR))
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> PLANT			= register("plant", () -> Type.Builder.of(prefix("plant"), Tier.SUPERTYPE).colour(0x24FF24)
			.setActions(ActionHandler.of(Action.BREATHE.get(), Action.SLEEP.get(), Action.REGEN.get()).allowBreathe(VTTags.AIR))
			.addAbility(VTAbilities.MITHRIDATIC.get()).build());
	public static final Supplier<Type> UNDEAD			= register("undead", () -> Type.Builder.of(prefix("undead"), Tier.SUPERTYPE).colour(0x1F1F1F)
			.setActions(ActionHandler.NONE)
			.addAbility(VTAbilities.BONUS_HEALTH.get(), nbt -> nbt.putInt(SingleAttributeAbility.AMOUNT, 10))
			.addAbility(VTAbilities.NIGHT_VISION.get(), VTAbilities.MITHRIDATIC.get()).build());
	
	public static final Supplier<Type> NATIVE			= register("native", () -> Type.Builder.of(prefix("native"), Tier.SUBTYPE).description(ModInfo.translate("type", "native.desc"))
			.setActions(ActionHandler.of()
				.add(Operation.add(IS_OTHALL, Action.EAT.get()))
				.add(Operation.add(IS_OTHALL, Action.SLEEP.get()))).build());
	public static final Supplier<Type> OTHAKIN			= register("othakin", () -> Type.Builder.of(prefix("othakin"), Tier.SUBTYPE).description(ModInfo.translate("type", "othakin.desc")).build());
	public static final Supplier<Type> ALTERED			= register("altered", () -> Type.Builder.of(prefix("altered"), Tier.SUBTYPE).description(ModInfo.translate("type", "altered.desc")).build());
	public static final Supplier<Type> AIR				= register("air", () -> Type.Builder.of(prefix("air"), Tier.SUBTYPE)
			.addAbility(VTAbilities.FLY.get()).build());
	public static final Supplier<Type> EARTH			= register("earth", () -> Type.Builder.of(prefix("earth"), Tier.SUBTYPE)
			.addAbility(AbilityBurrow.of(BlockTags.DIRT, BlockTags.BASE_STONE_OVERWORLD, BlockTags.BASE_STONE_NETHER)).build());
	public static final Supplier<Type> FIRE				= register("fire", () -> Type.Builder.of(prefix("fire"), Tier.SUBTYPE).build());
	public static final Supplier<Type> WATER			= register("water", () -> Type.Builder.of(prefix("water"), Tier.SUBTYPE)
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.water())
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.air())
			.addAbility(VTAbilities.SWIM.get()).build());
	public static final Supplier<Type> AQUATIC			= register("aquatic", () -> Type.Builder.of(prefix("aquatic"), Tier.SUBTYPE).description(ModInfo.translate("type", "aquatic.desc"))
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.water())
			.addAbility(VTAbilities.SUFFOCATE_FLUID.get(), AbilityBreathing.air())
			.addAbility(VTAbilities.SWIM.get()).build());
	public static final Supplier<Type> SPECTRAL			= register("spectral", () -> Type.Builder.of(prefix("spectral"), Tier.SUBTYPE)
			.addAbility(VTAbilities.INTANGIBLE.get()).build());
	
	/** A do-nothing subtype that can be given a customised ID and display name, primarily for flavour purposes */
	public static final Supplier<Type> DUMMY	= register("dummy", () -> DummyType.Builder.of(prefix("dummy")).build());
	
	public static final Supplier<Type> GOBLINOID	= dummyType("goblinoid");
	public static final Supplier<Type> LINN			= dummyTypeWithDesc("linn");
	public static final Supplier<Type> MUINTIR		= dummyTypeWithDesc("muintir");
	public static final Supplier<Type> NAIMHDE		= dummyTypeWithDesc("naimhde");
	public static final Supplier<Type> ORKIN		= dummyTypeWithDesc("orkin");
	public static final Supplier<Type> REPTILIAN	= dummyType("reptilian");
	public static final Supplier<Type> VERDINE		= dummyType("verdine");	// Elves
	
	public static Collection<Identifier> typeIds() { return TYPES.keySet(); }
	
	private static final Supplier<Type> dummyType(String name) { return () -> DummyType.create(prefix(name), ModInfo.translate("subtype", name)); }
	
	private static final Supplier<Type> dummyTypeWithDesc(String name) { return () -> DummyType.create(prefix(name), ModInfo.translate("subtype", name), ModInfo.translate("subtype", name+".desc")); }
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	public static Supplier<Type> register(String name, Supplier<Type> ability)
	{
		TYPES.put(prefix(name), ability);
		return ability;
	}
	
	public static void init()
	{
		Map<Type.Tier, Integer> tally = new HashMap<>();
		TYPES.values().forEach(val -> 
		{
			Tier tier = val.get().tier();
			tally.put(tier, tally.getOrDefault(tier, 0) + 1);
		});
		VariousTypes.LOGGER.info(" # Initialised "+tally.getOrDefault(Tier.SUPERTYPE, 0)+" supertypes, "+tally.getOrDefault(Tier.SUBTYPE, 0)+" subtypes");
	}
	
	@Nullable
	public static Type get(Identifier registryName)
	{
		return exists(registryName) ? TYPES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return TYPES.containsKey(registryName); }
	
	public static List<Supplier<Type>> ofTier(Tier tierIn)
	{
		List<Supplier<Type>> types = Lists.newArrayList();
		TYPES.values().stream().filter(sup -> sup.get().tier() == tierIn).forEach(type -> types.add(type));
		return types;
	}
	
	@Nullable
	public static Type fromNbt(NbtElement nbt)
	{
		switch(nbt.getType())
		{
			case NbtElement.STRING_TYPE:
				return get(new Identifier(nbt.asString()));
			case NbtElement.COMPOUND_TYPE:
				NbtCompound compound = (NbtCompound)nbt;
				if(compound.contains("Type", NbtElement.STRING_TYPE))
				{
					Type type = get(new Identifier(compound.getString("Type")));
					if(type == null)
						return null;
					type.read(compound);
					return type;
				}
				else
					return DummyType.fromNbt(nbt);
			default:
				return null;
		}
	}
}