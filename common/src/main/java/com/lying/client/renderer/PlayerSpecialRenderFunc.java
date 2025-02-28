package com.lying.client.renderer;

import org.joml.Vector3f;

import com.lying.utility.Cosmetic;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public interface PlayerSpecialRenderFunc
{
	default Vector3f getColor(LivingEntity player, Cosmetic instance) { return new Vector3f(1F, 1F, 1F); }
	default float getAlpha(LivingEntity player, Cosmetic instance) { return 1F; }
	default void beforeRender(PlayerEntity player, Cosmetic instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light) { }
	default void afterRender(PlayerEntity player, Cosmetic instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light) { }
	
	public static void renderModel(EntityModel<AbstractClientPlayerEntity> model, Identifier texture, PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexProvider, float tickDelta, int light, float red, float green, float blue, float alpha)
	{
		VertexConsumerProvider provider = vertexProvider instanceof VertexConsumerProviderWrapped ? ((VertexConsumerProviderWrapped)vertexProvider).internal() : vertexProvider;
		VertexConsumer consumer = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
		
		model.handSwingProgress = player.getHandSwingProgress(tickDelta);
		model.riding = player.hasVehicle();
		model.child = player.isBaby();
		
		if(model instanceof BipedEntityModel)
			((BipedEntityModel<?>)model).sneaking = player.isInSneakingPose();
		
		float n = (float)player.age + tickDelta;
		float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
		float headYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevHeadYaw, player.headYaw);
		float k = MathHelper.wrapDegrees(headYaw - bodyYaw);
		float scale = player.getScale();
		
		matrices.push();
			matrices.scale(scale, scale, scale);
			setupTransforms(player, matrices, n, bodyYaw, tickDelta, scale);
			matrices.scale(-1F, -1F, 1F);
			float g = 0.9375F;
			matrices.scale(g, g, g);
			matrices.translate(0F, -1.501F, 0F);
			float m = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
			if(LivingEntityRenderer.shouldFlipUpsideDown(player))
			{
				m *= -1F;
				k *= -1F;
			}
			float p = 0F;
			float o = 0F;
			if(!player.hasVehicle() && player.isAlive())
			{
				o = Math.min(1F, player.limbAnimator.getSpeed(tickDelta));
				p = player.limbAnimator.getPos(tickDelta) * (player.isBaby() ? 3F : 1F);
			}
			model.animateModel((AbstractClientPlayerEntity)player, p, o, tickDelta);
			model.setAngles((AbstractClientPlayerEntity)player, p, o, n, k, m);
			model.render(matrices, consumer, light, LivingEntityRenderer.getOverlay(player, 0F), red, green, blue, alpha);
		matrices.pop();
	}
	
	private static void setupTransforms(PlayerEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) 
	{
		if(entity.isFrozen())
			bodyYaw += (float)(Math.cos((double)((LivingEntity)entity).age * 3.25) * Math.PI * (double)0.4f);
		
		if(!((Entity)entity).isInPose(EntityPose.SLEEPING))
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - bodyYaw));
		
		if(((LivingEntity)entity).deathTime > 0)
		{
			float f = ((float)((LivingEntity)entity).deathTime + tickDelta - 1.0f) / 20.0f * 1.6f;
			if((f = MathHelper.sqrt(f)) > 1.0f)
				f = 1.0f;
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * 90F));
		}
		else if(((LivingEntity)entity).isUsingRiptide())
		{
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f - ((Entity)entity).getPitch()));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float)((LivingEntity)entity).age + tickDelta) * -75.0f));
		}
		else if(((Entity)entity).isInPose(EntityPose.SLEEPING))
		{
			Direction direction = ((LivingEntity)entity).getSleepingDirection();
			float g;
			switch(direction)
			{
				case SOUTH:	g = 90F; break;
				case WEST:	g = 0F; break;
				case NORTH:	g = 270F; break;
				case EAST:	g = 180F; break;
				default:
					g = bodyYaw;
					break;
			}
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90F));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0f));
		} else if (LivingEntityRenderer.shouldFlipUpsideDown(entity)) {
			matrices.translate(0.0f, (((Entity)entity).getHeight() + 0.1f) / scale, 0.0f);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
		}
	}
}