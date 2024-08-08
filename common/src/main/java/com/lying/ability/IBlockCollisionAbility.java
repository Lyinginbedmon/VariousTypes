package com.lying.ability;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

/**
 * An ability that can modify block collision behaviours, whether that be making them more or less passable<br>
 * This interface primarily handles making collision boxes MORE solid, otherwise use IPhasingAbility
 */
public interface IBlockCollisionAbility
{
	/** Returns whether this ability could apply to the given blockstate, absent any positional context */
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance);
	
	/** Returns an optional containing the modified collision shape, if any */
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance);
}
