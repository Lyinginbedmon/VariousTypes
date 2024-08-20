package com.lying.client.renderer;

import java.util.OptionalInt;

import org.joml.Vector3f;

import com.lying.client.utility.VTUtilsClient;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class VertexConsumerProviderWrapped implements VertexConsumerProvider
{
	private final VertexConsumerProvider internal;
	
	private OptionalInt	color = OptionalInt.empty();
	private Float alpha = 1F;
	
	public VertexConsumerProviderWrapped(VertexConsumerProvider providerIn)
	{
		this.internal = providerIn;
	}
	
	public VertexConsumerProvider internal() { return internal; }
	
	public VertexConsumer getBuffer(RenderLayer var1)
	{
		VertexConsumerWrapped buffer = new VertexConsumerWrapped(internal.getBuffer(var1));
		color.ifPresent(col -> buffer.setRGB(col));
		buffer.setAlpha(alpha);
		return buffer;
	}
	
	public void modifyColor(Vector3f colorIn)
	{
		int current = this.color.orElse(0xFFFFFF);
		this.color = OptionalInt.of(VTUtilsClient.vectorToDecimal(VTUtilsClient.decimalToVector(current).mul(colorIn)));
	}
	
	public void modifyAlpha(float a) { this.alpha *= a; }
}
