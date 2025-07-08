package com.lying.neoforge.client;

import com.lying.VariousTypes;
import com.lying.client.VariousTypesClient;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.particle.BiomeLeafParticle;
import com.lying.client.particle.LeafParticle;
import com.lying.client.particle.RageParticle;
import com.lying.client.particle.ShockwaveParticle;
import com.lying.client.particle.TintedLeafParticle;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTParticleTypes;
import com.lying.reference.Reference;

import net.minecraft.client.particle.SweepAttackParticle;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

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
    
    @SubscribeEvent
    private static void registerParticleProviders(RegisterParticleProvidersEvent event)
    {
    	event.registerSpriteSet(VTParticleTypes.SHOCKWAVE.get(), ShockwaveParticle.Factory::new);
    	event.registerSpriteSet(VTParticleTypes.LEAF.get(), LeafParticle.Factory::new);
    	event.registerSpriteSet(VTParticleTypes.BIOME_LEAF.get(), BiomeLeafParticle.Factory::new);
    	event.registerSpriteSet(VTParticleTypes.TINTED_LEAF.get(), TintedLeafParticle.Factory::new);
    	event.registerSpriteSet(VTParticleTypes.RAGE.get(), RageParticle.Factory::new);
    	event.registerSpriteSet(VTParticleTypes.REND.get(), SweepAttackParticle.Factory::new);
    }
}
