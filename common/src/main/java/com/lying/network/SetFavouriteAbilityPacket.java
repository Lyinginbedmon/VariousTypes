package com.lying.network;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class SetFavouriteAbilityPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.SET_FAVOURITE_ABILITY_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(int slot, @Nullable Identifier mapName)
	{
		NetworkManager.sendToServer(new Payload(slot, mapName == null ? Optional.empty() : Optional.of(mapName)));
	}
	
	public static record Payload(int slot, Optional<Identifier> contents) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(buffer.readInt(), buffer.readBoolean() ? Optional.of(buffer.readIdentifier()) : Optional.empty());
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeInt(slot);
			buffer.writeBoolean(contents.isPresent());
			if(contents.isPresent())
				buffer.writeIdentifier(contents.get());
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
