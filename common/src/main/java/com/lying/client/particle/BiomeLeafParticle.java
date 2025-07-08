package com.lying.client.particle;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class BiomeLeafParticle extends LeafParticle
{
	private static final BlockColorProvider LEAF_COLOR = (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
	
	public BiomeLeafParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velX, double velY, double velZ)
	{
		super(clientWorld, posX, posY, posZ, velX, velY, velZ);
		Random rand = clientWorld.getRandom();
		this.velocityY = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.velocityX = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
		this.velocityZ = 0.08D * (rand.nextDouble() - 0.5D) / 0.5D;
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta)
	{
		BlockPos pos = BlockPos.ofFloored(x, y, z);
		int tint = LEAF_COLOR.getColor(world.getBlockState(pos), world, pos, 0);
		setColor(
				(float)((tint >> 16) & 0xff) / 255F, 
				(float)((tint >> 8) & 0xff) / 255F, 
				(float)(tint & 0xff) / 255F);
		super.buildGeometry(vertexConsumer, camera, tickDelta);
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
			BiomeLeafParticle particle = new BiomeLeafParticle(clientWorld, x, y, z, velX, velY, velZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
