package com.lying.client.renderer.accessory;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public interface IAccessoryRenderer<E extends LivingEntity, T extends EntityModel<E>>
{
	public default int renderOrder() { return 0; }
	
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch);
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float partialTicks, boolean tinted, float r, float g, float b);
}