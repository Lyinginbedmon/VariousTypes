package com.lying.network;

import com.lying.ability.AbilitySet;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncActionablesPacket
{
	@SuppressWarnings("removal")
	public static void send(ServerPlayerEntity player, AbilitySet actionables)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), player.getRegistryManager());
		NbtCompound comp = new NbtCompound();
		comp.put("Abilities", actionables.writeToNbt());
		buffer.writeNbt(comp);
		NetworkManager.sendToPlayer(player, VTPacketHandler.SYNC_ACTIONABLES_ID, buffer);
	}
}
