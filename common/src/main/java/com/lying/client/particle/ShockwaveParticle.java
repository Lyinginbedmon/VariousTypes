package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;

import com.lying.reference.Reference;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;

public class ShockwaveParticle extends SpriteBillboardParticle
{
	private final SpriteProvider spriteProvider;
	// Values necessary to render the particle with a defined direction rather than facing camera
	private final Vec3d pos, normal;
	
	public ShockwaveParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider providerIn)
	{
		super(world, x, y, z, 0, 0, 0);
		this.spriteProvider = providerIn;
		this.pos = new Vec3d(x, y, z);
		this.normal = new Vec3d(velX, velY, velZ);
		this.velocityX = this.velocityY = this.velocityZ = 0D;
		this.maxAge = Reference.Values.TICKS_PER_SECOND * 3;
		this.scale = 1F;
		this.setSprite(spriteProvider);
		System.out.println("Shockwave particle generated at "+this.pos.toString());
	}
	
	public ParticleTextureSheet getType() { return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE; }
	
	public int getBrightness(float tickDelta) { return 240; }
	
	public float getSize(float tickDelta)
	{
		float age = (float)this.age + tickDelta;
		return this.scale * (age / maxAge);
	}
	
	protected float getMinU() { return 0F; }
	
	protected float getMaxU() { return 1F; }
	
	protected float getMinV() { return 0F; }
	
	protected float getMaxV() { return 1F; }
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		super.buildGeometry(vertexConsumer, camera, tickDelta);
		Vec3d pos = new Vec3d(x, y, z);
		System.out.println("Building geometry for shockwave particle at "+pos.toString());
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
			System.out.println("Creating shockwave particle");
			return new ShockwaveParticle(clientWorld, x, y, z, velX, velY, velZ, this.spriteProvider);
		}
	}
}
