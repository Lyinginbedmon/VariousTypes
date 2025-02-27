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

public class HornsHartebeestModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public HornsHartebeestModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		ModelPartData right_horn = head.addChild("right_horn", ModelPartBuilder.create(), ModelTransform.of(4.5F, -6.5F, -0.5F, 0.4367F, 0.0395F, 0.0184F));
		right_horn.addChild("cube_r1", ModelPartBuilder.create().uv(19, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(0.0F, 0.0F, 5.25F, -0.0602F, -0.1639F, 0.0922F));
		right_horn.addChild("cube_r2", ModelPartBuilder.create().uv(12, 1).cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(-0.45F)), ModelTransform.of(0.0F, 0.0F, 4.5F, 0.0F, -0.0873F, 0.0436F));
		right_horn.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.1745F));
		right_horn.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.0F));
		
		ModelPartData left_horn = head.addChild("left_horn", ModelPartBuilder.create(), ModelTransform.of(-4.5F, -6.5F, -0.5F, 0.4367F, -0.0395F, -0.0184F));
		left_horn.addChild("cube_r5", ModelPartBuilder.create().uv(19, 0).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 5.25F, -0.0602F, 0.1639F, -0.0922F));
		left_horn.addChild("cube_r6", ModelPartBuilder.create().uv(12, 1).mirrored().cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(-0.45F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 4.5F, 0.0F, 0.0873F, -0.0436F));
		left_horn.addChild("cube_r7", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.1745F));
		left_horn.addChild("cube_r8", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.25F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
}
