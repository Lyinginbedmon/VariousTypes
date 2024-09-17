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

public class WingsBirdModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	private final ModelPart rightRadius, rightPhalanges;
	private final ModelPart leftRadius, leftPhalanges;

	public WingsBirdModel(ModelPart root)
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
		
		Dilation dilation = new Dilation(0.1F);
		ModelPartData rightWing = body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(14, 0).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, dilation)
				.uv(0, 0).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 6.0F, Dilation.NONE), ModelTransform.of(-2.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData rightRadius = rightWing.addChild("radius", ModelPartBuilder.create().uv(18, 11).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 8.0F, dilation)
				.uv(0, 11).cuboid(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 8.0F, Dilation.NONE), ModelTransform.pivot(0.0F, -0.5F, 6.5F));
		
		rightRadius.addChild("phalanges", ModelPartBuilder.create().uv(20, 25).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 9.0F, dilation)
				.uv(0, 25).cuboid(-0.5F, 0.5F, -0.5F, 1.0F, 5.0F, 9.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 0.0F, 8.0F));
		
		ModelPartData leftWing = body.addChild(LEFT_WING, ModelPartBuilder.create().uv(14, 0).mirrored().cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, dilation).mirrored(false)
				.uv(0, 0).mirrored().cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 6.0F, Dilation.NONE).mirrored(false), ModelTransform.of(2.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		
		ModelPartData leftRadius = leftWing.addChild("radius", ModelPartBuilder.create().uv(18, 11).mirrored().cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 8.0F, dilation).mirrored(false)
				.uv(0, 11).mirrored().cuboid(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 8.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(0.0F, -0.5F, 6.5F));
		
		leftRadius.addChild("phalanges", ModelPartBuilder.create().uv(20, 25).mirrored().cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 9.0F, dilation).mirrored(false)
				.uv(0, 25).mirrored().cuboid(-0.5F, 0.5F, -0.5F, 1.0F, 5.0F, 9.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 8.0F));
		
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
			wingRight.pitch = (float)Math.toRadians(17.5D);
			wingRight.yaw = -(float)Math.toRadians(47.5D);
			
			rightRadius.pitch = -(float)Math.toRadians(80D);
			rightPhalanges.pitch = -(float)Math.toRadians(45D);
		}
		
		copyRotation(wingRight, wingLeft);
		copyRotation(rightRadius, leftRadius);
		copyRotation(rightPhalanges, leftPhalanges);
		wingLeft.yaw *= -1F;
		leftRadius.yaw *= -1F;
		leftPhalanges.yaw *= -1F;
	}
}
