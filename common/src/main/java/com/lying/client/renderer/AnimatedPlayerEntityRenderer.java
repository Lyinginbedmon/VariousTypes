package com.lying.client.renderer;

import com.lying.client.VariousTypesClient;
import com.lying.client.entity.AnimatedPlayerEntity;
import com.lying.client.model.AnimatedPlayerEntityModel;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AnimatedPlayerEntityRenderer extends LivingEntityRenderer<AnimatedPlayerEntity, AnimatedPlayerEntityModel<AnimatedPlayerEntity>>
{
	private final AnimatedPlayerEntityModel<AnimatedPlayerEntity> MODEL_WIDE, MODEL_SLIM;
	
	public AnimatedPlayerEntityRenderer(Context ctx)
	{
		super(ctx, new AnimatedPlayerEntityModel<AnimatedPlayerEntity>(ctx.getPart(VariousTypesClient.ANIMATED_PLAYER)), 0.5F);
		MODEL_WIDE = new AnimatedPlayerEntityModel<AnimatedPlayerEntity>(ctx.getPart(VariousTypesClient.ANIMATED_PLAYER));
		MODEL_SLIM = new AnimatedPlayerEntityModel<AnimatedPlayerEntity>(ctx.getPart(VariousTypesClient.ANIMATED_PLAYER_SLIM));
	}
	
	public void render(AnimatedPlayerEntity animatedPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		switch(animatedPlayerEntity.getSkinTextures().model())
		{
			case WIDE:
				this.model = MODEL_WIDE;
				break;
			case SLIM:
				this.model = MODEL_SLIM;
				break;
		}
		
		setModelPose(animatedPlayerEntity);
		super.render(animatedPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
	
	@SuppressWarnings("rawtypes")
	private void setModelPose(AnimatedPlayerEntity player)
	{
		AnimatedPlayerEntityModel model = (AnimatedPlayerEntityModel)getModel();
		model.setVisible(true);
		// FIXME Copy part visibility from player profile
	}
	
	protected boolean hasLabel(AnimatedPlayerEntity animatedPlayerEntity) { return false; }
	
	public Identifier getTexture(AnimatedPlayerEntity animatedPlayerEntity)
	{
		return animatedPlayerEntity.getSkinTextures().texture();
	}
	
	protected void scale(AnimatedPlayerEntity animatedPlayerEntity, MatrixStack matrixStack, float f)
	{
		float g = 0.9375F;
		matrixStack.scale(g, g, g);
	}
}
