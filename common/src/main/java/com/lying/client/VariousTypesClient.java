package com.lying.client;

import com.lying.client.init.ClientsideEntities;
import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.screen.CharacterCreationEditScreen;
import com.lying.client.screen.CharacterSheetScreen;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.reference.Reference;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.StringIdentifiable;

public class VariousTypesClient
{
	public static TextAlign ALIGN_TEXT = TextAlign.LEFT;
	
	public static final EntityModelLayer ANIMATED_PLAYER		= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player");
	public static final EntityModelLayer ANIMATED_PLAYER_SLIM	= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player_slim");
	
	public static void clientInit()
	{
		ClientBus.init();
		
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), CharacterCreationEditScreen::new);
		
		ClientsideEntities.init();
		EntityRendererRegistry.register(ClientsideEntities.ANIMATED_PLAYER, AnimatedPlayerEntityRenderer::new);
		EntityModelLayerRegistry.register(ANIMATED_PLAYER, () -> AnimatedPlayerEntityModel.createBodyLayer(new Dilation(0F), false));
		EntityModelLayerRegistry.register(ANIMATED_PLAYER_SLIM, () -> AnimatedPlayerEntityModel.createBodyLayer(new Dilation(0F), true));
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
