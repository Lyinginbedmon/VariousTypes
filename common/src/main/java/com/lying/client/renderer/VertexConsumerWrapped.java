package com.lying.client.renderer;

import java.util.Optional;
import java.util.OptionalInt;

import net.minecraft.client.render.VertexConsumer;

public class VertexConsumerWrapped implements VertexConsumer
{
	private final VertexConsumer internal;
	
	private OptionalInt color = OptionalInt.empty();
	private Optional<Float> alpha = Optional.empty();
	
	public VertexConsumerWrapped(VertexConsumer consumerIn)
	{
		this.internal = consumerIn;
	}
	
	public void setRGB(int color)
	{
		this.color = OptionalInt.of(color);
	}
	
	public void setAlpha(float a) { this.alpha = Optional.of(a); }
	
	public VertexConsumer color(int red, int green, int blue, int alpha)
	{
		if(color.isPresent())
		{
			int col = color.getAsInt();
			red = mixColors(red, (col & 0xFF0000) >> 16);
			green = mixColors(green, (col & 0xFF00) >> 8);
			blue = mixColors(blue, (col & 0xFF) >> 0);
		}
		
		if(this.alpha.isPresent())
			alpha *= this.alpha.get();
		
		return internal.color(red, green, blue, alpha);
	}
	
	/** Multiply colour components together as percentiles and return equivalent integer form */
	private static int mixColors(int colA, int colB)
	{
		float a = (float)colA / 255F;
		float b = (float)colB / 255F;
		return (int)(255 * (a * b));
	}
	
	public VertexConsumer vertex(double var1, double var3, double var5) { return internal.vertex(var1, var3, var5); }
	
	public VertexConsumer texture(float var1, float var2) { return internal.texture(var1, var2); }
	
	public VertexConsumer overlay(int var1, int var2) { return internal.overlay(var1, var2); }
	
	public VertexConsumer light(int var1, int var2) { return internal.light(var1, var2); }
	
	public VertexConsumer normal(float var1, float var2, float var3) { return internal.normal(var1, var2, var3); }
	
	public void next() { internal.next(); }
	
	public void fixedColor(int var1, int var2, int var3, int var4) { internal.fixedColor(var1, var2, var3, var4); }
	
	public void unfixColor() { internal.unfixColor(); }
}
