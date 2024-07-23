package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilityInstance;
import com.lying.ability.ActivatedAbility;
import com.lying.ability.ToggledAbility;
import com.lying.data.VTTags;
import com.lying.reference.Reference;
import com.lying.type.Action;
import com.lying.utility.ServerEvents;
import com.lying.utility.ServerEvents.Result;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class VTAbilities
{
	private static final Map<Identifier, Supplier<Ability>> ABILITIES = new HashMap<>();
	
	public static final Supplier<Ability> BREATHE_FLUID		= register("breathe_in_fluid", () -> new AbilityBreathing.Allow(prefix("breathe_in_fluid")));
	public static final Supplier<Ability> SUFFOCATE_FLUID	= register("suffocate_in_fluid", () -> new AbilityBreathing.Deny(prefix("suffocate_in_fluid")));
	public static final Supplier<Ability> AMPHIBIOUS		= register("amphibious", () -> new Ability(prefix("amphibious"))
	{
		public void registerEventHandlers()
		{
			ServerEvents.SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
			{
				// Adds the ability to breathe air after it may have been denied by other breathing abilities
				if(!handler.canBreathe(Fluids.EMPTY) && abilities.hasAbility(registryName()))
					handler.allowBreathe(VTTags.AIR);
			});
		}
	});
	public static final Supplier<Ability> NIGHT_VISION	= register("night_vision", () -> new ToggledAbility(prefix("night_vision")) 
	{
		public void registerEventHandlers()
		{
			ServerEvents.LivingEvents.GET_STATUS_EFFECT_EVENT.register((effect,living,abilities,actual) -> 
			{
				if(
					effect == StatusEffects.NIGHT_VISION 
					&& abilities.hasAbility(registryName()) 
					&& ToggledAbility.hasActive(abilities, registryName())
					)
					return new StatusEffectInstance(effect, Reference.Values.TICKS_PER_MINUTE, 0, true, false);
				return actual;
			});
		}
	});
	public static final Supplier<Ability> SCULK_SIGHT	= register("sculk_sight", () -> new ToggledAbility(prefix("sculk_sight")));
	public static final Supplier<Ability> SWIM			= register("swim", () -> new ActivatedAbility(prefix("swim")) 
	{
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
		{
			return owner.isSwimming();
		}
		
		protected void activate(LivingEntity owner, AbilityInstance instance)
		{
			owner.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, Reference.Values.TICKS_PER_SECOND * 3, 0, true, true));
		}
	});
	public static final Supplier<Ability> CLIMB			= register("climb", () -> new ToggledAbility(prefix("climb")));
	public static final Supplier<Ability> FLY			= register("fly", () -> new ToggledAbility(prefix("fly")) {});
	public static final Supplier<Ability> BURROW		= register("burrow", () -> new ToggledAbility(prefix("burrow")));
	public static final Supplier<Ability> TELEPORT		= register("teleport", () -> new ActivatedAbility(prefix("teleport")) {	// LoS teleport
		protected void activate(LivingEntity owner, AbilityInstance instance) { }});
	public static final Supplier<Ability> GHOSTLY		= register("ghostly", () -> new ToggledAbility(prefix("ghostly")));	// Incorporeal
	public static final Supplier<Ability> BURN_IN_SUN	= register("burn_in_sun", () -> new Ability(prefix("burn_in_sun")));
	public static final Supplier<Ability> CRITPROOF		= register("critproof", () -> new Ability(prefix("critproof")));	// Immune to critical hits
	public static final Supplier<Ability> MITHRIDATIC	= register("mithridatic", () -> new Ability(prefix("mithridatic")) 
	{
		public void registerEventHandlers()
		{
			ServerEvents.LivingEvents.CAN_HAVE_STATUS_EFFECT_EVENT.register((effect,abilities,result) -> effect.getEffectType().isIn(VTTags.POISONS) && abilities.hasAbilityInstance(registryName()) ? Result.DENY : result);
		}
	});
	public static final Supplier<Ability> REGENERATION	= register("regeneration", () -> new Ability(prefix("regeneration")));
	public static final Supplier<Ability> NAT_ARMOUR	= register("natural_armour", () -> new Ability(prefix("natural_armour")));
	public static final Supplier<Ability> DEEP_BREATH	= register("deep_breath", () -> new Ability(prefix("deep_breath"))
	{
		public void registerEventHandlers()
		{
			ServerEvents.LivingEvents.GET_MAX_AIR_EVENT.register((abilities,air) -> abilities.hasAbility(prefix("deep_breath")) ? air * 2 : air);
		}
	});
	public static final Supplier<Ability> MENDING		= register("mending", () -> new Ability(prefix("mending"))
	{
		public void registerEventHandlers()
		{
			ServerEvents.SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
			{
				// Adds the ability to breathe air after it may have been denied by other breathing abilities
				if(!handler.can(Action.REGEN.get()) && abilities.hasAbility(registryName()))
					handler.activate(Action.REGEN.get());
			});
		}
	});
	
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
	
	public static Supplier<Ability> register(String name, Supplier<Ability> ability)
	{
		ABILITIES.put(prefix(name), ability);
		return ability;
	}
	
	public static void init()
	{
		ABILITIES.values().forEach(entry -> entry.get().registerEventHandlers());
		VariousTypes.LOGGER.info(" # Initialised "+ABILITIES.size()+" abilities");
	}
	
	@Nullable
	public static Ability get(Identifier registryName)
	{
		return exists(registryName) ? ABILITIES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return ABILITIES.containsKey(registryName); }
}