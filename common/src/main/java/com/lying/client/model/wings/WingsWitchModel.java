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

public class WingsWitchModel<E extends LivingEntity> extends WingsBirdModel<E>
{
	public WingsWitchModel(ModelPart root)
	{
		super(root);
	}
	
	public static ModelData createWingsModel()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 0.5F, 4.0F));
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create().uv(0, 11).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.09F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(9, 13).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 6.0F));
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create().uv(16, 14).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.2F)).mirrored(false)
		.uv(28, 0).mirrored().cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.05F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 5.0F));
		
		rightPhalanges.addChild(RIGHT_METATARSAL, ModelPartBuilder.create().uv(40, 0).mirrored().cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 4.0F, 6.0F, new Dilation(-0.05F)).mirrored(false)
				.uv(21, 13).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 5.0F));
		rightRadius.addChild(RIGHT_SECONDARIES, ModelPartBuilder.create().uv(16, 0).mirrored().cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		rightHumerus.addChild(RIGHT_TERTIALS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 4.0F, 7.0F, new Dilation(-0.1F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 0.5F, 4.0F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create().uv(0, 11).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.09F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(9, 13).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 6.0F));
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create().uv(16, 14).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.2F))
		.uv(28, 0).cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 5.0F));
		
		leftPhalanges.addChild(LEFT_METATARSAL, ModelPartBuilder.create().uv(40, 0).cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 4.0F, 6.0F, new Dilation(-0.05F))
				.uv(21, 13).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 5.0F));
		leftRadius.addChild(LEFT_SECONDARIES, ModelPartBuilder.create().uv(16, 0).cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		leftHumerus.addChild(LEFT_TERTIALS, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 0.5F, 0.0F, 1.0F, 4.0F, 7.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return modelData;
	}
	
	public static TexturedModelData createBodyLayer()
	{
		return TexturedModelData.of(createWingsModel(), 64, 64);
	}
	
	protected void animateWings(AccessoryAnimationInterface anims, float age)
	{
		PlayerPose currentPose = anims.currentlyRunning();
		if(currentPose == null)
			return;
		
		boolean wingsVisible = currentPose == PlayerPose.CROUCHING || currentPose.isFlying();
		wingRight.visible = wingsVisible;
		wingLeft.visible = wingsVisible;
		if(wingsVisible)
		{
			AnimationState currentState = anims.getAnimation(currentPose);
			switch(currentPose)
			{
				case PlayerPose.FLYING_IDLE:
					updateAnimation(currentState, VTAnimations.AngelWings.WINGS_ANGEL_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.AngelWings.WINGS_ANGEL_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.AngelWings.WINGS_ANGEL_CROUCH, age);
					break;
				default:
					break;
			}
		}
	}
}
