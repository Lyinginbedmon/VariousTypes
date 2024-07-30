package com.lying.client.network;

import com.lying.network.VTPacketHandler;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.util.Identifier;

public class ActivateAbilityPacket
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	@SuppressWarnings("removal")
	public static void send(Identifier mapName)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager());
		buffer.writeIdentifier(mapName);
		NetworkManager.sendToServer(VTPacketHandler.ACTIVATE_ABILITY_ID, buffer);
	}
}
