package com.lying.fabric.client;

import com.lying.client.ClientBus;

import net.fabricmc.api.ClientModInitializer;

public final class VariousTypesFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    	ClientBus.init();
    }
}
