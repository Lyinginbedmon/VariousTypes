package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.Category;
import com.lying.ability.AbilityBadBreath;
import com.lying.ability.AbilityBerserk;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilityBurrow;
import com.lying.ability.AbilityDamageResist;
import com.lying.ability.AbilityDietRestriction;
import com.lying.ability.AbilityFaeskin;
import com.lying.ability.AbilityFastHeal;
import com.lying.ability.AbilityFavouredTerrain;
import com.lying.ability.AbilityFleece;
import com.lying.ability.AbilityFly;
import com.lying.ability.AbilityIgnoreSlowdown;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilityIntangible;
import com.lying.ability.AbilityInvisibility;
import com.lying.ability.AbilityLoSTeleport;
import com.lying.ability.AbilityNightVision;
import com.lying.ability.AbilityOresight;
import com.lying.ability.AbilityPariah;
import com.lying.ability.AbilityPhotosynth;
import com.lying.ability.AbilityQuake;
import com.lying.ability.AbilityRegeneration;
import com.lying.ability.AbilitySet;
import com.lying.ability.AbilityStatusEffectOnDemand;
import com.lying.ability.AbilityStatusTagImmune;
import com.lying.ability.AbilityThunderstep;
import com.lying.ability.AbilityWaterWalking;
import com.lying.ability.ActivatedAbility;
import com.lying.ability.PassiveNoXP;
import com.lying.ability.SingleAttributeAbility;
import com.lying.ability.SpawnProjectileAbility;
import com.lying.ability.ToggledAbility;
import com.lying.component.CharacterSheet;
import com.lying.data.VTTags;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.event.SheetEvents;
import com.lying.reference.Reference;
import com.lying.type.Action;
import com.lying.utility.VTUtils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
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
			SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
			{
				// Adds the ability to breathe air after it may have been denied by other breathing abilities
				if(!handler.canBreathe(Fluids.EMPTY) && abilities.hasAbility(registryName()))
					handler.allowBreathe(VTTags.AIR);
			});
		}
	});
	public static final Supplier<Ability> NIGHT_VISION	= register("night_vision", () -> new AbilityNightVision(prefix("night_vision"), Category.UTILITY));
	public static final Supplier<Ability> SCULK_SIGHT	= register("sculk_sight", () -> new ToggledAbility(prefix("sculk_sight"), Category.UTILITY));
	public static final Supplier<Ability> INVISIBILITY	= register("invisibility", () -> new AbilityInvisibility(prefix("invisibility"), Category.DEFENSE));
	public static final Supplier<Ability> SWIM			= register("swim", () -> new AbilityStatusEffectOnDemand(prefix("swim"), Category.UTILITY, new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, Reference.Values.TICKS_PER_SECOND * 3, 0, true, true)) 
	{
		public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 5; }
		
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return owner.isSwimming(); }
	});
	public static final Supplier<Ability> CLIMB			= register("climb", () -> new ToggledAbility(prefix("climb"), Category.UTILITY));
	public static final Supplier<Ability> FLY			= register("fly", () -> new AbilityFly(prefix("fly"), Category.UTILITY));
	public static final Supplier<Ability> BURROW		= register("burrow", () -> new AbilityBurrow(prefix("burrow"), Category.UTILITY));
	public static final Supplier<Ability> TELEPORT		= register("teleport", () -> new AbilityLoSTeleport(prefix("teleport"), Category.UTILITY));
	public static final Supplier<Ability> GHOSTLY		= register("ghostly", () -> new ToggledAbility(prefix("ghostly"), Category.UTILITY)
	{
		protected void onActivation(LivingEntity owner, AbilityInstance instance)
		{
			if(owner.getType() == EntityType.PLAYER)
				((PlayerEntity)owner).sendMessage(Reference.ModInfo.translate("gui", "ghostly_turned_on", VTUtils.describeAbility(VTAbilities.INTANGIBLE.get().instance(AbilitySource.MISC))));
		}
		
		protected void onDeactivation(LivingEntity owner, AbilityInstance instance)
		{
			if(owner.getType() == EntityType.PLAYER)
				((PlayerEntity)owner).sendMessage(Reference.ModInfo.translate("gui", "ghostly_turned_off", VTUtils.describeAbility(VTAbilities.INTANGIBLE.get().instance(AbilitySource.MISC))));
		}
		
		public Collection<AbilityInstance> getSubAbilities(AbilityInstance instance) { return isActive(instance) ? List.of(VTAbilities.INTANGIBLE.get().instance(AbilitySource.MISC)) : Collections.emptyList(); }
	});
	public static final Supplier<Ability> INTANGIBLE	= register("intangible", () -> new AbilityIntangible(prefix("intangible"), Category.UTILITY));
	public static final Supplier<Ability> BURN_IN_SUN	= register("burn_in_sun", () -> new Ability(prefix("burn_in_sun"), Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			LivingEvents.LIVING_MOVE_TICK_EVENT.register((living, sheetOpt) -> 
			{
				if(living.isInvisible() || sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.BURN_IN_SUN.get().registryName()))
					return;
				
				ItemStack helmet = living.getEquippedStack(EquipmentSlot.HEAD);
				if(helmet.isEmpty())
					living.setOnFireFor(8);
				else if(helmet.isDamageable())
				{					
					helmet.setDamage(helmet.getDamage() + living.getRandom().nextInt(2));
					if(helmet.getDamage() >= helmet.getMaxDamage())
					{
						living.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
						living.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
					}
				}
			});
		}
	});
	public static final Supplier<Ability> MITHRIDATIC	= register("mithridatic", () -> new AbilityStatusTagImmune(prefix("mithridatic"), Category.DEFENSE));
	public static final Supplier<Ability> FAST_HEALING	= register("fast_healing", () -> new AbilityFastHeal(prefix("fast_healing"), Category.DEFENSE));
	public static final Supplier<Ability> REGENERATION	= register("regeneration", () -> new AbilityRegeneration(prefix("regeneration"), Category.DEFENSE));
	public static final Supplier<Ability> NAT_ARMOUR	= register("natural_armour", () -> new SingleAttributeAbility.Armour(prefix("natural_armour"), Category.DEFENSE));
	public static final Supplier<Ability> BONUS_HEALTH	= register("bonus_health", () -> new SingleAttributeAbility.Health(prefix("bonus_health"), Category.DEFENSE));
	public static final Supplier<Ability> DEEP_BREATH	= register("deep_breath", () -> new Ability(prefix("deep_breath"), Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			LivingEvents.GET_MAX_AIR_EVENT.register((abilities,air) -> abilities.hasAbility(prefix("deep_breath")) ? air * 2 : air);
		}
	});
	public static final Supplier<Ability> MENDING		= register("mending", () -> new Ability(prefix("mending"), Category.DEFENSE)
	{
		public void registerEventHandlers()
		{
			SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
			{
				// Adds the ability to regenerate health after it may have been denied
				if(!handler.can(Action.REGEN.get()) && abilities.hasAbility(registryName()))
					handler.activate(Action.REGEN.get());
			});
		}
	});
	public static final Supplier<Ability> RUN_CMD		= register("run_command", () -> new ActivatedAbility(prefix("run_command"), Category.UTILITY)
	{
		protected boolean remappable() { return true; }
		
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
	public static final Supplier<Ability> PARIAH		= register("pariah", () -> new AbilityPariah(prefix("pariah"), Category.UTILITY));
	public static final Supplier<Ability> GOLDHEARTED	= register("goldheart", () -> new Ability(prefix("goldheart"), Category.DEFENSE));
	public static final Supplier<Ability> INDOMITABLE	= register("indomitable", () -> new Ability(prefix("indomitable"), Category.OFFENSE));
	public static final Supplier<Ability> WATER_WALKING = register("water_walking", () -> new AbilityWaterWalking(prefix("water_walking"), Category.UTILITY));
	public static final Supplier<Ability> RIBSHOT		= register("ribshot", () -> new SpawnProjectileAbility(prefix("ribshot"), Category.OFFENSE)
	{
		protected void shootFrom(LivingEntity owner, Vec3d direction, AbilityInstance instance)
		{
			ItemStack bone = Items.BONE.getDefaultStack().copy();
			bone.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
			PersistentProjectileEntity abstractarrow = ProjectileUtil.createArrowProjectile(owner, bone, 1F);	// TODO Replace with non-pickup bone projectile
			abstractarrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			abstractarrow.setVelocity(direction.x, direction.y, direction.z, 1.6f, 6);
			VTUtils.playSound(owner, SoundEvents.ENTITY_SKELETON_SHOOT, SoundCategory.PLAYERS, 1F, 1F / owner.getRandom().nextFloat() * 0.4F + 0.8F);
			owner.getWorld().spawnEntity(abstractarrow);
		}
	});
	public static final Supplier<Ability> FIREBALL		= register("fireball", () -> new SpawnProjectileAbility(prefix("fireball"), Category.OFFENSE)
	{
		protected void shootFrom(LivingEntity owner, Vec3d direction, AbilityInstance instance)
		{
			Entity projectile;
			if(instance.memory().getBoolean("Explosive"))
			{
				// Create ghast fireball
				projectile = new FireballEntity(owner.getWorld(), owner, direction.x * 4D, direction.y * 4D, direction.z * 4D, 1);
				projectile.setPosition(owner.getEyePos().add(direction));
			}
			else
			{
				// Create blaze fireball
				projectile = new SmallFireballEntity(owner.getWorld(), owner, direction.x * 4D, direction.y * 4D, direction.z * 4D);
				projectile.setPosition(owner.getEyePos().add(direction));
			}
			VTUtils.playSound(owner, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1F, 1F / owner.getRandom().nextFloat() * 0.4F + 0.8F);
			owner.getWorld().spawnEntity(projectile);
		}
	});
	public static final Supplier<Ability> BERSERK		= register("berserk", () -> new AbilityBerserk(prefix("berserk"), Category.OFFENSE));
	public static final Supplier<Ability> MINDLESS		= register("mindless", () -> new PassiveNoXP.Mindless(prefix("mindless"), Category.UTILITY));
	public static final Supplier<Ability> OMNISCIENT	= register("omniscient", () -> new PassiveNoXP.Omniscient(prefix("omniscient"), Category.UTILITY));
	public static final Supplier<Ability> FORGETFUL		= register("forgetful", () -> new PassiveNoXP.Forgetful(prefix("forgetful"), Category.UTILITY));
	public static final Supplier<Ability> QUAKE			= register("quake", () -> new AbilityQuake(prefix("quake"), Category.OFFENSE));
	public static final Supplier<Ability> GELATINOUS	= register("gelatinous", () -> new Ability(prefix("gelatinous"), Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			PlayerEvents.MODIFY_DAMAGE_TAKEN_EVENT.register((player, damage, amount) -> 
			{
				Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
				if(sheetOpt.isPresent() && sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
					if(damage.isIn(VTTags.PHYSICAL))
						return amount * 0.85F;
				
				return amount;
			});
		}
	});
	public static final Supplier<Ability> THUNDERSTEP	= register("thunderstep", () -> new AbilityThunderstep(prefix("thunderstep"), Category.OFFENSE));
	public static final Supplier<Ability> BAD_BREATH	= register("bad_breath", () -> new AbilityBadBreath(prefix("bad_breath"), Category.OFFENSE));
	public static final Supplier<Ability> WEBWALKER		= register("webwalker", () -> new AbilityIgnoreSlowdown(prefix("webwalker"), Category.UTILITY));
	public static final Supplier<Ability> HERBIVORE		= register("herbivore", () -> new AbilityDietRestriction(prefix("herbivore"), Category.UTILITY));
	public static final Supplier<Ability> FLAMEPROOF	= register("flameproof", () -> new AbilityDamageResist(prefix("flameproof"), Category.DEFENSE));
	public static final Supplier<Ability> FLEECE		= register("fleece", () -> new AbilityFleece(prefix("fleece"), Category.UTILITY));
	public static final Supplier<Ability> ORESIGHT		= register("oresight", () -> new AbilityOresight(prefix("oresight"), Category.UTILITY));
	public static final Supplier<Ability> HOME_TURF		= register("home_turf", () -> new AbilityFavouredTerrain(prefix("home_turf"), Category.DEFENSE));
	public static final Supplier<Ability> PHOTOSYNTH	= register("photosynth", () -> new AbilityPhotosynth(prefix("photosynth"), Category.UTILITY));
	public static final Supplier<Ability> FAESKIN		= register("faeskin", () -> new AbilityFaeskin(prefix("faeskin"), Category.UTILITY));
	public static final Supplier<Ability> COS_WINGS		= register("cosmetic_wings", () -> new Ability(prefix("cosmetic_wings"), Category.UTILITY)
	{
		public boolean isHidden(AbilityInstance instance) { return true; }
	});
	
	public static final Supplier<Ability> DUMMY = register("dummy", () -> new Ability(prefix("dummy"), Category.UTILITY)
	{
		protected boolean remappable() { return true; }
	});
	
	/*
	 * TODO Implement more abilities
	 	 * Constant status effect abilities
	 	 * 
	 	 * Analgesic - No hurt sound or animation, health display in HUD is inaccurate
		 * Arrowsnatcher - Projectile attacks fail on impact, instead add their item to your inventory. Ability then goes on cooldown.
		 * Blink - Very temporary (read single digit seconds) Spectator mode with no menu access, moderate cooldown
		 * Blood Draw - Melee-range attack that self heals, deals unblockable damage, Nausea, and Weakness effects, but moderate cooldown and only works on physical living targets
		 * Charge - Brief large boost to forward movement, damage and knockback entities collided with en route
		 * Cold-Blooded - Weak and slow in warm environments (configurable)
		 * Enchain - Locks a target in place with a set of magical chains
		 * Eye Ray - Shoots a beam of energy that can damage and/or deal status effects to those struck, highly configurable, does not affect invisible entities
		 * Fertile Aura - Bonemeal surrounding area (periodically? or activated)
		 * Flaming Fist - Applies Fire Aspect to all melee attacks
		 * Flitting - Creative-style flight
		 * Gaseous - Immune to all physical forms of damage, no collision with other entities
		 * Life Drain - Similar to Blood Draw, but long cooldown and reduces target's max HP by the same amount
		 * Luddite - Melee hits damage the attacker's held item (if any), or causes it to drop if unbreakable
		 * Mindreader - Toggled, detect all non-Mindless entities nearby similar to Sculksight and read any private messages they send (server config, admins always unaffected)
		 * Null Field - Denies the use of activated abilities near you (including your own) while active, long cooldown when turned off
		 * Omenpath - Create a stationary temporary portal to your home dimension, usable by any entity in either direction
		 * Poison Hand - Applies configurable status effects to target on melee hit
		 * Rend - Melee attacks deal extra damage to target's held items and equipment (if any), or causes it to drop if unbreakable
		 * Stealth - Temporary perfect Invisibility (ie. turns off rendering entirely) and mild Speed & Strength effect, long cooldown and ends immediately if you attack
		 * Sunblind - Afflicted with Dazzled status effect when exposed to direct sunlight, sharply reducing attack damage
		 * Webspinner - Throw a falling block entity of cobweb in the direction you are looking
		 * Worldbridge - Create a pair of linked portals between two points, you can only have two at once and the eldest despawns if another is made
	 */
	
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
	
	public static Collection<Identifier> abilityIds() { return ABILITIES.keySet(); }
}