package com.lying.network;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SetFavouriteAbilityReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
		
		int slot = value.readInt();
		boolean shouldSet = value.readBoolean();
		Identifier map = shouldSet ? value.readIdentifier() : null;
		
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			if(sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).setFavourite(slot, shouldSet ? map : null))
				sheet.markDirty();
		});
	}
}
