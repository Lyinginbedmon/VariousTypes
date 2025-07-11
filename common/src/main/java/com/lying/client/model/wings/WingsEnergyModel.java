package com.lying.client.model.wings;

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
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;

public class WingsEnergyModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public WingsEnergyModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-2F, 0.5F, 2F));
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1F, -1.5F, 0F, 2F, 3F, 4F, new Dilation(0F)).mirrored(false), ModelTransform.pivot(0F, -0.5F, 1F));
			rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(0, -7).cuboid(0F, -1F, -0.5F, 0F, 8F, 14F, new Dilation(0F)), ModelTransform.pivot(0F, 0F, 1F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2F, 0.5F, 2F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create().uv(0, 0).cuboid(-1F, -1.5F, 0F, 2F, 3F, 4F, new Dilation(0F)), ModelTransform.pivot(0F, -0.5F, 1F));
			leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(0, -7).mirrored().cuboid(0F, -1F, -0.5F, 0F, 8F, 14F, new Dilation(0F)).mirrored(false), ModelTransform.pivot(0F, 0F, 1F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	protected void animateWings(AccessoryAnimationInterface anims, float age)
	{
		PlayerPose currentPose = anims.currentlyRunning();
		if(currentPose != null)
		{
			AnimationState currentState = anims.getAnimation(currentPose);
			switch(currentPose)
			{
				case PlayerPose.FLYING_IDLE:
					updateAnimation(currentState, VTAnimations.EnergyWings.WINGS_ENERGY_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.EnergyWings.WINGS_ENERGY_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.EnergyWings.WINGS_ENERGY_CROUCH, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.EnergyWings.WINGS_ENERGY_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.EnergyWings.WINGS_ENERGY_IDLE, age);
	}
}
