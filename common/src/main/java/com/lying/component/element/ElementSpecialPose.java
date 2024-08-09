package com.lying.component.element;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.network.SyncPosePacket;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;

public class ElementSpecialPose implements ISheetElement<Optional<EntityPose>>
{
	private Optional<EntityPose> pose = Optional.empty();
	
	public SheetElement<?> registry() { return VTSheetElements.SPECIAL_POSE; }
	
	public Optional<EntityPose> value() { return pose; }
	
	public void set(@Nullable EntityPose poseIn, @Nullable PlayerEntity owner)
	{
		pose = poseIn == null ? Optional.empty() : Optional.of(poseIn);
		if(owner != null && !owner.getWorld().isClient())
			SyncPosePacket.send((ServerPlayerEntity)owner, poseIn);
	}
	
	public void clear(@Nullable PlayerEntity owner)
	{
		set(null, owner);
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt)
	{
		if(pose.isPresent())
			nbt.putString("Pose", pose.get().name());
		return nbt;
	}
	
	public void readFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("Pose", NbtElement.STRING_TYPE))
			pose = Optional.of(EntityPose.valueOf(nbt.getString("Pose")));
		else
			pose = Optional.empty();
	}
}
