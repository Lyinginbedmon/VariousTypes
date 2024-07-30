package com.lying.client.network;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;

public class SyncActionablesReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		NbtCompound comp = value.readNbt();
		NbtList actionables = comp.getList("Abilities", NbtElement.COMPOUND_TYPE);
		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			ElementActionables element = sheet.element(VTSheetElements.ACTIONABLES);
			AbilitySet.readFromNbt(actionables).abilities().forEach(inst -> element.set(inst));
		});
	}
}
