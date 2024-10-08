package com.lying.client.utility.highlights;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.google.common.collect.Lists;
import com.lying.client.utility.VTUtilsClient;
import com.lying.utility.Highlight;
import com.lying.utility.Highlight.Block;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockHighlights extends HighlightManager<Highlight.Block>
{
	private final Map<RegistryKey<World>, Map<BlockPos, Highlight.Block>> BLOCKS = new HashMap<>();
	
	public BlockHighlights() { super(Highlight.Type.BLOCK); }
	
	public void add(Block highlight) { }
	
	public void add(Block highlight, World world)
	{
		long time = world.getTime();
		if(highlight.pos().getY() < world.getBottomY() || highlight.pos().getY() > 256 || highlight.hasExpired(time))
			return;
		
		Map<BlockPos, Highlight.Block> blocks = BLOCKS.getOrDefault(world.getRegistryKey(), new HashMap<>());
		BlockPos pos = highlight.pos();
		if(!blocks.containsKey(pos) || highlight.expiry() > blocks.get(pos).expiry())
			blocks.put(pos, highlight);
		BLOCKS.put(world.getRegistryKey(), blocks);
	}
	
	public void clear() { BLOCKS.clear(); }
	
	public void clear(RegistryKey<World> world) { BLOCKS.remove(world); }
	
	public boolean isEmpty() { return BLOCKS.isEmpty() || BLOCKS.values().stream().allMatch(list -> list.isEmpty()); }
	
	public boolean isEmpty(RegistryKey<World> world) { return !BLOCKS.containsKey(world) || BLOCKS.getOrDefault(world, new HashMap<>()).isEmpty(); }
	
	public void tick(long currentTime)
	{
		// Remove all highlights that have completely expired across all worlds
		List<RegistryKey<World>> emptied = Lists.newArrayList();
		for(RegistryKey<World> world : BLOCKS.keySet())
		{
			Map<BlockPos, Highlight.Block> blocks = BLOCKS.getOrDefault(world, new HashMap<>());
			if(blocks.isEmpty()) continue;
			
			List<Highlight.Block> highlights = Lists.newArrayList();
			highlights.addAll(get(world));
			highlights.forEach(highlight -> 
			{
				if(highlight.hasExpired(currentTime))
					blocks.remove(highlight.pos());
			});
			
			if(!highlights.isEmpty())
				BLOCKS.put(world, blocks);
			else
				emptied.add(world);
		}
		emptied.forEach(world -> BLOCKS.remove(world));
	}
	
	public boolean shouldRender()
	{
		return !isEmpty() && mc.player.getWorld() != null && !isEmpty(mc.player.getWorld().getRegistryKey());
	}
	
	public Collection<Highlight.Block> get(RegistryKey<World> world)
	{
		return BLOCKS.containsKey(world) ? BLOCKS.get(world).values() : Lists.newArrayList();
	}
	
	/** Called in {@link WorldRenderer.render} via the AFTER_WORLD_RENDER event in ClientBus */
	public void renderHighlightedBlocks(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Matrix4f matrix4f1, Matrix4f matrix4f2, Camera camera, float tickDelta)
	{
		renderHighlightedBlocks(matrixStack, matrix4f2, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), camera.getPos());
	}
	
	public void renderHighlightedBlocks(MatrixStack matrixStack, Matrix4f matrix4f, VertexConsumer vertexConsumer, Vec3d cameraPos)
	{
		if(!shouldRender()) return;
		
		List<Line> linesToRender = Lists.newArrayList();
		
		// Build a set of all lines from each block (typically size 12n)
		for(Block block : get(mc.player.getWorld().getRegistryKey()))
		{
			BlockPos pos = block.pos();
			if(cameraPos.distanceTo(new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)) > 32D) continue;
			
			float alpha = block.alpha(mc.player.getWorld().getTime());
			Vec3d min = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
			// Horizontal planes at the top and bottom faces
			for(int y=0; y<2; y++)
				for(int x=0; x<2; x++)
				{
					linesToRender.add(new Line(min.add(x, y, x), min.add(1, y, 0), block.color(), alpha));
					linesToRender.add(new Line(min.add(x, y, x), min.add(0, y, 1), block.color(), alpha));
				}
			
			// Vertical, joining the two horizontal planes into 6 total faces
			for(int x=0; x<2; x++)
				for(int z=0; z<2; z++)
					linesToRender.add(new Line(min.add(x, 0, z), min.add(x, 1, z), block.color(), alpha));
		};
		
		// FIXME Ensure rendered outlines remain fixed to the block grid when running or view bobbing
		matrixStack.push();
			matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			Matrix4f entry = matrixStack.peek().getPositionMatrix();
			
			// XXX Merge connecting parallel lines to reduce draw volume?
			removeAllDuplicate(linesToRender).forEach(line -> 
			{
				Vec3d v1 = line.start;
				Vec3d v2 = line.end;
				
				Vector3f color = VTUtilsClient.decimalToVector(line.color());
				float a = line.alpha();
				
				vertexConsumer.vertex(entry, (float)v1.x, (float)v1.y, (float)v1.z).color(color.x, color.y, color.z, a).normal(1F, 0F, 0F).next();
				vertexConsumer.vertex(entry, (float)v2.x, (float)v2.y, (float)v2.z).color(color.x, color.y, color.z, a).normal(1F, 0F, 0F).next();
				
				vertexConsumer.vertex(entry, (float)v1.x, (float)v1.y, (float)v1.z).color(color.x, color.y, color.z, a).normal(0F, 1F, 0F).next();
				vertexConsumer.vertex(entry, (float)v2.x, (float)v2.y, (float)v2.z).color(color.x, color.y, color.z, a).normal(0F, 1F, 0F).next();
				
				vertexConsumer.vertex(entry, (float)v1.x, (float)v1.y, (float)v1.z).color(color.x, color.y, color.z, a).normal(0F, 0F, 1F).next();
				vertexConsumer.vertex(entry, (float)v2.x, (float)v2.y, (float)v2.z).color(color.x, color.y, color.z, a).normal(0F, 0F, 1F).next();
			});
			
		matrixStack.pop();
	}
	
	/** Returns a list of unique lines from the original list, resulting in an aggregate outline of highlighted blocks */
	private static List<Line> removeAllDuplicate(List<Line> lines)
	{
		List<Line> set = Lists.newArrayList();
		for(int i=0; i<lines.size(); i++)
		{
			Line line = lines.get(i);
			boolean duplicate = false;
			for(int j=0; j<lines.size(); j++)
				if(j != i && lines.get(j).overlaps(line))
				{
					duplicate = true;
					break;
				}
			if(!duplicate)
				set.add(line);
		}
		return set;
	}
	
	private static record Line(Vec3d start, Vec3d end, int color, float alpha)
	{
		/** Returns true if the given position occurs at some point on this line */
		public boolean intersects(Vec3d point)
		{
			double distStart = start.distanceTo(point);
			double distEnd = end.distanceTo(point);
			return distStart == 0D || distEnd == 0D || (distStart + distEnd) == start.distanceTo(end);
		}
		
		/** Returns true if this line overlaps both ends of the given line */
		public boolean overlaps(Line line2)
		{
			return intersects(line2.start) && intersects(line2.end);
		}
	}
}
