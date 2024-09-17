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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class WingsDragonflyModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	protected ModelPart wingLowerRight, wingLowerLeft;
	
	public WingsDragonflyModel(ModelPart root)
	{
		super(root);
		wingLowerRight = body.getChild("wingLowerRight");
		wingLowerLeft = body.getChild("wingLowerLeft");
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 20.0F, 0.0F, Dilation.NONE), ModelTransform.of(-2.0F, 1.0F, 2.0F, 0.2618F, 0.0F, 0.2618F));
		body.addChild("wingLowerRight", ModelPartBuilder.create().uv(8, 0).cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, Dilation.NONE), ModelTransform.of(-2.0F, 8.0F, 2.0F, 0.2618F, 0.0F, 0.2618F));
		
		body.addChild(LEFT_WING, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 20.0F, 0.0F, Dilation.NONE).mirrored(false), ModelTransform.of(2.0F, 1.0F, 2.0F, 0.2618F, 0.0F, -0.2618F));
		body.addChild("wingLowerLeft", ModelPartBuilder.create().uv(8, 0).mirrored().cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 14.0F, 0.0F, Dilation.NONE).mirrored(false), ModelTransform.of(2.0F, 8.0F, 2.0F, 0.2618F, 0.0F, -0.2618F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		wingLeft.resetTransform();
		wingRight.resetTransform();
		if(livingEntity.isFallFlying())
		{
			wingLeft.roll = -(float)Math.toRadians(90D);
			
			Vec3d vel = livingEntity.getVelocity();
			if(vel.y > 0)
			{
				float time = (float)MathHelper.clamp(Math.sin(age * 4), -1F, 1F);
				wingLeft.pitch = (float)Math.toRadians(-5 + (time * 15));
			}
		}
		else if(livingEntity.isInSneakingPose())
			wingLeft.roll = -(float)Math.toRadians(35D);
		
		copyRotation(wingLeft, wingRight);
		wingRight.roll *= -1F;
		
		copyRotation(wingRight, wingLowerRight);
		copyRotation(wingLeft, wingLowerLeft);
	}
}
