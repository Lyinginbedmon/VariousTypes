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

public class WingsSkeletonModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public static final String RIGHT_CLAW = "right_claw";
	public static final String RIGHT_HAND = EntityModelPartNames.RIGHT_HAND;
	public static final String RIGHT_DIGIT_1 = "right_digit_1";
	public static final String RIGHT_DIGIT_2 = "right_digit_2";
	public static final String RIGHT_DIGIT_3 = "right_digit_3";
	public static final String RIGHT_DIGIT_4 = "right_digit_4";
	
	public static final String LEFT_CLAW = "left_claw";
	public static final String LEFT_HAND = EntityModelPartNames.LEFT_HAND;
	public static final String LEFT_DIGIT_1 = "left_digit_1";
	public static final String LEFT_DIGIT_2 = "left_digit_2";
	public static final String LEFT_DIGIT_3 = "left_digit_3";
	public static final String LEFT_DIGIT_4 = "left_digit_4";
	
	public WingsSkeletonModel(ModelPart root)
	{
		super(root);
	}
	
	public static ModelData createBones()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.5F, 2.0F));
		
		ModelPartData rightHumerus = rightWing.addChild(RIGHT_HUMERUS, ModelPartBuilder.create().uv(0, 28).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
		
		ModelPartData rightRadius = rightHumerus.addChild(RIGHT_RADIUS, ModelPartBuilder.create().uv(24, 28).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 9.5F));
		
		rightRadius.addChild(RIGHT_CLAW, ModelPartBuilder.create().uv(24, 34).cuboid(-1.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 0.0F, 9.5F));
		
		ModelPartData rightDigit1 = rightRadius.addChild(RIGHT_DIGIT_1, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 9.5F));
			rightDigit1.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			rightDigit1.addChild("cube_r3", ModelPartBuilder.create().uv(26, 1).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		ModelPartData rightHand = rightRadius.addChild(RIGHT_HAND, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 9.5F));

		ModelPartData rightDigit2 = rightHand.addChild(RIGHT_DIGIT_2, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0218F));
			rightDigit2.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			rightDigit2.addChild("cube_r5", ModelPartBuilder.create().uv(26, 1).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		ModelPartData rightDigit3 = rightHand.addChild(RIGHT_DIGIT_3, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));
			rightDigit3.addChild("cube_r6", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			rightDigit3.addChild("cube_r7", ModelPartBuilder.create().uv(26, 1).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		ModelPartData rightDigit4 = rightHand.addChild(RIGHT_DIGIT_4, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0654F));
			rightDigit4.addChild("cube_r8", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			rightDigit4.addChild("cube_r9", ModelPartBuilder.create().uv(26, 1).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 0.5F, 2.0F));
		
		ModelPartData leftHumerus = leftWing.addChild(LEFT_HUMERUS, ModelPartBuilder.create().uv(0, 28).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
		
		ModelPartData leftRadius = leftHumerus.addChild(LEFT_RADIUS, ModelPartBuilder.create().uv(24, 28).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.2F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 9.5F));
		
		leftRadius.addChild(LEFT_CLAW, ModelPartBuilder.create().uv(24, 34).mirrored().cuboid(0.0F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-0.5F, 0.0F, 9.5F));
		
		ModelPartData leftDigit1 = leftRadius.addChild(LEFT_DIGIT_1, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 9.5F));
			leftDigit1.addChild("cube_r11", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			leftDigit1.addChild("cube_r12", ModelPartBuilder.create().uv(26, 1).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		ModelPartData leftHand = leftRadius.addChild(LEFT_HAND, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 9.5F));
		
		ModelPartData leftDigit2 = leftHand.addChild(LEFT_DIGIT_2, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0218F));
			leftDigit2.addChild("cube_r13", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			leftDigit2.addChild("cube_r14", ModelPartBuilder.create().uv(26, 1).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		ModelPartData leftDigit3 = leftHand.addChild(LEFT_DIGIT_3, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));
			leftDigit3.addChild("cube_r15", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			leftDigit3.addChild("cube_r16", ModelPartBuilder.create().uv(26, 1).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		ModelPartData leftDigit4 = leftHand.addChild(LEFT_DIGIT_4, ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0654F));
			leftDigit4.addChild("cube_r17", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 11.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(0.0F, 0.75F, 9.0F, -0.48F, 0.0F, 0.0F));
			leftDigit4.addChild("cube_r18", ModelPartBuilder.create().uv(26, 1).mirrored().cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 10.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		return modelData;
	}
	
	public static TexturedModelData createBodyLayer()
	{
		return TexturedModelData.of(createBones(), 64, 64);
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
					updateAnimation(currentState, VTAnimations.DragonWings.WINGS_DRAGON_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.DragonWings.WINGS_DRAGON_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					updateAnimation(currentState, VTAnimations.DragonWings.WINGS_DRAGON_CROUCH, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonWings.WINGS_DRAGON_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.DragonWings.WINGS_DRAGON_IDLE, age);
	}
}
