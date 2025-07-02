package com.lying.client.renderer;

import com.lying.entity.EmitterEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EmitterEntityRenderer extends EntityRenderer<EmitterEntity>
{
	public EmitterEntityRenderer(Context ctx)
	{
		super(ctx);
	}
	
	public Identifier getTexture(EmitterEntity var1) { return null; }
	
	public void render(EmitterEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) { }
}
