package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.function.Consumers;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityQuake.ConfigQuake;
import com.lying.component.CharacterSheet;
import com.lying.entity.ShakenBlockEntity;
import com.lying.event.LivingEvents;
import com.lying.init.VTParticleTypes;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
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
	
	/** The rate of expansion of the shockwave */
	public static final int INTERVAL = Reference.Values.TICKS_PER_SECOND / 10;
	
	public AbilityQuake(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigQuake values = instanceToValues(instance);
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.maxRange + 2));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 30; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance) && owner.fallDistance >= 1.5F; }
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance)
	{
		return ConfigQuake.fromNbt(instance.memory()).phase != Phase.INERT;
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.ON_FALL_EVENT.register((living, fallDistance, onGround, landedOnState, landedPosition) -> 
		{
			if(!onGround || living.getWorld().isClient()) return;
			
			VariousTypes.getSheet(living).ifPresent(sheet -> 
			{
				checkAndUpdateAbilities(sheet.element(VTSheetElements.ABILITIES), living, fallDistance);
				checkAndUpdateAbilities(sheet.element(VTSheetElements.ACTIONABLES), living, fallDistance);
			});
		});
	}
	
	private <T extends AbilitySet> void checkAndUpdateAbilities(T set, LivingEntity living, float fallDistance)
	{
		set.getAbilitiesOfType(registryName()).forEach(instance -> 
		{
			ConfigQuake values = memoryToValues(instance.memory());
			if(values.phase != Phase.FALLING)
				return;
			
			// Trigger shockwave on landing proportional to distance fallen
			// This technically constitutes an "IMPACT" phase inbetween FALLING and SHOCKWAVE
			values.setPhase(Phase.IMPACT);
			values.endTime = living.getWorld().getTime() + values.maxShockwaveTime() + 1;
			values.originPos = living.getBlockPos().down();
			values.distanceFallen = fallDistance;
			instance.setMemory(values.toNbt());
			
			// TODO Add impact SFX
			VTUtils.spawnParticles((ServerWorld)living.getWorld(), VTParticleTypes.SHOCKWAVE.get(), living.getPos().add(0, 0.5, 0), new Vec3d(0, 1, 0));
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
				values.setPhase(Phase.SHOCKWAVE);
				instance.setMemory(values.toNbt());
				break;
			case SHOCKWAVE:
				int ticksLeft = getTicksRemaining(instance, world.getTime());
				int ticksInShockwave = values.maxShockwaveTime() - ticksLeft;
				int radius = Math.floorDiv(ticksInShockwave, INTERVAL);
				if(ticksLeft <= 0 || radius > Math.min((int)(values.distanceFallen / 1.5F), values.maxRange))
				{
					stopTicking(instance, owner);
					return;
				}
				else if(ticksInShockwave%INTERVAL == 0)
				{
					// Start 1 block away from origin to reduce owner getting caught in their own shockwave
					radius += 1;
					
					// Iteratively calculate one quarter circle and transform that into the full circle with 90 degree rotations
					List<BlockPos> alreadyChecked = Lists.newArrayList();
					for(int x=radius; x>=0; x--)
						for(int z=radius; z>=0; z--)
						{
							if((int)Math.floor(Math.sqrt((x * x) + (z * z))) != radius)
								continue;
							
							tryAffectBlock(values.originPos.add(x, 0, z), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
							tryAffectBlock(values.originPos.add(x, 0, -z), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
							tryAffectBlock(values.originPos.add(-x, 0, z), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
							tryAffectBlock(values.originPos.add(-x, 0, -z), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
						}
				}
				break;
		}
	}
	
	private static boolean canShakeBlock(BlockPos pos, ServerWorld world, List<BlockPos> ignore)
	{
		if(ignore.stream().anyMatch(p -> p.withY(pos.getY()).equals(pos)))
			return false;
		else if(pos.getY() < world.getBottomY() || pos.getY() > world.getTopY())
			return false;
		else
			return 
					!world.isAir(pos) && 
					world.getBlockState(pos).isSolidBlock(world, pos) && 
					world.getBlockState(pos.up()).isReplaceable() && 
					world.getBlockEntity(pos) == null;
	}
	
	private static Optional<BlockPos> tryAffectBlock(BlockPos target, ServerWorld world, List<BlockPos> ignore)
	{
		if(!canShakeBlock(target, world, ignore))
		{
			for(int off : new int[] {1, -1, 2, -2})
			{
				BlockPos offset = target.add(0, off, 0);
				if(canShakeBlock(offset, world, ignore))
					return affectBlock(offset, world);
			}
			return Optional.empty();
		}
		
		return affectBlock(target, world);
	}
	
	private static Optional<BlockPos> affectBlock(BlockPos pos, ServerWorld world)
	{
		ShakenBlockEntity tile = ShakenBlockEntity.spawnFromBlock(world, pos, world.getBlockState(pos));
		tile.setVelocity(new Vec3d(0D, 0.4D, 0D));
		
		world.getOtherEntities(tile, tile.getBoundingBox().expand(0, 1, 0), Predicates.alwaysTrue()).forEach(ent -> ent.addVelocity(new Vec3d(0D, 0.4D, 0D)));
		
		return Optional.of(pos);
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
		ConfigQuake values = memoryToValues(instance.memory());
		instance.setMemory(ConfigQuake.ofRange(values.maxRange).toNbt());
		ITickingAbility.tryPutOnCooldown(instance, owner);
	}
	
	/** Returns how many ticks until this ability finishes its function */
	private int getTicksRemaining(AbilityInstance instance, long currentTime)
	{
		ConfigQuake values = memoryToValues(instance.memory());
		long finish = values.time().orElse(currentTime - 1);
		values.time().ifPresentOrElse(Consumers.nop(), () -> instance.setMemory(ConfigQuake.ofRange(values.maxRange).toNbt()));
		return (int)(finish - currentTime);
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
			maxRange = MathHelper.clamp(rangeIn.orElse(4), 0, 32);
		}
		
		public static ConfigQuake ofRange(int rangeIn) { return new ConfigQuake(rangeIn); }
		
		protected Optional<Long> time() { return Optional.of(endTime); }
		
		public int maxShockwaveTime() { return maxRange * AbilityQuake.INTERVAL; }
		
		public void setPhase(Phase phaseIn)
		{
			phase = phaseIn;
		}
		
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
		IMPACT,
		SHOCKWAVE;
		
		@SuppressWarnings("deprecation")
		public static final StringIdentifiable.EnumCodec<Phase> CODEC	= StringIdentifiable.createCodec(Phase::values);
		
		public String asString() { return name().toLowerCase(); }
	}
}
