package com.lying.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncFatiguePacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.SYNC_FATIGUE_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(ServerPlayerEntity player, float fatigue)
	{
		NetworkManager.sendToPlayer(player, new Payload(fatigue));
	}
	
	public static record Payload(float fatigue) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readFloat());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeFloat(fatigue);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
