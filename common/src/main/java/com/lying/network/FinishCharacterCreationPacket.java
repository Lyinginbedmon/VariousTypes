package com.lying.network;

import java.util.List;

import com.google.common.collect.Lists;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class FinishCharacterCreationPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.FINISH_CHARACTER_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::fromBuffer);
	
	public static void send(Identifier speciesId, List<Identifier> templateIds)
	{
		NetworkManager.sendToServer(new Payload(speciesId, templateIds));
	}
	
	public static record Payload(Identifier speciesId, List<Identifier> templateIds) implements CustomPayload
	{
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeIdentifier(speciesId == null ? new Identifier("debug:no_species") : speciesId);
			
			buffer.writeInt(templateIds.size());
			templateIds.forEach(tem -> buffer.writeIdentifier(tem));
		}
		
		public static Payload fromBuffer(RegistryByteBuf buffer)
		{
			Identifier speciesId = buffer.readIdentifier();
			if(speciesId.equals(new Identifier("debug:no_species")))
				speciesId = null;
			
			List<Identifier> templateIds = Lists.newArrayList();
			final int templateCount = buffer.readInt();
			for(int i=0; i<templateCount; i++)
				templateIds.add(buffer.readIdentifier());
			
			return new Payload(speciesId, templateIds);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
