package com.lying.network;

import com.lying.ability.ActivatedAbility;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.RegistryByteBuf;

public class ActivateAbilityReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		ActivatedAbility.tryTriggerAbility(context.getPlayer(), value.readIdentifier());
	}
}
