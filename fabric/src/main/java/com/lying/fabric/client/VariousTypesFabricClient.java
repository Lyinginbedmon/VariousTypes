package com.lying.fabric.client;

import com.lying.client.VariousTypesClient;
import com.lying.client.init.VTModelLayerParts;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class VariousTypesFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    	VariousTypesClient.clientInit();
    	
    	VTModelLayerParts.init((layer,data) -> EntityModelLayerRegistry.register(layer, data));
    }
}
