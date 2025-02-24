package com.lying.client.model.wings;

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

public class WingsButterflyModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public WingsButterflyModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		body.addChild(LEFT_WING, ModelPartBuilder.create().uv(0, -11).mirrored().cuboid(0.0F, -8.0F, 0.0F, 0.0F, 14.0F, 11.0F, Dilation.NONE).mirrored(false)
			.uv(0, 6).mirrored().cuboid(-0.25F, 4.0F, 0.0F, 0.0F, 8.0F, 8.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(1.0F, 3.0F, 2.0F));
		body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(0, -11).cuboid(0.0F, -8.0F, 0.0F, 0.0F, 14.0F, 11.0F, Dilation.NONE)
			.uv(0, 6).cuboid(0.25F, 4.0F, 0.0F, 0.0F, 8.0F, 8.0F, Dilation.NONE), ModelTransform.pivot(-1.0F, 3.0F, 2.0F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		if(livingEntity.isFallFlying())
		{
			wingLeft.yaw = (float)Math.toRadians(85D);
			Vec3d vel = livingEntity.getVelocity();
			if(vel.y > 0)
			{
				float time = (float)Math.abs(Math.sin(age));
				wingLeft.yaw -= Math.toRadians(80D) * time;
			}
		}
		else if(livingEntity.isInSneakingPose())
		{
			float time = (float)Math.abs(Math.sin(age / 30));
			wingLeft.yaw = (float)Math.toRadians(45D) + time * (float)Math.toRadians(15D);
		}
		else
		{
			float time = (float)(Math.sin(age / 15) + 1) / 2;
			float flutter = time * (float)Math.toRadians(60D) + (float)Math.toRadians(15D);
			wingLeft.yaw = flutter;
		}
		
		wingRight.yaw = -wingLeft.yaw;
	}
}
