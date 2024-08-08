package com.lying.ability;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class AbilityIntangible extends Ability implements IPhasingAbility
{
	public AbilityIntangible(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance)
	{
		return true;
	}
	
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance)
	{
		return isApplicableTo(state, living, instance) && pos.getY() > living.getWorld().getBottomY() && (living.isSneaking() || pos.getY() >= living.getY()) ? Optional.of(VoxelShapes.empty()) : Optional.empty();
	}
}
