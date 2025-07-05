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
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.2F)).mirrored(false), ModelTransform.pivot(0F, 0F, 0F));
			rightHumerus.addChild("cube_r1", ModelPartBuilder.create().uv(3, 10).mirrored().cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 4F, Dilation.NONE).mirrored(false), ModelTransform.of(0F, 3F, 3F, -1.9199F, 0F, 0F));
			rightHumerus.addChild(RIGHT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(1, 1).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 4F, new Dilation(0.15F)).mirrored(false), ModelTransform.pivot(0F, 0F, 6F));
			rightRadius.addChild("cube_r2", ModelPartBuilder.create().uv(2, 9).mirrored().cuboid(-0.5F, -0.5F, -2F, 1F, 1F, 4F, Dilation.NONE).mirrored(false), ModelTransform.of(0F, -1.5F, 4.25F, 0.7418F, 0F, 0F));
			rightRadius.addChild(RIGHT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create().uv(1, 1).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(0F, 0F, 4F));
			rightPhalanges.addChild("cube_r3", ModelPartBuilder.create().uv(2, 9).mirrored().cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 6F, Dilation.NONE).mirrored(false), ModelTransform.of(0F, 3F, 2.5F, -1.2654F, 0F, 0F));
		ModelPartData rightMetatarsal = rightPhalanges.addChild(RIGHT_METATARSAL, ModelPartBuilder.create().uv(1, 1).mirrored().cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.05F)).mirrored(false), ModelTransform.pivot(0F, 0F, 6F));
			rightMetatarsal.addChild("cube_r4", ModelPartBuilder.create().uv(3, 10).mirrored().cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 5F, Dilation.NONE).mirrored(false), ModelTransform.of(0F, 2.75F, 2F, -1.0472F, 0F, 0F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2F, 0.5F, 2F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.2F)), ModelTransform.pivot(0F, 0F, 0F));
			leftHumerus.addChild("cube_r5", ModelPartBuilder.create().uv(3, 10).cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 4F, Dilation.NONE), ModelTransform.of(0F, 3F, 3F, -1.9199F, 0F, 0F));
			leftHumerus.addChild(LEFT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 4F, new Dilation(0.15F)), ModelTransform.pivot(0F, 0F, 6F));
			leftRadius.addChild("cube_r6", ModelPartBuilder.create().uv(2, 9).cuboid(-0.5F, -0.5F, -2F, 1F, 1F, 4F, Dilation.NONE), ModelTransform.of(0F, -1.5F, 4.25F, 0.7418F, 0F, 0F));
			leftRadius.addChild(LEFT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.1F)), ModelTransform.pivot(0F, 0F, 4F));
			leftPhalanges.addChild("cube_r7", ModelPartBuilder.create().uv(2, 9).cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 6F, Dilation.NONE), ModelTransform.of(0F, 3F, 2.5F, -1.2654F, 0F, 0F));
		ModelPartData leftMetatarsal = leftPhalanges.addChild(LEFT_METATARSAL, ModelPartBuilder.create().uv(1, 1).cuboid(-0.5F, -0.5F, 0F, 1F, 1F, 6F, new Dilation(0.05F)), ModelTransform.pivot(0F, 0F, 6F));
			leftMetatarsal.addChild("cube_r8", ModelPartBuilder.create().uv(3, 10).cuboid(-0.5F, -0.5F, -3F, 1F, 1F, 5F, Dilation.NONE), ModelTransform.of(0F, 2.75F, 2F, -1.0472F, 0F, 0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createLeafLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-2F, 0.5F, 2F));
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData rightTertials = rightHumerus.addChild(RIGHT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
			rightTertials.addChild("cube_r1", ModelPartBuilder.create().uv(2, 2).mirrored().cuboid(-0.5F, -2.5F, -2.5F, 1F, 5F, 5F, new Dilation(-0.1F)).mirrored(false), ModelTransform.of(0F, 2.5F, 0.25F, -0.1745F, 0F, 0F));
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData rightSecondaries = rightRadius.addChild(RIGHT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
			rightSecondaries.addChild("cube_r2", ModelPartBuilder.create().uv(2, 3).mirrored().cuboid(-1F, -2F, -2F, 2F, 4F, 4F, new Dilation(-0.2F)).mirrored(false), ModelTransform.of(0.25F, 1.75F, -1F, 0F, 0F, -0.1309F));
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 4F));
			rightPhalanges.addChild("cube_r3", ModelPartBuilder.create().uv(2, 2).mirrored().cuboid(-0.5F, -3F, -2.5F, 1F, 6F, 5F, new Dilation(0.05F)).mirrored(false), ModelTransform.of(0F, 3.25F, 3.25F, 0.1309F, 0F, 0F));
		ModelPartData rightMetatarsal = rightPhalanges.addChild(RIGHT_METATARSAL, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
			rightMetatarsal.addChild("cube_r4", ModelPartBuilder.create().uv(2, 2).mirrored().cuboid(-0.5F, -3F, -2.5F, 1F, 6F, 5F, new Dilation(-0.05F)).mirrored(false), ModelTransform.of(0F, 2.75F, 2.25F, 0.1745F, 0F, 0F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2F, 0.5F, 2F));
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData leftTertials = leftHumerus.addChild(LEFT_TERTIALS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
			leftTertials.addChild("cube_r5", ModelPartBuilder.create().uv(2, 2).cuboid(-0.5F, -2.5F, -2.5F, 1F, 5F, 5F, new Dilation(-0.1F)), ModelTransform.of(0F, 2.5F, 0.25F, -0.1745F, 0F, 0F));
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
		ModelPartData leftSecondaries = leftRadius.addChild(LEFT_SECONDARIES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 3F));
			leftSecondaries.addChild("cube_r6", ModelPartBuilder.create().uv(2, 3).cuboid(-1F, -2F, -2F, 2F, 4F, 4F, new Dilation(-0.2F)), ModelTransform.of(-0.25F, 1.75F, -1F, 0F, 0F, 0.1309F));
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 4F));
			leftPhalanges.addChild("cube_r7", ModelPartBuilder.create().uv(2, 2).cuboid(-0.5F, -3F, -2.5F, 1F, 6F, 5F, new Dilation(0.05F)), ModelTransform.of(0F, 3.25F, 3.25F, 0.1309F, 0F, 0F));
		ModelPartData leftMetatarsal = leftPhalanges.addChild(LEFT_METATARSAL, ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 6F));
			leftMetatarsal.addChild("cube_r8", ModelPartBuilder.create().uv(2, 2).cuboid(-0.5F, -3F, -2.5F, 1F, 6F, 5F, new Dilation(-0.05F)), ModelTransform.of(0F, 2.75F, 2.25F, 0.1745F, 0F, 0F));
		
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
					updateAnimation(currentState, VTAnimations.FairyWings.WINGS_FAIRY_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.BirdWings.WINGS_BIRD_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.FairyWings.WINGS_FAIRY_CROUCH, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.FairyWings.WING_FAIRY_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.FairyWings.WING_FAIRY_IDLE, age);
	}
}
