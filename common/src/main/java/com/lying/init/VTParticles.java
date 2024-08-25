package com.lying.init;

import java.util.function.Supplier;

import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKeys;

public class VTParticles
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.PARTICLE_TYPE);
	
	public static final RegistrySupplier<ParticleType<SimpleParticleType>> SHOCKWAVE = register("shockwave", true);
	
	private static RegistrySupplier<ParticleType<SimpleParticleType>> register(String nameIn, boolean alwaysShow) { return register(nameIn, () -> new SimpleType(alwaysShow)); }
	
	private static <T extends ParticleEffect> RegistrySupplier<ParticleType<T>> register(String nameIn, Supplier<ParticleType<T>> supplierIn)
	{
		return PARTICLES.register(Reference.ModInfo.prefix(nameIn), supplierIn);
	}
	
	public static void init()
	{
		PARTICLES.register();
	}
	
	private static class SimpleType extends SimpleParticleType
	{
		public SimpleType(boolean alwaysShow)
		{
			super(alwaysShow);
		}
	}
}
