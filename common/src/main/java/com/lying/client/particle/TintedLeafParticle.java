package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class TintedLeafParticle extends LeafParticle
{
	public TintedLeafParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velX, double velY, double velZ)
	{
		super(clientWorld, posX, posY, posZ, velX, velY, velZ);
		setColor(
				MathHelper.clamp((float)velX, 0F, 1F),
				MathHelper.clamp((float)velY, 0F, 1F),
				MathHelper.clamp((float)velZ, 0F, 1F)
				);
		
		Random rand = clientWorld.getRandom();
		this.velocityY = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.velocityX = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.velocityZ = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
	}
	
	public static class Factory implements ParticleFactory<SimpleParticleType>
	{
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}
		
		@Nullable
		public Particle createParticle(SimpleParticleType particleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ)
		{
			TintedLeafParticle particle = new TintedLeafParticle(clientWorld, x, y, z, velX, velY, velZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
