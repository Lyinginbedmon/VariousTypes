package com.lying.client.model;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

/** Utility class specifically for making models that can be mapped on to either BipedEntityModel or AnimatedBipedEntityModel */
public interface IBipedLikeModel<E extends LivingEntity>
{
	public default void copyTransforms(EntityModel<E> parent)
	{
		if(parent instanceof BipedEntityModel)
			copyTransforms((BipedEntityModel<E>)parent);
		else if(parent instanceof AnimatedBipedEntityModel)
			copyTransforms((AnimatedBipedEntityModel<E>)parent);
	}
	
	public void copyTransforms(BipedEntityModel<E> parent);
	
	public void copyTransforms(AnimatedBipedEntityModel<E> parent);
	
	@SuppressWarnings("unchecked")
	public static <T extends LivingEntity> void poseModelForRender(EntityModel<T> model, EntityModel<T> contextModel, T player, float tickDelta)
	{
		model.handSwingProgress = player.getHandSwingProgress(tickDelta);
		model.riding = player.hasVehicle();
		model.child = player.isBaby();
		
		// Direct posing code
		float age = (float)player.age + tickDelta;
		float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
		float headYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevHeadYaw, player.headYaw);
		float yaw = MathHelper.wrapDegrees(headYaw - bodyYaw);
		float pitch = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
		if(LivingEntityRenderer.shouldFlipUpsideDown(player))
		{
			pitch *= -1F;
			yaw *= -1F;
		}
		
		float p = 0F;
		float o = 0F;
		if(!player.hasVehicle() && player.isAlive())
		{
			o = Math.min(1F, player.limbAnimator.getSpeed(tickDelta));
			p = player.limbAnimator.getPos(tickDelta) * (player.isBaby() ? 3F : 1F);
		}
		
		model.animateModel(player, p, o, tickDelta);
		model.setAngles(player, p, o, age, yaw, pitch);
		
		// Cloneable context models
		if(contextModel instanceof BipedEntityModel || contextModel instanceof AnimatedBipedEntityModel)
		{
			if(model instanceof IBipedLikeModel)
				((IBipedLikeModel<T>)model).copyTransforms(contextModel);
			else if(contextModel instanceof BipedEntityModel)
			{
				BipedEntityModel<T> parent = (BipedEntityModel<T>)contextModel;
				if(model instanceof BipedEntityModel)
					((BipedEntityModel<T>)model).copyBipedStateTo(parent);
			}
			else
			{
				AnimatedBipedEntityModel<T> parent = (AnimatedBipedEntityModel<T>)contextModel;
				if(model instanceof BipedEntityModel)
					parent.copyTransformsTo((BipedEntityModel<T>)model);
			}
		}
	}
}
