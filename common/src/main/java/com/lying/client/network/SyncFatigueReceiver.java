package com.lying.client.network;

import com.lying.VariousTypes;
import com.lying.component.element.ElementNonLethal;
import com.lying.init.VTSheetElements;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;

public class SyncFatigueReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		float fatigue = value.readFloat();
		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
			nonlethal.set(fatigue);
		});
	}
}
