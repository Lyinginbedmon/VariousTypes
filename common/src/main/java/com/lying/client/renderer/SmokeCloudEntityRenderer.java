package com.lying.client.renderer;

import java.util.Optional;

import org.joml.Matrix4f;

import com.lying.VariousTypes;
import com.lying.entity.SmokeCloudEntity;
import com.lying.init.VTBlocks;
import com.lying.utility.FloodFill;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

public class SmokeCloudEntityRenderer extends EntityRenderer<SmokeCloudEntity>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final BlockState state = VTBlocks.SMOKE.get().getDefaultState();
	private final BlockRenderManager blockRenderManager;
	
	private Frustum viewFrustrum;
	
	public SmokeCloudEntityRenderer(Context ctx)
	{
		super(ctx);
		blockRenderManager = ctx.getBlockRenderManager();
	}
	
	public boolean shouldRender(SmokeCloudEntity entity, Frustum frustum, double x, double y, double z)
	{
		this.viewFrustrum = frustum;
		return super.shouldRender(entity, frustum, x, y, z);
	}
	
	public void render(SmokeCloudEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		if(state.getRenderType() != BlockRenderType.MODEL)
			return;
		
		Optional<FloodFill> area = entity.getAffectedArea();
		if(area.isEmpty())
			return;
		
		Camera camera = mc.gameRenderer.getCamera();
		BlockPos camPos = camera.getBlockPos();
		
		FloodFill fill = area.get();
		BlockPos pos = entity.getBlockPos();
		if(fill.contains(camPos))
		{
			// FIXME If the camera is inside of the cloud, skip rendering and render smoke around the player's head
			return;
		}
		else
		{
			Random rand = Random.create(entity.getUuid().getLeastSignificantBits());
			Vec3d offset = new Vec3d(entity.getX()%1D, entity.getY()%1D, entity.getZ()%1D).add(0.5D, 0D, 0.5D).negate();
			double distToCore = camera.getPos().distanceTo(entity.getPos());
			fill.getAllExteriors().stream()
				.filter(p -> 
					// Check if the position is within the view frustrum
					viewFrustrum.isVisible(Box.enclosing(p, p)) &&
					// Check if the position isn't farther from the camera than the core of the cloud is
					(new Vec3d(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D)).distanceTo(camera.getPos()) <= distToCore)
				.forEach(block -> 
					renderSmokeAt(block, pos, offset, 1F, entity.getWorld(), entity.age, tickDelta, matrices, vertexProvider, light, rand));	// FIXME Calculate scale at position radius
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
	
	public static boolean shouldBlindPlayer(SmokeCloudEntity cloud)
	{
		Optional<FloodFill> area = cloud.getAffectedArea();
		if(area.isEmpty())
			return false;
		
		Camera camera = mc.gameRenderer.getCamera();
		return area.get().contains(camera.getBlockPos());
	}
}
