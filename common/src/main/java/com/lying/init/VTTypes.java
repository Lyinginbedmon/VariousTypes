package com.lying.init;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.AbilityBreathing;
import com.lying.reference.Reference.ModInfo;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.ActionHandler.Operation;
import com.lying.type.DummyType;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

public class VTTypes
{
	private static final Map<Identifier, Supplier<Type>> TYPES = new HashMap<>();
	
	private static final Predicate<TypeSet> IS_OTHALL = types -> types.contains(VTTypes.OTHALL.get());
	
	public static final Supplier<Type> ANIMAL			= register("animal", () -> Type.Builder.of(prefix("animal"), Tier.SUPERTYPE).build());
	public static final Supplier<Type> ADUAIN			= register("aduain", () -> Type.Builder.of(prefix("aduain"), Tier.SUPERTYPE)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> ARTHROPOD		= register("arthropod", () -> Type.Builder.of(prefix("arthropod"), Tier.SUPERTYPE)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> CONSTRUCT		= register("construct", () -> Type.Builder.of(prefix("construct"), Tier.SUPERTYPE)
			.setActions(ActionHandler.NONE)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> DRAGON			= register("dragon", () -> Type.Builder.of(prefix("dragon"), Tier.SUPERTYPE)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> ELEMENT			= register("element", () -> Type.Builder.of(prefix("element"), Tier.SUPERTYPE)
			.setActions(ActionHandler.REGEN_ONLY).build());
	public static final Supplier<Type> FAE				= register("fae", () -> Type.Builder.of(prefix("fae"), Tier.SUPERTYPE).build());
	public static final Supplier<Type> HUMAN			= register("human", () -> Type.Builder.of(prefix("human"), Tier.SUPERTYPE).build());
	public static final Supplier<Type> OOZE				= register("ooze", () -> Type.Builder.of(prefix("ooze"), Tier.SUPERTYPE)
			.setActions(ActionHandler.of(Action.EAT.get(), Action.BREATHE.get(), Action.REGEN.get())).build());
	public static final Supplier<Type> OTHALL			= register("othall", () -> Type.Builder.of(prefix("othall"), Tier.SUPERTYPE)
			.setActions(ActionHandler.of(Action.BREATHE.get(), Action.REGEN.get()).allowBreathe(Fluids.EMPTY))
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	public static final Supplier<Type> PLANT			= register("plant", () -> Type.Builder.of(prefix("plant"), Tier.SUPERTYPE)
			.setActions(ActionHandler.of(Action.BREATHE.get(), Action.SLEEP.get(), Action.REGEN.get()).allowBreathe(Fluids.EMPTY)).build());
	public static final Supplier<Type> UNDEAD			= register("undead", () -> Type.Builder.of(prefix("undead"), Tier.SUPERTYPE)
			.setActions(ActionHandler.NONE)
			.addAbility(VTAbilities.NIGHT_VISION.get()).build());
	
	public static final Supplier<Type> NATIVE			= register("native", () -> Type.Builder.of(prefix("native"), Tier.SUBTYPE)
			.setActions(ActionHandler.of()
				.add(Operation.add(IS_OTHALL, Action.EAT.get()))
				.add(Operation.add(IS_OTHALL, Action.SLEEP.get()))).build());
	public static final Supplier<Type> OTHAKIN			= register("othakin", () -> Type.Builder.of(prefix("othakin"), Tier.SUBTYPE).build());
	public static final Supplier<Type> ALTERED			= register("altered", () -> Type.Builder.of(prefix("altered"), Tier.SUBTYPE).build());
	public static final Supplier<Type> AIR				= register("air", () -> Type.Builder.of(prefix("air"), Tier.SUBTYPE)
			.addAbility(VTAbilities.FLY.get()).build());
	public static final Supplier<Type> EARTH			= register("earth", () -> Type.Builder.of(prefix("earth"), Tier.SUBTYPE)
			.addAbility(VTAbilities.BURROW.get()).build());
	public static final Supplier<Type> FIRE				= register("fire", () -> Type.Builder.of(prefix("fire"), Tier.SUBTYPE).build());
	public static final Supplier<Type> WATER			= register("water", () -> Type.Builder.of(prefix("water"), Tier.SUBTYPE)
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.air())
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.water())
			.addAbility(VTAbilities.SWIM.get()).build());
	public static final Supplier<Type> AQUATIC			= register("aquatic", () -> Type.Builder.of(prefix("aquatic"), Tier.SUBTYPE)
			.addAbility(VTAbilities.BREATHE_FLUID.get(), AbilityBreathing.water())
			.addAbility(VTAbilities.SUFFOCATE_FLUID.get(), AbilityBreathing.air())
			.addAbility(VTAbilities.SWIM.get()).build());
	public static final Supplier<Type> SPECTRAL			= register("spectral", () -> Type.Builder.of(prefix("spectral"), Tier.SUBTYPE)
			.addAbility(VTAbilities.GHOSTLY.get()).build());
	
	/** A do-nothing subtype that can be given a customised ID and display name, primarily for flavour purposes */
	public static final Supplier<Type> DUMMY	= register("dummy", () -> DummyType.Builder.of(prefix("dummy")).build());
	
	public static final Type GOBLINOID	= dummyType("goblinoid");
	public static final Type LINN		= dummyType("linn");	// Players
	public static final Type MUINTIR	= dummyType("muintir");	// Villagers
	public static final Type NAIMHDE	= dummyType("naimhde");	// Illagers
	public static final Type ORKIN		= dummyType("orkin");	// Piglins
	public static final Type REPTILIAN	= dummyType("reptilian");
	public static final Type VERDINE	= dummyType("verdine");	// Elves
	
	private static final Type dummyType(String name) { return DummyType.create(prefix(name), ModInfo.translate("subtype", name)); }
	
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
		VariousTypes.LOGGER.info("# Initialised "+tally.getOrDefault(Tier.SUPERTYPE, 0)+" supertypes, "+tally.getOrDefault(Tier.SUBTYPE, 0)+" subtypes");
	}
	
	@Nullable
	public static Type get(Identifier registryName)
	{
		return exists(registryName) ? TYPES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return TYPES.containsKey(registryName); }
}