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

public class EarsElfModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public EarsElfModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(10, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
			.uv(12, 1).cuboid(0.5F, 1.0F, -2.0F, 0.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -5.5F, 0.25F, 0.1745F, 0.48F, 0.0F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
			.uv(0, 1).mirrored().cuboid(-0.5F, 1.0F, -2.0F, 0.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.0F, -5.5F, 0.25F, 0.1745F, -0.48F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
}
