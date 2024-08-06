package com.lying.fabric.client;

import com.lying.client.VariousTypesClient;
import com.lying.init.VTEntityTypes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.player.PlayerEntity;

public final class VariousTypesFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    	VariousTypesClient.clientInit();
    	
    	FabricDefaultAttributeRegistry.register(VTEntityTypes.ANIMATED_PLAYER.get(), PlayerEntity.createPlayerAttributes());
    }
}
