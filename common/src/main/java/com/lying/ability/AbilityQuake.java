package com.lying.ability;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.function.Consumers;
import org.joml.Vector2i;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityQuake.OperatingValuesQuake;
import com.lying.component.CharacterSheet;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class AbilityQuake extends ActivatedAbility implements ITickingAbility, IComplexAbility<OperatingValuesQuake>
{
	public AbilityQuake(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 10; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance) && owner.fallDistance > 0F; }
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance) { return getCurrentTick(instance, owner.getWorld().getTime()) >= 0; }
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		// TODO Tie maximum radius to the minimum of configured range and fall distance, trigger shockwave on landing
		OperatingValuesQuake values = memoryToValues(instance.memory());
		ServerWorld world = (ServerWorld)owner.getWorld();
		int tick = getCurrentTick(instance, world.getTime());
		if(tick <= 0)
		{
			ITickingAbility.tryPutOnCooldown(instance, owner);
			return;
		}
		
		values.origin().ifPresent(origin -> 
		{
			int ticksRunning = values.duration() - tick;
			if(ticksRunning%OperatingValuesQuake.INTERVAL > 0) return;
			int range = Math.floorDiv(ticksRunning, OperatingValuesQuake.INTERVAL);
			if(range > values.maxRange)
				return;
			range += 2;
			
			List<BlockPos> alreadyChecked = Lists.newArrayList();
			for(int i=0; i<360; i++)
			{
				Vector2i rotate = VTUtils.rotateDegrees2D(new Vector2i(0, range), i);
				tryAffectBlock(origin.add(rotate.x, 0, rotate.y), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
			}
		});
	}
	
	private static Optional<BlockPos> tryAffectBlock(BlockPos affected, ServerWorld world, List<BlockPos> ignore)
	{
		if(world.isAir(affected) || !world.isAir(affected.up()) || world.getBlockEntity(affected) != null || ignore.contains(affected))
			return Optional.empty();
		
		FallingBlockEntity tile = FallingBlockEntity.spawnFromBlock(world, affected, world.getBlockState(affected));
		tile.setVelocity(new Vec3d(0D, 0.6D, 0D));
		
		// TODO Implement custom FallingBlockEntity to ensure particle sync
		Random rand = world.getRandom();
		for(int i=0; i<2; i++)
		{
			double x = affected.getX() + 0.5D;
			double y = affected.getY() + 1D;
			double z = affected.getZ() + 0.5D;
			
			double velX = (rand.nextDouble() - 0.5) * 2;
			double velY = 0D;
			double velZ = (rand.nextDouble() - 0.5) * 2;
			world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 1, velX, velY, velZ, 1);
		}
		
		return Optional.of(affected);
	}
	
	private void startTicking(AbilityInstance instance, LivingEntity owner)
	{
		OperatingValuesQuake values = memoryToValues(instance.memory());
		long start = owner.getWorld().getTime();
		values = new OperatingValuesQuake(Optional.of(start + values.duration() + 1), Optional.of(owner.getBlockPos().down()), values.range());
		instance.setMemory(values.toNbt());
		
		ITickingAbility.tryPutOnIndefiniteCooldown(instance.mapName(), owner);
	}
	
	private int getCurrentTick(AbilityInstance instance, long currentTime)
	{
		OperatingValuesQuake values = memoryToValues(instance.memory());
		long finish = values.time().orElse(currentTime - 1);
		values.time().ifPresentOrElse(Consumers.nop(), () -> instance.setMemory((new OperatingValuesQuake(Optional.empty(), Optional.empty(), values.range())).toNbt()));
		return (int)(finish - currentTime);
	}
	
	public OperatingValuesQuake memoryToValues(NbtCompound data) { return OperatingValuesQuake.fromNbt(data); }
	
	public static class OperatingValuesQuake
	{
		protected static final Codec<OperatingValuesQuake> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.LONG.optionalFieldOf("Time").forGetter(OperatingValuesQuake::time), 
				BlockPos.CODEC.optionalFieldOf("Origin").forGetter(OperatingValuesQuake::origin),
				Codec.INT.optionalFieldOf("Range").forGetter(OperatingValuesQuake::range))
					.apply(instance, OperatingValuesQuake::new));
		
		/** The rate of expansion of the shockwave */
		public static final int INTERVAL = Reference.Values.TICKS_PER_SECOND / 3;
		protected final long endTime;
		protected final BlockPos originPos;
		protected final int maxRange;
		
		public OperatingValuesQuake()
		{
			this(Optional.empty(), Optional.empty(), Optional.empty());
		}
		
		public OperatingValuesQuake(Optional<Long> finishIn, Optional<BlockPos> originIn, Optional<Integer> rangeIn)
		{
			endTime = finishIn.orElse(0L);
			originPos = originIn.orElse(BlockPos.ORIGIN);
			maxRange = rangeIn.orElse(10);
		}
		
		protected Optional<Long> time() { return Optional.of(endTime); }
		protected Optional<BlockPos> origin() { return Optional.of(originPos); }
		protected Optional<Integer> range() { return Optional.of(maxRange); }
		
		public int duration() { return maxRange * INTERVAL; }
		
		public NbtCompound toNbt()
		{
			return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static OperatingValuesQuake fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
