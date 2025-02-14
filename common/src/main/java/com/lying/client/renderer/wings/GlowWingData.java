package com.lying.client.renderer.wings;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class GlowWingData<E extends LivingEntity, T extends EntityModel<E>> extends WingData<E, T>
{
	private final Identifier glowTexture;
	
	protected GlowWingData(T modelIn, Identifier glowTex)
	{
		super(modelIn, glowTex, glowTex);
		glowTexture = glowTex;
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> GlowWingData<E,T> create(T modelIn, Identifier glowTex)
	{
		return new GlowWingData<E,T>(modelIn, glowTex);
	}
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
	{
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(glowTexture));
		model.render(matrixStack, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, r, g, b, 1F);
	}
}