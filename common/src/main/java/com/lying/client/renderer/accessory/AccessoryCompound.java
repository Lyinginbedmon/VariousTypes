package com.lying.client.renderer.accessory;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class AccessoryCompound<E extends LivingEntity, T extends EntityModel<E>> implements IAccessoryRenderer<E, T>
{
	private final List<IAccessoryRenderer<E, T>> data = Lists.newArrayList();
	
	@SuppressWarnings("unchecked")
	protected AccessoryCompound(IAccessoryRenderer<E, T>... dataIn)
	{
		for(int i=0; i<dataIn.length; i++)
			data.add(dataIn[i]);
	}
	
	@SafeVarargs
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryCompound<E,T> create(IAccessoryRenderer<E, T>... dataIn)
	{
		return new AccessoryCompound<E,T>(dataIn);
	}
	
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
	{
		data.forEach(d -> d.prepareModel(entity, contextModel, limbAngle, limbDistance, tickDelta, headYaw, headPitch));
	}
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float partialTicks, boolean tinted, float r, float g, float b)
	{
		data.forEach(d -> d.renderFor(matrixStack, vertexConsumerProvider, light, entity, partialTicks, tinted, r, g, b));
	}
}