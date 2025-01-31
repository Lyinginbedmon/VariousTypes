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
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;

public class WingsBatModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public WingsBatModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(50, 0).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.1F))
			.uv(0, 16).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 0.5F, 2.0F));
		
		ModelPartData rightRadius = rightWing.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(40, 16).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 6.0F));
			rightRadius.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, -5.5F, 0.0F, 11.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, 5.5F, 0.0F, 0.0F, -0.0175F));
		
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create().uv(32, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 15.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, 0.0F, 11.0F));
			rightPhalanges.addChild("cube_r2", ModelPartBuilder.create().uv(0, -14).cuboid(0.0F, -0.25F, -7.0F, 0.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, 7.0F, 0.0F, 0.0F, -0.0349F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.1F)).mirrored(false)
				.uv(0, 16).mirrored().cuboid(0.0F, 0.0F, 0.0F, 0.0F, 13.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 0.5F, 2.0F));
		
		ModelPartData leftRadius = leftWing.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 11.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -0.5F, 6.0F));
			leftRadius.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.0F, 0.0F, -5.5F, 0.0F, 11.0F, 11.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.5F, 5.5F, 0.0F, 0.0F, 0.0175F));
		
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create().uv(32, 0).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 15.0F, new Dilation(-0.1F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 11.0F));
			leftPhalanges.addChild("cube_r4", ModelPartBuilder.create().uv(0, -14).mirrored().cuboid(0.0F, -0.25F, -7.0F, 0.0F, 11.0F, 14.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.5F, 7.0F, 0.0F, 0.0F, 0.0349F));
		
		return TexturedModelData.of(modelData, 64, 64);
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
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.WINGS_BAT_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.WINGS_BAT_FLYING_IDLE, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WINGS_BAT_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.WINGS_BAT_IDLE, age);
	}
}