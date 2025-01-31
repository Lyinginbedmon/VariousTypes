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

public class WingsBirdModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public WingsBirdModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, 1.0F, 2.0F));
			rightWing.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(4.0F, -2.0F, 0.0F, 1.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, 2.0F, 0.0F, -0.086F, 0.0145F, -0.0045F));
			rightWing.addChild("cube_r2", ModelPartBuilder.create().uv(14, 0).cuboid(4.0F, -2.0F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(-4.5F, 1.0F, 0.0F, -0.086F, 0.0145F, -0.0045F));
		
		ModelPartData rightRadius = rightWing.addChild(RIGHT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.5F, 6.5F));
			ModelPartData r2 = rightRadius.addChild("r2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, -0.25F, 0.0F, 0.0F, -0.0175F));
			r2.addChild("cube_r3", ModelPartBuilder.create().uv(0, 11).cuboid(7.0F, -1.0F, -1.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, 1.5F, 0.75F, -0.0704F, 0.016F, 0.0485F));
			r2.addChild("cube_r4", ModelPartBuilder.create().uv(18, 11).cuboid(7.0F, -1.0F, -1.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.1F)), ModelTransform.of(-7.5F, 0.5F, 0.75F, -0.0704F, 0.016F, 0.0485F));
		
		ModelPartData rightPhalanges = rightRadius.addChild(RIGHT_PHALANGES, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 8.0F));
			ModelPartData p2 = rightPhalanges.addChild("p2", ModelPartBuilder.create(), ModelTransform.of(-0.25F, 0.0F, -0.5F, 0.0F, 0.0F, -0.0349F));
			p2.addChild("cube_r5", ModelPartBuilder.create().uv(0, 25).cuboid(8.0F, -1.0F, -1.0F, 1.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-8.25F, 1.5F, 1.0F, -0.0302F, -0.006F, 0.0817F));
			p2.addChild("cube_r6", ModelPartBuilder.create().uv(20, 25).cuboid(8.0F, -1.0F, -1.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.1F)), ModelTransform.of(-8.25F, 0.5F, 1.0F, -0.0302F, -0.006F, 0.0817F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 1.0F, 2.0F));
			leftWing.addChild("cube_r7", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-5.0F, -2.0F, 0.0F, 1.0F, 5.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.5F, 2.0F, 0.0F, -0.086F, -0.0145F, 0.0045F));
			leftWing.addChild("cube_r8", ModelPartBuilder.create().uv(14, 0).mirrored().cuboid(-5.0F, -2.0F, 0.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(4.5F, 1.0F, 0.0F, -0.086F, -0.0145F, 0.0045F));
		
		ModelPartData leftRadius = leftWing.addChild(LEFT_RADIUS, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.5F, 6.5F));
			ModelPartData r3 = leftRadius.addChild("r3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, -0.25F, 0.0F, 0.0F, 0.0175F));
			r3.addChild("cube_r9", ModelPartBuilder.create().uv(0, 11).mirrored().cuboid(-8.0F, -1.0F, -1.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.5F, 1.5F, 0.75F, -0.0704F, -0.016F, -0.0485F));
			r3.addChild("cube_r10", ModelPartBuilder.create().uv(18, 11).mirrored().cuboid(-8.0F, -1.0F, -1.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(7.5F, 0.5F, 0.75F, -0.0704F, -0.016F, -0.0485F));
		
		ModelPartData leftPhalanges = leftRadius.addChild(LEFT_PHALANGES, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 8.0F));
			ModelPartData p3 = leftPhalanges.addChild("p3", ModelPartBuilder.create(), ModelTransform.of(0.25F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0349F));
			p3.addChild("cube_r11", ModelPartBuilder.create().uv(0, 25).mirrored().cuboid(-9.0F, -1.0F, -1.0F, 1.0F, 5.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(8.25F, 1.5F, 1.0F, -0.0302F, 0.006F, -0.0817F));
			p3.addChild("cube_r12", ModelPartBuilder.create().uv(20, 25).mirrored().cuboid(-9.0F, -1.0F, -1.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(8.25F, 0.5F, 1.0F, -0.0302F, 0.006F, -0.0817F));
		
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
					updateAnimation(currentState, VTAnimations.WINGS_BIRD_FLYING_IDLE, age);
					break;
				case PlayerPose.FLYING_POWERED:
					updateAnimation(currentState, VTAnimations.WINGS_BIRD_FLYING_POWERED, age);
					break;
				case PlayerPose.CROUCHING:
					// TODO Add crouching animation for bird wings
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WINGS_BIRD_IDLE, age);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.WINGS_BIRD_IDLE, age);
	}
}
