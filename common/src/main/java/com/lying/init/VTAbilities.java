package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;
import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.Category;
import com.lying.ability.AbilityBadBreath;
import com.lying.ability.AbilityBerserk;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilityBurrow;
import com.lying.ability.AbilityColdBlood;
import com.lying.ability.AbilityCosmetics;
import com.lying.ability.AbilityDamageGear;
import com.lying.ability.AbilityDamageResist;
import com.lying.ability.AbilityDietRestriction;
import com.lying.ability.AbilityFaeskin;
import com.lying.ability.AbilityFastHeal;
import com.lying.ability.AbilityFavouredTerrain;
import com.lying.ability.AbilityFleece;
import com.lying.ability.AbilityFlexible;
import com.lying.ability.AbilityFly;
import com.lying.ability.AbilityGelatinous;
import com.lying.ability.AbilityIgnoreSlowdown;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilityIntangible;
import com.lying.ability.AbilityInvisibility;
import com.lying.ability.AbilityLoSTeleport;
import com.lying.ability.AbilityNightVision;
import com.lying.ability.AbilityOnMeleeHit;
import com.lying.ability.AbilityOresight;
import com.lying.ability.AbilityPariah;
import com.lying.ability.AbilityPhotosynth;
import com.lying.ability.AbilityPoison;
import com.lying.ability.AbilityQuake;
import com.lying.ability.AbilityRegeneration;
import com.lying.ability.AbilitySculksight;
import com.lying.ability.AbilitySet;
import com.lying.ability.AbilityStatusEffectOnDemand;
import com.lying.ability.AbilityStatusTagImmune;
import com.lying.ability.AbilitySunblind;
import com.lying.ability.AbilityThrowBlock;
import com.lying.ability.AbilityThunderstep;
import com.lying.ability.AbilityWaterWalking;
import com.lying.ability.ActivatedAbility;
import com.lying.ability.ICosmeticSupplier;
import com.lying.ability.PassiveNoXP;
import com.lying.ability.SingleAttributeAbility;
import com.lying.ability.SpawnProjectileAbility;
import com.lying.ability.ToggledAbility;
import com.lying.component.element.ElementCosmetics;
import com.lying.data.VTTags;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.event.SheetEvents;
import com.lying.reference.Reference;
import com.lying.type.Action;
import com.lying.utility.LootBag;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class VTAbilities
{
	private static final Map<Identifier, Supplier<Ability>> ABILITIES = new HashMap<>();
	
	public static final Supplier<Ability> AMPHIBIOUS		= register("amphibious", (id) -> new Ability(id, Category.UTILITY)
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
	public static final Supplier<Ability> BAD_BREATH		= register("bad_breath", (id) -> new AbilityBadBreath(id, Category.OFFENSE));
	public static final Supplier<Ability> BERSERK			= register("berserk", (id) -> new AbilityBerserk(id, Category.OFFENSE));
	public static final Supplier<Ability> BONUS_DMG			= register("bonus_damage", (id) -> new SingleAttributeAbility.Damage(id, Category.OFFENSE));
	public static final Supplier<Ability> BONUS_HEALTH		= register("bonus_health", (id) -> new SingleAttributeAbility.Health(id, Category.DEFENSE));
	public static final Supplier<Ability> BREATHE_FLUID		= register("breathe_in_fluid", (id) -> new AbilityBreathing.Allow(id));
	public static final Supplier<Ability> BURN_IN_SUN		= register("burn_in_sun", (id) -> new Ability(id, Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			LivingEvents.LIVING_MOVE_TICK_EVENT.register((living, sheetOpt) -> 
			{
				if(living.isInvisible() || !isAffectedByDaylight(living) || sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.BURN_IN_SUN.get().registryName()))
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
		
		@SuppressWarnings("deprecation")
		private static boolean isAffectedByDaylight(Entity ent)
		{
			if(ent.getWorld().isDay() && !ent.getWorld().isClient())
			{
				float f = ent.getBrightnessAtEyes();
				BlockPos pos = BlockPos.ofFloored(ent.getX(), ent.getEyeY(), ent.getZ());
				boolean unaffected = ent.isWet() || ent.inPowderSnow || ent.wasInPowderSnow;
				return f > 0.5F && !unaffected && ent.getWorld().isSkyVisible(pos) && ent.getWorld().getRandom().nextFloat() * 30F < (f - 0.4F) * 2F;
			}
			return false;
		}
	});
	public static final Supplier<Ability> BURROW			= register("burrow", (id) -> new AbilityBurrow(id, Category.UTILITY));
	public static final Supplier<Ability> CLIMB				= register("climb", (id) -> new ToggledAbility(id, Category.UTILITY));
	public static final Supplier<Ability> COLD_BLOODED		= register("cold_blooded", (id) -> new AbilityColdBlood(id, Category.UTILITY));
	public static final Supplier<Ability> COSMETICS			= register("cosmetics", (id) -> new AbilityCosmetics(id, Category.UTILITY));
	public static final Supplier<Ability> DEEP_BREATH		= register("deep_breath", (id) -> new Ability(id, Category.UTILITY)
	{
		public void registerEventHandlers()
		{
			LivingEvents.GET_MAX_AIR_EVENT.register((abilities,air) -> abilities.hasAbility(id) ? air * 2 : air);
		}
	});
	public static final Supplier<Ability> DROPS				= register("drops", (id) -> new Ability(id, Category.UTILITY)
	{
		public Optional<Text> description(AbilityInstance instance)
		{
			return Optional.of(translate("ability", id.getPath()+".desc", getBag(instance.memory()).description()));
		}
		
		public void registerEventHandlers()
		{
			PlayerEvents.PLAYER_DROPS_EVENT.register((living, source) -> VariousTypes.getSheet(living).ifPresent(sheet -> 
			{
				AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
				if(!abilities.hasAbility(id))
					return;
				
				AbilityInstance inst = abilities.get(id);
				LootBag loot = getBag(inst.memory());
				if(loot != null)
					loot.dropFrom(living, source);
			}));
		}
		
		private static LootBag getBag(NbtCompound nbt)
		{
			return nbt.contains("Loot", NbtElement.COMPOUND_TYPE) ? LootBag.fromNbt(nbt.getCompound("Loot")) : LootBag.ofTable(EntityType.ZOMBIE.getLootTableId());
		}
	});
	public static final Supplier<Ability> FAESKIN			= register("faeskin", (id) -> new AbilityFaeskin(id, Category.UTILITY));
	public static final Supplier<Ability> FAST_HEALING		= register("fast_healing", (id) -> new AbilityFastHeal(id, Category.DEFENSE));
	public static final Supplier<Ability> FIREBALL			= register("fireball", (id) -> new SpawnProjectileAbility(id, Category.OFFENSE)
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
	public static final Supplier<Ability> FLAMEPROOF		= register("flameproof", (id) -> new AbilityDamageResist(id, Category.DEFENSE));
	public static final Supplier<Ability> FLAMING_FIST		= register("flaming_fist", (id) -> new AbilityOnMeleeHit.SetFire(id, Category.OFFENSE));
	public static final Supplier<Ability> FLEECE			= register("fleece", (id) -> new AbilityFleece(id, Category.UTILITY));
	public static final Supplier<Ability> FLEXIBLE			= register("flexible", (id) -> new AbilityFlexible(id, Category.UTILITY));
	public static final Supplier<Ability> FLY				= register("fly", (id) -> new AbilityFly(id, Category.UTILITY));
	public static final Supplier<Ability> FORGETFUL			= register("forgetful", (id) -> new PassiveNoXP.Forgetful(id, Category.UTILITY));
	public static final Supplier<Ability> GELATINOUS		= register("gelatinous", (id) -> new AbilityGelatinous(id, Category.UTILITY));
	public static final Supplier<Ability> GHOSTLY			= register("ghostly", (id) -> new ToggledAbility(id, Category.UTILITY)
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
	public static final Supplier<Ability> GOLDHEARTED		= register("goldheart", (id) -> new Ability(id, Category.DEFENSE));
	public static final Supplier<Ability> HERBIVORE			= register("herbivore", (id) -> new AbilityDietRestriction(id, Category.UTILITY));
	public static final Supplier<Ability> HOME_TURF			= register("home_turf", (id) -> new AbilityFavouredTerrain(id, Category.DEFENSE));
	public static final Supplier<Ability> INDOMITABLE		= register("indomitable", (id) -> new Ability(id, Category.OFFENSE));
	public static final Supplier<Ability> INTANGIBLE		= register("intangible", (id) -> new AbilityIntangible(id, Category.UTILITY));
	public static final Supplier<Ability> INVISIBILITY		= register("invisibility", (id) -> new AbilityInvisibility(id, Category.DEFENSE));
	public static final Supplier<Ability> LUDDITE			= register("luddite", (id) -> new AbilityDamageGear(id, Category.OFFENSE, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
	public static final Supplier<Ability> MENDING			= register("mending", (id) -> new Ability(id, Category.DEFENSE)
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
	public static final Supplier<Ability> MINDLESS			= register("mindless", (id) -> new PassiveNoXP.Mindless(id, Category.UTILITY));
	public static final Supplier<Ability> MITHRIDATIC		= register("mithridatic", (id) -> new AbilityStatusTagImmune(id, Category.DEFENSE));
	public static final Supplier<Ability> NAT_ARMOUR		= register("natural_armour", (id) -> new SingleAttributeAbility.Armour(id, Category.DEFENSE));
	public static final Supplier<Ability> NIGHT_VISION		= register("night_vision", (id) -> new AbilityNightVision(id, Category.UTILITY));
	public static final Supplier<Ability> OMNISCIENT		= register("omniscient", (id) -> new PassiveNoXP.Omniscient(id, Category.UTILITY));
	public static final Supplier<Ability> ORESIGHT			= register("oresight", (id) -> new AbilityOresight(id, Category.UTILITY));
	public static final Supplier<Ability> PARIAH			= register("pariah", (id) -> new AbilityPariah(id, Category.UTILITY));
	public static final Supplier<Ability> PHOTOSYNTH		= register("photosynth", (id) -> new AbilityPhotosynth(id, Category.UTILITY));
	public static final Supplier<Ability> POISON			= register("poison", (id) -> new AbilityPoison(id, Category.OFFENSE));
	public static final Supplier<Ability> QUAKE				= register("quake", (id) -> new AbilityQuake(id, Category.OFFENSE));
	public static final Supplier<Ability> REGENERATION		= register("regeneration", (id) -> new AbilityRegeneration(id, Category.DEFENSE));
	public static final Supplier<Ability> REND				= register("rend", (id) -> new AbilityDamageGear(id, Category.OFFENSE, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	public static final Supplier<Ability> RIBSHOT			= register("ribshot", (id) -> new SpawnProjectileAbility(id, Category.OFFENSE)
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
	public static final Supplier<Ability> RUN_CMD			= register("run_command", (id) -> new ActivatedAbility(id, Category.UTILITY)
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
	public static final Supplier<Ability> SCALE				= register("scale", (id) -> new SingleAttributeAbility.Scale(id, Category.UTILITY));
	public static final Supplier<Ability> SCULK_SIGHT		= register("sculk_sight", (id) -> new AbilitySculksight(id, Category.UTILITY));
	public static final Supplier<Ability> SUFFOCATE_FLUID	= register("suffocate_in_fluid", (id) -> new AbilityBreathing.Deny(id));
	public static final Supplier<Ability> SUNBLIND			= register("sunblind", (id) -> new AbilitySunblind(id, Category.UTILITY));
	public static final Supplier<Ability> STEALTH			= register("stealth", (id) -> new AbilityStatusEffectOnDemand(id, Category.DEFENSE, m -> new StatusEffectInstance(VTStatusEffects.getEntry(m, VTStatusEffects.STEALTH), Reference.Values.TICKS_PER_SECOND * 30, 0, false, false))
	{
		public int cooldownDefault() { return Reference.Values.TICKS_PER_MINUTE * 5; }
	});
	public static final Supplier<Ability> SWIM				= register("swim", (id) -> new AbilityStatusEffectOnDemand(id, Category.UTILITY, m -> new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, Reference.Values.TICKS_PER_SECOND * 3, 0, true, true)) 
	{
		public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 5; }
		
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return owner.isSwimming(); }
	});
	public static final Supplier<Ability> TELEPORT			= register("teleport", (id) -> new AbilityLoSTeleport(id, Category.UTILITY));
	public static final Supplier<Ability> THUNDERSTEP		= register("thunderstep", (id) -> new AbilityThunderstep(id, Category.OFFENSE));
	public static final Supplier<Ability> WATER_WALKING		= register("water_walking", (id) -> new AbilityWaterWalking(id, Category.UTILITY));
	public static final Supplier<Ability> WEBSPINNER		= register("webspinner", (id) -> new AbilityThrowBlock(id, Category.OFFENSE));
	public static final Supplier<Ability> WEBWALKER			= register("webwalker", (id) -> new AbilityIgnoreSlowdown(id, Category.UTILITY));
	
	public static final Supplier<Ability> DUMMY = register("dummy", (id) -> new Ability(id, Category.UTILITY)
	{
		protected boolean remappable() { return true; }
	});
	
	/*
	 * TODO Implement more abilities
	 	 * Constant status effect abilities
	 	 * 
	 	 * Analgesic - No hurt sound or animation, health display in HUD is inaccurate
		 * Arrowsnatcher - Projectile attacks fail on impact, instead add their item to your inventory. Ability then goes on cooldown.
		 * Blood Draw - Melee-range attack that self heals, deals unblockable damage, Nausea, and Weakness effects, but moderate cooldown and only works on physical living targets
		 * Camouflage - Perfect Invisibility with unlimited duration whilst within certain conditions or until attack/ed
		 * Charge - Brief large boost to forward movement, damage and knockback entities collided with en route
		 * Enchain - Locks a target in place with a set of magical chains
		 * Eye Ray - Shoots a beam of energy that can damage and/or deal status effects to those struck, highly configurable, does not affect invisible entities
		 * Fertile Aura - Bonemeal surrounding area (periodically? or activated)
		 * Flit - Very temporary (read: single digit seconds) Spectator mode with no menu access, moderate cooldown
		 * Fury - Temporary mild damage buff and resistance, ends after configured duration OR if owner deals no damage after 5 seconds
		 * Gaseous - Immune to all physical forms of damage, no collision with other entities
		 * Life Drain - Similar to Blood Draw, but long cooldown and reduces target's max HP by the same amount
		 * Flexible - Toggled, applies a configured scale modifier (up or down)
		 * Mindeater - Melee-range attack on players that drains XP levels or kills outright
		 * Mindreader - Toggled, detect all non-Mindless entities nearby similar to Sculksight and read any private messages they send (server config, admins always unaffected)
		 * Null Field - Denies the use of activated abilities near you (including your own) while active, long cooldown when turned off
		 * Omenpath - Create a stationary temporary portal to your home dimension, usable by any entity in either direction
		 * Roar - AoE damage effect
		 * Smokescreen - Spawn an AoE particle cloud obscuring vision
		 * Untethered - Creative-style flight
		 * Worldbridge - Create a pair of linked portals between two points, you can only have two at once and the eldest despawns if another is made
	 */
	
	public static Supplier<Ability> register(String name, Function<Identifier, Ability> ability)
	{
		final Identifier regName = prefix(name);
		return register(regName, () -> ability.apply(regName));
	}
	
	public static Supplier<Ability> register(Identifier regName, Supplier<Ability> supplier)
	{
		ABILITIES.put(regName, supplier);
		return supplier;
	}
	
	public static void init()
	{
		abilities().forEach(entry -> 
		{
			entry.get().registerEventHandlers();
			Ability ab = entry.get();
			if(ab instanceof ICosmeticSupplier)
			{
				Identifier registry = ab.registryName();
				ICosmeticSupplier supplier = (ICosmeticSupplier)ab;
				ElementCosmetics.GET_LIVING_COSMETICS_EVENT.register((living, set) -> 
				{
					VariousTypes.getSheet(living).ifPresent(sheet -> 
					{
						AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
						if(abilities.hasAbility(registry))
							abilities.getAbilitiesOfType(registry).forEach(inst -> supplier.getCosmetics(inst).forEach(cos -> set.add(cos)));
					});
				});
			}
		});
		VariousTypes.LOGGER.info(" # Initialised "+ABILITIES.size()+" abilities");
		Map<Category, Integer> tallies = new HashMap<>();
		ABILITIES.values().forEach(a -> tallies.put(a.get().category(), tallies.getOrDefault(a.get().category(), 0) + 1));
		tallies.entrySet().stream().sorted((a,b) -> a.getValue() < b.getValue() ? -1 : a.getValue() > b.getValue() ? 1 : 0).forEach(entry -> VariousTypes.LOGGER.info(" # - {} {}", entry.getValue(), entry.getKey().name().toLowerCase()));
	}
	
	@Nullable
	public static Ability get(Identifier registryName)
	{
		return exists(registryName) ? ABILITIES.get(registryName).get() : null;
	}
	
	public static boolean exists(Identifier registryName) { return ABILITIES.containsKey(registryName); }
	
	public static Collection<Identifier> abilityIds() { return ABILITIES.keySet(); }
	
	public static Collection<Supplier<Ability>> abilities() { return ABILITIES.values(); }
}