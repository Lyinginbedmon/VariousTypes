package com.lying.entity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.init.VTEntityTypes;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

public class PortalEntity extends Entity
{
	public static final TrackedData<NbtCompound> DEST	= DataTracker.registerData(PortalEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	public static final TrackedData<Integer> LIFETIME	= DataTracker.registerData(PortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<String> PHASE		= DataTracker.registerData(PortalEntity.class, TrackedDataHandlerRegistry.STRING);
	
	public PortalEntity(EntityType<? extends PortalEntity> type, World world)
	{
		super(type, world);
	}
	
	public static boolean createLinked(ServerWorld from, ServerWorld to, BlockPos fromPos, Optional<BlockPos> toPos, Consumer<PortalEntity> setupFunc)
	{
		BlockPos destination;
		if(toPos.isPresent())
			destination = toPos.get();
		else
		{
			// If we don't have a predefined exit position, calculate it as though spawning a Nether portal
			
			// Translate the starting position according to dimension scaling
			// Then find safest position nearby as the exact destination
			BlockPos translated = Destination.adjustPosition(from, to, fromPos);
			Optional<BlockPos> adjusted = Destination.estimatePortalPosition(to, translated);
			if(adjusted.isEmpty())
			{
				VariousTypes.LOGGER.warn(" = Failed to identify a suitable exit portal position");
				return false;
			}
			else
			{
				toPos = adjusted;
				destination = adjusted.get();
			}
		}
		
		if(destination.getSquaredDistance(fromPos) < 1 && from.getRegistryKey() == to.getRegistryKey())
		{
			VariousTypes.LOGGER.warn(" = Tried to spawn a portal linking to its own position");
			return false;
		}
		
		PortalEntity start = create(from, fromPos, to, toPos, setupFunc);
		if(start == null)
		{
			VariousTypes.LOGGER.error(" = Failed to spawn start portal in pair at {}", fromPos.toShortString());
			return false;
		}
		
		PortalEntity end = create(to, destination, from, Optional.of(fromPos), setupFunc);
		if(end == null)
		{
			VariousTypes.LOGGER.error(" = Failed to spawn end portal in pair at {}", destination.toShortString());
			start.discard();
			return false;
		}
		return true;
	}
	
	@Nullable
	public static PortalEntity create(ServerWorld from, BlockPos fromPos, ServerWorld to, Optional<BlockPos> toPos, Consumer<PortalEntity> setupFunc)
	{
		PortalEntity portal = VTEntityTypes.PORTAL.get().spawn(from, fromPos, SpawnReason.MOB_SUMMONED).setDestination(new Destination(toPos, Optional.of(to.getRegistryKey())));
		if(portal == null)
		{
			VariousTypes.LOGGER.error(" = Failed to spawn start portal in pair");
			return null;
		}
		setupFunc.accept(portal);
		return portal;
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(DEST, new NbtCompound());
		builder.add(LIFETIME, 0);
		builder.add(PHASE, "");
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		setLifetime(nbt.getInt("Lifetime"));
		if(nbt.contains("Phase", NbtElement.STRING_TYPE))
			getDataTracker().set(PHASE, nbt.getString("Phase"));
		if(nbt.contains("Destination", NbtElement.COMPOUND_TYPE))
			getDataTracker().set(DEST, nbt.getCompound("Destination"));
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("Lifetime", getLifetime());
		nbt.putString("Phase", getDataTracker().get(PHASE));
		getDestination().ifPresent(dest -> nbt.put("Destination", dest.toNbt()));
	}
	
	public Optional<Destination> getDestination()
	{
		Destination dest = Destination.fromNbt(getDataTracker().get(DEST));
		return dest == null || dest.isBlank() ? Optional.empty() : Optional.of(dest);
	}
	
	protected PortalEntity setDestination(Destination destIn)
	{
		getDataTracker().set(DEST, (NbtCompound)destIn.toNbt());
		return this;
	}
	
	public Phase getPhase() { return getPhase(0F); }
	
	public Phase getPhase(float partialTicks)
	{
		return Phase.fromAge(getLifetime() + partialTicks);
	}
	
	@Nullable
	private Phase latestPhase()
	{
		return Phase.fromString(getDataTracker().get(PHASE));
	}
	
	private void setPhase(Phase phaseIn)
	{
		getDataTracker().set(PHASE, phaseIn == null ? "" : phaseIn.asString());
	}
	
	public void tick()
	{
		super.tick();
		if(getWorld().isClient())
			return;
		
		updateLifetime();
		updateTeleporting();
	}
	
	private void updateLifetime()
	{
		final Phase oldPhase = latestPhase();
		setLifetime(getLifetime() + 1);
		
		final Phase currentPhase = getPhase();
		if(currentPhase != oldPhase)
		{
			currentPhase.playSound(this::playSound, random);
			currentPhase.doTransitionFX((ServerWorld)getWorld(), getPos().add(0D, getHeight() * 0.5D, 0D), random);
			setPhase(currentPhase);
		}
		else if(currentPhase == Phase.OPEN)
		{
			if(this.random.nextInt(100) == 0)
				playSound(SoundEvents.BLOCK_PORTAL_AMBIENT, 0.5F, random.nextFloat() * 0.4F + 0.8F);
		}
	}
	
	private void updateTeleporting()
	{
		Optional<Destination> dest = getDestination();
		List<Entity> targets = findTeleportTargets();
		if(dest.isEmpty())
		{
			removeForEmpty();
			return;
		}
		else if(targets.isEmpty())
			return;
		
		final GameRules gameRules = getWorld().getGameRules();
		final BiConsumer<Entity,Destination> handler = getPhase() == Phase.OPEN ? (e,d) -> 
		{
			if(!e.hasPortalCooldown() && !d.applyTo(e, this, (ServerWorld)getWorld()))
				VariousTypes.LOGGER.error(" = Teleport of {} failed", e.getDisplayName().getString());
		} : (e,d) -> {};
		
		targets.forEach(e -> 
		{
			handler.accept(e, dest.get());
			e.setPortalCooldown(
					e.getType() != EntityType.PLAYER || !((PlayerEntity)e).isInCreativeMode() ? 
						gameRules.getInt(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY) : 
						gameRules.getInt(GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY) * 10);
		});
	}
	
	public int getLifetime() { return getDataTracker().get(LIFETIME).intValue(); }
	
	public void setLifetime(int age) { getDataTracker().set(LIFETIME, age); }
	
	private List<Entity> findTeleportTargets()
	{
		Box bounds = this.getBoundingBox();
		List<Entity> targets = Lists.newArrayList();
		targets.addAll(getWorld().getEntitiesByClass(LivingEntity.class, bounds, EntityPredicates.VALID_LIVING_ENTITY));
		targets.addAll(getWorld().getEntitiesByClass(ItemEntity.class, bounds, EntityPredicates.VALID_ENTITY));
		return targets;
	}
	
	private void removeForEmpty()
	{
		VariousTypes.LOGGER.warn("Portal entity at {} removed due to having no destination", getBlockPos().toShortString());
		discard();
	}
	
	public PistonBehavior getPistonBehavior() { return PistonBehavior.IGNORE; }
	
	public static record Destination(Optional<BlockPos> pos, Optional<RegistryKey<World>> dimension)
	{
		public static final Codec<Destination> CODEC	= RecordCodecBuilder.create(instance -> instance.group(
				BlockPos.CODEC.optionalFieldOf("Position").forGetter(Destination::pos),
				RegistryKey.createCodec(RegistryKeys.WORLD).optionalFieldOf("Dimension").forGetter(Destination::dimension))
					.apply(instance, Destination::new));
		
		public static BlockPos adjustPosition(ServerWorld from, ServerWorld to, BlockPos start)
		{
			WorldBorder border = to.getWorldBorder();
			double scaling = DimensionType.getCoordinateScaleFactor(from.getDimension(), to.getDimension());
			return border.clamp(start.getX() * scaling, start.getY(), start.getZ() * scaling);
		}
		
		public static Optional<BlockPos> estimatePortalPosition(ServerWorld world, BlockPos pos)
		{
			BlockPos.Mutable estimation = pos.mutableCopy();
			
			WorldBorder worldBorder = world.getWorldBorder();
			if(!worldBorder.contains(estimation))
			{
				BlockPos clamped = worldBorder.clamp(estimation.getX(), estimation.getY(), estimation.getZ());
				estimation.set(clamped.getX(), estimation.getY(), clamped.getZ());
			}
			
			boolean shouldUnload = false;
			final Chunk chunk = world.getChunk(pos);
			final ChunkPos chunkPos = chunk.getPos();
			if(!world.isChunkLoaded(chunkPos.x, chunkPos.z))
			{
				world.getChunkManager().setChunkForced(chunkPos, true);
				shouldUnload = true;
			}
			
			final int heightLimit = Math.min(world.getTopY(), world.getBottomY() + world.getLogicalHeight()) - 1;
			int columnHeight = Math.min(heightLimit, world.getTopY(Heightmap.Type.MOTION_BLOCKING, estimation.getX(), estimation.getZ()));
			
			// If we would be in midair above terrain, start at surface level
			if(estimation.getY() > columnHeight)
				estimation.setY(columnHeight);
			
			// If surface level isn't a valid portal position, search the vicinity for somewhere better
			if(!isValidPortalPos(estimation, world))
				for(BlockPos.Mutable option : BlockPos.Mutable.iterateInSquare(estimation, 16, Direction.EAST, Direction.SOUTH))
				{
					Optional<BlockPos> trial = trialColumn(option, world, chunk, heightLimit, pos.getY());
					if(trial.isPresent())
					{
						estimation = trial.get().mutableCopy();
						break;
					}
				}
			
			if(shouldUnload)
				world.getChunkManager().setChunkForced(chunkPos, false);
			
			return isValidPortalPos(estimation, world) ? Optional.of(estimation.toImmutable()) : Optional.empty();
		}
		
		/** Tests every position in the given column to find the closest viable position to {@link baseY} */
		private static Optional<BlockPos> trialColumn(BlockPos.Mutable option, ServerWorld world, Chunk chunk, int heightLimit, int baseY)
		{
			// Ignore any position outside of the original chunk, as it may not be loaded
			if(world.getChunk(option) != chunk)
				return Optional.empty();
			
			// The actual maximum height within the logical altitude where we can place a portal 
			int columnHeight = Math.min(heightLimit, world.getTopY(Heightmap.Type.MOTION_BLOCKING, option.getX(), option.getZ()));
			
			// Iterate up & down in the column progressively farther from baseY to identify vertically-closest viable position, if any
			for(int i=0; i<(columnHeight - world.getBottomY()); ++i)
			{
				int up = baseY + i;
				if(up <= columnHeight)
				{
					if(isValidPortalPos(option.setY(up), world))
						return Optional.of(option.toImmutable());
				}
				
				int down = baseY - i;
				if(i > 0 && down > world.getBottomY())
				{
					if(isValidPortalPos(option.setY(down), world))
						return Optional.of(option.toImmutable());
				}
			}
			return Optional.empty();
		}
		
		public static boolean isBlockStateValid(BlockPos.Mutable pos, ServerWorld world)
		{
			return isBlockStateValid(world.getBlockState(pos));
		}
		
		public static boolean isBlockStateValid(BlockState blockState)
		{
			return blockState.isReplaceable() && blockState.getFluidState().isEmpty();
		}
		
		@SuppressWarnings("deprecation")
		public static boolean isValidPortalPos(BlockPos pos, ServerWorld world)
		{
			if(!world.getWorldBorder().contains(pos))
				return false;
			
			BlockState state = world.getBlockState(pos);
			if(!isBlockStateValid(state))
				return false;
			
			BlockState up = world.getBlockState(pos.up());
			if(!isBlockStateValid(up))
				return false;
			
			BlockState flooring = world.getBlockState(pos.down());
			return flooring.isSolid() && !isBlockStateValid(flooring);
		}
		
		public MutableText display()
		{
			String position = this.pos.orElse(BlockPos.ORIGIN).toShortString();
			String dimension = this.dimension.orElse(World.OVERWORLD).getValue().toString();
			if(isBlank())
				return Text.translatable("gui.vatypes.portal.blank");
			else if(this.dimension.isPresent() && this.pos.isPresent())
				return Text.translatable("gui.vartypes.portal.dest_and_dim", position, dimension);
			else if(this.dimension.isPresent())
				return Text.translatable("gui.vartypes.portal.dim_only", dimension);
			else
				return Text.translatable("gui.vartypes.portal.dest_only", position);
		}
		
		public boolean isBlank() { return pos.isEmpty() && dimension.isEmpty(); }
		
		public boolean applyTo(Entity ent, PortalEntity portal, ServerWorld world)
		{
			if(isBlank())
			{
				portal.removeForEmpty();
				return false;
			}
			
			ServerWorld destWorld = dimension.isEmpty() ? world : world.getServer().getWorld(dimension.get());
			
			WorldBorder border = destWorld.getWorldBorder();
			BlockPos position = pos.orElse(portal.getBlockPos());
			if(pos.isEmpty())
			{
				// Translate position to target dimension
				double scaling = DimensionType.getCoordinateScaleFactor(world.getDimension(), destWorld.getDimension());
				position = border.clamp(position.getX() * scaling, position.getY(), position.getZ() * scaling);
			}
			else
				position = border.clamp(position.getX(), position.getY(), position.getZ());
			
			return tryTeleportTo(ent, destWorld, position);
		}
		
		private static boolean tryTeleportTo(Entity ent, ServerWorld dimension, BlockPos position)
		{
			Vec3d pos = new Vec3d(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D);
			if(dimension != ent.getEntityWorld())
				return ent.teleport(dimension, pos.getX(), pos.getY(), pos.getZ(), PositionFlag.getFlags(0), ent.getYaw(), ent.getPitch());
			else
			{
				ent.teleport(pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		
		public NbtElement toNbt()
		{
			return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		@Nullable
		public static Destination fromNbt(NbtElement ele)
		{
			return CODEC.parse(NbtOps.INSTANCE, ele).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
	
	public static enum Phase implements StringIdentifiable
	{
		CRACK0("crack_0", glassFX(8), () -> SoundEvents.BLOCK_GLASS_BREAK, 0F),
		CRACK1("crack_1", glassFX(16), () -> SoundEvents.BLOCK_GLASS_BREAK, 15F),
		CRACK2("crack_2", glassFX(24), () -> SoundEvents.BLOCK_GLASS_BREAK, 25F),
		OPEN("portal", openFX(), () -> SoundEvents.BLOCK_END_PORTAL_SPAWN, r -> 0.5F + r.nextFloat() * 0.5F, 30F);
		
		@FunctionalInterface
		private static interface PhaseFX
		{
			public void doFX(ServerWorld world, Vec3d pos, Random rand);
		}
		
		private static final PhaseFX glassFX(int count)
		{
			return (world,pos,random) -> 
			{
				final BlockState state = Blocks.GLASS.getDefaultState();
				for(int i=count;i>0;--i)
				{
					double x = random.nextDouble() - 0.5D;
					double y = random.nextDouble() - 0.5D;
					double z = random.nextDouble() - 0.5D;
					VTUtils.spawnParticles(world, new BlockStateParticleEffect(ParticleTypes.BLOCK, state), pos, new Vec3d(x, y, z).normalize());
				}
			};
		}
		
		private static final PhaseFX openFX()
		{
			return (world,pos,random) -> {};	// TODO Implement portal opening phase FX
		}
		
		private static final List<Phase> SET = List.of(values());
		private static final Comparator<Phase> SORT	= (a,b) -> a.start < b.start ? 1 : a.start > b.start ? -1 : 0;
		
		private final float start;
		private final Identifier spriteID;
		private final Supplier<SoundEvent> sound;
		private final Function<Random, Float> volume;
		private final PhaseFX effect;
		
		private Phase(String nameIn, PhaseFX effectIn, Supplier<SoundEvent> soundIn, float startIn)
		{
			this(nameIn, effectIn, soundIn, r -> 1F, startIn);
		}
		
		private Phase(String nameIn, PhaseFX effectIn, Supplier<SoundEvent> soundIn, Function<Random, Float> volumeIn, float startIn)
		{
			spriteID = Reference.ModInfo.prefix(nameIn);
			effect = effectIn;
			sound = soundIn;
			volume = volumeIn;
			start = startIn;
		}
		
		public String asString() { return name().toLowerCase(); }
		
		@Nullable
		public static Phase fromString(String nameIn)
		{
			return SET.stream().filter(p -> p.asString().equals(nameIn)).findFirst().orElse(null);
		}
		
		public static Phase fromAge(float age)
		{
			return SET.stream().filter(p -> p.start <= age).sorted(SORT).findFirst().get();
		}
		
		public float start() { return start; }
		
		public Identifier texture() { return spriteID; }
		
		public void playSound(SoundPlayer playFunc, Random random)
		{
			playFunc.apply(sound.get(), volume.apply(random), random.nextFloat() * 0.3F);
		}
		
		public void doTransitionFX(ServerWorld world, Vec3d pos, Random rand)
		{
			effect.doFX(world, pos, rand);
		}
		
		@FunctionalInterface
		public static interface SoundPlayer
		{
			public void apply(SoundEvent sound, float volume, float pitch);
		}
	}
}
