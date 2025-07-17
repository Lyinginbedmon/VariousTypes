package com.lying.client.renderer;

import java.util.Optional;
import java.util.function.Predicate;

import com.lying.entity.SmokeCloudEntity;
import com.lying.init.VTBlocks;
import com.lying.utility.FloodFill;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class SmokeCloudEntityRenderer extends EntityRenderer<SmokeCloudEntity>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final BlockState state = VTBlocks.SMOKE.get().getDefaultState();
	private final BlockRenderManager blockRenderManager;
	
	private Frustum viewFrustum;
	
	public SmokeCloudEntityRenderer(Context ctx)
	{
		super(ctx);
		blockRenderManager = ctx.getBlockRenderManager();
	}
	
	public boolean shouldRender(SmokeCloudEntity entity, Frustum frustum, double x, double y, double z)
	{
		viewFrustum = frustum;
		return super.shouldRender(entity, frustum, x, y, z);
	}
	
	public void render(SmokeCloudEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		if(state.getRenderType() != BlockRenderType.MODEL)
			return;
		
		final Camera camera = mc.gameRenderer.getCamera();
		final BlockPos camPos = camera.getBlockPos();
		final BlockPos entBlock = entity.getBlockPos();
		final Vec3d entOffset = new Vec3d(entity.getX()%1D, entity.getY()%1D, entity.getZ()%1D).add(0.5D, 0D, 0.5D).negate();
		
		final Vec3d camVec = camera.getPos();
		// Disable distance culling if the camera is inside of the inner volume
		final double distToCore = camVec.distanceTo(entity.getPos()) < entity.getInnerRadius() ? -1 : camVec.distanceTo(entity.getPos());
		Random rand = Random.create(entity.getUuid().getLeastSignificantBits());
		
		// Opaque inner volume
		final FloodFill innerFill = entity.getInnerArea().orElse(null);
		if(innerFill == null)
			return;
		else
		{
			// Don't bother rendering at all if the camera is inside of the inner volume, because they'll be blinded anyway
			if(innerFill.contains(camPos))
				return;
			
			final int innerRadius = entity.getInnerRadius() * entity.getInnerRadius();
			Predicate<BlockPos> cullCheck = p -> 
				/*
				 * Always render if the position is closer to the core than the outer shell should be
				 * This prevents interior voids from being exposed
				 */
				p.getSquaredDistance(entBlock) < innerRadius || 
				// Otherwise only render if the position is at least as close to the camera as the core is
				new Vec3d(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D).distanceTo(camVec) <= distToCore;
			
			renderFloodFill(
					innerFill, 
					entBlock, 
					entOffset,
					cullCheck,
					1F,
					entity.getWorld(),
					entity.age,
					camera,
					rand,
					tickDelta,
					matrices,
					vertexProvider,
					light);
			
			// Semi-opaque outer volume
			final float outerScale = entity.getOuterScale(tickDelta);
			entity.getOuterArea().ifPresent(fill -> renderFloodFill(
					fill, 
					entBlock, 
					entOffset,
					cullCheck,
					outerScale,
					entity.getWorld(),
					entity.age,
					camera,
					rand,
					tickDelta,
					matrices,
					vertexProvider,
					light));
		}
	}
	
	public Identifier getTexture(SmokeCloudEntity entity) { return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE; }
	
	private void renderFloodFill(FloodFill fill, BlockPos core, Vec3d positionOffset, Predicate<BlockPos> cullCheck, float scale, World world, int age, Camera camera, Random rand, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light)
	{
		fill.getAllExteriors().stream()
			.filter(p -> 
				// Exclude positions outside the current view frustum
				viewFrustum.isVisible(Box.enclosing(p, p)) && cullCheck.test(p))
			.forEach(block -> 
				renderSmokeAt(block, core, positionOffset, scale, world, age, tickDelta, matrices, vertexProvider, light, rand));
	}
	
	public void renderSmokeAt(BlockPos pos, BlockPos core, Vec3d offset, float scale, BlockRenderView world, int time, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light, Random rand)
	{
		float size = 1F;
		Random posRand = Random.create(pos.getX() * pos.getY() * pos.getZ());
		if(scale < 1F)
		{
			if(posRand.nextInt(3) > 0) return;
			size = Math.max(0.5F, posRand.nextFloat()) * scale;
		}
		
		// TODO Transparency proportional to scale?
		matrices.push();
			matrices.translate(pos.getX() - core.getX() + offset.getX(), pos.getY() - core.getY() + offset.getY(), pos.getZ() - core.getZ() + offset.getZ());
			matrices.translate(0D, 0.5D, 0D);
			matrices.scale(size, size, size);
			matrices.translate(-0.5F, -0.5F, -0.5F);
			if(size < 1F)
			{
				float age = ((float)time + tickDelta) / 10F;
				matrices.translate(
						MathHelper.sin(age + posRand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + posRand.nextFloat() * 10000) * 0.1F + 0.1F, 
						MathHelper.sin(age + posRand.nextFloat() * 10000) * 0.1F + 0.1F);
			}
			BakedModel blockModel = this.blockRenderManager.getModel(state);
			this.blockRenderManager.getModelRenderer().render(
					world, 
					blockModel, 
					state, 
					pos, 
					matrices, 
					vertexProvider.getBuffer(RenderLayers.getMovingBlockLayer(state)), 
					false, 
					rand, 
					state.getRenderingSeed(pos), light);
		matrices.pop();
	}
	
	/** Returns true if the given cloud is completely obscuring the position of the camera */
	public static boolean shouldBlindPlayer(SmokeCloudEntity cloud)
	{
		Optional<FloodFill> area = cloud.getInnerArea();
		if(area.isEmpty())
			return false;
		
		Camera camera = mc.gameRenderer.getCamera();
		return area.get().contains(camera.getPos());
	}
}
