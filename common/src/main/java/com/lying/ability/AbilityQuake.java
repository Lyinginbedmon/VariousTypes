package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.function.Consumers;
import org.joml.Vector2i;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityQuake.ConfigQuake;
import com.lying.component.CharacterSheet;
import com.lying.entity.ShakenBlockEntity;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.ServerEvents;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AbilityQuake extends ActivatedAbility implements ITickingAbility, IComplexAbility<ConfigQuake>
{
	/** The rate of expansion of the shockwave */
	public static final int INTERVAL = Reference.Values.TICKS_PER_SECOND / 10;
	
	public AbilityQuake(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigQuake values = memoryToValues(instance.memory());
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
		return ConfigQuake.fromNbt(instance.memory()).phase >= 0;
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.LivingEvents.ON_FALL_EVENT.register((living, fallDistance, onGround, landedOnState, landedPosition) -> 
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
			if(values.phase >= 0 && Phase.values()[values.phase] == Phase.FALLING)
			{
				// Trigger shockwave on landing proportional to distance fallen
				// This technically constitutes an "IMPACT" phase inbetween FALLING and SHOCKWAVE
				values.phase = 1;
				values.endTime = living.getWorld().getTime() + values.maxShockwaveTime() + 1;
				values.originPos = living.getBlockPos().down();
				values.distanceFallen = fallDistance;
				instance.setMemory(values.toNbt());
				
				// TODO Add impact particle & SFX
//				World world = living.getWorld();
//				if(!world.isClient())
//					((ServerWorld)world).spawnParticles(VTParticles.SHOCKWAVE.get(), living.getX(), living.getY(), living.getZ(), 1, 0, 0, 0, 1);
			}
		});
	}
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		ConfigQuake values = memoryToValues(instance.memory());
		ServerWorld world = (ServerWorld)owner.getWorld();
		switch(Phase.values()[values.phase])
		{
			case FALLING:
				if(owner.isOnGround() || owner.isFallFlying() || owner.isSpectator() || owner.getType() == EntityType.PLAYER && owner.isInCreativeMode() && ((PlayerEntity)owner).getAbilities().flying || world.getBlockState(owner.getBlockPos()).getFluidState().isIn(FluidTags.WATER))
				{
					// Deactivate if the owner goes into any form of flying mode or lands inside water at all
					stopTicking(instance, owner);
					break;
				}
				// TODO Accelerate fall speed?
				if(owner.age%(Reference.Values.TICKS_PER_SECOND / 4) == 0)
					world.spawnParticles(ParticleTypes.CRIT, owner.getX(), owner.getY(), owner.getZ(), 1, 0, 0, 0, 0);
				break;
			case SHOCKWAVE:
				int ticksLeft = getTicksRemaining(instance, world.getTime());
				int ticksInShockwave = values.maxShockwaveTime() - ticksLeft;
				int range = Math.floorDiv(ticksInShockwave, INTERVAL);
				if(ticksLeft <= 0 || range > Math.min((int)(values.distanceFallen / 1.5F), values.maxRange))
				{
					stopTicking(instance, owner);
					return;
				}
				else if(ticksInShockwave%INTERVAL == 0)
				{
					// Start 2 blocks away from origin to reduce owner getting caught in their own shockwave
					range += 2;
					List<BlockPos> alreadyChecked = Lists.newArrayList();
					for(int i=0; i<360; i++)
					{
						Vector2i rotate = VTUtils.rotateDegrees2D(new Vector2i(0, range), i);
						if(rotate.length() < 2D) continue;
						tryAffectBlock(values.originPos.add(rotate.x, 0, rotate.y), world, alreadyChecked).ifPresent(pos -> alreadyChecked.add(pos));
					}
				}
				break;
		}
	}
	
	private static Optional<BlockPos> tryAffectBlock(BlockPos affected, ServerWorld world, List<BlockPos> ignore)
	{
		if(world.isAir(affected) || !world.isAir(affected.up()) || world.getBlockEntity(affected) != null || ignore.contains(affected))
		{
			for(int off : new int[] {1, -1, 2, -2})
			{
				Optional<BlockPos> alt;
				if((alt = tryAffectBlock(affected.add(0, off, 0), world, ignore)).isPresent())
					return alt;
			}
			
			return Optional.empty();
		}
		
		ShakenBlockEntity tile = ShakenBlockEntity.spawnFromBlock(world, affected, world.getBlockState(affected));
		tile.setVelocity(new Vec3d(0D, 0.4D, 0D));
		
		world.getOtherEntities(tile, tile.getBoundingBox().expand(0, 1, 0), Predicates.alwaysTrue()).forEach(ent -> ent.addVelocity(new Vec3d(0D, 0.4D, 0D)));
		
		return Optional.of(affected);
	}
	
	private void startTicking(AbilityInstance instance, LivingEntity owner)
	{
		ConfigQuake values = memoryToValues(instance.memory());
		values.phase = 0;
		instance.setMemory(values.toNbt());
		
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
				Codec.INT.optionalFieldOf("Phase").forGetter(ConfigQuake::phase),
				Codec.LONG.optionalFieldOf("Time").forGetter(ConfigQuake::time), 
				BlockPos.CODEC.optionalFieldOf("Origin").forGetter(ConfigQuake::origin),
				Codec.FLOAT.optionalFieldOf("Distance").forGetter(ConfigQuake::distance),
				Codec.INT.optionalFieldOf("Range").forGetter(ConfigQuake::range))
					.apply(instance, ConfigQuake::new));
		
		/** The configured limit on how far the shockwave can spread, between 0 and 32 blocks */
		protected int maxRange;
		
		// Operational values
		protected int phase;
		protected long endTime;
		protected BlockPos originPos;
		protected float distanceFallen;
		
		private ConfigQuake(int range)
		{
			this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(range));
		}
		
		public ConfigQuake(Optional<Integer> phaseIn, Optional<Long> finishIn, Optional<BlockPos> originIn, Optional<Float> distanceIn, Optional<Integer> rangeIn)
		{
			phase = phaseIn.orElse(-1);
			endTime = finishIn.orElse(0L);
			originPos = originIn.orElse(BlockPos.ORIGIN);
			distanceFallen = distanceIn.orElse(0F);
			maxRange = MathHelper.clamp(rangeIn.orElse(4), 0, 32);
		}
		
		public static ConfigQuake ofRange(int rangeIn) { return new ConfigQuake(rangeIn); }
		
		protected Optional<Integer> phase() { return Optional.of(phase); }
		protected Optional<Long> time() { return Optional.of(endTime); }
		protected Optional<BlockPos> origin() { return Optional.of(originPos); }
		protected Optional<Float> distance() { return Optional.of(distanceFallen); }
		protected Optional<Integer> range() { return Optional.of(maxRange); }
		
		public int maxShockwaveTime() { return maxRange * AbilityQuake.INTERVAL; }
		
		public NbtCompound toNbt()
		{
			return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static ConfigQuake fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
	
	private static enum Phase
	{
		FALLING,
		SHOCKWAVE;
	}
}
