package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;

import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class AbilityLoSTeleport extends ActivatedAbility
{
	private static final SoundEvent VWORP_SOUND = SoundEvents.ENTITY_ENDERMAN_TELEPORT;
	private static final ImmutableList<Vec3i> VALID_HORIZONTAL_OFFSETS = ImmutableList.of(
			new Vec3i(0, 0, 0),
			new Vec3i(0, 0, -1), 
			new Vec3i(-1, 0, 0), 
			new Vec3i(0, 0, 1), 
			new Vec3i(1, 0, 0), 
			new Vec3i(-1, 0, -1), 
			new Vec3i(1, 0, -1), 
			new Vec3i(-1, 0, 1), 
			new Vec3i(1, 0, 1));
	private static final ImmutableList<Vec3i> VALID_OFFSETS = (new ImmutableList.Builder<Vec3i>())
			.addAll(VALID_HORIZONTAL_OFFSETS)
			.addAll(VALID_HORIZONTAL_OFFSETS.stream().map(Vec3i::down).iterator())
			.addAll(VALID_HORIZONTAL_OFFSETS.stream().map(Vec3i::up).iterator())
			.add(new Vec3i(0, 1, 0)).build();
	
	public AbilityLoSTeleport(Identifier registryName, Category category)
	{
		super(registryName, category);
		this.soundSettings = new ActivationSoundSettings();
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability",registryName().getPath()+".desc", (int)range(instance.memory())));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 2; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
	{
		return owner.raycast(range(instance.memory()), 1F, true).getType() != HitResult.Type.MISS;
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		double range = range(instance.memory());
		HitResult trace = owner.raycast(range, 1F, true);
		if(trace.getType() == HitResult.Type.MISS)
			return;
		
		Vec3d destination = validateTeleportTarget(trace.getPos(), owner.getWorld(), owner);
		if(destination == null)
			return;
		
		playVworp(owner.getBlockPos(), owner);
		owner.teleport(destination.getX(), destination.getY(), destination.getZ());
		playVworp(owner.getBlockPos(), owner);
	}
	
	private static void playVworp(BlockPos pos, Entity owner)
	{
		VTUtils.playSound(owner, VWORP_SOUND, SoundCategory.PLAYERS, 1F, 1F);
	}
	
	// Finds a safe position to teleport to around the target point
	private static Vec3d validateTeleportTarget(Vec3d target, World world, LivingEntity owner)
	{
		BlockPos pos = new BlockPos((int)target.getX(), (int)target.getY(), (int)target.getZ());
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for(Vec3i offset : VALID_OFFSETS)
		{
			mutable.set(pos).move(offset);
			Vec3d point = Dismounting.findRespawnPos(owner.getType(), world, mutable, true);
			if(point == null) continue;
			return point;
		}
		return new Vec3d(mutable.getX() + 0.5D, mutable.getY(), mutable.getZ() + 0.5D);
	}
	
	public static double range(NbtCompound memory)
	{
		return memory.contains("Range", NbtElement.DOUBLE_TYPE) ? memory.getDouble("Range") : 16D;
	}
}
