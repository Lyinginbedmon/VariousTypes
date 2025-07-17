package com.lying.client.renderer;

import com.lying.entity.PortalEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PortalEntityRenderer extends EntityRenderer<PortalEntity>
{
	public PortalEntityRenderer(Context ctx)
	{
		super(ctx);
	}
	
	public Identifier getTexture(PortalEntity var1) { return null; }
	
	public void render(PortalEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) { }
}
