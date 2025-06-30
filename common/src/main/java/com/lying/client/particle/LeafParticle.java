package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;

import com.lying.reference.Reference;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class LeafParticle extends SpriteBillboardParticle
{
	public LeafParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velX, double velY, double velZ)
	{
		super(clientWorld, posX, posY, posZ);
		Random rand = clientWorld.getRandom();
		this.velocityY = 0D;
		this.velocityX = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.velocityZ = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.angle = this.prevAngle = (float)Math.toRadians(rand.nextInt(90));
		this.maxAge = Reference.Values.TICKS_PER_SECOND;
		this.scale = 0.15F;
	}
	
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public void tick()
	{
		super.tick();
		this.prevAngle = this.angle;
		this.angle += (float)Math.toRadians(2.25D);
		
		float time = (float)this.age / (float)this.maxAge;
		this.alpha = 1F - (float)Math.pow(time, 6D);
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
			LeafParticle particle = new LeafParticle(clientWorld, x, y, z, velX, velY, velZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
