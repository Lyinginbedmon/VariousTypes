package com.lying.client;

import org.lwjgl.glfw.GLFW;

import com.lying.VariousTypes;
import com.lying.client.config.ClientConfig;
import com.lying.client.init.VTKeybinds;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.screen.AbilityMenu;
import com.lying.client.screen.CharacterCreationEditScreen;
import com.lying.client.screen.CharacterSheetScreen;
import com.lying.client.utility.ClientBus;
import com.lying.component.element.ElementActionables;
import com.lying.component.element.ElementNonLethal;
import com.lying.component.element.ElementSpecialPose;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSheetElements;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.network.SyncPosePacket;
import com.lying.reference.Reference;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityPose;

public class VariousTypesClient
{
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static ClientConfig config;
	
	public static final EntityModelLayer ANIMATED_PLAYER		= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player");
	public static final EntityModelLayer ANIMATED_PLAYER_SLIM	= new EntityModelLayer(Reference.ModInfo.prefix("animated_player"), "player_slim");
	
	public static void clientInit()
	{
		config = new ClientConfig(mc.runDirectory.getAbsolutePath() + "/config/VariousTypesClient.cfg");
		config.read();
		
		ClientBus.init();
		registerMenus();
		registerS2CPacketReceivers();
		registerEntityRenderers();
		registerKeyBindings();
	}
	
	private static void registerMenus()
	{
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), CharacterSheetScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), CharacterCreationEditScreen::new);
		MenuRegistry.registerScreenFactory(VTScreenHandlerTypes.ABILITY_MENU_HANDLER.get(), AbilityMenu::new);
	}
	
	private static void registerS2CPacketReceivers()
	{
    	NetworkManager.registerReceiver(NetworkManager.s2c(), SyncActionablesPacket.PACKET_TYPE, SyncActionablesPacket.PACKET_CODEC, (value, context) -> 
    	{
    		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
    		{
    			ElementActionables element = sheet.element(VTSheetElements.ACTIONABLES);
    			value.actionables().abilities().forEach(inst -> element.set(inst));
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.s2c(), SyncFatiguePacket.PACKET_TYPE, SyncFatiguePacket.PACKET_CODEC, (value, context) -> 
    	{
    		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
    		{
    			ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
    			nonlethal.set(value.fatigue());
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.s2c(), SyncPosePacket.PACKET_TYPE, SyncPosePacket.PACKET_CODEC, (value, context) -> 
    	{
    		boolean isSet = value.pose().isPresent();
    		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
    		{
    			ElementSpecialPose specialPose = sheet.element(VTSheetElements.SPECIAL_POSE);
    			if(isSet)
    			{
    				specialPose.set(value.pose().get(), null);
    				mc.player.setPose(specialPose.value().get());
    			}
    			else
    			{
    				specialPose.clear(null);
    				mc.player.setPose(EntityPose.STANDING);
    			}
    		});
    	});
	}
	
	private static void registerKeyBindings()
	{
		VTKeybinds.keyOpenAbilities = VTKeybinds.make("open_abilities", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V);
		int[] defaults = new int[] {GLFW.GLFW_KEY_F5, GLFW.GLFW_KEY_F6, GLFW.GLFW_KEY_F7, GLFW.GLFW_KEY_F8};
		for(int i=0; i<VTKeybinds.keyFavAbility.length; i++)
			VTKeybinds.keyFavAbility[i] = VTKeybinds.make("use_fav_ability_"+(i+1), InputUtil.Type.KEYSYM, defaults[i]);
		KeybindHandling.init();
	}
	
	private static void registerEntityRenderers()
	{
		EntityRendererRegistry.register(VTEntityTypes.ANIMATED_PLAYER, AnimatedPlayerEntityRenderer::new);
	}
}
