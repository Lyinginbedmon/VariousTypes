package com.lying.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenAbilityMenuPacket() implements CustomPayload
{
	private static final Identifier PACKET_ID = VTPacketHandler.OPEN_ABILITY_MENU_ID;
	public static final CustomPayload.Id<OpenAbilityMenuPacket> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, OpenAbilityMenuPacket> PACKET_CODEC = CustomPayload.codecOf(OpenAbilityMenuPacket::write, OpenAbilityMenuPacket::new);
	
	public static void send()
	{
		NetworkManager.sendToServer(new OpenAbilityMenuPacket());
	}
	
	public OpenAbilityMenuPacket(RegistryByteBuf buffer) { this(); }
	
	public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	
	public void write(RegistryByteBuf buffer) { }
}
