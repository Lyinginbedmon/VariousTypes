package com.lying.neoforge.client;

import com.lying.client.VariousTypesClient;
import com.lying.init.VTEntityTypes;
import com.lying.reference.Reference;

import net.minecraft.entity.player.PlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VariousTypesNeoForgeClient
{
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event)
	{
    	VariousTypesClient.clientInit();
	}
	
	public void registerEntityAttributes(final EntityAttributeCreationEvent event)
	{
		event.put(VTEntityTypes.ANIMATED_PLAYER.get(), PlayerEntity.createPlayerAttributes().build());
	}
}
