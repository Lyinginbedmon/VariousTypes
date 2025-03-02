package com.lying.client.model.tail;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class TailRatModel<E extends LivingEntity> extends TailKirinModel<E>
{
	public TailRatModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData tail = body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.25F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.2F))
				.uv(4, 0).cuboid(-1.0F, -0.75F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.5F, 1.75F, 1.5708F, 0.0F, 0.0F));
		ModelPartData seg1 = tail.addChild(SEG1, ModelPartBuilder.create().uv(0, 7).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 5.75F, 0.0F));
		ModelPartData seg2 = seg1.addChild(SEG2, ModelPartBuilder.create().uv(4, 7).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, -0.5F));
		seg2.addChild(SEG3, ModelPartBuilder.create().uv(8, 7).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, 4.75F, 0.5F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
}
