package com.lying.client;

import com.lying.client.screen.CharacterSheetScreen;
import com.lying.init.VTScreenHandlerTypes;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.MinecraftClient;

public class VariousTypesClient
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void clientInit()
	{
		ClientBus.init();
		
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
	}
}
