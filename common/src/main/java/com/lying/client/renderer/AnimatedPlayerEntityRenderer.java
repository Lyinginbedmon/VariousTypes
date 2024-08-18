package com.lying.client.renderer;

import com.lying.client.entity.AnimatedPlayerEntity;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.AnimatedPlayerEntityModel;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class AnimatedPlayerEntityRenderer extends LivingEntityRenderer<AnimatedPlayerEntity, AnimatedPlayerEntityModel<AnimatedPlayerEntity>>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private final AnimatedPlayerEntityModel<AnimatedPlayerEntity> MODEL_WIDE, MODEL_SLIM;
	
	public AnimatedPlayerEntityRenderer(Context ctx)
	{
		super(ctx, new AnimatedPlayerEntityModel<AnimatedPlayerEntity>(ctx.getPart(VTModelLayerParts.ANIMATED_PLAYER)), 0.5F);
		MODEL_WIDE = getModel();
		MODEL_SLIM = new AnimatedPlayerEntityModel<AnimatedPlayerEntity>(ctx.getPart(VTModelLayerParts.ANIMATED_PLAYER_SLIM));
	}
	
	public void render(AnimatedPlayerEntity animatedPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		switch(getSkinTextures(animatedPlayerEntity).model())
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
		
		PlayerEntity player2 = mc.world.getPlayerByUuid(player.getGameProfile().getId());
		if(player2 == null)
			return;
		
		model.hat.visible = player2.isPartVisible(PlayerModelPart.HAT);
		model.jacket.visible = player2.isPartVisible(PlayerModelPart.JACKET);
		model.leftSleeve.visible = player2.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
		model.rightSleeve.visible = player2.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
		model.leftPants.visible = player2.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
		model.rightPants.visible = player2.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
	}
	
	protected boolean hasLabel(AnimatedPlayerEntity animatedPlayerEntity) { return false; }
	
	public SkinTextures getSkinTextures(AnimatedPlayerEntity animatedPlayerEntity)
	{
		PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(animatedPlayerEntity.getUuid());
		return playerListEntry == null ? DefaultSkinHelper.getSkinTextures(animatedPlayerEntity.getGameProfile()) : playerListEntry.getSkinTextures();
	}
	
	public Identifier getTexture(AnimatedPlayerEntity animatedPlayerEntity)
	{
		return getSkinTextures(animatedPlayerEntity).texture();
	}
	
	protected void scale(AnimatedPlayerEntity animatedPlayerEntity, MatrixStack matrixStack, float f)
	{
		float g = 0.9375F;
		matrixStack.scale(g, g, g);
	}
}
