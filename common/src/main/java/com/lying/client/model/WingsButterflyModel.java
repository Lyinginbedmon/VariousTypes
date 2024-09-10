package com.lying.client.model;

import java.util.List;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class WingsButterflyModel<E extends LivingEntity> extends BipedEntityModel<E>
{
	private final ModelPart wingLeft;
	private final ModelPart wingRight;
	
	public WingsButterflyModel(ModelPart root)
	{
		super(root);
		this.wingLeft = body.getChild(EntityModelPartNames.LEFT_WING);
		this.wingRight = body.getChild(EntityModelPartNames.RIGHT_WING);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getModelData(Dilation.NONE, 0F);
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		body.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().uv(0, -9).mirrored().cuboid(0.0F, -3.0F, 0.0F, 0.0F, 11.0F, 9.0F, Dilation.NONE).mirrored(false)
			.uv(0, 2).mirrored().cuboid(0.25F, -9.0F, 0.0F, 0.0F, 9.0F, 9.0F, Dilation.NONE).mirrored(false), ModelTransform.of(1.0F, 6.0F, 2.0F, 0.0F, 0.7854F, 0.0F));
		body.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().uv(0, -9).cuboid(0.0F, -3.0F, 0.0F, 0.0F, 11.0F, 9.0F, Dilation.NONE)
			.uv(0, 2).cuboid(-0.25F, -9.0F, 0.0F, 0.0F, 9.0F, 9.0F, Dilation.NONE), ModelTransform.of(-1.0F, 6.0F, 2.0F, 0.0F, -0.7854F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 32);
	}
	
	protected Iterable<ModelPart> getHeadParts() { return List.of(); }
	
	protected Iterable<ModelPart> getBodyParts() { return List.of(this.body); }
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		float time = (float)(Math.sin(age / 5) + 1) / 2;
		float flutter = time * (float)Math.toRadians(30D) + (float)Math.toRadians(15D);
		wingLeft.yaw = flutter;
		wingRight.yaw = -flutter;
	}
}
