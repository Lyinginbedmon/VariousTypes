package com.lying.client.network;

import com.lying.network.VTPacketHandler;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;

public class OpenAbilityMenuPacket
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	@SuppressWarnings("removal")
	public static void send()
	{
		NetworkManager.sendToServer(VTPacketHandler.OPEN_ABILITY_MENU_ID, new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager()));
	}
}
