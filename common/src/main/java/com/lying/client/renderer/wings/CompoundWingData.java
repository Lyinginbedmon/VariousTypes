package com.lying.client.renderer.wings;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class CompoundWingData<E extends LivingEntity, T extends EntityModel<E>> implements WingRenderer<E, T>
{
	private final List<WingRenderer<E, T>> data = Lists.newArrayList();
	
	@SuppressWarnings("unchecked")
	protected CompoundWingData(WingRenderer<E, T>... dataIn)
	{
		for(int i=0; i<dataIn.length; i++)
			data.add(dataIn[i]);
	}
	
	@SafeVarargs
	public static <E extends LivingEntity, T extends EntityModel<E>> CompoundWingData<E,T> create(WingRenderer<E, T>... dataIn)
	{
		return new CompoundWingData<E,T>(dataIn);
	}
	
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
	{
		data.forEach(d -> d.prepareModel(entity, contextModel, limbAngle, limbDistance, tickDelta, headYaw, headPitch));
	}
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
	{
		data.forEach(d -> d.renderFor(matrixStack, vertexConsumerProvider, light, entity, tinted, r, g, b));
	}
}