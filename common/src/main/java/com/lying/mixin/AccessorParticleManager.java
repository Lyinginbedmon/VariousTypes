package com.lying.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;

@Mixin(ParticleManager.class)
public interface AccessorParticleManager
{
	@Invoker("createParticle")
	@Nullable
	public <T extends ParticleEffect> Particle makeParticle(T parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);
}
