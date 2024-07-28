package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.Category;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class VTAbilities
{
	private static final Map<Identifier, Supplier<Ability>> ABILITIES = new HashMap<>();
	
	public static final Supplier<Ability> BREATHE_FLUID		= register("breathe_in_fluid", () -> new AbilityBreathing.Allow(prefix("breathe_in_fluid")));
	public static final Supplier<Ability> SUFFOCATE_FLUID	= register("suffocate_in_fluid", () -> new AbilityBreathing.Deny(prefix("suffocate_in_fluid")));
	public static final Supplier<Ability> AMPHIBIOUS		= register("amphibious", () -> new Ability(prefix("amphibious"), Category.UTILITY)
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
	public static final Supplier<Ability> NIGHT_VISION	= register("night_vision", () -> new ToggledAbility(prefix("night_vision"), Category.UTILITY) 
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
	public static final Supplier<Ability> SCULK_SIGHT	= register("sculk_sight", () -> new ToggledAbility(prefix("sculk_sight"), Category.UTILITY));
	public static final Supplier<Ability> SWIM			= register("swim", () -> new ActivatedAbility(prefix("swim"), Category.UTILITY) 
	{
		public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 5; }
		
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
		{
			return owner.isSwimming();
		}
		
		protected void activate(LivingEntity owner, AbilityInstance instance)
		{
			if(!owner.getWorld().isClient())
				owner.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, Reference.Values.TICKS_PER_SECOND * 3, 0, true, true));
		}
	});
	public static final Supplier<Ability> CLIMB			= register("climb", () -> new ToggledAbility(prefix("climb"), Category.UTILITY));
	public static final Supplier<Ability> FLY			= register("fly", () -> new ToggledAbility(prefix("fly"), Category.UTILITY));
	public static final Supplier<Ability> BURROW		= register("burrow", () -> new ToggledAbility(prefix("burrow"), Category.UTILITY));
	public static final Supplier<Ability> TELEPORT		= register("teleport", () -> new ActivatedAbility(prefix("teleport"), Category.UTILITY){	// LoS teleport
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
		{
			double range = instance.memory().contains("Range", NbtElement.DOUBLE_TYPE) ? instance.memory().getDouble("Range") : 8D;
			return owner.raycast(range, 1F, false).getType() != HitResult.Type.MISS;
		}
		
		protected void activate(LivingEntity owner, AbilityInstance instance)
		{
			double range = instance.memory().contains("Range", NbtElement.DOUBLE_TYPE) ? instance.memory().getDouble("Range") : 8D;
			HitResult trace = owner.raycast(range, 1F, false);
			if(trace.getType() == HitResult.Type.MISS)
				return;
			
			Vec3d hitPos = trace.getPos();
			owner.sendMessage(Text.literal("Hit: "+hitPos.toString()));
		}});
	public static final Supplier<Ability> GHOSTLY		= register("ghostly", () -> new ToggledAbility(prefix("ghostly"), Category.UTILITY));	// Incorporeal
	public static final Supplier<Ability> BURN_IN_SUN	= register("burn_in_sun", () -> new Ability(prefix("burn_in_sun"), Category.UTILITY));
	public static final Supplier<Ability> MITHRIDATIC	= register("mithridatic", () -> new Ability(prefix("mithridatic"), Category.DEFENSE) 
	{
		public void registerEventHandlers()
		{
			ServerEvents.LivingEvents.CAN_HAVE_STATUS_EFFECT_EVENT.register((effect,abilities,result) -> effect.getEffectType().isIn(VTTags.POISONS) && abilities.hasAbilityInstance(registryName()) ? Result.DENY : result);
		}
	});
	public static final Supplier<Ability> REGENERATION	= register("regeneration", () -> new Ability(prefix("regeneration"), Category.DEFENSE));
	public static final Supplier<Ability> NAT_ARMOUR	= register("natural_armour", () -> new Ability(prefix("natural_armour"), Category.DEFENSE));
	public static final Supplier<Ability> DEEP_BREATH	= register("deep_breath", () -> new Ability(prefix("deep_breath"), Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			ServerEvents.LivingEvents.GET_MAX_AIR_EVENT.register((abilities,air) -> abilities.hasAbility(prefix("deep_breath")) ? air * 2 : air);
		}
	});
	public static final Supplier<Ability> MENDING		= register("mending", () -> new Ability(prefix("mending"), Category.DEFENSE)
	{
		public void registerEventHandlers()
		{
			ServerEvents.SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
			{
				// Adds the ability to regenerate health after it may have been denied
				if(!handler.can(Action.REGEN.get()) && abilities.hasAbility(registryName()))
					handler.activate(Action.REGEN.get());
			});
		}
	});
	public static final Supplier<Ability> RUN_CMD	= register("run_command", () -> new ActivatedAbility(prefix("run_command"), Category.UTILITY)
	{
		public Identifier mapName(AbilityInstance instance)
		{
			if(instance.memory().contains("MapName", NbtElement.STRING_TYPE))
				return new Identifier(instance.memory().getString("MapName"));
			return super.mapName(instance);
		}
		
		protected void activate(LivingEntity owner, AbilityInstance instance)
		{
			try
			{
				String command = instance.memory().contains("Command", NbtElement.STRING_TYPE) ? instance.memory().getString("Command") : "playsound minecraft:entity.zombie_villager.cure ambient @s ~ ~ ~ 1 1";
				owner.getServer().getCommandManager().executeWithPrefix(owner.getCommandSource(), command);
			}
			catch(Exception e) { }
		}
	});
	
	/** An ability that does nothing but which can be given a custom map name */
	public static final Supplier<Ability> DUMMY = register("dummy", () -> new Ability(prefix("dummy"), Category.UTILITY)
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