package com.lying.client.renderer.wings;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class EndPortalWingData<E extends LivingEntity, T extends EntityModel<E>> extends WingData<E, T>
{
	protected EndPortalWingData(T modelIn)
	{
		super(modelIn, null, null);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> EndPortalWingData<E,T> create(T modelIn)
	{
		return new EndPortalWingData<E,T>(modelIn);
	}
	
	protected void doRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
	{
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEndPortal());
		model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
	}
}
