package com.lying.network;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncPosePacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.SYNC_POSE_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void sendSync(ServerPlayerEntity player, Optional<EntityPose> pose)
	{
		send(player, pose.isPresent() ? pose.get() : null);
	}
	
	public static void send(ServerPlayerEntity player, @Nullable EntityPose pose)
	{
		NetworkManager.sendToPlayer(player, new Payload(pose == null ? Optional.empty() : Optional.of(pose)));
	}
	
	public static record Payload(Optional<EntityPose> pose) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readBoolean() ? Optional.of(buffer.readEnumConstant(EntityPose.class)) : Optional.empty());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeBoolean(pose.isPresent());
			if(pose.isPresent())
				buffer.writeEnumConstant(pose.get());
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
