package com.lying.network;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncPosePacket
{
	public static void sendSync(ServerPlayerEntity player, Optional<EntityPose> pose)
	{
		send(player, pose.isPresent() ? pose.get() : null);
	}
	
	@SuppressWarnings("removal")
	public static void send(ServerPlayerEntity player, @Nullable EntityPose pose)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), player.getRegistryManager());
		buffer.writeBoolean(pose != null);
		if(pose != null)
			buffer.writeEnumConstant(pose);
		NetworkManager.sendToPlayer(player, VTPacketHandler.SYNC_POSE_ID, buffer);
	}
}
