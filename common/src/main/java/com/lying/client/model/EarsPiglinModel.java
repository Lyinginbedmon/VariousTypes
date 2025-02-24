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

public class EarsPiglinModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	private final ModelPart rightEar, leftEar;
	
	public EarsPiglinModel(ModelPart part)
	{
		super(part);
		this.rightEar = this.head.getChild(EntityModelPartNames.RIGHT_EAR);
		this.leftEar = this.head.getChild(EntityModelPartNames.LEFT_EAR);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, Dilation.NONE).mirrored(false), ModelTransform.of(-3.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(10, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, Dilation.NONE), ModelTransform.of(3.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	public void setAngles(E arg, float f, float g, float h, float i, float j)
	{
		super.setAngles(arg, f, g, h, i, j);
        float k = (float)Math.toRadians(30D);
        float l = h * 0.1f + f * 0.5f;
        float m = 0.08f + g * 0.4f;
        this.leftEar.roll = -k - MathHelper.cos(l * 1.2f) * m;
        this.rightEar.roll = k + MathHelper.cos(l) * m;
	}
}
