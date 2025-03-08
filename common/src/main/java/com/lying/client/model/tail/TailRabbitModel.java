package com.lying.client.model.tail;

import com.lying.entity.AccessoryAnimationInterface;

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

public class TailRabbitModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public TailRabbitModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -0.25F, -1.25F, 4.0F, 2.0F, 3.0F, Dilation.NONE), ModelTransform.of(0.0F, 10.5F, 1.75F, 1.5708F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
	{
		tail.yaw = MathHelper.cos(limbAngle * 0.6662f) * 0.3f * limbDistance;
	}
}
