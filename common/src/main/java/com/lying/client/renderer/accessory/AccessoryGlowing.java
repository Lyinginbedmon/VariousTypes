package com.lying.client.renderer.accessory;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class AccessoryGlowing<E extends LivingEntity, T extends EntityModel<E>> extends AccessoryBasic<E, T>
{
	private final Identifier glowTexture;
	
	protected AccessoryGlowing(T modelIn, Identifier glowTex)
	{
		super(modelIn, glowTex, glowTex);
		glowTexture = glowTex;
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryGlowing<E,T> create(T modelIn, Identifier glowTex)
	{
		return new AccessoryGlowing<E,T>(modelIn, glowTex);
	}
	
	protected void doRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
	{
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(glowTexture));
		model.render(matrixStack, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, r, g, b, 1F);
	}
}