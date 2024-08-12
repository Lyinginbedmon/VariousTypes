package com.lying.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ActivateAbilityPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.ACTIVATE_ABILITY_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(Identifier mapName)
	{
		NetworkManager.sendToServer(new Payload(mapName));
	}
	
	public static record Payload(Identifier mapName) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readIdentifier());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeIdentifier(mapName);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
