package com.lying.network;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.RegistryByteBuf;

public class ActivateAbilityReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		VariousTypes.getSheet(context.getPlayer()).ifPresent(sheet -> sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).enactActionable(context.getPlayer(), value.readIdentifier()));
	}
}
