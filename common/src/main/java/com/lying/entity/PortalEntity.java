package com.lying.entity;

import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.lying.init.VTEntityTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;

public class PortalEntity extends Entity
{
	public static final TrackedData<NbtCompound> DEST	= DataTracker.registerData(PortalEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	
	public PortalEntity(EntityType<? extends PortalEntity> type, World world)
	{
		super(type, world);
	}
	
	public static void createLinked(ServerWorld from, ServerWorld to, BlockPos fromPos, Optional<BlockPos> toPos, Consumer<PortalEntity> setupFunc)
	{
		PortalEntity start = VTEntityTypes.PORTAL.get().create(from).setDestination(new Destination(toPos, Optional.of(to.getRegistryKey())));
		start.setPos(fromPos.getX() + 0.5D, fromPos.getY(), fromPos.getZ() + 0.5D);
		setupFunc.accept(start);
		if(!from.spawnEntity(start))
		{
			VariousTypes.LOGGER.error(" = Failed to spawn start portal");
			return;
		}
		
		PortalEntity end = VTEntityTypes.PORTAL.get().create(to).setDestination(new Destination(Optional.of(fromPos), Optional.of(from.getRegistryKey())));
		BlockPos destination = toPos.orElse(Destination.adjustPosition(from, to, fromPos));
		end.setPos(destination.getX() + 0.5D, destination.getY(), destination.getZ() + 0.5D);
		setupFunc.accept(end);
		if(!to.spawnEntity(end))
		{
			VariousTypes.LOGGER.error(" = Failed to spawn end portal");
			start.discard();
			return;
		}
	}
	
	protected void initDataTracker(Builder builder)
	{
		builder.add(DEST, new NbtCompound());
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("Destination", NbtElement.COMPOUND_TYPE))
			getDataTracker().set(DEST, nbt.getCompound("Destination"));
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
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
	
	public void tick()
	{
		super.tick();
		if(getWorld().isClient())
		{
			getWorld().addParticle((ParticleEffect)ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
			return;
		}
		else
		{
			// FIXME Ensure portals work properly as soon as they are spawned
			final int cooldown = getWorld().getGameRules().getInt(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY);
			getDestination().ifPresentOrElse(dest -> 
				getWorld().getEntitiesByClass(LivingEntity.class, getBoundingBox(), EntityPredicates.VALID_LIVING_ENTITY).forEach(ent -> 
				{
					if(!ent.hasPortalCooldown())
					{
						if(dest.applyTo(ent, this, (ServerWorld)getWorld()))
							VariousTypes.LOGGER.info(" # Teleport successful");
						else
							VariousTypes.LOGGER.error(" = Teleport failed");
					}
					
					ent.setPortalCooldown(cooldown);
				}),
				this::removeForEmpty
			);
		}
	}
	
	private void removeForEmpty()
	{
		VariousTypes.LOGGER.error("Portal entity at {} removed due to no destination", getBlockPos().toShortString());
		discard();
	}
	
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
			if(dimension != ent.getEntityWorld())
				return ent.teleport(dimension, position.getX(), position.getY(), position.getZ(), PositionFlag.getFlags(0), ent.getYaw(), ent.getPitch());
			else
			{
				ent.teleport(position.getX(), position.getY(), position.getZ());
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
}
