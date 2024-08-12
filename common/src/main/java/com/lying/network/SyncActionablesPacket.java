package com.lying.network;

import com.lying.ability.AbilitySet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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
	
	public static void send(ServerPlayerEntity player, AbilitySet actionables)
	{
		NetworkManager.sendToPlayer(player, new Payload(actionables));
	}
	
	public static record Payload(AbilitySet actionables) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(AbilitySet.readFromNbt(buffer.readNbt().getList("Abilities", NbtElement.COMPOUND_TYPE)));
		}
		
		public void write(RegistryByteBuf buffer)
		{
			NbtCompound compound = new NbtCompound();
			compound.put("Abilities", actionables.writeToNbt());
			buffer.writeNbt(compound);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
