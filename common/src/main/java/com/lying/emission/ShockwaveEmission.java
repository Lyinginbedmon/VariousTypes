package com.lying.emission;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityQuake;
import com.lying.entity.EmitterEntity;
import com.lying.entity.ShakenBlockEntity;
import com.lying.init.VTEmissions;
import com.lying.init.VTParticleTypes;
import com.lying.init.VTSoundEvents;
import com.lying.utility.VTUtils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ShockwaveEmission extends Emission
{
	public static final int INTERVAL	= AbilityQuake.INTERVAL;
	private Optional<BlockPos> origin = Optional.empty();
	private int maxRadius = 4;
	
	public ShockwaveEmission(Identifier regNameIn)
	{
		super(regNameIn);
	}
	
	public static ShockwaveEmission create(BlockPos origin, int radius)
	{
		ShockwaveEmission emission = VTEmissions.SHOCKWAVE.get();
		emission.origin = Optional.of(origin);
		emission.maxRadius = radius;
		return emission;
	}
	
	public boolean shouldTick(int age)
	{
		if(maxRadius < 1)
		{
			VariousTypes.LOGGER.warn(" # Shockwave emission had no useful max radius");
			return false;
		}
		return age < (INTERVAL * maxRadius);
	}
	
	protected Emission readFromNbt(NbtCompound nbt)
	{
		maxRadius = nbt.getInt("radius");
		origin = NbtHelper.toBlockPos(nbt, "origin");
		return this;
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.putInt("radius", maxRadius);
		origin.ifPresent(p -> nbt.put("origin", NbtHelper.fromBlockPos(p)));
		return nbt;
	}
	
	public void tick(Vec3d pos, ServerWorld world, int age, EmitterEntity emitter)
	{
		if(origin.isEmpty())
			origin = Optional.of(emitter.getBlockPos().down());
		
		if(age == 0)
		{
			VTUtils.playSound(emitter, VTSoundEvents.QUAKE_IMPACT.get(), SoundCategory.PLAYERS, 1F, 1F);
			VTUtils.spawnParticles(
					world, 
					VTParticleTypes.SHOCKWAVE.get(), 
					new Vec3d(origin.get().getX(), origin.get().getY(), origin.get().getZ()).add(0.5D, 1.5D, 0.5D), 
					new Vec3d(0, 1, 0).multiply(maxRadius + 2));
		}
		else if(age%INTERVAL > 0)
			return;
		
		/** Current shockwave radius */
		int radius = 1 + Math.floorDiv(age, INTERVAL);
		
		origin.ifPresent(epicenter -> 
		{
			// Iteratively calculate one quarter circle and transform that into the full circle with 90 degree rotations
			List<BlockPos> alreadyChecked = Lists.newArrayList();
			for(int x=radius; x>=0; x--)
				for(int z=radius; z>=0; z--)
				{
					if((int)Math.floor(Math.sqrt((x * x) + (z * z))) != radius)
						continue;
					
					// TODO Additional disqualification conditions to further reduce iteration load
					
					tryAffectBlock(epicenter.add(x, 0, z), world, alreadyChecked).ifPresent(p -> alreadyChecked.add(p));
					tryAffectBlock(epicenter.add(x, 0, -z), world, alreadyChecked).ifPresent(p -> alreadyChecked.add(p));
					tryAffectBlock(epicenter.add(-x, 0, z), world, alreadyChecked).ifPresent(p -> alreadyChecked.add(p));
					tryAffectBlock(epicenter.add(-x, 0, -z), world, alreadyChecked).ifPresent(p -> alreadyChecked.add(p));
				}
		});
	}
	
	private static boolean canShakeBlock(BlockPos pos, ServerWorld world, List<BlockPos> ignore)
	{
		if(ignore.stream().anyMatch(p -> p.withY(pos.getY()).equals(pos)))
			return false;
		else if(pos.getY() < world.getBottomY() || pos.getY() > world.getTopY())
			return false;
		else if(world.getBlockState(pos).isIn(BlockTags.WITHER_IMMUNE))
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
}
