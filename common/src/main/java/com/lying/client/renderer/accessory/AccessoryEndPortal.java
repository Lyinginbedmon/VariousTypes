package com.lying.client.renderer.accessory;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class AccessoryEndPortal<E extends LivingEntity, T extends EntityModel<E>> extends AccessoryBasic<E, T>
{
	protected AccessoryEndPortal(T modelIn)
	{
		super(modelIn, null, null);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryEndPortal<E,T> create(T modelIn)
	{
		return new AccessoryEndPortal<E,T>(modelIn);
	}
	
	protected void doRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
	{
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEndPortal());
		model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
	}
}
