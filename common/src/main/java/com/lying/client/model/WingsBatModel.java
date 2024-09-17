package com.lying.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class WingsBatModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	private final ModelPart rightRadius, rightPhalanges;
	private final ModelPart leftRadius, leftPhalanges;
	
	public WingsBatModel(ModelPart root)
	{
		super(root);
		
		rightRadius = wingRight.getChild("radius");
		rightPhalanges = rightRadius.getChild("phalanges");
		
		leftRadius = wingLeft.getChild("radius");
		leftPhalanges = leftRadius.getChild("phalanges");
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(50, 0).cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, Dilation.NONE)
		.uv(0, 16).cuboid(0.0F, 0.0F, -1.0F, 0.0F, 13.0F, 6.0F, Dilation.NONE), ModelTransform.of(-2.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		
		ModelPartData radius = rightWing.addChild("radius", ModelPartBuilder.create(), ModelTransform.pivot(0.25F, -1.0F, 5.0F));
		radius.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 11.0F, 11.0F, Dilation.NONE), ModelTransform.of(-0.25F, 3.0F, 1.0F, 0.0F, 0.0F, -0.0175F));
		radius.addChild("cube_r2", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 11.0F, Dilation.NONE), ModelTransform.of(0.25F, 1.0F, 1.0F, 0.0F, 0.0F, -0.0175F));
		
		ModelPartData phalanges = radius.addChild("phalanges", ModelPartBuilder.create(), ModelTransform.pivot(0.25F, 0.0F, 11.0F));
		phalanges.addChild("cube_r3", ModelPartBuilder.create().uv(0, -14).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 11.0F, 14.0F, Dilation.NONE), ModelTransform.of(-0.5F, 3.0F, 1.0F, 0.0F, 0.0F, -0.0349F));
		phalanges.addChild("cube_r4", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 15.0F, Dilation.NONE), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0349F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, Dilation.NONE).mirrored(false)
		.uv(0, 16).mirrored().cuboid(0.0F, 0.0F, -1.0F, 0.0F, 13.0F, 6.0F, Dilation.NONE).mirrored(false), ModelTransform.of(2.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		
		ModelPartData radius2 = leftWing.addChild("radius", ModelPartBuilder.create(), ModelTransform.pivot(-0.25F, -1.0F, 5.0F));
		radius2.addChild("cube_r5", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.0F, -2.0F, -1.0F, 0.0F, 11.0F, 11.0F, Dilation.NONE).mirrored(false), ModelTransform.of(0.25F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0175F));
		radius2.addChild("cube_r6", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 11.0F, Dilation.NONE).mirrored(false), ModelTransform.of(-0.25F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0175F));

		ModelPartData phalanges2 = radius2.addChild("phalanges", ModelPartBuilder.create(), ModelTransform.pivot(-0.25F, 0.0F, 11.0F));
		phalanges2.addChild("cube_r7", ModelPartBuilder.create().uv(0, -14).mirrored().cuboid(0.0F, -2.0F, -1.0F, 0.0F, 11.0F, 14.0F, Dilation.NONE).mirrored(false), ModelTransform.of(0.5F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0349F));
		phalanges2.addChild("cube_r8", ModelPartBuilder.create().uv(32, 0).mirrored().cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 15.0F, Dilation.NONE).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0349F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		wingRight.traverse().forEach(part -> part.resetTransform());
		wingLeft.traverse().forEach(part -> part.resetTransform());
		if(livingEntity.isFallFlying())
		{
			wingRight.yaw = -(float)Math.toRadians(87.5D);
			
			Vec3d vel = livingEntity.getVelocity();
			if(vel.y > 0)
			{
				float time = (float)Math.sin(age);
				rightRadius.yaw = time * (float)Math.toRadians(30D);
			}
			else
				rightRadius.yaw = (float)Math.toRadians(5D);
			
			rightPhalanges.yaw = rightRadius.yaw;
		}
		else if(livingEntity.isInSneakingPose())
		{
			float time = (float)Math.abs(Math.sin(age / 30));
			wingRight.yaw = -(float)Math.toRadians(45D) + time * (float)Math.toRadians(15D);
		}
		else
		{
			wingRight.pitch = -(float)Math.toRadians(5D);
			wingRight.yaw = -(float)Math.toRadians(45D);
			
			rightRadius.pitch = -(float)Math.toRadians(35D);
			rightPhalanges.pitch = -(float)Math.toRadians(70D);
		}
		
		copyRotation(wingRight, wingLeft);
		copyRotation(rightRadius, leftRadius);
		copyRotation(rightPhalanges, leftPhalanges);
		wingLeft.yaw *= -1F;
		leftRadius.yaw *= -1F;
		leftPhalanges.yaw *= -1F;
	}
}