package com.lying.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncFatiguePacket
{
	@SuppressWarnings("removal")
	public static void send(ServerPlayerEntity player, float fatigue)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), player.getRegistryManager());
		buffer.writeFloat(fatigue);
		NetworkManager.sendToPlayer(player, VTPacketHandler.SYNC_FATIGUE_ID, buffer);
	}
}
