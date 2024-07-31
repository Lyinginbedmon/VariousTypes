package com.lying.client;

import org.lwjgl.glfw.GLFW;

import com.lying.client.config.ClientConfig;
import com.lying.client.init.ClientsideEntities;
import com.lying.client.init.VTKeybinds;
import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.network.SyncActionablesReceiver;
import com.lying.client.network.SyncFatigueReceiver;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.screen.AbilityMenu;
import com.lying.client.screen.CharacterCreationEditScreen;
import com.lying.client.screen.CharacterSheetScreen;
import com.lying.client.utility.ClientBus;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.network.VTPacketHandler;
import com.lying.reference.Reference;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.InputUtil;

public class VariousTypesClient
{
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static ClientConfig config;
	
	public static final EntityModelLayer ANIMATED_PLAYER		= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player");
	public static final EntityModelLayer ANIMATED_PLAYER_SLIM	= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player_slim");
	
	@SuppressWarnings("removal")
	public static void clientInit()
	{
		config = new ClientConfig(mc.runDirectory.getAbsolutePath() + "/config/VariousTypesClient.cfg");
		config.read();
		ClientBus.init();
		
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), CharacterCreationEditScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.ABILITY_MENU_HANDLER.get(), AbilityMenu::new);
		
    	NetworkManager.registerReceiver(NetworkManager.Side.S2C, VTPacketHandler.SYNC_ACTIONABLES_ID, new SyncActionablesReceiver());
    	NetworkManager.registerReceiver(NetworkManager.Side.S2C, VTPacketHandler.SYNC_FATIGUE_ID, new SyncFatigueReceiver());
		
		ClientsideEntities.init();
		EntityRendererRegistry.register(ClientsideEntities.ANIMATED_PLAYER, AnimatedPlayerEntityRenderer::new);
		EntityModelLayerRegistry.register(ANIMATED_PLAYER, () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, false));
		EntityModelLayerRegistry.register(ANIMATED_PLAYER_SLIM, () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, true));
		
		VTKeybinds.keyOpenAbilities = VTKeybinds.make("open_abilities", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V);
		int[] defaults = new int[] {GLFW.GLFW_KEY_F5, GLFW.GLFW_KEY_F6, GLFW.GLFW_KEY_F7, GLFW.GLFW_KEY_F8};
		for(int i=0; i<VTKeybinds.keyFavAbility.length; i++)
			VTKeybinds.keyFavAbility[i] = VTKeybinds.make("use_fav_ability_"+(i+1), InputUtil.Type.KEYSYM, defaults[i]);
		KeybindHandling.init();
	}
}
