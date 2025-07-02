package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class RageParticle extends SpriteBillboardParticle
{
	private final SpriteProvider spriteProvider;
	
	public RageParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velX, double velY, double velZ, SpriteProvider spriteProviderIn)
	{
		super(clientWorld, posX, posY, posZ);
		setSprite(spriteProvider = spriteProviderIn);
		Random rand = clientWorld.getRandom();
		this.velocityX = this.velocityZ = 0D;
		this.velocityY = 0.04D * rand.nextDouble();
		
		this.maxAge = Reference.Values.TICKS_PER_SECOND;
		this.scale = 0.5F;
		this.alpha = 0F;
	}
	
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public void tick()
	{
		super.tick();
		float time = (float)this.age / (float)this.maxAge;
		this.alpha = Math.min(time / 0.2F, 1F - (float)Math.pow(time, 6D));
		setSpriteForAge(this.spriteProvider);
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
        Vec3d camPos = camera.getPos();
        Vec3d prevPos = new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
        Vec3d pos = new Vec3d(this.x, this.y, this.z);
        Vec3d offset = pos.subtract(camPos).normalize();
        
        float renderX = (float)(MathHelper.lerp((double)tickDelta, prevPos.x, pos.x) - camPos.getX() + offset.getX());
        float renderY = (float)(MathHelper.lerp((double)tickDelta, prevPos.y, pos.y) - camPos.getY() + offset.getY());
        float renderZ = (float)(MathHelper.lerp((double)tickDelta, prevPos.z, pos.z) - camPos.getZ() + offset.getZ());
        Quaternionf rotation = new Quaternionf();
        getRotator().setRotation(rotation, camera, tickDelta);
        Vector3f[] vector3fs = new Vector3f[]{
        		new Vector3f(-1F, -1F, 0F), 
        		new Vector3f(-1F, 1F, 0F), 
        		new Vector3f(1F, 1F, 0F), 
        		new Vector3f(1F, -1F, 0F)};
        float i = this.getSize(tickDelta);
        for (int j = 0; j < 4; ++j)
        {
            Vector3f vector3f = vector3fs[j];
            vector3f.rotate(rotation);
            vector3f.mul(i);
            vector3f.add(renderX, renderY, renderZ);
        }
        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = this.getBrightness(tickDelta);
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
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
			RageParticle particle = new RageParticle(clientWorld, x, y, z, velX, velY, velZ, spriteProvider);
//			particleType.getParentUuid().ifPresent(id -> particle.parentId = Optional.of(id));
			return particle;
		}
	}
}
