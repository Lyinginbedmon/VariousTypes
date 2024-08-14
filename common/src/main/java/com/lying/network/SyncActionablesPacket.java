package com.lying.network;

import com.lying.component.element.ElementActionables;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncActionablesPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.SYNC_ACTIONABLES_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(ServerPlayerEntity player, ElementActionables actionables)
	{
		NetworkManager.sendToPlayer(player, new Payload(actionables));
	}
	
	public static record Payload(ElementActionables actionables) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(ElementActionables.buildFromNbt(buffer.readNbt()));
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeNbt(actionables.storeNbt());
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
