package com.lying.client.model.tail;

import com.lying.client.init.VTAnimations;
import com.lying.entity.AccessoryAnimationInterface;
import com.lying.utility.PlayerPose;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public abstract class SimpleTailModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public SimpleTailModel(ModelPart part)
	{
		super(part);
	}
	
	public static class Fox<E extends LivingEntity> extends SimpleTailModel<E>
	{
		public Fox(ModelPart part)
		{
			super(part);
		}
		
		public static TexturedModelData createFoxTail()
		{
			ModelData modelData = getRig();
			ModelPartData modelPartData = modelData.getRoot();
			ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
			
			body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-2F, 0.25F, -2.5F, 4F, 9F, 5F, Dilation.NONE), ModelTransform.of(0F, 10.5F, 1.75F, 1.5708F, 0F, 0F));
			
			return TexturedModelData.of(modelData, 32, 16);
		}
		
		protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
		{
			PlayerPose currentPose = anims.currentlyRunning();
			if(currentPose != null)
			{
				switch(currentPose)
				{
					case PlayerPose.FLYING_IDLE:
					case PlayerPose.FLYING_POWERED:
					case PlayerPose.SWIMMING_IDLE:
					case PlayerPose.SWIMMING_POWERED:
					case PlayerPose.CRAWLING:
						updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_FLYING, age);
						break;
					case PlayerPose.SITTING:
						updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_SITTING, age);
						break;
					default:
						if(isMoving)
						{
							tail.pitch = (float)Math.toRadians(75D);
							double lift = Math.toRadians(35D);
							tail.pitch += Math.min(limbDistance * lift, lift);
							tail.yaw = MathHelper.cos(limbAngle * 0.6662f) * 0.3f * limbDistance;
						}
						else
							updateAnimation(anims.getAnimation(currentPose), VTAnimations.FoxTail.TAIL_FOX_IDLE, age);
						break;
				}
			}
			else
				updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_IDLE, age);
		}
	}
	
	public static class Wolf<E extends LivingEntity> extends SimpleTailModel<E>
	{
		public Wolf(ModelPart part)
		{
			super(part);
		}
		
		public static TexturedModelData createWolfTail()
		{
			ModelData modelData = getRig();
			ModelPartData modelPartData = modelData.getRoot();
			ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
			
			body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-1F, 0.25F, -1F, 2F, 8F, 2F, Dilation.NONE), ModelTransform.of(0F, 10.5F, 1.75F, 1.5708F, 0F, 0F));
			
			return TexturedModelData.of(modelData, 16, 16);
		}
		
		protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
		{
			PlayerPose currentPose = anims.currentlyRunning();
			if(currentPose != null)
			{
				switch(currentPose)
				{
					case PlayerPose.FLYING_IDLE:
					case PlayerPose.FLYING_POWERED:
					case PlayerPose.SWIMMING_IDLE:
					case PlayerPose.SWIMMING_POWERED:
					case PlayerPose.CRAWLING:
						updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_FLYING, age);
						break;
					case PlayerPose.SITTING:
						updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_SITTING, age);
						break;
					case PlayerPose.CROUCHING:
						updateAnimation(anims.getAnimation(currentPose), VTAnimations.WolfTail.TAIL_WOLF_CROUCHING, age);
						break;
					default:
						if(isMoving)
						{
							tail.pitch = (float)Math.toRadians(75D);
							double lift = Math.toRadians(35D);
							tail.pitch += Math.min(limbDistance * lift, lift);
							tail.yaw = MathHelper.cos(limbAngle * 0.6662f) * 0.3f * limbDistance;
						}
						else
							updateAnimation(anims.getAnimation(currentPose), VTAnimations.FoxTail.TAIL_FOX_IDLE, age);
						break;
				}
			}
			else
				updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_IDLE, age);
		}
	}
}
