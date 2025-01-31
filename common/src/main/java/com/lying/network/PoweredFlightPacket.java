package com.lying.network;

import java.util.UUID;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PoweredFlightPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.POWERED_FLIGHT_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(Iterable<ServerPlayerEntity> players, UUID playerID, boolean powered)
	{
		NetworkManager.sendToPlayers(players, new Payload(playerID, powered));
	}
	
	public static record Payload(UUID playerID, boolean powered) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readUuid(), buffer.readBoolean());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeUuid(playerID);
			buffer.writeBoolean(powered);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
