package com.lying.client.network;

import org.jetbrains.annotations.Nullable;

import com.lying.network.VTPacketHandler;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.util.Identifier;

public class SetFavouriteAbilityPacket
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	@SuppressWarnings("removal")
	public static void send(int slot, @Nullable Identifier mapName)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager());
		buffer.writeInt(slot);
		buffer.writeBoolean(mapName != null);
		if(mapName != null)
			buffer.writeIdentifier(mapName);
		NetworkManager.sendToServer(VTPacketHandler.SET_FAVOURITE_ABILITY_ID, buffer);
	}
}
