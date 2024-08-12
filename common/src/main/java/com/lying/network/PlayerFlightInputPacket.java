package com.lying.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayerFlightInputPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.PLAYER_FLYING_INPUT_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(float forward, float strafe, boolean jump, boolean sneak)
	{
		NetworkManager.sendToServer(new Payload(forward, strafe, jump, sneak));
	}
	
	public static record Payload(float forward, float strafing, boolean jumping, boolean sneaking) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeFloat(forward);
			buffer.writeFloat(strafing);
			buffer.writeBoolean(jumping);
			buffer.writeBoolean(sneaking);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
