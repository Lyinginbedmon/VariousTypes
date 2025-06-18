package com.lying.fabric.client;

import com.lying.client.VariousTypesClient;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.particle.ShockwaveParticle;
import com.lying.init.VTParticleTypes;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public final class VariousTypesFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    	VariousTypesClient.clientInit();
    	
    	registerParticleFactories();
    	VTModelLayerParts.init((layer,data) -> EntityModelLayerRegistry.register(layer, data));
    }
	
	private static void registerParticleFactories()
	{
		ParticleFactoryRegistry.getInstance().register(VTParticleTypes.SHOCKWAVE.get(), ShockwaveParticle.Factory::new);
	}
}
