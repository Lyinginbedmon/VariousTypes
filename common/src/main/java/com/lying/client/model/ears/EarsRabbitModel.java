package com.lying.client.model.ears;

import com.lying.client.init.VTAnimations;
import com.lying.client.model.AbstractAccessoryModel;
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

public class EarsRabbitModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	private final ModelPart rightEar, leftEar;
	
	public EarsRabbitModel(ModelPart part)
	{
		super(part);
		this.rightEar = this.head.getChild(EntityModelPartNames.RIGHT_EAR);
		this.leftEar = this.head.getChild(EntityModelPartNames.LEFT_EAR);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(6, 0).cuboid(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(2.5F, -8.0F, 2.5F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -5.0F, -0.5F, 2.0F, 5.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(-2.5F, -8.0F, 2.5F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float i, float j)
	{
		this.rightEar.traverse().forEach(p -> p.resetTransform());
		this.leftEar.traverse().forEach(p -> p.resetTransform());
		
		if(livingEntity instanceof AccessoryAnimationInterface)
		{
			AccessoryAnimationInterface anims = (AccessoryAnimationInterface)livingEntity;
			PlayerPose currentPose = anims.currentlyRunning();
			switch(currentPose)
			{
				case PlayerPose.FLYING_IDLE:
				case PlayerPose.FLYING_POWERED:
				case PlayerPose.SWIMMING_IDLE:
				case PlayerPose.SWIMMING_POWERED:
				case PlayerPose.CRAWLING:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.RabbitEars.EARS_RABBIT_FLYING, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.RabbitEars.EARS_RABBIT_CROUCHING, age);
					break;
				default:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.RabbitEars.EARS_RABBIT_IDLE, age);
					break;
			}
		}
	}
}
