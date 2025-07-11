package com.lying.entity;

import com.lying.emission.Emission;
import com.lying.init.VTEmissions;
import com.lying.init.VTEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EmitterEntity extends Entity
{
	protected static final TrackedData<Integer> TICKS_EMITTING			= DataTracker.registerData(EmitterEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	protected Emission emission = VTEmissions.SMOKE.get();
	
	public EmitterEntity(EntityType<? extends EmitterEntity> type, World world)
	{
		super(type, world);
		setNoGravity(true);	// No gravity by default but can be enabled to fall
	}
	
	public static EmitterEntity create(Vec3d position, ServerWorld world, Emission emission)
	{
		EmitterEntity emitter = VTEntityTypes.EMITTER.get().create(world);
		emitter.setPos(position.x, position.y, position.z);
		emitter.emission = emission;
		return emitter;
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(TICKS_EMITTING, 0);
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		getDataTracker().set(TICKS_EMITTING, nbt.getInt("TicksEmitting"));
		if(nbt.contains("Emission", NbtElement.COMPOUND_TYPE))
			emission = Emission.fromNbt(nbt.getCompound("Emission"));
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("TicksEmitting", getAge());
		nbt.put("Emission", emission.toNbt());
	}
	
	protected double getGravity() { return LivingEntity.GRAVITY; }
	
	public int getAge() { return getDataTracker().get(TICKS_EMITTING).intValue(); }
	
	public void tick()
	{
		super.tick();
		if(getEntityWorld().isClient()) return;
		
		int ticksEmitting = getAge();
		if(emission.shouldTick(ticksEmitting))
		{
			emission.tick(getPos(), (ServerWorld)getWorld(), ticksEmitting, this);
			getDataTracker().set(TICKS_EMITTING, ++ticksEmitting);
		}
		else
			discard();
	}
}
