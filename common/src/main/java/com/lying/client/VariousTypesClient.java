package com.lying.client;

import org.lwjgl.glfw.GLFW;

import com.lying.VariousTypes;
import com.lying.client.config.ClientConfig;
import com.lying.client.init.VTKeybinds;
import com.lying.client.init.VTPlayerSpecialRenderingRegistry;
import com.lying.client.particle.ParentedParticles;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.renderer.EmitterEntityRenderer;
import com.lying.client.renderer.PortalEntityRenderer;
import com.lying.client.renderer.SmokeCloudEntityRenderer;
import com.lying.client.renderer.ThrownBlockEntityRenderer;
import com.lying.client.screen.AbilityMenu;
import com.lying.client.screen.CharacterCreationEditScreen;
import com.lying.client.screen.CharacterSheetScreen;
import com.lying.client.utility.ClientBus;
import com.lying.client.utility.highlights.BlockHighlights;
import com.lying.client.utility.highlights.EntityHighlights;
import com.lying.client.utility.highlights.HighlightManager;
import com.lying.component.element.ElementActionables;
import com.lying.component.element.ElementNonLethal;
import com.lying.component.element.ElementSpecialPose;
import com.lying.entity.AccessoryAnimationInterface;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSheetElements;
import com.lying.network.HighlightPacket;
import com.lying.network.ParentedParticlePacket;
import com.lying.network.PoweredFlightPacket;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.network.SyncPosePacket;
import com.lying.utility.Highlight;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class VariousTypesClient
{
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static ClientConfig config;
	
	public static final BlockHighlights BLOCK_HIGHLIGHTS = new BlockHighlights();
	public static final EntityHighlights ENTITY_HIGHLIGHTS = new EntityHighlights();
	public static final ParentedParticles PARENTED_PARTICLES = new ParentedParticles();
	public static final HighlightManager<?>[] HIGHLIGHTS = new HighlightManager[] { BLOCK_HIGHLIGHTS, ENTITY_HIGHLIGHTS };
	
	public static void clientInit()
	{
		config = new ClientConfig(mc.runDirectory.getAbsolutePath() + "/config/VariousTypesClient.cfg");
		config.read();
		
		ClientBus.init();
		VTPlayerSpecialRenderingRegistry.init();
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
    			element.readFromNbt(value.actionables().storeNbt());
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.s2c(), SyncFatiguePacket.PACKET_TYPE, SyncFatiguePacket.PACKET_CODEC, (value, context) -> 
    	{
    		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
    		{
    			ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
    			nonlethal.set(value.fatigue(), null);
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
    	NetworkManager.registerReceiver(NetworkManager.s2c(), HighlightPacket.PACKET_TYPE, HighlightPacket.PACKET_CODEC, (value, context) -> 
    	{
    		World world = mc.player.getWorld();
    		value.highlights().forEach(highlight -> 
    		{
    			if(world != null && BLOCK_HIGHLIGHTS.canAccept(highlight))
					BLOCK_HIGHLIGHTS.add((Highlight.Block)highlight, world);
    			
    			if(ENTITY_HIGHLIGHTS.canAccept(highlight))
					ENTITY_HIGHLIGHTS.add((Highlight.Entity)highlight);
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.s2c(), PoweredFlightPacket.PACKET_TYPE, PoweredFlightPacket.PACKET_CODEC, (value, context) -> 
    	{
    		PlayerEntity player = value.playerID().equals(mc.player.getUuid()) ? mc.player : mc.player.getWorld().getPlayerByUuid(value.playerID());
    		if(player != null)
    			((AccessoryAnimationInterface)player).setPoweredFlight(value.powered());
    	});
    	NetworkManager.registerReceiver(NetworkManager.s2c(), ParentedParticlePacket.PACKET_TYPE, ParentedParticlePacket.PACKET_CODEC, (value, context) -> 
    	{
    		PARENTED_PARTICLES.addParticle(value.uuid(), value.params(), value.x(), value.y(), value.z(), value.velX(), value.velY(), value.velZ());
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
		EntityRendererRegistry.register(VTEntityTypes.SHAKEN_BLOCK, ThrownBlockEntityRenderer::new);
		EntityRendererRegistry.register(VTEntityTypes.THROWN_BLOCK, ThrownBlockEntityRenderer::new);
		EntityRendererRegistry.register(VTEntityTypes.EMITTER, EmitterEntityRenderer::new);
		EntityRendererRegistry.register(VTEntityTypes.THICK_SMOKE_CLOUD, SmokeCloudEntityRenderer::new);
		EntityRendererRegistry.register(VTEntityTypes.PORTAL, PortalEntityRenderer::new);
	}
}
