package com.lying.client.network;

import com.lying.VariousTypes;
import com.lying.component.element.ElementSpecialPose;
import com.lying.init.VTSheetElements;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.RegistryByteBuf;

public class SyncPoseReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		boolean isSet = value.readBoolean();
		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			ElementSpecialPose specialPose = sheet.element(VTSheetElements.SPECIAL_POSE);
			if(isSet)
			{
				specialPose.set(value.readEnumConstant(EntityPose.class), null);
				mc.player.setPose(specialPose.value().get());
			}
			else
			{
				specialPose.clear(null);
				mc.player.setPose(EntityPose.STANDING);
			}
		});
	}
}
