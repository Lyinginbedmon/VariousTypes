package com.lying.entity;

import com.lying.init.VTEntityTypes;
import com.lying.reference.Reference;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SmokeEntity extends Entity
{
	protected static final TrackedData<Integer> TICKS_REMAINING	= DataTracker.registerData(SmokeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<Float> SIZE				= DataTracker.registerData(SmokeEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final EntityDimensions BASE_SIZE = EntityDimensions.changing(1F, 1F).withEyeHeight(0.5F);
	private static final float STEP_SIZE		= 1F / 4F;
	public static final int SHRINK_START_TIME	= Reference.Values.TICKS_PER_SECOND * 5;
	
	@SuppressWarnings("deprecation")
	public SmokeEntity(EntityType<? extends SmokeEntity> type, World world)
	{
		super(type, world);
		reinitDimensions();
		setNoGravity(true);
	}
	
	public static SmokeEntity create(BlockPos position, ServerWorld world)
	{
		return create(new Vec3d(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D), world);
	}
	
	public static SmokeEntity create(Vec3d position, ServerWorld world)
	{
		SmokeEntity smoke = VTEntityTypes.THICK_SMOKE.get().create(world);
		smoke.setPos(position.x, position.y, position.z);
		return smoke;
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(TICKS_REMAINING, Reference.Values.TICKS_PER_MINUTE);
		builder.add(SIZE, 1F);
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("TicksRemaining", getTicksRemaining());
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("TicksRemaining"))
			getDataTracker().set(TICKS_REMAINING, nbt.getInt("TicksRemaining"));
	}
	
	protected double getGravity() { return LivingEntity.GRAVITY; }
	
	public int getTicksRemaining() { return getDataTracker().get(TICKS_REMAINING).intValue(); }
	
	public void setLifespan(int ticks) { getDataTracker().set(TICKS_REMAINING, ticks); }
	
	public void tick()
	{
		super.tick();
		if(getEntityWorld().isClient()) return;
		
		float size = getSize();
		int lifespan = getTicksRemaining();
		if(lifespan > 0)
		{
			getDataTracker().set(TICKS_REMAINING, --lifespan);
			
			if(size != getSize())
			{
				refreshPosition();
				calculateDimensions();
			}
		}
		else
		{
			getWorld().addParticle(ParticleTypes.SMOKE, getX(), getEyeY(), getZ(), 0, 0, 0);
			discard();
		}
	}
	
	public void calculateDimensions()
	{
		double x = getX(), y = getY(), z = getZ();
		super.calculateDimensions();
		setPosition(x, y, z);
	}
	
	public EntityDimensions getDimensions(EntityPose pose)
	{
		return BASE_SIZE.scaled(getSize());
	}
	
	public void onTrackedDataSet(TrackedData<?> data)
	{
        if(TICKS_REMAINING.equals(data))
        	getDataTracker().set(SIZE, calculateSize());
        else if(SIZE.equals(data))
            calculateDimensions();
        super.onTrackedDataSet(data);
    }
	
	public float getSize() { return getDataTracker().get(SIZE).floatValue(); }
	
	public float calculateSize()
	{
		float size = (float)Math.pow((float)getTicksRemaining() / (float)SHRINK_START_TIME, 3);
		size -= size%STEP_SIZE;
		return MathHelper.clamp(size, STEP_SIZE, 1F);
	}
	
	public PistonBehavior getPistonBehavior() { return PistonBehavior.DESTROY; }
}
