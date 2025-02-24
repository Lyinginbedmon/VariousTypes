package com.lying.client.model.wings;

import com.lying.entity.AccessoryAnimationInterface;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class WingsElytraModel<E extends LivingEntity> extends AbstractWingsModel<E>
{
	public WingsElytraModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		Dilation dilation = new Dilation(1F);
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		body.addChild(RIGHT_WING, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-2.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, dilation).mirrored(false), ModelTransform.of(-4.0F, 0.0F, 2.0F, 0.2618F, 0.0F, 0.2618F));
		body.addChild(LEFT_WING, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, dilation), ModelTransform.of(4.0F, 0.0F, 2.0F, 0.2618F, 0.0F, -0.2618F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		float k = 0.2617994f;
		float l = -0.2617994f;
		float m = 0.0f;
		float n = 0.0f;
		if(livingEntity.isFallFlying())
		{
			float o = 1.0f;
			Vec3d vec3d = livingEntity.getVelocity();
			if(vec3d.y < 0.0)
				o = 1.0f - (float)Math.pow(-vec3d.normalize().y, 1.5);
			
			k = o * 0.34906584f + (1.0f - o) * k;
			l = o * -1.5707964f + (1.0f - o) * l;
		}
		else if(livingEntity.isInSneakingPose())
		{
			k = 0.6981317f;
			l = -0.7853982f;
			m = 3.0f;
			n = 0.08726646f;
		}
		this.wingLeft.pivotY = m;
		if (livingEntity instanceof AbstractClientPlayerEntity)
		{
			AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity;
			abstractClientPlayerEntity.elytraPitch += (k - abstractClientPlayerEntity.elytraPitch) * 0.1f;
			abstractClientPlayerEntity.elytraYaw += (n - abstractClientPlayerEntity.elytraYaw) * 0.1f;
			abstractClientPlayerEntity.elytraRoll += (l - abstractClientPlayerEntity.elytraRoll) * 0.1f;
			this.wingLeft.pitch = abstractClientPlayerEntity.elytraPitch;
			this.wingLeft.yaw = abstractClientPlayerEntity.elytraYaw;
			this.wingLeft.roll = abstractClientPlayerEntity.elytraRoll;
		}
		else
		{
			this.wingLeft.pitch = k;
			this.wingLeft.roll = l;
			this.wingLeft.yaw = n;
		}
		this.wingRight.yaw = -this.wingLeft.yaw;
		this.wingRight.pivotY = this.wingLeft.pivotY;
		this.wingRight.pitch = this.wingLeft.pitch;
		this.wingRight.roll = -this.wingLeft.roll;
	}
	
	protected void animateWings(AccessoryAnimationInterface anims, float age) { }
}
