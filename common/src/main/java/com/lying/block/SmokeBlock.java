package com.lying.block;

import com.lying.init.VTItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SmokeBlock extends Block 
{
	public SmokeBlock(Settings settings)
	{
		super(settings);
	}
	
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return VoxelShapes.empty();
	}
	
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return context.isHolding(VTItems.SMOKE.get()) ? VoxelShapes.fullCube() : VoxelShapes.empty();
	}
}
