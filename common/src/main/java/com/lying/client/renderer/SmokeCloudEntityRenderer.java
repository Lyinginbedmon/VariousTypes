package com.lying.client.renderer;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.entity.SmokeCloudEntity;
import com.lying.init.VTBlocks;
import com.lying.utility.FloodFill;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

public class SmokeCloudEntityRenderer extends EntityRenderer<SmokeCloudEntity>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final BlockState state = VTBlocks.SMOKE.get().getDefaultState();
	private final BlockRenderManager blockRenderManager;
	
	public SmokeCloudEntityRenderer(Context ctx)
	{
		super(ctx);
		blockRenderManager = ctx.getBlockRenderManager();
	}
	
	public void render(SmokeCloudEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		if(state.getRenderType() != BlockRenderType.MODEL)
			return;
		
		int size = entity.getRadius();
		if(size == 0)
			return;
		
		Optional<FloodFill> area = entity.getAffectedArea();
		if(area.isEmpty())
			return;
		
		Camera camera = mc.gameRenderer.getCamera();
		BlockPos camPos = camera.getBlockPos();
		
		// FIXME Ensure rendering is locked to world grid
		Vec3d offset = new Vec3d(entity.getX()%1D, entity.getY()%1D, entity.getZ()%1D).add(0.5D, 0D, 0.5D).negate();
		
		FloodFill fill = area.get();
		Random rand = Random.create(entity.getUuid().getLeastSignificantBits());
		BlockPos pos = entity.getBlockPos();
		if(fill.contains(camPos))
		{
			// If the camera is inside of the cloud, just render the block it's inside of
			return;
		}
		else
		{
			double distToCore = camera.getPos().distanceTo(entity.getPos());
			List<BlockPos> blocksToRender = Lists.newArrayList();
			for(int i=size; i>0; i--)
				blocksToRender.addAll(
					fill.get(i).stream()
						// Ignore any position that is "behind" the core relative to the camera	FIXME Refine using planar calculation
						.filter(p -> (new Vec3d(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D)).distanceTo(camera.getPos()) <= distToCore)
						.toList());
			
			// TODO Ignore any block that is completely obscured by other rendered blocks in the cloud
			blocksToRender.forEach(block -> 
			{
				// TODO Calculate scale of this distance
				renderSmokeAt(block, pos, offset, 1F, entity.getWorld(), entity.age, tickDelta, matrices, vertexProvider, light, rand);
			});
		};
	}
	
	public Identifier getTexture(SmokeCloudEntity entity) { return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE; }
	
	public void renderSmokeAt(BlockPos pos, BlockPos core, Vec3d offset, float scale, BlockRenderView world, int time, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light, Random rand)
	{
		// TODO Transparency proportional to scale
		matrices.push();
			matrices.translate(pos.getX() - core.getX() + offset.getX(), pos.getY() - core.getY() + offset.getY(), pos.getZ() - core.getZ() + offset.getZ());
			matrices.scale(scale, scale, scale);
			matrices.translate(-0.5F, 0F, -0.5F);
			if(scale < 1F)
			{
				float age = ((float)time + tickDelta) / 10F;
				matrices.translate(
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + rand.nextFloat() * 10000) * 0.1F + 0.1F);
			}
			BakedModel blockModel = this.blockRenderManager.getModel(state);
			this.blockRenderManager.getModelRenderer().render(world, blockModel, state, pos, matrices, vertexProvider.getBuffer(RenderLayers.getMovingBlockLayer(state)), false, rand, state.getRenderingSeed(pos), light);
		matrices.pop();
	}
}
