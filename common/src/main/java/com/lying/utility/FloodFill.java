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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class FloodFill
{
	private static final Codec<FloodFill> CODEC = Row.CODEC.listOf().xmap(FloodFill::fromRows, FloodFill::toRows);
	
	/** Map of radii to the set of positions at the outer edge at that radius */
	private final Map<Integer, List<BlockPos>> fill = new HashMap<>();
	private final List<BlockPos> 
		allPositions = Lists.newArrayList(),
		extPositions = Lists.newArrayList();
	
	private BlockPos min = null, max = null;
	private int maxRadius = 0;
	
	private static FloodFill fromRows(List<FloodFill.Row> rows)
	{
		FloodFill floodFill = new FloodFill();
		
		rows.forEach(row -> 
			row.points.forEach(p -> 
				floodFill.add(p, row.radius)));
		
		floodFill.calculateExteriors();
		return floodFill;
	}
	
	private List<FloodFill.Row> toRows()
	{
		return fill.entrySet().stream().map(e -> new Row(e.getKey(), e.getValue())).toList();
	}
	
	private void add(BlockPos pos, int dist)
	{
		maxRadius = Math.max(dist, maxRadius);
		recordPosition(pos, dist);
		adjustBounds(pos);
	}
	
	private void recordPosition(BlockPos pos, int dist)
	{
		List<BlockPos> pointsAt = fill.getOrDefault(dist, Lists.newArrayList());
		if(!pointsAt.contains(pos))
		{
			pointsAt.add(pos);
			fill.put(dist, pointsAt);
		}
		
		if(!allPositions.contains(pos))
			allPositions.add(pos);
	}
	
	private void adjustBounds(BlockPos pos)
	{
		if(min == null || max == null)
			min = max = pos;
		else
		{
			if(pos.getX() < min.getX())
				min = new BlockPos(pos.getX(), min.getY(), min.getZ());
			else if(pos.getX() > max.getX())
				max = new BlockPos(pos.getX(), max.getY(), max.getZ());
			
			if(pos.getY() < min.getY())
				min = min.withY(pos.getY());
			else if(pos.getY() > max.getY())
				max = max.withY(pos.getY());
			
			if(pos.getZ() < min.getZ())
				min = new BlockPos(min.getX(), min.getY(), pos.getZ());
			else if(pos.getZ() > max.getZ())
				max = new BlockPos(max.getX(), max.getY(), pos.getZ());
		}
	}
	
	public Box bounds() { return Box.enclosing(min, max); }
	
	public boolean isEmpty(int distance) { return !fill.containsKey(distance) || getAt(distance).isEmpty(); }
	
	public boolean isEmpty() { return allPositions.isEmpty() || fill.isEmpty() || fill.values().stream().allMatch(List::isEmpty); }
	
	public int size() { return allPositions.size(); }
	
	public boolean contains(BlockPos pos)
	{
		return getAll().contains(pos);
	}
	
	/** Returns all positions in the entire cloud */
	public List<BlockPos> getAll() { return allPositions; }
	
	/** Returns all positions at the given distance from the origin point */
	public List<BlockPos> getAt(int distance)
	{
		return fill.getOrDefault(distance, List.of());
	}
	
	/** Returns all positions on the boundary of the cloud */
	public List<BlockPos> getAllExteriors() { return extPositions; }
	
	/** Returns all exterior positions at the given distance */
	public List<BlockPos> getExteriorsAt(int distance)
	{
		List<BlockPos> atDist = getAt(distance);
		return atDist.isEmpty() ? List.of() : extPositions.stream().filter(atDist::contains).toList();
	}
	
	private void calculateExteriors()
	{
		extPositions.clear();
		extPositions.addAll(allPositions.stream().filter(p -> isExteriorPoint(p, allPositions)).toList());
	}
	
	private static boolean isExteriorPoint(BlockPos point, final List<BlockPos> points)
	{
		return List.of(Direction.values()).stream().anyMatch(f -> !points.contains(point.offset(f)));
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
					
					for(BlockPos p : calculateAccessibleNeighbours(point, world, entity).stream().filter(p -> Math.sqrt(p.getSquaredDistance(origin)) <= maxRadius).toList())
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