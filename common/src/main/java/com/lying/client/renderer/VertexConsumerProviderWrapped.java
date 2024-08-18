package com.lying.client.renderer;

import java.util.Optional;
import java.util.OptionalInt;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class VertexConsumerProviderWrapped implements VertexConsumerProvider
{
	private final VertexConsumerProvider internal;
	
	private OptionalInt	color = OptionalInt.empty();
	private Optional<Float>	alpha = Optional.empty();
	
	public VertexConsumerProviderWrapped(VertexConsumerProvider providerIn)
	{
		this.internal = providerIn;
	}
	
	public VertexConsumerProvider internal() { return internal; }
	
	public VertexConsumer getBuffer(RenderLayer var1)
	{
		VertexConsumerWrapped buffer = new VertexConsumerWrapped(internal.getBuffer(var1));
		color.ifPresent(col -> buffer.setRGB(col));
		alpha.ifPresent(alp -> buffer.setAlpha(alp));
		return buffer;
	}
	
	public void setRGB(int color)
	{
		this.color = OptionalInt.of(color);
	}
	
	public void setAlpha(float a) { this.alpha = Optional.of(a); }
}
