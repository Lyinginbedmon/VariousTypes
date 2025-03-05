package com.lying.client.renderer.accessory;

import com.lying.reference.Reference;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class AccessoryLightning<E extends LivingEntity, T extends EntityModel<E>> extends AccessoryBasic<E, T>
{
	public static final Identifier TEXTURE = Reference.ModInfo.prefix("textures/entity/lightning.png");
	
	protected AccessoryLightning(T modelIn)
	{
		super(modelIn, null, null);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryLightning<E,T> create(T modelIn)
	{
		return new AccessoryLightning<E,T>(modelIn);
	}
	
	public int renderOrder() { return 1000; }
	
	protected void doRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float partialTicks, boolean tinted, float r, float g, float b)
	{
		float f = ((float)entity.age + partialTicks) * 0.004F;
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEnergySwirl(TEXTURE, f, f));
		model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
	}
}
