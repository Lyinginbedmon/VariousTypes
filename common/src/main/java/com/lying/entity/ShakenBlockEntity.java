package com.lying.entity;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.lying.init.VTEntityTypes;

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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class ShakenBlockEntity extends Entity
{
	protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(ShakenBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
	protected static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(ShakenBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);
	
	public int timeFalling;
	public boolean dropItem = true;
	protected boolean destroyedOnLanding;
	protected boolean hurtEntities;
	protected int fallHurtMax = 40;
	protected float fallHurtAmount;
	@Nullable
	public NbtCompound blockEntityData;
	
	public ShakenBlockEntity(EntityType<? extends ShakenBlockEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public ShakenBlockEntity(World world, double x, double y, double z, BlockState block)
	{
		super(VTEntityTypes.SHAKEN_BLOCK.get(), world);
		this.setBlockState(block);
		this.setShakenBlockPos(this.getBlockPos());
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(0D, 0.6D, 0D);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}
	
	public static ShakenBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state)
	{
		ShakenBlockEntity blockEntity = new ShakenBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state.contains(Properties.WATERLOGGED) ? (BlockState)state.with(Properties.WATERLOGGED, false) : state);
		world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
		world.spawnEntity(blockEntity);
		return blockEntity;
	}
	
	protected void initDataTracker(DataTracker.Builder builder)
	{
		builder.add(BLOCK_STATE, Blocks.SAND.getDefaultState());
		builder.add(BLOCK_POS, BlockPos.ORIGIN);
	}
	
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.put("BlockState", NbtHelper.fromBlockState(getBlockState()));
		nbt.putInt("Time", this.timeFalling);
		nbt.putBoolean("DropItem", this.dropItem);
		nbt.putBoolean("HurtEntities", this.hurtEntities);
		nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
		nbt.putInt("FallHurtMax", this.fallHurtMax);
		if(this.blockEntityData != null)
			nbt.put("TileEntityData", this.blockEntityData);
		nbt.putBoolean("CancelDrop", this.destroyedOnLanding);
	}
	
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		this.setBlockState(NbtHelper.toBlockState(getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState")));
		this.timeFalling = nbt.getInt("Time");
		if(nbt.contains("HurtEntities", NbtElement.NUMBER_TYPE))
		{
			this.hurtEntities = nbt.getBoolean("HurtEntities");
			this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
			this.fallHurtMax = nbt.getInt("FallHurtMax");
		}
		else if(getBlockState().isIn(BlockTags.ANVIL))
			this.hurtEntities = true;
		
		if (nbt.contains("DropItem", NbtElement.NUMBER_TYPE))
			this.dropItem = nbt.getBoolean("DropItem");
		
		if (nbt.contains("TileEntityData", NbtElement.COMPOUND_TYPE))
			this.blockEntityData = nbt.getCompound("TileEntityData").copy();
		
		this.destroyedOnLanding = nbt.getBoolean("CancelDrop");
		
		if(getBlockState().isAir())
			setBlockState(Blocks.SAND.getDefaultState());
	}
	
	public FallingBlockEntity toFallingBlockEntity()
	{
		FallingBlockEntity falling = EntityType.FALLING_BLOCK.create(getWorld());
		falling.readNbt(this.writeNbt(new NbtCompound()));
		return falling;
	}
	
	protected Text getDefaultName() { return Text.translatable("entity.minecraft.falling_block_type", getBlockState().getBlock().getName()); }
	
	public boolean isAttackable() { return false; }
	
	public boolean canHit() { return !this.isRemoved(); }
	
	protected double getGravity() { return 0.04D; }
	
	public boolean isCollidable() { return true; }
	
	public void setBlockState(BlockState state) { this.getDataTracker().set(BLOCK_STATE, state); }
	
	public BlockState getBlockState() { return getDataTracker().get(BLOCK_STATE); }
	
	public void setShakenBlockPos(BlockPos pos) { this.getDataTracker().set(BLOCK_POS, pos); }
	
	public BlockPos getShakenBlockPos() { return getDataTracker().get(BLOCK_POS); }
	
	protected Entity.MoveEffect getMoveEffect() { return Entity.MoveEffect.NONE; }
	
	public boolean entityDataRequiresOperator() { return true; }
	
	public void tick()
	{
		if(getBlockState().isAir())
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
	
	protected void clientTick()
	{
		Random rand = getWorld().getRandom();
		if(rand.nextInt(5) > 0)
			return;
		
		getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, getBlockState()), getX(), getY() + 0.5D, getZ(), 0, 0, 0);
	}
	
	@SuppressWarnings("resource")
	protected void serverTick()
	{
		ServerWorld world = (ServerWorld)getWorld();
		BlockState blockState = getBlockState();
		Block block = blockState.getBlock();
		BlockPos blockPos = this.getBlockPos();
		boolean isPowder = blockState.getBlock() instanceof ConcretePowderBlock;
		boolean isWetPowder = isPowder && world.getFluidState(blockPos).isIn(FluidTags.WATER);
		
		BlockHitResult blockHitResult;
		if(
				isPowder && 
				getVelocity().lengthSquared() > 1.0 && 
				(blockHitResult = world.raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this))).getType() != HitResult.Type.MISS && 
				world.getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER))
		{
			blockPos = blockHitResult.getBlockPos();
			isWetPowder = true;
		}
		
		if(this.isOnGround() || isWetPowder)
		{
			BlockState blockStateAt = world.getBlockState(blockPos);
			this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
			if(!blockStateAt.isOf(Blocks.MOVING_PISTON))
			{
				if(!this.destroyedOnLanding)
				{
					boolean canReplace = blockStateAt.canReplace(new AutomaticItemPlacementContext(world, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
					boolean canFallThrough = FallingBlock.canFallThrough(world.getBlockState(blockPos.down())) && (!isPowder || !isWetPowder);
					boolean canPlace = blockState.canPlaceAt(world, blockPos) && !canFallThrough;
					if (canReplace && canPlace) {
						if(blockState.contains(Properties.WATERLOGGED) && world.getFluidState(blockPos).getFluid() == Fluids.WATER)
							setBlockState((BlockState)blockState.with(Properties.WATERLOGGED, true));
						
						if(world.setBlockState(blockPos, blockState, 3))
						{
							BlockEntity blockEntity;
							world.getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, world.getBlockState(blockPos)));
							this.discard();
							if(blockStateAt instanceof LandingBlock)
								((LandingBlock)((Object)blockStateAt)).onLanding(world, blockPos, blockState, blockStateAt, this.toFallingBlockEntity());
							
							if(this.blockEntityData != null && blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(blockPos)) != null)
							{
								NbtCompound nbtCompound = blockEntity.createNbt(world.getRegistryManager());
								for (String string : this.blockEntityData.getKeys())
									nbtCompound.put(string, this.blockEntityData.get(string).copy());
								
								try
								{
									blockEntity.read(nbtCompound, world.getRegistryManager());
								}
								catch (Exception exception) { VariousTypes.LOGGER.error("Failed to load block entity from falling block", exception); }
								blockEntity.markDirty();
							}
						}
						else if (this.dropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						{
							this.discard();
							this.onDestroyedOnLanding(block, blockPos);
							this.dropItem(block);
						}
					}
					else
					{
						this.discard();
						if (this.dropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						{
							this.onDestroyedOnLanding(block, blockPos);
							this.dropItem(block);
						}
					}
				}
				else
				{
					this.discard();
					this.onDestroyedOnLanding(block, blockPos);
				}
			}
		}
		else if(!((this.timeFalling <= 100 || blockPos.getY() > world.getBottomY() && blockPos.getY() <= world.getTopY()) && this.timeFalling <= 600))
		{
			if(this.dropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
				this.dropItem(block);
			this.discard();
		}
	}
	
	public void onDestroyedOnLanding(Block block, BlockPos pos)
	{
		if(block instanceof LandingBlock)
			((LandingBlock)block).onDestroyedOnLanding(getWorld(), pos, this.toFallingBlockEntity());
	}
	
	public Packet<ClientPlayPacketListener> createSpawnPacket() { return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(getBlockState())); }
	
	public void onSpawnPacket(EntitySpawnS2CPacket packet)
	{
		super.onSpawnPacket(packet);
		setBlockState(Block.getStateFromRawId(packet.getEntityData()));
		this.intersectionChecked = true;
		this.setPosition(packet.getX(), packet.getY(), packet.getZ());
		this.setShakenBlockPos(this.getBlockPos());
	}
}
