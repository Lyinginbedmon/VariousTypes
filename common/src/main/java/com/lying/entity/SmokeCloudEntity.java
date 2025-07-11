package com.lying.entity;

import java.util.Optional;

import com.lying.emission.SmokescreenEmission;
import com.lying.utility.FloodFill;

import net.minecraft.entity.Entity;
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

public class SmokeCloudEntity extends Entity
{
	public static final TrackedData<NbtCompound> FILL	= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	public static final TrackedData<Integer> MAX_SIZE	= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> SIZE		= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> AGE		= DataTracker.registerData(SmokeCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	public static final int RATE_OF_EXPANSION	= SmokescreenEmission.RATE_OF_EXPANSION;
	protected FloodFill fillCache = null;
	
	public SmokeCloudEntity(EntityType<? extends SmokeCloudEntity> type, World world)
	{
		super(type, world);
		setNoGravity(true);
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(FILL, new NbtCompound());
		builder.add(MAX_SIZE, 4);
		builder.add(SIZE, 0);
		builder.add(AGE, 0);
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		getDataTracker().set(AGE, nbt.getInt("Lifetime"));
		if(nbt.contains("Radius"))
			setRadius(nbt.getInt("Radius"));
		if(nbt.contains("MaxRadius"))
			getDataTracker().set(MAX_SIZE, nbt.getInt("MaxRadius"));
		if(nbt.contains("AffectedArea", NbtElement.COMPOUND_TYPE))
			getDataTracker().set(FILL, nbt.getCompound("AffectedArea"));
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("Lifetime", getDataTracker().get(AGE).intValue());
		nbt.putInt("MaxRadius", getDataTracker().get(MAX_SIZE).intValue());
		nbt.putInt("Radius", getRadius());
		NbtCompound fill = getDataTracker().get(FILL);
		if(!fill.isEmpty())
			nbt.put("AffectedArea", fill);
	}
	
	public Optional<FloodFill> getAffectedArea()
	{
		NbtCompound nbt = getDataTracker().get(FILL);
		if(nbt.isEmpty() || !nbt.contains("Map"))
			return Optional.empty();
		
		FloodFill fill = null;
		try
		{
			fill = FloodFill.fromNbt(nbt.get("Map"));
		}
		catch(Exception e) { }
		return fill == null ? Optional.empty() : Optional.of(fill);
	}
	
	private void setAffectedArea(NbtElement fill)
	{
		NbtCompound nbt = new NbtCompound();
		nbt.put("Map", fill);
		getDataTracker().set(FILL, nbt);
	}
	
	public int getRadius() { return getDataTracker().get(SIZE).intValue(); }
	
	public void tick()
	{
		super.tick();
		if(getWorld().isClient()) return;
		
		int age = getDataTracker().get(AGE);
		getDataTracker().set(AGE, ++age);
		
		int maxRadius = getMaxRadius();
		
		if(fillCache == null)
		{
			Optional<FloodFill> fill = getAffectedArea();
			if(fill.isEmpty())
			{
				FloodFill.Search search = new FloodFill.Search(getBlockPos(), maxRadius);
				search.doFloodFill((ServerWorld)getWorld(), this);
				fillCache = search.results();
				setAffectedArea(fillCache.toNbt());
			}
			else
				fillCache = fill.get();
		}
		
		if(age%RATE_OF_EXPANSION == 0)
			updateRadius(age, maxRadius);
	}
	
	protected void updateRadius(int age, int maxRadius)
	{
		int radius = getRadius();
		int newRadius = (int)MathHelper.clamp(Math.floorDiv(age, RATE_OF_EXPANSION), 0, maxRadius);
		if(radius != newRadius)
			setRadius(newRadius);
	}
	
	protected void setRadius(int radius) { getDataTracker().set(SIZE, radius); }
	
	public int getMaxRadius() { return getDataTracker().get(MAX_SIZE).intValue(); }
}
