package com.lying.ability;

import java.util.Optional;

import com.lying.data.VTTags;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

// TODO Incorporate more broadly to prevent suffocation damage and permit limited vision whilst inside of blocks
public class AbilityIntangible extends Ability implements IBlockCollisionAbility
{
	public AbilityIntangible(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance)
	{
		return !(state.isIn(VTTags.UNPHASEABLE) || state.isIn(BlockTags.WITHER_IMMUNE));
	}
	
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance)
	{
		return isApplicableTo(state, living, instance) && (living.isSneaking() || pos.getY() >= living.getY()) ? Optional.of(VoxelShapes.empty()) : Optional.empty();
	}
}
