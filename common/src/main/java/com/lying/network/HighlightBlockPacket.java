package com.lying.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class HighlightBlockPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.HIGHLIGHT_BLOCK_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(ServerPlayerEntity player, BlockPos position)
	{
		NetworkManager.sendToPlayer(player, new Payload(position));
	}
	
	// FIXME Adapt to accept multiple positions at once
	public static record Payload(BlockPos pos) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readBlockPos());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeBlockPos(pos);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
