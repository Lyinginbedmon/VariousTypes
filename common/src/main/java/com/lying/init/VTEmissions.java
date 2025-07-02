package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.emission.Emission;
import com.lying.emission.ShockwaveEmission;
import com.lying.entity.EmitterEntity;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class VTEmissions
{
	private static final Map<Identifier, Supplier<? extends Emission>> EMISSION_REGISTRY = new HashMap<>();
	
	public static final Supplier<ShockwaveEmission> SHOCKWAVE	= register(prefix("shockwave"), ShockwaveEmission::new);
	
	public static final Supplier<Emission> SMOKE	= register(prefix("smoke"), id -> new Emission(id)
	{
		public boolean shouldTick(int age)
		{
			return age < Reference.Values.TICKS_PER_MINUTE;
		}
		
		public void tick(Vec3d pos, ServerWorld world, int age, EmitterEntity emitter)
		{
			if(age%10 == 0)
				VTUtils.spawnParticles(world, ParticleTypes.SMOKE, pos, Vec3d.ZERO);
		}
	});
	
	public static <T extends Emission> Supplier<T> register(Identifier regName, Function<Identifier,T> type)
	{
		return register(() -> type.apply(regName));
	}
	
	public static <T extends Emission> Supplier<T> register(Supplier<T> supplierIn)
	{
		EMISSION_REGISTRY.put(supplierIn.get().registryName(), supplierIn);
		return supplierIn;
	}
	
	public static boolean exists(Identifier registryName) { return EMISSION_REGISTRY.containsKey(registryName); }
	
	@Nullable
	public static Emission get(Identifier registryName)
	{
		return exists(registryName) ? EMISSION_REGISTRY.get(registryName).get() : null;
	}
	
	public static void init()
	{
		VariousTypes.LOGGER.info(" # Registered {} emission effects", EMISSION_REGISTRY.size());
	}
}
