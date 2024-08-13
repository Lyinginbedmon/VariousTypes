package com.lying.fabric.client;

import com.lying.client.VariousTypesClient;
import com.lying.client.model.AnimatedPlayerEntityModel;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.Dilation;

public final class VariousTypesFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    	VariousTypesClient.clientInit();
    	
		EntityModelLayerRegistry.register(VariousTypesClient.ANIMATED_PLAYER, () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, false));
		EntityModelLayerRegistry.register(VariousTypesClient.ANIMATED_PLAYER_SLIM, () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, true));
    }
}
