package com.lying.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.lying.init.VTEntityTypes;
import com.lying.reference.Reference;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.LandingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * Essentially a re-implementation of FallingBlockEntity, but made to be more easily extendable or adjustable
 */
public class ThrownBlockEntity extends Entity
{
	protected static final TrackedData<BlockState> BLOCK_STATE			= DataTracker.registerData(ThrownBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);
	protected static final TrackedData<NbtCompound> BLOCK_ENTITY_DATA	= DataTracker.registerData(ThrownBlockEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
	protected static final TrackedData<Optional<UUID>> OWNER_UUID		= DataTracker.registerData(ThrownBlockEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	
	public int timeFalling = 0;
	protected boolean isCollidable = false;
	protected boolean sticky = false;
	protected boolean dropItem = true;
	protected boolean destroyedOnLanding;
	protected boolean hurtEntities = false;
	protected int fallHurtMax = 40;
	protected float fallHurtAmount;
	@Nullable
	public NbtCompound blockEntityData;
	
	public ThrownBlockEntity(EntityType<? extends ThrownBlockEntity> entityType, World world)
	{
		super(VTEntityTypes.THROWN_BLOCK.get(), world);
	}
	
	public ThrownBlockEntity(World world, Vec3d pos, BlockState state)
	{
		this(VTEntityTypes.THROWN_BLOCK.get(), world);
		setBlockState(state);
		this.intersectionChecked = true;
		setPos(pos.x, pos.y, pos.z);
		setVelocity(Vec3d.ZERO);
		this.prevX = pos.x;
		this.prevY = pos.y;
		this.prevZ = pos.z;
	}
	
	protected void initDataTracker(DataTracker.Builder builder)
	{
		builder.add(BLOCK_STATE, defaultBlock());
		builder.add(BLOCK_ENTITY_DATA, new NbtCompound());
		builder.add(OWNER_UUID, Optional.empty());
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.put("BlockState", NbtHelper.fromBlockState(blockState()));
		if(hasBlockEntityData())
			nbt.put("TileEntityData", blockEntityData());
		if(hasOwner())
			nbt.putUuid("OwnerID", getDataTracker().get(OWNER_UUID).get());
		nbt.putInt("Time", this.timeFalling);
		nbt.putBoolean("DropItem", this.dropItem);
		nbt.putBoolean("HurtEntities", this.hurtEntities);
		nbt.putBoolean("CancelDrop", this.destroyedOnLanding);
		nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
		nbt.putInt("FallHurtMax", this.fallHurtMax);
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		setBlockState(NbtHelper.toBlockState(getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState")));
		if(blockState().isAir())
			setBlockState(defaultBlock());
		if(nbt.contains("TileEntityData", NbtElement.COMPOUND_TYPE))
			setBlockEntityData(nbt.getCompound("TileEntityData"));
		if(nbt.contains("OwnerID", NbtElement.INT_ARRAY_TYPE))
			getDataTracker().set(OWNER_UUID, Optional.of(nbt.getUuid("OwnerID")));
		this.timeFalling = nbt.getInt("Time");
		this.destroyedOnLanding = nbt.getBoolean("CancelDrop");
		this.dropItem = nbt.getBoolean("DropItem");
		if(nbt.contains("HurtEntities", 99))
		{
			this.hurtEntities = nbt.getBoolean("HurtEntities");
			this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
			this.fallHurtMax = nbt.getInt("FallHurtMax");
		}
		else if(blockState().isIn(BlockTags.ANVIL))
			this.hurtEntities = true;
	}
	
	public BlockState defaultBlock() { return Blocks.COBWEB.getDefaultState(); }
	
	public BlockState blockState() { return getDataTracker().get(BLOCK_STATE); }
	
	public void setBlockState(BlockState state) { getDataTracker().set(BLOCK_STATE, state); }
	
	public boolean hasBlockEntityData() { return blockEntityData() != null && !blockEntityData().isEmpty(); }
	
	@Nullable
	public NbtCompound blockEntityData() { return getDataTracker().get(BLOCK_ENTITY_DATA); }
	
	public void setBlockEntityData(NbtCompound nbt) { getDataTracker().set(BLOCK_ENTITY_DATA, nbt); }
	
	public boolean hasOwner() { return getDataTracker().get(OWNER_UUID).isPresent(); }
	
	public boolean isOwner(Entity ent) { return hasOwner() && ent.getUuid().equals(getDataTracker().get(OWNER_UUID).get()); }
	
	public void setOwner(Entity ent) { getDataTracker().set(OWNER_UUID, Optional.of(ent.getUuid())); }
	
	public ThrownBlockEntity hurtEntities(boolean bool) { this.hurtEntities = bool; return this; }
	
	public ThrownBlockEntity dropItem(boolean bool) { this.dropItem = bool; return this; }
	
	public ThrownBlockEntity destroyOnLanding(boolean bool) { this.destroyedOnLanding = bool; return this; }
	
	public ThrownBlockEntity sticky(boolean bool) { this.sticky = bool; return this; }
	
	public boolean isAttackable() { return false; }
	
	public boolean isCollidable() { return this.isCollidable; }
	
	protected Entity.MoveEffect getMoveEffect() { return Entity.MoveEffect.NONE; }
	
	public boolean canHit() { return !isRemoved(); }
	
	protected double getGravity() { return 0.04D; }
	
	public boolean doesRenderOnFire() { return false; }
	
	public void populateCrashReport(CrashReportSection section)
	{
		super.populateCrashReport(section);
		section.add("Imitating BlockState", blockState().toString());
	}
	
	protected Text getDefaultName() { return Reference.ModInfo.translate("entity", "thrown_block_type", blockState().getBlock().getName()); }
	
	public boolean entityDataRequiresOperator() { return true; }
	
	public void tick()
	{
		if(blockState().isAir())
		{
			this.discard();
			return;
		}
		++this.timeFalling;
		this.applyGravity();
		this.move(MovementType.SELF, getVelocity());
		if(getWorld().isClient())
			clientTick();
		else
			serverTick();
		setVelocity(getVelocity().multiply(0.98D));
	}
	
	@SuppressWarnings("resource")
	public void serverTick()
	{
		BlockState blockState = blockState();
		Block block = blockState.getBlock();
		World world = getWorld();
		BlockHitResult hitResult;
		BlockPos blockPos = getBlockPos();
		boolean isConcrete = block instanceof ConcretePowderBlock;
		boolean isWetConcrete = isConcrete && world.getFluidState(blockPos).isIn(FluidTags.WATER);
		double vel = getVelocity().lengthSquared();
		if(
				isConcrete && 
				vel > 1D && 
				(hitResult = world.raycast(new RaycastContext(new Vec3d(prevX, prevY, prevZ), getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this))).getType() != HitResult.Type.MISS && 
				world.getFluidState(hitResult.getBlockPos()).isIn(FluidTags.WATER))
		{
			blockPos = hitResult.getBlockPos();
			isWetConcrete = true;
		}
		if(isOnGround() || isWetConcrete || shouldStick())
		{
			BlockState stateHere = world.getBlockState(blockPos);
			setVelocity(getVelocity().multiply(0.7D, -0.5D, 0.7D));
			if(!stateHere.isOf(Blocks.MOVING_PISTON))
			{
				if(this.destroyedOnLanding)
				{
					discard();
					onDestroyedOnLanding(blockPos);
				}
				else
				{
					boolean canReplace = stateHere.canReplace(new AutomaticItemPlacementContext(world, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
					boolean canFallThrough = FallingBlock.canFallThrough(world.getBlockState(blockPos.down())) && (!isConcrete || !isWetConcrete);
					boolean canPlace = blockState.canPlaceAt(world, blockPos) && (!canFallThrough || sticky);
					if(canReplace && canPlace)
					{
						if(blockState.contains(Properties.WATERLOGGED) && world.getFluidState(blockPos).getFluid() == Fluids.WATER)
							setBlockState(blockState.with(Properties.WATERLOGGED, true));
						if(world.setBlockState(blockPos, blockState, 3))
						{
							((ServerWorld)world).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, world.getBlockState(blockPos)));
							discard();
							onLanding(blockPos, blockState, stateHere);
							BlockEntity blockEntity;
							if(hasBlockEntityData() && blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(blockPos)) != null)
							{
								NbtCompound nbt = blockEntity.createNbt(world.getRegistryManager());
								for(String str : blockEntityData().getKeys())
									nbt.put(str, blockEntityData().get(str));
								try
								{
									blockEntity.read(nbt, world.getRegistryManager());
								}
								catch(Exception e) { VariousTypes.LOGGER.error("Failed to load block entity from thrown block", e); }
								blockEntity.markDirty();
							}
						}
						else if(tryDropItem(blockPos))
							discard();
					}
					else
					{
						discard();
						tryDropItem(blockPos);
					}
				}
			}
		}
	}
	
	public boolean shouldStick() { return sticky && (horizontalCollision || verticalCollision); }
	
	protected void clientTick() { }
	
	protected boolean tryDropItem(BlockPos blockPos)
	{
		if(!this.dropItem || !getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
			return false;
		
		onDestroyedOnLanding(blockPos);
		dropItem(blockState().getBlock());
		return true;
	}
	
	protected boolean isLandingBlock() { return blockState().getBlock() instanceof LandingBlock; }
	
	public FallingBlockEntity toFallingBlockEntity()
	{
		FallingBlockEntity falling = EntityType.FALLING_BLOCK.create(getWorld());
		falling.readNbt(this.writeNbt(new NbtCompound()));
		return falling;
	}
	
	public void onLanding(BlockPos blockPos, BlockState state, BlockState stateHere)
	{
		if(!isLandingBlock()) return;
		((LandingBlock)state.getBlock()).onLanding(this.getWorld(), blockPos, state, stateHere, toFallingBlockEntity());
	}
	
	public void onDestroyedOnLanding(BlockPos pos)
	{
		if(!isLandingBlock()) return;
		((LandingBlock)blockState().getBlock()).onDestroyedOnLanding(getWorld(), pos, toFallingBlockEntity());
	}
	
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source)
	{
		DamageSource fallDamage;
		if(!this.hurtEntities)
			return false;
		int i = MathHelper.ceil(fallDistance - 1F);
		if(i < 0)
			return false;
		Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(EntityPredicates.VALID_LIVING_ENTITY).and(entity -> !isOwner(entity));
		Block block = blockState().getBlock();
		if(block instanceof LandingBlock)
		{
			LandingBlock landingBlock = (LandingBlock)blockState().getBlock();
			fallDamage = landingBlock.getDamageSource(this);
		}
		else
			fallDamage = getDamageSources().fallingBlock(this);
		DamageSource fallDamage2 = fallDamage;
		float f = Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax);
		getWorld().getOtherEntities(this, getBoundingBox(), predicate).forEach(entity -> entity.damage(fallDamage2, f));
		boolean isAnvil = blockState().isIn(BlockTags.ANVIL);
		if(isAnvil && f > 0F && random.nextFloat() < 0.05F + (float)i * 0.05F)
		{
			BlockState blockState = AnvilBlock.getLandingState(blockState());
			if(blockState == null)
				this.destroyedOnLanding = true;
			else
				setBlockState(blockState);
		}
		return false;
	}
}
