package com.lying.client;

import com.lying.client.screen.CharacterCreationScreen;
import com.lying.client.screen.CharacterSheetScreen;
import com.lying.init.VTScreenHandlerTypes;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.util.StringIdentifiable;

public class VariousTypesClient
{
	public static TextAlign ALIGN_TEXT = TextAlign.LEFT;
	
	public static void clientInit()
	{
		ClientBus.init();
		
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), CharacterCreationScreen::new);
	}
	
	public static enum TextAlign implements StringIdentifiable
	{
		RIGHT,
		LEFT,
		CENTRE;
		
		public String asString() { return name(); }
		
		public static TextAlign fromString(String name)
		{
			for(TextAlign val : values())
				if(val.asString().equalsIgnoreCase(name))
					return val;
			return LEFT;
		}
	}
}
