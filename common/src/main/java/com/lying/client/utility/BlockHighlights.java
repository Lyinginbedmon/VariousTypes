package com.lying.client.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockHighlights
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final Map<RegistryKey<World>, List<Highlight>> BLOCKS = new HashMap<>();
	
	public static void add(BlockPos pos, World world, int duration)
	{
		if(pos.getY() < world.getBottomY() || pos.getY() > 256)
			return;
		
		List<Highlight> blocks = BLOCKS.getOrDefault(world.getRegistryKey(), Lists.newArrayList());
		blocks.add(new Highlight(pos, world.getTime() + duration));
		BLOCKS.put(world.getRegistryKey(), blocks);
	}
	
	public static void clear() { BLOCKS.clear(); }
	
	public static boolean isEmpty() { return BLOCKS.values().stream().allMatch(list -> list.isEmpty()); }
	
	public static boolean isEmpty(RegistryKey<World> world) { return BLOCKS.getOrDefault(world, Lists.newArrayList()).isEmpty(); }
	
	public static void tick(long currentTime)
	{
		for(RegistryKey<World> world : BLOCKS.keySet())
		{
			List<Highlight> blocks = BLOCKS.getOrDefault(world, Lists.newArrayList());
			if(blocks.isEmpty()) continue;
			blocks.removeIf(pos -> pos.hasExpired(currentTime));
			BLOCKS.put(world, blocks);
		}
	}
	
	public static boolean shouldRender()
	{
		return mc.player.getWorld() != null && BLOCKS.containsKey(mc.player.getWorld().getRegistryKey()) && !isEmpty(mc.player.getWorld().getRegistryKey());
	}
	
	public static void renderHighlightedBlocks(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Camera camera)
	{
		if(!shouldRender()) return;
		List<Highlight> blocks = BLOCKS.getOrDefault(mc.player.getWorld().getRegistryKey(), Lists.newArrayList());
		Vec3d cameraPos = camera.getPos();
		blocks.forEach(block -> 
		{
			matrixStack.push();
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
				BlockPos pos = block.pos;
				double x = pos.getX() - cameraPos.getX();
				double y = pos.getY() - cameraPos.getY();
				double z = pos.getZ() - cameraPos.getZ();
				WorldRenderer.drawBox(matrixStack, vertexConsumer, x, y, z, x + 1D, y + 1D, z + 1D, 0.9f, 0.9f, 0.9f, 1.0f, 0.5f, 0.5f, 0.5f);
				// FIXME Replace with conglomerate rendering instead of individual wireframe
			matrixStack.pop();
		});
	}
	
	public static record Highlight(BlockPos pos, long expiry)
	{
		public boolean hasExpired(long currentTime) { return currentTime > expiry; }
		
		public NbtCompound toNbt(NbtCompound nbt)
		{
			nbt.putLong("Expires", expiry);
			nbt.put("Pos", NbtHelper.fromBlockPos(pos));
			return nbt;
		}
	}
}
