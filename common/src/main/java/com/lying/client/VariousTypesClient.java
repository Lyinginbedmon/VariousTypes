package com.lying.client;

import com.lying.client.screen.CharacterSheetScreen;
import com.lying.init.VTScreenHandlerTypes;

import dev.architectury.registry.menu.MenuRegistry;

public class VariousTypesClient
{
	public static void clientInit()
	{
		ClientBus.init();
		
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
	}
}
