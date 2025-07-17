package com.lying.entity;

import java.util.Optional;

import com.lying.reference.Reference;
import com.lying.utility.FloodFill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// FIXME Add lifespan value before despawn
public class SmokeCloudEntity extends Entity
{
	public static final TrackedData<NbtCompound> INNER_FILL	= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	public static final TrackedData<NbtCompound> OUTER_FILL	= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	public static final TrackedData<Integer> MAX_SIZE	= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> SIZE		= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> AGE		= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	public static final int RATE_OF_EXPANSION	= Reference.Values.TICKS_PER_SECOND / 3;
	protected FloodFill innerCache = null, outerCache = null;
	
	public SmokeCloudEntity(EntityType<? extends SmokeCloudEntity> type, World world)
	{
		super(type, world);
		setNoGravity(true);
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(MAX_SIZE, 4);
		builder.add(INNER_FILL, new NbtCompound());
		builder.add(OUTER_FILL, new NbtCompound());
		builder.add(SIZE, 0);
		builder.add(AGE, 0);
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		getDataTracker().set(AGE, nbt.getInt("Lifetime"));
		if(nbt.contains("MaxRadius"))
			getDataTracker().set(MAX_SIZE, nbt.getInt("MaxRadius"));
		if(nbt.contains("Radius"))
			setRadius(nbt.getInt("Radius"));
		loadFloodFill("InnerArea", INNER_FILL, nbt);
		loadFloodFill("OuterArea", OUTER_FILL, nbt);
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("Lifetime", getDataTracker().get(AGE).intValue());
		nbt.putInt("MaxRadius", getMaxRadius());
		nbt.putInt("Radius", getInnerRadius());
		storeFloodFill("InnerArea", INNER_FILL, nbt);
		storeFloodFill("OuterArea", OUTER_FILL, nbt);
	}
	
	protected void loadFloodFill(String target, TrackedData<NbtCompound> entry, NbtCompound nbt)
	{
		if(nbt.contains(target, NbtElement.COMPOUND_TYPE))
			getDataTracker().set(entry, nbt.getCompound(target));
	}
	
	protected void storeFloodFill(String target, TrackedData<NbtCompound> entry, NbtCompound nbt)
	{
		NbtCompound fill = getDataTracker().get(entry);
		if(!fill.isEmpty())
			nbt.put(target, fill);
	}
	
	public int getMaxRadius() { return getDataTracker().get(MAX_SIZE).intValue(); }
	
	public int getInnerRadius() { return getDataTracker().get(SIZE).intValue(); }
	
	private void setInnerArea(FloodFill fill)
	{
		innerCache = fill;
		NbtCompound nbt = new NbtCompound();
		nbt.put("Map", fill.toNbt());
		getDataTracker().set(INNER_FILL, nbt);
	}
	
	public Optional<FloodFill> getInnerArea()
	{
		if(innerCache != null)
			return Optional.of(innerCache);
		
		NbtCompound nbt = getDataTracker().get(INNER_FILL);
		if(nbt.isEmpty() || !nbt.contains("Map"))
			return Optional.empty();
		
		FloodFill fill = null;
		try
		{
			fill = FloodFill.fromNbt(nbt.get("Map"));
		}
		catch(Exception e) { }
		
		if(fill != null)
			return Optional.of(innerCache = fill);
		else
			return Optional.empty();
	}
	
	public float getOuterScale(float partialTicks)
	{
		int radius = getInnerRadius();
		if(radius > 8)
			return 0F;
		
		float age = getDataTracker().get(AGE);
		int maxRadius = getMaxRadius();
		if(radius != maxRadius)
		{
			age += partialTicks;
			return (age%RATE_OF_EXPANSION) / (float)RATE_OF_EXPANSION;
		}
		else
		{
			if(Math.floorDiv((int)age, RATE_OF_EXPANSION) > maxRadius)
				return 0.5F;
			else
				return Math.min(0.5F, partialTicks);
		}
	}
	
	private void setOuterArea(FloodFill fill)
	{
		outerCache = fill;
		NbtCompound nbt = new NbtCompound();
		nbt.put("Map", fill.toNbt());
		getDataTracker().set(OUTER_FILL, nbt);
	}
	
	public Optional<FloodFill> getOuterArea()
	{
		if(getInnerRadius() > 8)
			return Optional.empty();
		
		if(outerCache != null)
			return Optional.of(outerCache);
		
		NbtCompound nbt = getDataTracker().get(OUTER_FILL);
		if(nbt.isEmpty() || !nbt.contains("Map"))
			return Optional.empty();
		
		FloodFill fill = null;
		try
		{
			fill = FloodFill.fromNbt(nbt.get("Map"));
		}
		catch(Exception e) { }
		
		if(fill != null)
			return Optional.of(outerCache = fill);
		else
			return Optional.empty();
	}
	
	public void onTrackedDataSet(TrackedData<?> data)
	{
		if(INNER_FILL.equals(data))
			innerCache = null;
		else if(OUTER_FILL.equals(data))
			outerCache = null;
		else if(SIZE.equals(data))
			calculateDimensions();
	}
	
	public void tick()
	{
		super.tick();
		if(getWorld().isClient())
			return;
		
		int age = getDataTracker().get(AGE);
		getDataTracker().set(AGE, ++age);
		
		if(age%RATE_OF_EXPANSION == 0)
			updateRadius(age, getMaxRadius());
	}
	
	protected void updateRadius(int age, int maxRadius)
	{
		int radius = getDataTracker().get(SIZE).intValue();
		int newRadius = (int)MathHelper.clamp(Math.floorDiv(age, RATE_OF_EXPANSION), 0, maxRadius);
		
		// Limit cloud expansion/contraction to 1-block-change steps
		setRadius(radius + (int)Math.signum(newRadius - radius));
	}
	
	protected void setRadius(int radius)
	{
		if(radius == getInnerRadius())
			return;
		
		if(!getWorld().isClient())
		{
			FloodFill.Search innerSearch = new FloodFill.Search(getBlockPos(), radius);
			FloodFill.Search outerSearch = new FloodFill.Search(getBlockPos(), radius + 1);
			
			innerSearch.doFloodFill((ServerWorld)getWorld(), this);
			outerSearch.doFloodFill((ServerWorld)getWorld(), this);
			
			FloodFill inner = innerSearch.results();
			setInnerArea(innerSearch.results());
			// Exclude exterior positions of the inner area from the outer area to reduce excess position rendering
			setOuterArea(outerSearch.results().exclude(inner));
		}
		
		getDataTracker().set(SIZE, radius);
		calculateDimensions();
	}
	
	public void calculateDimensions()
	{
		double x = getX(), y = getY(), z = getZ();
		super.calculateDimensions();
		setPosition(x, y, z);
	}
	
	public EntityDimensions getDimensions(EntityPose pose)
	{
		float radius = Math.max(1F, getDataTracker().get(SIZE).floatValue());
		return EntityDimensions.fixed(radius * 2F, radius );
	}
}
