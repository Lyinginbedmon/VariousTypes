package com.lying.emission;

import com.lying.VariousTypes;
import com.lying.entity.EmitterEntity;
import com.lying.entity.SmokeEntity;
import com.lying.init.VTEmissions;
import com.lying.reference.Reference;
import com.lying.utility.FloodFill;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SmokescreenEmission extends Emission
{
	public static final int RATE_OF_EXPANSION	= Reference.Values.TICKS_PER_SECOND / 4;
	private int maxRadius = 4;
	private int prevRadius = -1;
	private int duration = Reference.Values.TICKS_PER_SECOND * 20;
	
	private FloodFill floodFill = null;
	
	public SmokescreenEmission(Identifier regNameIn)
	{
		super(regNameIn);
	}
	
	public static SmokescreenEmission create(int radius, int duration)
	{
		SmokescreenEmission emission = VTEmissions.SMOKESCREEN.get();
		emission.maxRadius = radius;
		return emission;
	}
	
	public boolean shouldTick(int age)
	{
		if(maxRadius < 1)
		{
			VariousTypes.LOGGER.warn(" # Smokescreen emission had no useful max radius");
			return false;
		}
		return age < duration && prevRadius < maxRadius;
	}
	
	protected Emission readFromNbt(NbtCompound nbt)
	{
		prevRadius = nbt.getInt("prevRadius");
		maxRadius = nbt.getInt("radius");
		duration = nbt.getInt("duration");
		return this;
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.putInt("prevRadius", prevRadius);
		nbt.putInt("radius", maxRadius);
		nbt.putInt("duration", duration);
		return nbt;
	}
	
	public void tick(Vec3d pos, ServerWorld world, int age, EmitterEntity emitter)
	{
		if(age%RATE_OF_EXPANSION > 0)
			return;
		
		if(floodFill == null)
		{
			FloodFill.Search search = new FloodFill.Search(emitter.getBlockPos(), maxRadius);
			search.doFloodFill(world, emitter);
			floodFill = search.results();
		}
		
		int radius = (int)MathHelper.clamp(Math.floorDiv(age, RATE_OF_EXPANSION), 0, maxRadius);
		if(radius == prevRadius)
			return;
		
		float progress = 1F - ((float)radius / (float)maxRadius);
		int duration = (int)(progress * this.duration);
		floodFill.get(radius).stream()
			.forEach(e -> spawnSmoke(e, world, duration));
		
		prevRadius = radius;
	}
	
	public static void spawnSmoke(BlockPos position, ServerWorld world, int lifespan)
	{
		SmokeEntity smoke = SmokeEntity.create(position, world);
		smoke.setLifespan(lifespan + world.getRandom().nextInt(Reference.Values.TICKS_PER_SECOND * 5));
		world.spawnEntity(smoke);
	}
}
