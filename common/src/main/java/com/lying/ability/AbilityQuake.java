package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.UUID;

import com.lying.VariousTypes;
import com.lying.ability.AbilityQuake.ConfigQuake;
import com.lying.component.CharacterSheet;
import com.lying.emission.ShockwaveEmission;
import com.lying.entity.EmitterEntity;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSoundEvents;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AbilityQuake extends ActivatedAbility implements ITickingAbility, IComplexAbility<ConfigQuake>
{
	private static final UUID GRAVITY_UUID	= UUID.fromString("bb981f2a-17eb-48ac-b4d3-701f38ccd183");
	
	/** The rate of expansion of the shockwave, 10 blocks/s */
	public static final int INTERVAL			= Reference.Values.TICKS_PER_SECOND / 10;
	/** Distance fallen in blocks per block of resulting shockwave radius */
	public static final float FALL_PER_BLOCK	= 1.5F;
	
	public AbilityQuake(Identifier regName, Category catIn)
	{
		super(regName, catIn);
		this.soundSettings = new ActivationSoundSettings(i -> VTSoundEvents.QUAKE_ACTIVATE.get(), 1F, true);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigQuake values = instanceToValues(instance);
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.maxRange + 1));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 30; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance) && owner.fallDistance >= 1.5F; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
	}
	
	private void startTicking(AbilityInstance instance, LivingEntity owner)
	{
		ConfigQuake values = memoryToValues(instance.memory());
		values.setPhase(Phase.FALLING);
		instance.setMemory(values.toNbt());
		owner.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY).addTemporaryModifier(new EntityAttributeModifier(GRAVITY_UUID, "quake_gravity", 0.75D, Operation.ADD_VALUE));
		ITickingAbility.tryPutOnIndefiniteCooldown(instance.mapName(), owner);
	}
	
	private void stopTicking(AbilityInstance instance, LivingEntity owner)
	{
		owner.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY).removeModifier(GRAVITY_UUID);
		
		ConfigQuake values = memoryToValues(instance.memory());
		instance.setMemory(ConfigQuake.ofRange(values.maxRange).toNbt());
		ITickingAbility.tryPutOnCooldown(instance, owner);
	}
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance)
	{
		return ConfigQuake.fromNbt(instance.memory()).isTicking();
	}
	
	public void registerEventHandlers()
	{
		/** Deactivate if we die before hitting the ground */
		EntityEvent.LIVING_DEATH.register((living, source) -> 
		{
			VariousTypes.getSheet(living).ifPresent(sheet -> ((AbilitySet)sheet.element(VTSheetElements.ACTIONABLES)).getAbilitiesOfType(registryName()).forEach(i -> 
			{
				ConfigQuake values = memoryToValues(i.memory());
				if(values.isTicking())
					stopTicking(i, living);
			}));
			return EventResult.pass();
		});
		
		/** Advance to impact phase on landing */
		LivingEvents.ON_FALL_EVENT.register((living, fallDistance, onGround, landedOnState, landedPosition) -> 
			VariousTypes.getSheet(living).ifPresent(sheet -> 
			{
				((AbilitySet)sheet.element(VTSheetElements.ACTIONABLES)).getAbilitiesOfType(registryName()).forEach(instance -> 
				{
					ConfigQuake values = memoryToValues(instance.memory());
					if(values.phase != Phase.FALLING)
						return;
					
					// Trigger shockwave on landing proportional to distance fallen
					values.setPhase(Phase.IMPACT);
					values.endTime = living.getWorld().getTime() + values.maxShockwaveTime() + 1;
					values.originPos = living.getBlockPos().down();
					values.distanceFallen = fallDistance;
					instance.setMemory(values.toNbt());
				});
			}));
		
		/** Prevent user from dying on impact from their own ability */
		PlayerEvents.MODIFY_DAMAGE_TAKEN_EVENT.register((living, source, amount) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(!source.isOf(DamageTypes.FALL) || sheetOpt.isEmpty())
				return 1F;
			
			return 
					((AbilitySet)sheetOpt.get().element(VTSheetElements.ACTIONABLES)).getAbilitiesOfType(registryName()).stream().map(this::instanceToValues)
						.anyMatch(ConfigQuake::isGuardingAgainstFallDamage) ? 0.1F : 1F;
		});
	}
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		ConfigQuake values = memoryToValues(instance.memory());
		ServerWorld world = (ServerWorld)owner.getWorld();
		switch(values.phase)
		{
			case INERT:
				return;
			case FALLING:
				if(owner.isOnGround() || owner.isFallFlying() || owner.isSpectator() || owner.getType() == EntityType.PLAYER && owner.isInCreativeMode() && ((PlayerEntity)owner).getAbilities().flying || world.getBlockState(owner.getBlockPos()).getFluidState().isIn(FluidTags.WATER))
				{
					// Deactivate if the owner goes into any form of flying mode or lands inside water at all
					stopTicking(instance, owner);
					break;
				}
				if(owner.age%(Reference.Values.TICKS_PER_SECOND / 4) == 0)
					VTUtils.spawnParticles(world, ParticleTypes.CRIT, owner.getPos(), Vec3d.ZERO);
				break;
			case IMPACT:
				owner.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY).removeModifier(GRAVITY_UUID);
				world.spawnEntity(EmitterEntity.create(owner.getPos(), world, ShockwaveEmission.create(values.originPos, values.getFinalRadius())));
				stopTicking(instance, owner);
				break;
		}
	}
	
	public ConfigQuake memoryToValues(NbtCompound data) { return ConfigQuake.fromNbt(data); }
	
	public static class ConfigQuake
	{
		protected static final Codec<ConfigQuake> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Phase.CODEC.optionalFieldOf("Phase").forGetter(q -> Optional.of(q.phase)),
				Codec.LONG.optionalFieldOf("Time").forGetter(ConfigQuake::time), 
				BlockPos.CODEC.optionalFieldOf("Origin").forGetter(q -> Optional.of(q.originPos)),
				Codec.FLOAT.optionalFieldOf("Distance").forGetter(q -> Optional.of(q.distanceFallen)),
				Codec.INT.optionalFieldOf("Range").forGetter(q -> Optional.of(q.maxRange)))
					.apply(instance, ConfigQuake::new));
		
		/** The configured limit on how far the shockwave can spread, between 0 and 32 blocks */
		protected int maxRange;
		
		// Operational values
		protected Phase phase;
		protected long endTime;
		protected BlockPos originPos;
		protected float distanceFallen;
		
		private ConfigQuake(int range)
		{
			this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(range));
		}
		
		public ConfigQuake(Optional<Phase> phaseIn, Optional<Long> finishIn, Optional<BlockPos> originIn, Optional<Float> distanceIn, Optional<Integer> rangeIn)
		{
			phase = phaseIn.orElse(Phase.INERT);
			endTime = finishIn.orElse(0L);
			originPos = originIn.orElse(BlockPos.ORIGIN);
			distanceFallen = distanceIn.orElse(0F);
			maxRange = MathHelper.clamp(rangeIn.orElse(6), 0, 32);
		}
		
		public static ConfigQuake ofRange(int rangeIn) { return new ConfigQuake(rangeIn); }
		
		public int getFinalRadius() { return Math.min((int)(distanceFallen / FALL_PER_BLOCK), maxRange); }
		
		protected Optional<Long> time() { return Optional.of(endTime); }
		
		public int maxShockwaveTime() { return maxRange * AbilityQuake.INTERVAL; }
		
		public void setPhase(Phase phaseIn)
		{
			phase = phaseIn;
		}
		
		public boolean isTicking() { return phase != Phase.INERT; }
		
		public boolean isGuardingAgainstFallDamage() { return phase == Phase.FALLING || phase == Phase.IMPACT; }
		
		public NbtCompound toNbt()
		{
			return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static ConfigQuake fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
	
	private static enum Phase implements StringIdentifiable
	{
		INERT,
		FALLING,
		IMPACT;
		
		@SuppressWarnings("deprecation")
		public static final StringIdentifiable.EnumCodec<Phase> CODEC	= StringIdentifiable.createCodec(Phase::values);
		
		public String asString() { return name().toLowerCase(); }
	}
}
