package com.lying.entity;

import com.lying.init.VTEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class ShakenBlockEntity extends ThrownBlockEntity
{
	public ShakenBlockEntity(EntityType<? extends ShakenBlockEntity> entityType, World world)
	{
		super(entityType, world);
		this.isCollidable = true;
	}
	
	public ShakenBlockEntity(World world, double x, double y, double z, BlockState block)
	{
		this(VTEntityTypes.SHAKEN_BLOCK.get(), world);
		this.setBlockState(block);
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(0D, 0.6D, 0D);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}
	
	public static ShakenBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state)
	{
		ShakenBlockEntity blockEntity = new ShakenBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state.contains(Properties.WATERLOGGED) ? (BlockState)state.with(Properties.WATERLOGGED, false) : state);
		world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
		world.spawnEntity(blockEntity);
		return blockEntity;
	}
	
	protected void clientTick()
	{
		Random rand = getWorld().getRandom();
		if(rand.nextInt(5) > 0)
			return;
		
		getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState()), getX(), getY() + 0.5D, getZ(), 0, 0, 0);
	}
	
	public void serverTick()
	{
		super.serverTick();
		if(isRemoved()) return;
		BlockPos blockPos = getBlockPos();
		World world = getWorld();
		if(!((this.timeFalling <= 100 || blockPos.getY() > world.getBottomY() && blockPos.getY() <= world.getTopY()) && this.timeFalling <= 600))
		{
			if(this.dropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
				this.dropItem(blockState().getBlock());
			discard();
		}
	}
}
