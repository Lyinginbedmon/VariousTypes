package com.lying.client.renderer;

import com.lying.entity.SmokeEntity;
import com.lying.init.VTBlocks;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class SmokeEntityRenderer extends EntityRenderer<SmokeEntity>
{
	private static final BlockState state = VTBlocks.SMOKE.get().getDefaultState();
	private final BlockRenderManager blockRenderManager;
	
	public SmokeEntityRenderer(Context ctx)
	{
		super(ctx);
		this.shadowRadius = 0F;
		this.blockRenderManager = ctx.getBlockRenderManager();
	}
	
	public void render(SmokeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		if(state.getRenderType() != BlockRenderType.MODEL)
			return;
		
		// TODO Transparency proportional to scale
		float scale = entity.calculateSize();
		Random rand = Random.create(entity.getUuid().getLeastSignificantBits());
		matrices.push();
			matrices.scale(scale, scale, scale);
			matrices.translate(-0.5F, 0F, -0.5F);
			if(scale < 1F)
			{
				float age = ((float)entity.age + tickDelta) / 10F;
				matrices.translate(
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F);
			}
			BakedModel blockModel = this.blockRenderManager.getModel(state);
			BlockPos pos = entity.getBlockPos();
			this.blockRenderManager.getModelRenderer().render(entity.getWorld(), blockModel, state, pos, matrices, vertexProvider.getBuffer(RenderLayers.getMovingBlockLayer(state)), false, rand, state.getRenderingSeed(pos), light);
		matrices.pop();
	}
	
	public Identifier getTexture(SmokeEntity entity) { return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE; }
}
