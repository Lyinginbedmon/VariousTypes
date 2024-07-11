package com.lying.neoforge.client;

import com.lying.client.VariousTypesClient;
import com.lying.reference.Reference;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VariousTypesNeoForgeClient
{
	@SubscribeEvent
	public void setupClient(final FMLClientSetupEvent event)
	{
    	VariousTypesClient.clientInit();
	}
}
