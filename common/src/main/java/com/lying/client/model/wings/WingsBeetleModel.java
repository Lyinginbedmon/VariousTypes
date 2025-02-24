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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class WingsBeetleModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	protected ModelPart rightShell, leftShell;
	
	public WingsBeetleModel(ModelPart root)
	{
		super(root);
		rightShell = body.getChild("rightShell");
		leftShell = body.getChild("leftShell");
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -2.0F, 0.25F, 6.0F, 21.0F, 0.0F, Dilation.NONE), ModelTransform.pivot(-2.0F, 2.0F, 2.0F));
		body.addChild(LEFT_WING, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-3.2412F, -1.933F, 0.5F, 6.0F, 21.0F, 0.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(2.0F, 2.0F, 2.0F));
		
		body.addChild("rightShell", ModelPartBuilder.create().uv(12, 0).cuboid(-3.0F, -1.0F, -1.0F, 4.0F, 10.0F, 3.0F, Dilation.NONE), ModelTransform.pivot(-1.0F, 1.0F, 3.0F));
		body.addChild("leftShell", ModelPartBuilder.create().uv(12, 0).mirrored().cuboid(-1.0F, -1.0F, -1.0F, 4.0F, 10.0F, 3.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(1.0F, 1.0F, 3.0F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		wingRight.hidden = wingLeft.hidden = !(livingEntity.isFallFlying() || livingEntity.isInSneakingPose());
		
		wingLeft.resetTransform();
		wingRight.resetTransform();
		rightShell.resetTransform();
		leftShell.resetTransform();
		
		if(livingEntity.isFallFlying())
		{
			rightShell.pitch = (float)Math.toRadians(20D);
			rightShell.roll = (float)Math.toRadians(50D);
			
			wingRight.roll = (float)Math.toRadians(80D);
			
			Vec3d vel = livingEntity.getVelocity();
			if(vel.y > 0)
			{
				float time = (float)MathHelper.clamp(Math.sin(age * 4), -1F, 1F);
				wingRight.pitch = (float)Math.toRadians(-5 + (time * 15));
			}
		}
		else if(livingEntity.isInSneakingPose())
		{
			rightShell.pitch = (float)Math.toRadians(20D);
			rightShell.roll = (float)Math.toRadians(25D);
			
			wingRight.pitch = (float)Math.toRadians(15D);
			wingRight.roll = (float)Math.toRadians(15D);
		}
		
		if(!wingRight.hidden)
		{
			copyRotation(wingRight, wingLeft);
			wingLeft.roll *= -1F;
		}
		
		copyRotation(rightShell, leftShell);
		leftShell.roll *= -1F;
	}
}
