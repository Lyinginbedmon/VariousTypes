package com.lying.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class FloodFill
{
	private static final Codec<FloodFill> CODEC = Row.CODEC.listOf().xmap(FloodFill::fromRows, FloodFill::toRows);
	private final Map<Integer, List<BlockPos>> fill = new HashMap<>();
	
	private static FloodFill fromRows(List<FloodFill.Row> rows)
	{
		FloodFill floodFill = new FloodFill();
		rows.forEach(r -> floodFill.fill.put(r.radius, r.points));
		return floodFill;
	}
	
	private List<FloodFill.Row> toRows()
	{
		return fill.entrySet().stream().map(e -> new Row(e.getKey(), e.getValue())).toList();
	}
	
	private void add(BlockPos pos, int dist)
	{
		List<BlockPos> points = get(dist);
		if(!points.contains(pos))
		{
			points.add(pos);
			fill.put(dist, points);
		}
	}
	
	public boolean isEmpty(int distance) { return !fill.containsKey(distance) || get(distance).isEmpty(); }
	
	public boolean isEmpty() { return fill.isEmpty() || fill.values().stream().allMatch(List::isEmpty); }
	
	public int size()
	{
		int tally = 0;
		for(List<BlockPos> points : fill.values())
			tally += points.size();
		return tally;
	}
	
	public boolean contains(BlockPos pos)
	{
		return fill.values().stream().anyMatch(range -> range.stream().anyMatch(point -> point.equals(pos)));
	}
	
	public List<BlockPos> get(int distance)
	{
		return fill.getOrDefault(distance, Lists.newArrayList());
	}
	
	public NbtElement toNbt()
	{
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	@Nullable
	public static FloodFill fromNbt(NbtElement ele)
	{
		return CODEC.parse(NbtOps.INSTANCE, ele).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	private record Row(int radius, List<BlockPos> points)
	{
		public static final Codec<List<BlockPos>> POINTS_CODEC = BlockPos.CODEC.listOf();
		public static final Codec<FloodFill.Row> CODEC	= RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("Radius").forGetter(Row::radius), 
				POINTS_CODEC.fieldOf("Points").forGetter(Row::points))
					.apply(instance, FloodFill.Row::new));
		
	}
	
	public static class Search
	{
		private static final List<Vec3i> MOVES = Lists.newArrayList();
		static
		{
			for(int y=-1; y<2; y++)
				for(int x=-1; x<2; x++)
					for(int z=-1; z<2; z++)
					{
						if(x == 0 && y == 0 && z == 0) continue;
						MOVES.add(new Vec3i(x, y, z));
					}
		}
		
		private Map<BlockPos, Integer> posToDist = new HashMap<>();
		
		private final int maxRadius;
		private final BlockPos origin;
		private final AccessibilityCheck checker;
		
		public Search(BlockPos origin, int maxRadius, AccessibilityCheck checker)
		{
			this.origin = origin;
			this.maxRadius = maxRadius;
			this.checker = checker;
		}
		
		public Search(BlockPos origin, int maxRadius)
		{
			this(origin, maxRadius, (to, from, world, entity) -> 
			{
				BlockState toState = world.getBlockState(to);
				VoxelShape toShape = toState.getCollisionShape(world, to);
				if(toShape == VoxelShapes.fullCube())
					return false;
				else if(toShape.isEmpty())
				{
					BlockState fromState = world.getBlockState(from);
					VoxelShape fromShape = fromState.getCollisionShape(world, from);
					if(fromShape.isEmpty())
						return true;
				}
				
				Vec3d core = new Vec3d(from.getX() + 0.5D, from.getY() + 0.5D, from.getZ() + 0.5D);
				Vec3d coreB = new Vec3d(to.getX() + 0.5D, to.getY() + 0.5D, to.getZ() + 0.5D);
				BlockHitResult hitResult = world.raycast(new RaycastContext(core, coreB, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, entity));
				return hitResult.getType() == HitResult.Type.MISS;
			});
		}
		
		public void doFloodFill(World world, Entity entity)
		{
			List<BlockPos> check = Lists.newArrayList();
			check.add(origin);
			int steps = maxRadius;
			while(!check.isEmpty() && --steps > 0)
			{
				List<BlockPos> nextSet = Lists.newArrayList();
				for(BlockPos point : check)
				{
					posToDist.put(point, steps);
					
					for(BlockPos p : calculateAccessibleNeighbours(point, world, entity).stream().filter(p -> p.isWithinDistance(origin, maxRadius)).toList())
						if(!check.contains(p) && (!posToDist.containsKey(p) || posToDist.get(p) < steps))
						{
							nextSet.removeIf(p::equals);
							nextSet.add(p);
						}
				}
				
				check.clear();
				check.addAll(nextSet);
			}
		}
		
		public FloodFill results()
		{
			FloodFill result = new FloodFill();
			posToDist.entrySet().forEach(e -> result.add(e.getKey(), maxRadius - e.getValue()));
			return result;
		}
		
		public List<BlockPos> calculateAccessibleNeighbours(BlockPos position, World world, Entity entity)
		{
			Vec3d core = new Vec3d(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D);
			List<BlockPos> points = Lists.newArrayList();
			for(Vec3i move : MOVES)
			{
				BlockPos neighbour = position.add(move);
				if(checker.test(neighbour, position, world, entity))
					points.add(neighbour);
				
				Vec3d coreB = new Vec3d(neighbour.getX() + 0.5D, neighbour.getY() + 0.5D, neighbour.getZ() + 0.5D);
				BlockHitResult hitResult = world.raycast(new RaycastContext(core, coreB, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, entity));
				if(hitResult.getType() == HitResult.Type.MISS)
					points.add(neighbour);
			}
			return points;
		}
		
		@FunctionalInterface
		public static interface AccessibilityCheck
		{
			public boolean test(BlockPos to, BlockPos from, World world, Entity entity);
		}
	}
}