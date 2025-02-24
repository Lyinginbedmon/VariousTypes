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

public class NosePiglinModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public NosePiglinModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createPigNose()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild("nose", ModelPartBuilder.create().uv(4, 0).cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	public static TexturedModelData createPiglinNose()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild("nose", ModelPartBuilder.create().uv(4, 0).cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
			.uv(14, 2).cuboid(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
			.uv(0, 2).mirrored().cuboid(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
}
