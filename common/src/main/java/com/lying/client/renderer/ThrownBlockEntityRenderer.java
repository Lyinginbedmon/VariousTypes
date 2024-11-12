package com.lying.client.renderer;

import com.lying.entity.ThrownBlockEntity;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ThrownBlockEntityRenderer extends EntityRenderer<ThrownBlockEntity>
{
	private final BlockRenderManager blockRenderManager;
	
	public ThrownBlockEntityRenderer(Context ctx)
	{
		super(ctx);
		this.shadowRadius = 0.5F;
		this.blockRenderManager = ctx.getBlockRenderManager();
	}
	
	public void render(ThrownBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		BlockState state = entity.blockState();
		if(state.getRenderType() != BlockRenderType.MODEL)
			return;
		
		World world = entity.getWorld();
		BlockPos blockPos = entity.getBlockPos();
		if(state == world.getBlockState(blockPos))
			return;
		
		matrices.push();
			BlockPos pos = BlockPos.ofFloored(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
			matrices.translate(-0.5F, 0F, -0.5F);
			BakedModel blockModel = this.blockRenderManager.getModel(state);
			this.blockRenderManager.getModelRenderer().render(world, blockModel, state, pos, matrices, vertexProvider.getBuffer(RenderLayers.getMovingBlockLayer(state)), false, Random.create(), state.getRenderingSeed(blockPos), light);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexProvider, light);
	}
	
	public Identifier getTexture(ThrownBlockEntity var1) { return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE; }
}
