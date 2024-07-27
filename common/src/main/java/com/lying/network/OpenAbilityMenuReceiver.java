package com.lying.network;

import com.lying.screen.AbilityMenuHandler;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class OpenAbilityMenuReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		MenuRegistry.openMenu((ServerPlayerEntity)context.getPlayer(), new SimpleNamedScreenHandlerFactory((id, playerInv, custom) -> new AbilityMenuHandler(id), context.getPlayer().getDisplayName()));
	}
}
