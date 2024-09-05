package com.lying.network;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.utility.BlockHighlight;
import com.mojang.serialization.Codec;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HighlightBlockPacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.HIGHLIGHT_BLOCK_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static void send(ServerPlayerEntity player, BlockHighlight position)
	{
		send(player, List.of(position));
	}
	
	public static void send(ServerPlayerEntity player, List<BlockHighlight> highlights)
	{
		NetworkManager.sendToPlayer(player, new Payload(highlights));
	}
	
	public static record Payload(List<BlockHighlight> highlights) implements CustomPayload
	{
		private static final Codec<List<BlockHighlight>> CODEC = BlockHighlight.CODEC.listOf();
		
		public Payload(RegistryByteBuf buffer)
		{
			this(nbtToList(buffer.readNbt().getList("List", NbtElement.COMPOUND_TYPE)));
		}
		
		public void write(RegistryByteBuf buffer)
		{
			NbtCompound nbt = new NbtCompound();
			nbt.put("List", CODEC.encodeStart(NbtOps.INSTANCE, highlights).getOrThrow());
			buffer.writeNbt(nbt);
		}
		
		private static List<BlockHighlight> nbtToList(NbtElement nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(Lists.newArrayList());
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
