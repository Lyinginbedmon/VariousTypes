package com.lying.fabric.client;

import com.lying.client.VariousTypesClient;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.particle.LeafParticle;
import com.lying.client.particle.RageParticle;
import com.lying.client.particle.ShockwaveParticle;
import com.lying.client.particle.TintedLeafParticle;
import com.lying.init.VTParticleTypes;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SweepAttackParticle;

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
		ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
		registry.register(VTParticleTypes.SHOCKWAVE.get(), ShockwaveParticle.Factory::new);
		registry.register(VTParticleTypes.LEAF.get(), LeafParticle.Factory::new);
		registry.register(VTParticleTypes.TINTED_LEAF.get(), TintedLeafParticle.Factory::new);
		registry.register(VTParticleTypes.RAGE.get(), RageParticle.Factory::new);
		registry.register(VTParticleTypes.REND.get(), SweepAttackParticle.Factory::new);
	}
}
