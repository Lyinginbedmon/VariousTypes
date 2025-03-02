package com.lying.client.model.ears;

import com.lying.client.init.VTAnimations;
import com.lying.client.model.AbstractAccessoryModel;
import com.lying.entity.AccessoryAnimationInterface;

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
import net.minecraft.util.math.BlockPos;

public class GillsAxolotlModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public static final String LEFT_GILLS = EntityModelPartNames.LEFT_EAR;
	public static final String RIGHT_GILLS = EntityModelPartNames.RIGHT_EAR;
	public static final String TOP_GILLS = "top_gills";
	
	private final ModelPart rightGills, leftGills, topGills;
	
	public GillsAxolotlModel(ModelPart part)
	{
		super(part);
		this.rightGills = this.head.getChild(RIGHT_GILLS);
		this.leftGills = this.head.getChild(LEFT_GILLS);
		this.topGills = this.head.getChild(TOP_GILLS);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(RIGHT_GILLS, ModelPartBuilder.create().uv(10, 3).cuboid(0.0F, -4.0F, 0.0F, 3.0F, 8.0F, 0.0F, Dilation.NONE), ModelTransform.of(4.0F, -4.0F, 0.0F, 0.0F, -0.5236F, 0.0F));
		head.addChild(LEFT_GILLS, ModelPartBuilder.create().uv(0, 3).cuboid(-3.0F, -4.0F, 0.0F, 3.0F, 8.0F, 0.0F, Dilation.NONE), ModelTransform.of(-4.0F, -4.0F, 0.0F, 0.0F, 0.5236F, 0.0F));
		head.addChild(TOP_GILLS, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -3.0F, 0.0F, 8.0F, 3.0F, 0.0F, Dilation.NONE), ModelTransform.of(0.0F, -8.0F, 0.0F, -0.5236F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float i, float j)
	{
		this.rightGills.traverse().forEach(p -> p.resetTransform());
		this.leftGills.traverse().forEach(p -> p.resetTransform());
		this.topGills.traverse().forEach(p -> p.resetTransform());
		
		AnimationState idleState = new AnimationState();
		if(livingEntity instanceof AccessoryAnimationInterface)
			idleState = ((AccessoryAnimationInterface)livingEntity).getIdleAnimation();
		
		boolean moving = limbDistance > 1.0E-5f || livingEntity.getPitch() != livingEntity.prevPitch || livingEntity.getYaw() != livingEntity.prevYaw;
		
		BlockPos eyePos = BlockPos.ofFloored(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
		if(!livingEntity.getWorld().getFluidState(eyePos).isEmpty())
		{
			if(moving)
				updateAnimation(idleState, VTAnimations.AxolotlGills.GILLS_WATER_MOVING, age);
			else
				updateAnimation(idleState, VTAnimations.AxolotlGills.GILLS_WATER_STANDING, age);
		}
		else if(livingEntity.isOnGround())
		{
			if(moving)
				updateAnimation(idleState, VTAnimations.AxolotlGills.GILLS_GROUND_MOVING, age);
			else
				updateAnimation(idleState, VTAnimations.AxolotlGills.GILLS_GROUND_STANDING, age);
		}
	}
}
