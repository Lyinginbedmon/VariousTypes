package com.lying.client.model;

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

public class TailDragonModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public TailDragonModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData tail = body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 3.25F, 2.0F, 1.0F, 2.0F, 1.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 0.25F, 2.0F, 1.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.of(0.0F, 10.5F, 1.75F, 1.5708F, 0.0F, 0.0F));
		
		ModelPartData seg1 = tail.addChild(SEG1, ModelPartBuilder.create().uv(0, 10).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 3.5F, 1.25F, 1.0F, 2.0F, 1.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 0.5F, 1.25F, 1.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 5.5F, -0.25F));
		
		seg1.addChild(SEG2, ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 0.5F, 0.5F, 1.0F, 2.0F, 1.0F, Dilation.NONE)
			.uv(0, 0).cuboid(-0.5F, 3.5F, 0.5F, 1.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 5.75F, -0.25F));
		
		return TexturedModelData.of(modelData, 16, 32);
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
					updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonTail.TAIL_DRAGON_FLYING, age);
					break;
				case PlayerPose.SITTING:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonTail.TAIL_DRAGON_SITTING, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonTail.TAIL_DRAGON_IDLE, age);
					tail.yaw += MathHelper.cos(limbAngle * 0.6662f) * 0.3f * limbDistance;
					double lift = Math.toRadians(25D); 
					tail.pitch += Math.min(limbDistance * lift, lift);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonTail.TAIL_DRAGON_IDLE, age);
	}
}
