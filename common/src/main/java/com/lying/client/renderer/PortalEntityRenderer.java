package com.lying.client.renderer;

import com.lying.client.texture.PortalSpriteManager;
import com.lying.entity.PortalEntity;
import com.lying.entity.PortalEntity.Phase;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class PortalEntityRenderer extends EntityRenderer<PortalEntity>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final float SCALE = 1.5F;
	
	public PortalEntityRenderer(Context ctx)
	{
		super(ctx);
	}
	
	public Identifier getTexture(PortalEntity entity)
	{
		return PortalSpriteManager.ATLAS_ID;
	}
	
	public void render(PortalEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		Phase phase = entity.getPhase(tickDelta);
		renderSprite(
				PortalSpriteManager.INSTANCE.getSprite(phase.texture()), 
				entity.getHeight(), 
				entity.getPos(), 
				matrices, 
				vertexConsumers);
	}
	
	private static void renderSprite(Sprite sprite, float height, Vec3d entityPos, MatrixStack matrices, VertexConsumerProvider vertexConsumers)
	{
		final Vec3d camPos = mc.gameRenderer.getCamera().getPos();
		final Vec3d offset = entityPos.add(0D, height * 0.5D, 0D).subtract(camPos).normalize();
		final double porYaw = MathHelper.atan2(offset.getX(), offset.getZ());
		final double porPitch = -MathHelper.atan2(offset.getY(), offset.horizontalLength());
		
		matrices.push();
			matrices.translate(0D, height * 0.5D, 0D);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)porYaw));
			matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)porPitch));
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(sprite.getAtlasId()));
			MatrixStack.Entry entry = matrices.peek();
			float minU = sprite.getMinU();
			float maxU = sprite.getMaxU();
			float minV = sprite.getMinV();
			float maxV = sprite.getMaxV();
			
			float minX = -0.5F * SCALE;
			float maxX = 0.5F * SCALE;
			float minY = -0.5F * SCALE;
			float maxY = 0.5F * SCALE;
			vertex(vertexConsumer, entry, maxX, maxY, minU, minV, 255, 1F);
			vertex(vertexConsumer, entry, maxX, minY, minU, maxV, 255, 1F);
			vertex(vertexConsumer, entry, minX, minY, maxU, maxV, 255, 1F);
			vertex(vertexConsumer, entry, minX, maxY, maxU, minV, 255, 1F);
		matrices.pop();
	}
	
	private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry entry, float x, float y, float u, float v, int light, float alpha)
	{
		vertexConsumer.vertex(entry, x, y, 0).color(255, 255, 255, (int)(255 * alpha)).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0F, 0F, 1F).next();
	}
}
