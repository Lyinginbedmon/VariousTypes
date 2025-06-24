package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.lying.ability.AbilityQuake;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ShockwaveParticle extends SpriteBillboardParticle
{
	private final Vec3d rotation;
	private final float maxRadius;
	
	public ShockwaveParticle(ClientWorld clientWorld, double x, double y, double z, double angX, double angY, double angZ)
	{
		super(clientWorld, x, y, z, 0F, 0F, 0F);
		this.velocityX = this.velocityY = this.velocityZ = 0D;
		
		Vec3d vel = new Vec3d(angX, angY, angZ);
		rotation = vel.normalize();
		maxRadius = (float)(int)vel.length();
		this.maxAge = (int)(maxRadius * AbilityQuake.INTERVAL);
		this.scale = 0F;
	}
	
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public void tick()
	{
		super.tick();
		
		float time = (float)this.age / (float)this.maxAge;
		this.scale = time * maxRadius;
		
		this.alpha = Math.min(time / 0.2F, 1F - (float)Math.pow(time, 6D));
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		Quaternionf rotation = vectorToQuaternion(this.rotation);
		Vec3d camPos = camera.getPos();
		float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - camPos.getX());
		float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - camPos.getY());
		float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - camPos.getZ());
		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
		float i = this.getSize(tickDelta);
		for(int j = 0; j < 4; ++j)
		{
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(rotation);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}
		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = 255;
		// Primary face
		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		
		// Reverse face
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
	}
	
	private static Quaternionf vectorToQuaternion(Vec3d rotation)
	{
		float pitch = (float)Math.asin(-rotation.y);
		float yaw = (float)Math.atan2(rotation.x, rotation.z);
		return new Quaternionf().rotateXYZ(pitch, yaw, 0);
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
			ShockwaveParticle particle = new ShockwaveParticle(clientWorld, x, y, z, velX, velY, velZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
