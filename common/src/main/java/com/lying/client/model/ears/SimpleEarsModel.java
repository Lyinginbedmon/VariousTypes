package com.lying.client.model.ears;

import com.lying.client.model.AbstractAccessoryModel;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class SimpleEarsModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public SimpleEarsModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createElfEars()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(10, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, Dilation.NONE)
			.uv(12, 1).cuboid(0.5F, 1.0F, -2.0F, 0.0F, 3.0F, 4.0F, Dilation.NONE), ModelTransform.of(4.0F, -5.5F, 0.25F, 0.1745F, 0.48F, 0.0F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, Dilation.NONE).mirrored(false)
			.uv(0, 1).mirrored().cuboid(-0.5F, 1.0F, -2.0F, 0.0F, 3.0F, 4.0F, Dilation.NONE).mirrored(false), ModelTransform.of(-4.0F, -5.5F, 0.25F, 0.1745F, -0.48F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	public static TexturedModelData createGoblinEars()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(12, 0).cuboid(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 5.0F, Dilation.NONE)
			.uv(14, 1).cuboid(0.5F, 1.0F, -2.0F, 0.0F, 4.0F, 5.0F, Dilation.NONE), ModelTransform.of(4.0F, -6.5F, 0.25F, 0.1745F, 0.48F, 0.0F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 5.0F, Dilation.NONE).mirrored(false)
			.uv(0, 1).mirrored().cuboid(-0.5F, 1.0F, -2.0F, 0.0F, 4.0F, 5.0F, Dilation.NONE).mirrored(false), ModelTransform.of(-4.0F, -6.5F, 0.25F, 0.1745F, -0.48F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	public static TexturedModelData createCatEars()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(6, 0).cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, Dilation.NONE), ModelTransform.pivot(3.0F, -8.0F, 3.0F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, Dilation.NONE), ModelTransform.pivot(-3.0F, -8.0F, 3.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createWolfEars()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(6, 0).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(3.0F, -8.0F, 1.5F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(-3.0F, -8.0F, 1.5F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createFoxEars()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(6, 0).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(3.0F, -8.0F, -1.5F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(-3.0F, -8.0F, -1.5F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
}
