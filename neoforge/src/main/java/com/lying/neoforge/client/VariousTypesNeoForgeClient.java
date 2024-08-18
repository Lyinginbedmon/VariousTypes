package com.lying.neoforge.client;

import com.lying.VariousTypes;
import com.lying.client.VariousTypesClient;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.init.VTEntityTypes;
import com.lying.reference.Reference;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VariousTypesNeoForgeClient
{
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event)
	{
    	VariousTypesClient.clientInit();
	}
	
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		VariousTypes.LOGGER.info("Entity renderer registration");
		event.registerEntityRenderer(VTEntityTypes.ANIMATED_PLAYER.get(), AnimatedPlayerEntityRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		VariousTypes.LOGGER.info("Entity layer registration");
		VTModelLayerParts.init((layer,data) -> event.registerLayerDefinition(layer, data));
	}
}
