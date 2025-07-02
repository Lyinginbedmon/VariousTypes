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

public class WingsFairyModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public static final String RIGHT_SECONDARIES = "right_secondaries";
	public static final String LEFT_SECONDARIES = "left_secondaries";
	public static final String RIGHT_TERTIALS = "right_tertials";
	public static final String LEFT_TERTIALS = "left_tertials";
	
	public WingsFairyModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createLogLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-2F, 0.5F, 2F));
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.09F)).mirrored(false), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 5F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 5F, new Dilation(0.2F)).mirrored(false), ModelTransform.pivot(0F, 0F, 5F));
		rightPhalanges.addChild(RIGHT_METATARSAL, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 7F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(0F, 0F, 5F));
		rightRadius.addChild(RIGHT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		rightHumerus.addChild(RIGHT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2F, 0.5F, 2F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.09F)), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 5F, new Dilation(0.1F)), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 5F, new Dilation(0.2F)), ModelTransform.pivot(0F, 0F, 5F));
		leftPhalanges.addChild(LEFT_METATARSAL, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 7F, new Dilation(0.1F)), ModelTransform.pivot(0F, 0F, 5F));
		leftRadius.addChild(LEFT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		leftHumerus.addChild(LEFT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createLeafLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-2F, 0.5F, 2F));
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create().uv(2, 2).mirrored().cuboid(-0.5F, 0.5F, 0F, 1F, 5F, 5F, new Dilation(0.05F)).mirrored(false), ModelTransform.pivot(0F, 0F, 5F));
		rightPhalanges.addChild(RIGHT_METATARSAL, ModelPartBuilder.create().uv(1, 1).mirrored().cuboid(-0.5F, 0.5F, 0F, 1F, 4F, 6F, new Dilation(-0.05F)).mirrored(false), ModelTransform.pivot(0F, 0F, 5F));
		rightRadius.addChild(RIGHT_SECONDARIES, ModelPartBuilder.create().uv(2, 2).mirrored().cuboid(-0.5F, 0.5F, -3F, 1F, 5F, 5F, new Dilation(0F)).mirrored(false), ModelTransform.pivot(0F, 0F, 3F));
		rightHumerus.addChild(RIGHT_TERTIALS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, 0.5F, -3F, 1F, 4F, 7F, new Dilation(-0.1F)).mirrored(false), ModelTransform.pivot(0F, 0F, 3F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2F, 0.5F, 2F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create().uv(2, 2).cuboid(-0.5F, 0.5F, 0F, 1F, 5F, 5F, new Dilation(0.05F)), ModelTransform.pivot(0F, 0F, 5F));
		leftPhalanges.addChild(LEFT_METATARSAL, ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, 0.5F, 0F, 1F, 4F, 6F, new Dilation(-0.05F)), ModelTransform.pivot(0F, 0F, 5F));
		leftRadius.addChild(LEFT_SECONDARIES, ModelPartBuilder.create().uv(2, 2).cuboid(-0.5F, 0.5F, 0F, 1F, 5F, 5F, new Dilation(0F)), ModelTransform.pivot(0F, 0F, 0F));
		leftHumerus.addChild(LEFT_TERTIALS, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 0.5F, 0F, 1F, 4F, 7F, new Dilation(-0.1F)), ModelTransform.pivot(0F, 0F, 0F));
		
		return TexturedModelData.of(modelData, 16, 16);
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
					updateAnimation(currentState, VTAnimations.BirdWings.WINGS_BIRD_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.BirdWings.WINGS_BIRD_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.BirdWings.WINGS_BIRD_CROUCH, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.BirdWings.WINGS_BIRD_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.BirdWings.WINGS_BIRD_IDLE, age);
	}
}
